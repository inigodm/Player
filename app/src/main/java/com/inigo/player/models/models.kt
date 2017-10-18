package com.inigo.player.models

/**
 * Created by inigo on 8/09/17.
 */
data class Artist(val id: Int = 0, val name: String = "")
data class Album(val id: Int = 0, val name: String = "")
data class PlayList(val id: Int, val name: String)
data class PlayListSongs (val id_playlist: Int, val id_song: Int)
