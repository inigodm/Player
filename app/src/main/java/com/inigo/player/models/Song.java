package com.inigo.player.models;

/**
 * Created by inigo on 16/07/17.
 */

public class Song {
    private String name;
    private String author;
    private String album;
    private int duration;
    private String path;

    public String getPath() { return path; }

    public Song setPath(String path) { this.path = path; return this;}
    public String getName() {
        return name;
    }

    public Song setName(String name) {this.name = name; return this;}

    public String getAuthor() {
        return author;
    }

    public Song setAuthor(String author) { this.author = author; return this; }

    public String getAlbum() {
        return album;
    }

    public Song setAlbum(String album) { this.album = album; return this; }

    public int getDuration() {
        return duration;
    }

    public Song setDuration(int duration) { this.duration = duration; return this; }

    public String getTitle() {return name;};

    public String getSubtitle() {return new StringBuffer(author).append(" - ").append(album).toString();};
}
