package com.inigo.player.logics.tasks;

import com.inigo.player.models.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inigo on 3/08/17.
 */

public class StatusObserver {
    List<StatusListener> listeners = new ArrayList<>();

    public void subscribe(StatusListener pl){
        listeners.add(pl);
    }

    public void unsubscribe(StatusListener pl){
        listeners.remove(pl);
    }

    public void statusChanged(Status status){
        for (StatusListener pl : listeners) {
            if (pl != null) {
                pl.onUpdatedMediaPlayerStatus(status);
            }
        }
    }
}
