package com.example.background

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class AudioPlayServices : Service() {

    private var mediaPlayer: MediaPlayer? = null

    companion object {
        const val FILENAME = "FILENAME"
        const val COMMAND = "COMMAND"
        const val PLAY = "PLAY"
        const val STOP = "STOP"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filename = intent?.getStringExtra(FILENAME)
        val command = intent?.getStringExtra(COMMAND)

        if (command == PLAY) {
            audioPlay(filename)
        } else if (command == STOP) {
            audioStop()
        }

        return START_STICKY
    }

    private fun audioPlay(filename: String?) {
        if (filename != null) {
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
            }
        }
    }

    private fun audioStop() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        audioStop()
    }
}
