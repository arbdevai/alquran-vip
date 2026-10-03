package com.arbdevai.quranvip.audio

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.arbdevai.quranvip.data.model.Reciters
import com.arbdevai.quranvip.data.model.Surah
import kotlinx.coroutines.flow.MutableStateFlow
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

class PlaybackController(context: Context) {
    private val mutableState = MutableStateFlow(PlaybackState())
    val state = mutableState.asStateFlow()
    private var controller: MediaController? = null
    private var closed = false
    private val future = MediaController.Builder(context, SessionToken(context,
        ComponentName(context, QuranPlaybackService::class.java))).buildAsync()

    init {
        future.addListener({
            if (!closed) {
                try {
                    controller = future.get().also { player ->
                        player.addListener(object : Player.Listener {
                            override fun onEvents(player: Player, events: Player.Events) = sync()
                            override fun onPlayerError(error: PlaybackException) {
                                mutableState.update { it.copy(error = "Audio gagal dimuat. Periksa koneksi lalu putar ulang.", loading = false) }
                            }
                        })
                    }
                    sync()
                } catch (_: Exception) {
                    mutableState.update { it.copy(error = "Pemutar audio tidak dapat terhubung.") }
                }
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun sync() {
        val player = controller ?: return
        val metadata = player.currentMediaItem?.mediaMetadata
        mutableState.update {
            it.copy(connected = true, playing = player.isPlaying,
                loading = player.playbackState == Player.STATE_BUFFERING,
                title = metadata?.title?.toString().orEmpty(),
                subtitle = metadata?.artist?.toString().orEmpty(),
                surah = metadata?.extras?.getInt("surah") ?: 0,
                ayah = metadata?.extras?.getInt("ayah") ?: 0)
        }
    }

    fun play(surah: Surah, qoriKey: String, startAyah: Int = 1, singleAyahOnly: Boolean = false) {
        val player = controller
        if (player == null) {
            mutableState.update { it.copy(error = "Pemutar sedang terhubung. Coba lagi sebentar.") }
            return
        }
        val selected = surah.ayat.filter { if (singleAyahOnly) it.nomorAyat == startAyah else it.nomorAyat >= startAyah }
        if (selected.isEmpty() || selected.any { it.audio[qoriKey].isNullOrBlank() }) {
            mutableState.update { it.copy(error = "Audio qari ini tidak tersedia untuk ayat yang dipilih.") }
            return
        }
        val items = selected.map { ayah ->
            MediaItem.Builder().setMediaId("${surah.nomor}:${ayah.nomorAyat}:$qoriKey")
                .setUri(ayah.audio.getValue(qoriKey))
                .setMediaMetadata(MediaMetadata.Builder()
                    .setTitle("${surah.namaLatin} · Ayat ${ayah.nomorAyat}")
                    .setArtist(Reciters.names[qoriKey].orEmpty())
                    .setExtras(Bundle().apply { putInt("surah", surah.nomor); putInt("ayah", ayah.nomorAyat) })
                    .build()).build()
        }
        mutableState.update { it.copy(error = null) }
        player.setMediaItems(items)
        player.prepare()
        player.play()
    }

    fun toggle() {
        controller?.let { if (it.isPlaying) it.pause() else it.play() }
    }
    fun stop() {
        controller?.stop()
        controller?.clearMediaItems()
        mutableState.update { PlaybackState(connected = it.connected) }
    }
    fun close() {
        closed = true
        MediaController.releaseFuture(future)
        controller = null
    }
}
