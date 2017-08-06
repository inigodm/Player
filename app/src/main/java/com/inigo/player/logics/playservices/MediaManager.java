package com.inigo.player.logics.playservices;

import android.media.MediaPlayer;
import android.util.Log;

import com.inigo.player.logics.MediaPlayerObserver;
import com.inigo.player.logics.tasks.StatusListener;
import com.inigo.player.models.Status;
import com.inigo.player.models.Song;
import com.inigo.player.models.TitleSubtitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inigo on 19/07/17.
 */

public class MediaManager extends MediaPlayerObserver {
    MediaPlayer MP = new MediaPlayer();
    List<TitleSubtitle> songs = new ArrayList<>();
    int index = 0;
    private static MediaManager MANAGER;

    /**
     * Singleton. This class must be unique because there will be only one MediaPlayer
     * and only one list of songs to play
     * @return
     */
    private MediaManager(Status ps){
        this.status = ps;
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
        try {
            if (!status.isPlaying()){
                String absfilename = ((Song)(songs.get(index))).getPath();
                MP.reset();
                MP.setDataSource(absfilename);
                MP.prepare();
            }
            if (songs.size() > 0) {
                MP.start();
                MP.setOnCompletionListener(new GoToNextOnCompletionListener());
                status.setPlaying(true);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("ERROR", "ERROR 38!!!!");
            e.printStackTrace();
        }
    }

    public void stop() {
        if (status.isPlaying()) {
            MP.stop();
            status.setPlaying(false);
        }
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

    public void setSongs(List<TitleSubtitle> songs) {
        this.songs = songs;
        this.index = 0;
    }

    private void setNextSong(int next) {
        if ((next > songs.size() - 1) || (next < 0)){
            index = 0;
        }else{
            index = next;
        }
    }

    public int getIndex(){
        return index;
    }
    public boolean isPlaying() {
        return status.isPlaying();
    }

    class GoToNextOnCompletionListener implements MediaPlayer.OnCompletionListener {
        public void onCompletion(MediaPlayer mp) {
            MediaManager.this.next();
        }
    }
}
