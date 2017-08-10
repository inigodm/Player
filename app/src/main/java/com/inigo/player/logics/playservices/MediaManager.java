package com.inigo.player.logics.playservices;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.inigo.player.R;
import com.inigo.player.activities.MainActivity;
import com.inigo.player.logics.StatusListener;
import com.inigo.player.models.Status;
import com.inigo.player.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inigo on 19/07/17.
 */

public class MediaManager extends Service {
    MediaPlayer MP = new MediaPlayer();
    List<Song> songs = new ArrayList<>();
    int index = 0;
    private static MediaManager MANAGER;
    protected Status status;
    private final IBinder mBinder = new LocalBinder();
    private int NOTIFICATION_ID = 1234;
    private static final int SECS_TO_NOT_PREVIOUS = 3000;

    /**
     * Singleton. This class must be unique because there will be only one MediaPlayer
     * and only one list of songs to play
     * @return
     */
    private MediaManager(Status ps){
        this.status = ps;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public static MediaManager getInstance(Status ps){
        if (MANAGER == null){
            MANAGER = new MediaManager(ps);
        }
        return MANAGER;
    }

    public static MediaManager getInstance(){
        if (MANAGER == null){
            MANAGER = new MediaManager(new Status());
        }
        return MANAGER;
    }

    public void play(int songIndex){
        setNextSong(songIndex);
        stop();
        play();
    }
    /**
     * Hace el start del mediaplayer
     */
    public void play(){
        if (songs.size() <= 0) { return; }
        try {
            if (!status.isPlaying()){
                status.reset((Song)(songs.get(index)));
                MP.reset();
                MP.setDataSource(status.getPath());
                MP.prepare();
            }
            MP.start();
            MP.setOnCompletionListener(new GoToNextOnCompletionListener());
            status.setPlaying(true);
            showNotification("uno", "dos" , "tres", true);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("ERROR", "ERROR 38!!!!");
            e.printStackTrace();
        }
    }

    public void stop() {
        MP.stop();
        status.setPlaying(false);
    }

    public void pause(){
        MP.pause();
        status.setPaused(true);
    }

    public void next(){
        setNextSong(index + 1);
        stop();
        play();
    }

    public void previous() {
        if (!status.isPlaying()){
            setNextSong(index -1);
        }
        stop();
        play();
    }

    public void invertPauseOrPlay(){
        if (status.isPlaying()){
            pause();
        }else{
            play();
        }
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        this.index = 0;
    }

    private void setNextSong(int next) {
        // only change the song if we are in the first 3 seconds
        if (MP.getCurrentPosition() > SECS_TO_NOT_PREVIOUS){
            if ((next > songs.size() - 1) || (next < 0)){
                index = 0;
            }else{
                index = next;
            }
        }
    }

    /**
     * Show a notification while this service is running.
     * @param ticket Texto principal de la notacion
     * @param subticket Texto secundario bajo el ticket
     * @param text Es el mensaje que sale cuando se pone la notificacion
     * @param foreground
     */
    private void showNotification(String ticket, String subticket, String text, boolean foreground) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        Notification noti = new Notification.Builder(this)
                .setTicker("This is ticker")
                .setContentTitle(ticket)
                .setContentTitle(subticket)
                .setSubText("subtexto ")
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .setSmallIcon(android.R.drawable.arrow_down_float)
                .build();
        noti.flags|=Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR;
        startForeground(NOTIFICATION_ID, noti);
    }

    public int getIndex(){
        return index;
    }
    public boolean isPlaying() {
        return status.isPlaying();
    }

    public void subscribe(StatusListener pl){
        status.subscribe(pl);
    }

    public void unsubscribe(StatusListener pl){
        status.unsubscribe(pl);
    }

    class GoToNextOnCompletionListener implements MediaPlayer.OnCompletionListener {
        public void onCompletion(MediaPlayer mp) {
            MediaManager.this.next();
        }
    }

    public class LocalBinder extends Binder {
        MediaManager getService() {
            return MediaManager.this;
        }
    }
}
