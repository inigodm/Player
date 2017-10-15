package com.inigo.player.models

/**
 * Created by inigo on 8/09/17.
 */
data class Artist(val id: Int = 0, val name: String = "")
data class Album(val id: Int = 0, val name: String = "")
data class Song(
        val name: String = "", val duration: Int = 0, val path: String = "", val id: Int = 0, val artistId: Int = 0,
        val albumId: Int = 0, val Artist: String = "", val Album: String = "")
data class PlayList(val id: Int, val name: String)
data class PlayListSongs (val id_playlist: Int, val id_song: Int)
