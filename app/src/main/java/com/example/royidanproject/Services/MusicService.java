package com.example.royidanproject.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.royidanproject.R;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    private MediaPlayer player;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        player = MediaPlayer.create(getApplicationContext(), R.raw.music_spring_in_my_step);
        player.setVolume(100f, 100f);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getBooleanExtra("isRunning", false)) {
            player.start();
        } else {
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();

        super.onDestroy();
    }
}