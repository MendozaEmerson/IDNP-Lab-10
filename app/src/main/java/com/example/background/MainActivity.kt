package com.example.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Crear el NotificationChannel
        createNotificationChannel()

        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val btnStop = findViewById<Button>(R.id.btnStop)

        btnPlay.setOnClickListener(onClickListenerPlay())
        btnStop.setOnClickListener(onClickListenerStop())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Audio Playback"
            val descriptionText = "Notification channel for audio playback"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("AUDIO_PLAYBACK_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun onClickListenerPlay(): (View) -> Unit = {
        val audioPlayServiceIntent = Intent(applicationContext, AudioPlayServices::class.java)
        audioPlayServiceIntent.putExtra(AudioPlayServices.FILENAME, "mariposa.mp3")
        audioPlayServiceIntent.putExtra(AudioPlayServices.COMMAND, AudioPlayServices.PLAY)
        startService(audioPlayServiceIntent)
    }

    private fun onClickListenerStop(): (View) -> Unit = {
        val audioPlayServiceIntent = Intent(applicationContext, AudioPlayServices::class.java)
        audioPlayServiceIntent.putExtra(AudioPlayServices.COMMAND, AudioPlayServices.STOP)
        startService(audioPlayServiceIntent)
    }
}
