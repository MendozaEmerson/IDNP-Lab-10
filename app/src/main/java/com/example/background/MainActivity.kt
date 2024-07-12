package com.example.background

import android.content.Intent
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

        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val btnStop = findViewById<Button>(R.id.btnStop)

        btnPlay.setOnClickListener(onClickListenerPlay())
        btnStop.setOnClickListener(onClickListenerStop())
    }

    private fun onClickListenerPlay():(View)->Unit={
        val audioPlayServiceIntent = Intent(applicationContext, AudioPlayServices::class.java)
        audioPlayServiceIntent.putExtra(AudioPlayServices.FILENAME, "mariposa1.mp3")
        audioPlayServiceIntent.putExtra(AudioPlayServices.COMMAND, AudioPlayServices.PLAY)
        startService(audioPlayServiceIntent)
    }

    private fun onClickListenerStop():(View)->Unit={
        val audioPlayServiceIntent = Intent(applicationContext, AudioPlayServices::class.java)
        audioPlayServiceIntent.putExtra(AudioPlayServices.COMMAND, AudioPlayServices.STOP)
        startService(audioPlayServiceIntent)
    }
}