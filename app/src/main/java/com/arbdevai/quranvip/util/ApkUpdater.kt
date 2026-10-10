package com.arbdevai.quranvip.util

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

object ApkUpdater {
    fun downloadAndInstall(context: Context, url: String, version: String) {
        val fileName = "alquran-vip-$version.apk"
        val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadDir, fileName)

        // 1. Check if the specific version APK is already downloaded
        if (file.exists()) {
            if (isValidApk(context, file)) {
                Toast.makeText(context, "Update sudah diunduh. Memulai instalasi...", Toast.LENGTH_SHORT).show()
                installApk(context, file)
                return
            } else {
                // If corrupted, delete it and proceed to download
                file.delete()
            }
        }

        // Clean up older apk files to free space
        downloadDir?.listFiles()?.forEach { 
            if (it.name.startsWith("alquran-vip-") && it.name.endsWith(".apk") && it.name != fileName) {
                it.delete()
            }
        }

        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle("Mengunduh Al-Quran VIP $version")
            setDescription("Tunggu sebentar, update sedang diunduh...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
        }

        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = manager.enqueue(request)
        Toast.makeText(context, "Memulai unduhan update $version...", Toast.LENGTH_SHORT).show()

        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent: Intent) {
                if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id == downloadId) {
                        if (isValidApk(context, file)) {
                            installApk(context, file)
                        } else {
                            Toast.makeText(context, "Unduhan rusak atau tidak valid.", Toast.LENGTH_LONG).show()
                            file.delete()
                        }
                        context.unregisterReceiver(this)
                    }
                }
            }
        }
        androidx.core.content.ContextCompat.registerReceiver(
            context,
            onComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            androidx.core.content.ContextCompat.RECEIVER_EXPORTED
        )
    }

    private fun isValidApk(context: Context, file: File): Boolean {
        if (!file.exists()) return false
        val pm = context.packageManager
        val info = pm.getPackageArchiveInfo(file.absolutePath, 0)
        return info != null
    }

    private fun installApk(context: Context, file: File) {
        if (!file.exists()) return

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val installIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(installIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal menginstal APK", Toast.LENGTH_SHORT).show()
        }
    }
}
