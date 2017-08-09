package com.inigo.player.models;

import com.inigo.player.logics.StatusObserver;

/**
 * Created by inigo on 3/08/17.
 */

public class Status extends StatusObserver{
    boolean isPlaying = false;
    boolean isPaused = false;
    Song song;

    public Status(Song s){
        this.song = s;
    }

    public Status(){
        this.song = Song.buildDummieSong();
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song s) {
        if (s == null){
            s = Song.buildDummieSong();
        }
        this.song = s;
    }

    public String getPath() {
        return song.getPath();
    }

    public void setPath(String path) {
        song.setPath(path);
    }

    public String getName() {
        return song.getName();
    }

    public void setName(String name) {
        song.setName(name);
    }

    public String getAuthor() {
        return song.getAuthor();
    }

    public void setAuthor(String author) {
        song.setAuthor(author);
    }

    public String getAlbum() {
        return song.getAlbum();
    }

    public void setAlbum(String album) {
        song.setAlbum(album);
    }

    public String getDuration() {
        return ((int)song.getDuration()/60000) + ":" + ((int)song.getDuration()/1000)%60;
    }

    public void setDuration(int duration) {
        song.setDuration(duration);
    }

    public String getTitle() {
        return song.getTitle();
    }

    public String getSubtitle() {
        return song.getSubtitle();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
        // This method will be only called to change the state of 'isPlaying'
        //So, when 'stop' or 'play' is pressed, and in those cases:
        isPaused = false;
        statusChanged(this);
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
        statusChanged(this);
    }

    public void reset(Song s){
        this.song = s;
    }
}
