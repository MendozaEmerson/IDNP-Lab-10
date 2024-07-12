package com.example.background

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import java.io.IOException

class AudioPlayServices : Service() {

    private var mediaPlayer: MediaPlayer? = null

    companion object {
        const val FILENAME = "FILENAME"
        const val COMMAND = "COMMAND"
        const val PLAY = "PLAY"
        const val STOP = "STOP"
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filename = intent?.getStringExtra(FILENAME)
        val command = intent?.getStringExtra(COMMAND)

        if (command == PLAY) {
            startForegroundService()
            Thread {
                try {
                    audioPlay(filename)
                } catch (e: IOException) {
                    Log.e("AudioPlayServices", "Error in onStartCommand", e)
                }
            }.start()
        } else if (command == STOP) {
            audioStop()
            stopForeground(true)
            stopSelf()
        }

        return START_STICKY
    }

    private fun startForegroundService() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "AUDIO_PLAYBACK_CHANNEL")
            .setContentTitle("Audio Playback")
            .setContentText("Playing audio in the background")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    private fun audioPlay(filename: String?) {
        if (filename != null) {
            try {
                val assetFileDescriptor = assets.openFd(filename)
                mediaPlayer = MediaPlayer()
                mediaPlayer?.apply {
                    setDataSource(
                        assetFileDescriptor.fileDescriptor,
                        assetFileDescriptor.startOffset,
                        assetFileDescriptor.length
                    )
                    assetFileDescriptor.close()
                    prepare()
                    setVolume(1f, 1f)
                    isLooping = false
                    start()
                    Log.d("AudioPlayServices", "Audio started")
                }
            } catch (e: IOException) {
                Log.e("AudioPlayServices", "Error playing audio", e)
            }
        } else {
            Log.e("AudioPlayServices", "Filename is null")
        }
    }

    private fun audioStop() {
        mediaPlayer?.apply {
            stop()
            release()
            Log.d("AudioPlayServices", "Audio stopped")
        }
        mediaPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        audioStop()
    }
}
