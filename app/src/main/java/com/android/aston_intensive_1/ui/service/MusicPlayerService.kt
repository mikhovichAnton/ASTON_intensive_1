package com.android.aston_intensive_1.ui.service


import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.android.aston_intensive_1.MyMusicPlayer
import com.android.aston_intensive_1.ui.activity.PlayerViewModel
import com.android.aston_intensive_1.R

class MusicPlayerService : Service() {

    private lateinit var myMusicPlayer: MyMusicPlayer

    private lateinit var viewModel: PlayerViewModel

    override fun onCreate() {
        super.onCreate()
        viewModel = PlayerViewModel()
        myMusicPlayer = MyMusicPlayer(this,viewModel)
        notification("First Try")
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.PLAY.toString() -> myMusicPlayer.play()
            Actions.PAUSE.toString() -> myMusicPlayer.pause()
            Actions.NEXT.toString() -> myMusicPlayer.nextTrack()
            Actions.PREVIOUS.toString() -> myMusicPlayer.previousTrack()
            Actions.CLOSE_APP_SERVICE.toString() -> closeService()
        }
        return super.onStartCommand(intent, flags, startId)
    }



    override fun onDestroy() {
        myMusicPlayer.onDestroy()
        super.onDestroy()
    }

    private fun closeService(){
        stopService(Intent(this, MusicPlayerService::class.java))
        onDestroy()
    }

    private fun notification(trackName: String) {
        val playIntent = Intent(this, MusicPlayerService::class.java).apply { action = Actions.PLAY.toString() }
        val pauseIntent = Intent(this,
            MusicPlayerService::class.java).apply { action = Actions.PAUSE.toString() }
        val previousIntent = Intent(this,
            MusicPlayerService::class.java).apply { action = Actions.PREVIOUS.toString() }
        val nextIntent = Intent(this, MusicPlayerService::class.java).apply { action = Actions.NEXT.toString() }
        val closeServiceIntent = Intent(this, MusicPlayerService::class.java).apply { action = Actions.CLOSE_APP_SERVICE.toString() }

        val playPendingIntent = PendingIntent.getService(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        val pausePendingIntent = PendingIntent.getService(this,0,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        val previousPendingIntent = PendingIntent.getService(this,0,previousIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        val nextPendingIntent = PendingIntent.getService(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        val closeServicePendingIntent = PendingIntent.getService(this,0,closeServiceIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val musicPlayerNotification = NotificationCompat
            .Builder(this,"music_player_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Music Player")
            .setContentText(trackName)
            .addAction(R.drawable.previous_button,"Prev", previousPendingIntent)
            .addAction(R.drawable.play_button,"Pl", playPendingIntent)
            .addAction(R.drawable.pause_button,"Pa", pausePendingIntent)
            .addAction(R.drawable.next_button,"Ne", nextPendingIntent)
            .addAction(R.drawable.x_01,"close", closeServicePendingIntent)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0,1,2,3,4,5))
            .setAutoCancel(true)
            .build()
        startForeground(1,musicPlayerNotification)
    }

    enum class Actions {
        PLAY,
        PAUSE,
        PREVIOUS,
        NEXT,
        CLOSE_APP_SERVICE
    }
}