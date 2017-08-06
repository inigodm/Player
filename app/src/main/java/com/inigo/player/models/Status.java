package com.inigo.player.models;

import com.inigo.player.logics.tasks.StatusObserver;

/**
 * Created by inigo on 3/08/17.
 */

public class Status extends StatusObserver{
    boolean isPlaying = false;
    boolean isPaused = false;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
        // This method will be only called to change the state of 'isPlaying'
        //So, when 'stop' or 'play' is pressed, and in those cases:
        isPaused = !isPlaying;
        statusChanged(this);
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
        statusChanged(this);
    }
}
