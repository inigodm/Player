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
    List<TitleSubtitle> datos = new ArrayList<>();
    Runnable runnable;

    public PlayListLoader(MainActivity.PlayerFragment playlistFragment) {
        super();
        this.playlistFragment = playlistFragment;
    }

    @Override
    protected Void doInBackground(String... strings) {
        datos.clear();
        runnable = new SongsLoader(playlistFragment.getContext(), datos);
        runnable.run();
        return null;
    }


    @Override
    protected void onPostExecute(Void o) {
        super.onPostExecute(o);
        playlistFragment.setData(datos);
        playlistFragment.setSpinnerVisible(false);
    }

    @Override
    protected void onProgressUpdate(Song... values) {
        super.onProgressUpdate(values);
    }

    public List<TitleSubtitle> getDatos() {
        return datos;
    }

    public void setDatos(List<TitleSubtitle> datos) {
        this.datos = datos;
    }
}
