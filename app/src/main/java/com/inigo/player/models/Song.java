package com.inigo.player.models;

/**
 * Created by inigo on 16/07/17.
 */

public class Song{
    private String name;
    private String author;
    private String album;
    private int duration;
    private String path;

    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {return name;};

    public String getSubtitle() {return new StringBuffer(author).append(" - ").append(album).toString();};

    public static Song buildDummieSong() {
        Song s = new Song();
        s.setDuration(0);
        s.setAlbum("-");
        s.setAuthor("-");
        s.setName("-");
        s.setPath("-");
        return s;
    }
}
