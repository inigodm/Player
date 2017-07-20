package com.inigo.player.logics.tasks.playlistload;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

import com.inigo.player.models.Song;
import com.inigo.player.models.TitleSubtitle;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by inigo on 19/07/17.
 */

public class SongsLoader implements Runnable {

    ContentResolver cr ;
    List<TitleSubtitle> songs;

    public SongsLoader(ContentResolver cr, List<TitleSubtitle> songs){
        this.cr = cr;
        this.songs = songs;
    }

    @Override
    public void run() {
        songs.clear();
        loadData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.Audio.Media.IS_MUSIC + "!= 0");
        loadData(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, MediaStore.Audio.Media.IS_MUSIC + "!= 0");
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
}
