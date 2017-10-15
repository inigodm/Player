package com.inigo.player.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inigo.player.models.Song;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import kotlin.jvm.functions.Function1;

/**
 * Created by janisharali on 25/12/16.
 */

@Singleton
public class DbHelper extends SQLiteOpenHelper {

    //USER TABLE
    public static final String SONG_TABLE_NAME = "song";
    public static final String SONG_COLUMN_ID = "id";
    public static final String SONG_COLUMN_NAME = "sng_name";
    public static final String SONG_COLUMN_PATH = "sng_path";
    public static final String SONG_COLUMN_ID_ALBUM = "sng_id_album";
    public static final String SONG_COLUMN_ID_AUTHOR = "sng_id_author";
    public static final String SONG_COLUMN_DURATION = "sng_duration";
    public static final String ALBUM_TABLE_NAME = "album";
    public static final String ALBUM_COLUMN_ID = "id";
    public static final String ALBUM_COLUMN_NAME = "alb_name";
    public static final String ARTIST_TABLE_NAME = "artist";
    public static final String ARTIST_COLUMN_ID = "id";
    public static final String ARTIST_COLUMN_NAME = "art_name";
    public static final String PLAYLIST_TABLE_NAME = "playlist";
    public static final String PLAYLIST_COLUMN_ID = "id";
    public static final String PLAYLIST_COLUMN_NAME = "pl_name";
    public static final String PLAYLISTSONG_TABLE_NAME = "playlistsong";
    public static final String PLAYLISTSONG_COLUMN_ID_PLAYLIST = "id_playlist";
    public static final String PLAYLISTSONG_COLUMN_ID_SONG = "id_song";

    @Inject
    public DbHelper(Context context,
                    String dbName,
                    Integer version) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        tableCreateStatements(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SONG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ALBUM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ARTIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYLIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYLISTSONG_TABLE_NAME);
        onCreate(db);
    }

    private void tableCreateStatements(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + SONG_TABLE_NAME + "("
                            + SONG_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + SONG_COLUMN_NAME + " VARCHAR, "
                            + SONG_COLUMN_PATH + " VARCHAR, "
                            + SONG_COLUMN_ID_ALBUM + " int DEFAULT -1, "
                            + SONG_COLUMN_ID_AUTHOR + " int DEFAULT -1, "
                            + SONG_COLUMN_DURATION + " int DEFAULT -1)"
            );
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + ALBUM_TABLE_NAME + "("
                            + ALBUM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + ALBUM_COLUMN_NAME + " VARCHAR"
            );
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + ARTIST_TABLE_NAME + "("
                            + ARTIST_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + ARTIST_COLUMN_NAME + " VARCHAR"
            );
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + PLAYLIST_TABLE_NAME + "("
                            + PLAYLIST_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + PLAYLIST_COLUMN_NAME + " VARCHAR"
            );
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + PLAYLISTSONG_TABLE_NAME + "("
                            + PLAYLISTSONG_COLUMN_ID_PLAYLIST + " INTEGER PRIMARY, "
                            + PLAYLISTSONG_COLUMN_ID_SONG + " INTEGER"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected <T> List<T> getAllItemsFrom(String tableName, Function<Cursor, List<T>> function){
        Cursor cursor = null;
        List<T> res = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM " + tableName, null);
            res = function.apply(cursor);
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return res;
    }

    Function<Cursor, List<Song>> findSongs = new Function<Cursor, List<Song>>() {
        @Override
        public List<Song> apply(Cursor cursor) {
            List<Song> res = null;
            while (cursor.moveToNext()){
                Song song = new Song(
                        cursor.getString(cursor.getColumnIndex(SONG_COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndex(SONG_COLUMN_DURATION)),
                        cursor.getString(cursor.getColumnIndex(SONG_COLUMN_PATH)),
                        cursor.getInt(cursor.getColumnIndex(SONG_COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(SONG_COLUMN_ID_AUTHOR)),
                        cursor.getInt(cursor.getColumnIndex(SONG_COLUMN_ID_ALBUM)), "", "");
                res.add(song);
            }
            return res;
        }
    };

    protected Long insertSong(Song song) throws Exception {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SONG_COLUMN_NAME, song.getName());
            contentValues.put(SONG_COLUMN_PATH, song.getPath());
            contentValues.put(SONG_COLUMN_NAME, song.getName());
            return db.insert(SONG_TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    public static abstract class Function<T,R>{
        public abstract R apply(T t);
    }
}