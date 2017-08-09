package com.inigo.player.async.tasks;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.inigo.player.activities.MainActivity;
import com.inigo.player.fragments.PlayerFragment;
import com.inigo.player.models.Song;

import java.util.List;

/**
 * Created by inigo on 19/07/17.
 */

public class PlayListLoader extends AsyncTask<String, Song, Void>{
    PlayerFragment playlistFragment;
    ContentResolver cr ;
    List<Song> songs;

    public PlayListLoader(PlayerFragment playlistFragment, List<Song> songs) {
        super();
        this.playlistFragment = playlistFragment;
        this.cr = playlistFragment.getContext().getContentResolver();
        this.songs = songs;
    }

    public PlayListLoader(ContentResolver cr, List<Song> songs) {
        super();
        this.cr = cr;
        this.songs = songs;
    }

    @Override
    public Void doInBackground(String... strings) {
        songs.clear();
        loadData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.Audio.Media.IS_MUSIC + "!= 0");
        loadData(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, MediaStore.Audio.Media.IS_MUSIC + "!= 0");
        return null;
    }


    @Override
    protected void onPostExecute(Void o) {
        super.onPostExecute(o);
        playlistFragment.refreshPlaylist();
        playlistFragment.setSpinnerVisible(false);
    }

    private void loadData(Uri uri, String selection){
        Song song;
        Cursor cur = cr.query(uri, null, selection, null, null);
        while(cur.moveToNext()) {
            song = new Song();
            song.setName(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            song.setAlbum(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            song.setAuthor(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            song.setDuration(cur.getInt(cur.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            song.setPath(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA)));
            songs.add(song);
        }
        cur.close();
    }

    @Override
    protected void onProgressUpdate(Song... values) {
        super.onProgressUpdate(values);
    }
}
