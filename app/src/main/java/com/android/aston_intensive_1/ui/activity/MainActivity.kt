package com.android.aston_intensive_1.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.aston_intensive_1.ui.service.MusicPlayerService
import com.android.aston_intensive_1.MyMusicPlayer
import com.android.aston_intensive_1.R
import com.android.aston_intensive_1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PlayerViewModel by viewModels()
    private lateinit var mediaPlayer: MyMusicPlayer
    private var trackId = 0
    private var trackName = ""
    private var trackProgress = 0

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.isPlaying = savedInstanceState.getBoolean("isPlaying", false)
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MyMusicPlayer(this, viewModel)
        getTrackModel()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requestNotificationPermission()
        }

        if (savedInstanceState != null) {
            Intent(
                this@MainActivity,
                MusicPlayerService::class.java
            ).also {
                it.action = MusicPlayerService.Actions.PLAY.toString()
                startService(it)
            }
            viewModel.isPlaying = true
            onPlayClicked()
        }

        with(binding) {
            playButton.setOnClickListener {
                if(viewModel.isPlaying){
                    playButton.background = getDrawable(R.drawable.play_button)
                    Intent(
                        this@MainActivity,
                        MusicPlayerService::class.java
                    ).also {
                        it.action = MusicPlayerService.Actions.PAUSE.toString()
                        startService(it)
                    }
                    viewModel.isPlaying = false
                } else {
                    Intent(
                        this@MainActivity,
                        MusicPlayerService::class.java
                    ).also {
                        it.action = MusicPlayerService.Actions.PLAY.toString()
                        startService(it)
                    }
                    viewModel.isPlaying = true
                    onPlayClicked()
                }

            }

            previousButton.setOnClickListener {
                Intent(
                    this@MainActivity,
                    MusicPlayerService::class.java
                ).also {
                    it.action = MusicPlayerService.Actions.PREVIOUS.toString()
                    startService(it)
                }
                viewModel.isPlaying = true
                onPlayClicked()
            }

            nextButton.setOnClickListener {
                onPlayClicked()
                Intent(
                    this@MainActivity,
                    MusicPlayerService::class.java
                ).also {
                    it.action = MusicPlayerService.Actions.NEXT.toString()
                    startService(it)
                }
                viewModel.isPlaying = true
                onPlayClicked()
            }
        }
    }

    private fun onPlayClicked(){
        if (viewModel.isPlaying){
            binding.playButton.background = getDrawable(R.drawable.pause_button)
            Toast.makeText(this@MainActivity, trackId.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),10
        )
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean("isPlaying", viewModel.isPlaying)
    }

    override fun onDestroy() {
        stopService(Intent(this@MainActivity, MusicPlayerService::class.java))
        super.onDestroy()
    }

    private fun getTrackModel(){
        viewModel.trackModelInit.observeForever { track ->
            this.trackId = track.id
            this.trackName = track.trackName
            this.trackProgress = track.trackProgress
        }
    }

    companion object{
        private val trackList = listOf(
            R.raw.blue_loop,
            R.raw.brown_fields,
            R.raw.grey_terasse
        )
    }
}