package com.inigo.player.logics.tasks;

import com.inigo.player.models.Status;

/**
 * Created by inigo on 3/08/17.
 */

public interface StatusListener {
    void onUpdatedMediaPlayerStatus(Status status);
}
