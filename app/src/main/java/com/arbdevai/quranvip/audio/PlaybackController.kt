package com.arbdevai.quranvip.audio

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.arbdevai.quranvip.data.model.Reciters
import com.arbdevai.quranvip.data.model.Surah
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
data class PlaybackState(
    val connected: Boolean = false,
    val playing: Boolean = false,
    val loading: Boolean = false,
    val title: String = "",
    val subtitle: String = "",
    val surah: Int = 0,
    val ayah: Int = 0,
    val error: String? = null
)

class PlaybackController(private val context: Context) {
    private val _state = MutableStateFlow(PlaybackState())
    val state: StateFlow<PlaybackState> = _state.asStateFlow()

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null

    init {
        connect()
    }

    private fun connect() {
        val sessionToken = SessionToken(context, ComponentName(context, QuranPlaybackService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            try {
                controller = controllerFuture?.get()
                controller?.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        _state.update { it.copy(playing = isPlaying) }
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        _state.update {
                            it.copy(
                                loading = playbackState == Player.STATE_BUFFERING,
                                error = if (playbackState == Player.STATE_IDLE) "Gagal memutar audio" else null
                            )
                        }
                    }

                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        val extras = mediaItem?.mediaMetadata?.extras
                        val surahNum = extras?.getInt("surah") ?: 0
                        val ayahNum = extras?.getInt("ayah") ?: 0
                        val title = mediaItem?.mediaMetadata?.title?.toString() ?: ""
                        val subtitle = mediaItem?.mediaMetadata?.artist?.toString() ?: ""
                        _state.update {
                            it.copy(
                                title = title,
                                subtitle = subtitle,
                                surah = surahNum,
                                ayah = ayahNum
                            )
                        }
                    }
                })
                _state.update { it.copy(connected = true) }
            } catch (e: Exception) {
                _state.update { it.copy(connected = false, error = e.localizedMessage) }
            }
        }, MoreExecutors.directExecutor())
    }

    fun play(surah: Surah, qoriKey: String, startAyah: Int = 1, singleAyahOnly: Boolean = false) {
        val ctrl = controller ?: return
        val qoriName = Reciters.names[qoriKey] ?: "Qari Pilihan"

        ctrl.stop()
        ctrl.clearMediaItems()

        if (singleAyahOnly) {
            val target = surah.ayat.firstOrNull { it.nomorAyat == startAyah }
            val audioUrl = target?.audio?.get(qoriKey) ?: target?.audio?.values?.firstOrNull()
            if (audioUrl != null) {
                val item = MediaItem.Builder()
                    .setUri(audioUrl)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("QS. ${surah.namaLatin} : $startAyah")
                            .setArtist(qoriName)
                            .setExtras(android.os.Bundle().apply {
                                putInt("surah", surah.nomor)
                                putInt("ayah", startAyah)
                            })
                            .build()
                    )
                    .build()
                ctrl.setMediaItem(item)
                ctrl.prepare()
                ctrl.play()
            }
        } else {
            // Full Surah or Playlist of Ayahs
            val fullAudio = surah.audioFull[qoriKey]
            if (fullAudio != null && startAyah == 1) {
                val item = MediaItem.Builder()
                    .setUri(fullAudio)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("Surah ${surah.namaLatin} (Full)")
                            .setArtist(qoriName)
                            .setExtras(android.os.Bundle().apply {
                                putInt("surah", surah.nomor)
                                putInt("ayah", 0)
                            })
                            .build()
                    )
                    .build()
                ctrl.setMediaItem(item)
                ctrl.prepare()
                ctrl.play()
            } else {
                val items = surah.ayat.mapNotNull { ayah ->
                    val url = ayah.audio[qoriKey] ?: ayah.audio.values.firstOrNull()
                    if (url != null) {
                        MediaItem.Builder()
                            .setUri(url)
                            .setMediaMetadata(
                                MediaMetadata.Builder()
                                    .setTitle("QS. ${surah.namaLatin} : ${ayah.nomorAyat}")
                                    .setArtist(qoriName)
                                    .setExtras(android.os.Bundle().apply {
                                        putInt("surah", surah.nomor)
                                        putInt("ayah", ayah.nomorAyat)
                                    })
                                    .build()
                            )
                            .build()
                    } else null
                }
                if (items.isNotEmpty()) {
                    ctrl.setMediaItems(items, (startAyah - 1).coerceAtLeast(0), 0L)
                    ctrl.prepare()
                    ctrl.play()
                }
            }
        }
    }

    fun toggle() {
        val ctrl = controller ?: return
        if (ctrl.isPlaying) ctrl.pause() else ctrl.play()
    }

    fun stop() {
        controller?.stop()
        _state.update { it.copy(playing = false, title = "", surah = 0, ayah = 0) }
    }

    fun close() {
        controllerFuture?.let { MediaController.releaseFuture(it) }
    }
}
