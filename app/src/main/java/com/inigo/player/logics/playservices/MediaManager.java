package com.inigo.player.logics.playservices;

import android.media.MediaPlayer;
import android.util.Log;

import com.inigo.player.exceptions.ServiceException;
import com.inigo.player.models.Song;
import com.inigo.player.models.TitleSubtitle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by inigo on 19/07/17.
 */

public class MediaManager {
    MediaPlayer MP = new MediaPlayer();
    boolean isPlaying = false;
    List<TitleSubtitle> songs = new ArrayList<>();
    int index = 0;

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
            if (!isPlaying){
                String absfilename = ((Song)(songs.get(index))).getPath();
                MP.reset();
                MP.setDataSource(absfilename);
                MP.prepare();
            }
            if (songs.size() > 0) {
                MP.start();
                isPlaying = true;
                MP.setOnCompletionListener(new GoToNextOnCompletionListener());
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("ERROR", "ERROR 38!!!!");
            e.printStackTrace();
        }
    }

    public void stop() {
        if (isPlaying) {
            MP.stop();
            Log.w(getClass().getName(), "Got to stop()!");
            isPlaying=false;
        }
    }

    public void pause(){
        MP.pause();
    }

    public void playNext(){
        setNextSong(index + 1);
        stop();
        play();
    }

    public void playPrevious() {
        if (!isPlaying){
            setNextSong(index -1);
        }
        stop();
        play();
    }

    public void setSongs(List<TitleSubtitle> songs) {
        this.songs = songs;
        this.index = 0;
    }

    public boolean isPlaying() {
        return isPlaying;
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

    class GoToNextOnCompletionListener implements MediaPlayer.OnCompletionListener {
        public void onCompletion(MediaPlayer mp) {
            MediaManager.this.playNext();
        }
    }
}
