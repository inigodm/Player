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

    Context act;
    List<TitleSubtitle> songs;

    public SongsLoader(Context act, List<TitleSubtitle> songs){
        this.act = act;
        this.songs = songs;
    }

    @Override
    public void run() {
        songs.clear();
        try {
            Song song;
            ContentResolver cr = act.getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
            //String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
            Cursor cur = cr.query(uri, null, selection, null, null);
            int count = 0;
            if(cur != null)
            {
                count = cur.getCount();

                if(count > 0)
                {
                    while(cur.moveToNext())
                    {
                        song = new Song();
                        song.setName(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                        song.setAlbum(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                        song.setAuthor(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                        song.setDuration(cur.getInt(cur.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                        songs.add(song);
                        System.out.print(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                    }

                }
            }
            cur.close();
            uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
            //String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
            cur = cr.query(uri, null, selection, null, null);
            count = 0;
            if(cur != null)
            {
                count = cur.getCount();

                if(count > 0)
                {
                    while(cur.moveToNext())
                    {
                        song = new Song();
                        song.setName(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                        song.setAlbum(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                        song.setAuthor(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                        song.setDuration(cur.getInt(cur.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                        songs.add(song);
                        System.out.print(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                    }

                }
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
