package com.inigo.player.exceptions;

/**
 * Created by inigo on 16/07/17.
 */

public class PlayerException extends Exception {
    public PlayerException(String msg, Exception e){
        super(msg, e);
    }
}
