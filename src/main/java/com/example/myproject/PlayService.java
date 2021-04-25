package com.example.myproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class PlayService extends Service {
    private static final String TAG = null;
    MediaPlayer player; // the player
    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.snakesandladdersmusic); // where the mp3 file is from
        player.setLooping(true); // Set looping
        player.setVolume(100,100);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return 1;
    }

    public void onStart(Intent intent, int startId) {

    }
    public IBinder onUnBind(Intent arg0) {

        return null;
    }

    public void onStop() {

    }
    public void onPause() {
        player.pause();
    }
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }
}