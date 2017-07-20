package com.inigo.player.logics.tasks;

import android.os.AsyncTask;

import com.inigo.player.MainActivity;
import com.inigo.player.logics.tasks.playlistload.SongsLoader;
import com.inigo.player.models.Song;
import com.inigo.player.models.TitleSubtitle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by inigo on 19/07/17.
 */

public class PlayListLoader extends AsyncTask<String, Song, Void>{
    MainActivity.PlayerFragment playlistFragment;
    Runnable runnable;

    public PlayListLoader(MainActivity.PlayerFragment playlistFragment, Runnable runnable) {
        super();
        this.playlistFragment = playlistFragment;
        this.runnable = runnable;
    }

    @Override
    protected Void doInBackground(String... strings) {
        runnable.run();
        return null;
    }


    @Override
    protected void onPostExecute(Void o) {
        super.onPostExecute(o);
        playlistFragment.refreshPlaylist();
        playlistFragment.setSpinnerVisible(false);
    }

    @Override
    protected void onProgressUpdate(Song... values) {
        super.onProgressUpdate(values);
    }
}
