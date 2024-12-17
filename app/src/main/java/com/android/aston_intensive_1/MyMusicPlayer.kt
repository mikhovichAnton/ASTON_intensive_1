package com.android.aston_intensive_1

import android.content.Context
import android.media.MediaPlayer
import com.android.aston_intensive_1.ui.activity.PlayerViewModel

class MyMusicPlayer(private val context: Context,private val viewModel: PlayerViewModel) {

    private var mediaPlayer: MediaPlayer? = null
    private var currentIndex: Int = 0
    private val trackList = listOf(
        R.raw.blue_loop,
        R.raw.brown_fields,
        R.raw.grey_terasse
    )

    fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun play() {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
                mediaPlayer?.reset()
            }
                mediaPlayer = MediaPlayer.create(context, trackList[currentIndex])
                mediaPlayer?.start()
                viewModel.setTrackList(trackList[currentIndex],"",0)
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    fun previousTrack() {
        if (currentIndex == 0) {
            return
        }
        currentIndex -= 1
        play()
    }

    fun nextTrack() {
        if (currentIndex == trackList.size - 1) {
            return
        }
        currentIndex += 1
        play()
    }

}