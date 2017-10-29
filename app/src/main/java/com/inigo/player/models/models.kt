package com.inigo.player.models

import kotlin.reflect.KProperty

/**
 * Created by inigo on 8/09/17.
 */
data class Artist(val id: Int = 0, val name: String = "")
data class Album(val id: Int = 0, val name: String = "")
data class PlayListSongs (val id_playlist: Int, val id_song: Int)

/*class PlayList {
    var name: String  = ""
    var id: Int = 0
    var id_type: Int = 0
}*/