package com.android.aston_intensive_1.ui.activity

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.aston_intensive_1.model.MediaPlayerStateModel
import com.android.aston_intensive_1.model.TrackModel

class PlayerViewModel : ViewModel() {
    var isPlaying = false

    private val initPlayerState = MutableLiveData<MediaPlayerStateModel>()
    private val initTrackModel = MutableLiveData<TrackModel>()

    val playerStateInit: LiveData<MediaPlayerStateModel> get() = initPlayerState
    val trackModelInit: LiveData<TrackModel> get() = initTrackModel

    fun setPlayerState(isPlaying: Boolean, buttonIconState: Drawable) {
        initPlayerState.value =
            MediaPlayerStateModel(isPlaying, buttonIconState)
    }

    fun setTrackList(id: Int, trackName: String, trackProgress: Int) {
        initTrackModel.value = TrackModel(id,trackName,trackProgress)
    }
}