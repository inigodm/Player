package com.inigo.player.exceptions;

/**
 * Created by inigo on 18/07/17.
 */

public class ServiceException extends PlayerException {
    public ServiceException(String msg, Exception e){
        super(msg, e);
    }
}
