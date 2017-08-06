package com.inigo.player.logics;

import com.inigo.player.logics.tasks.StatusListener;
import com.inigo.player.models.Status;

/**
 * Created by inigo on 3/08/17.
 */

public abstract class MediaPlayerObserver {
    protected Status status;

    public void subscribe(StatusListener pl){
        status.subscribe(pl);
    }

    public void unsubscribe(StatusListener pl){
        status.unsubscribe(pl);
    }
}
