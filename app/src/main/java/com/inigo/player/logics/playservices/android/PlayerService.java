package com.inigo.player.logics.playservices.android;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.inigo.player.exceptions.ServiceException;

import java.io.IOException;

/**
 * Created by inigo on 19/07/17.
 */

public class PlayerService extends Service {
    private final IBinder mBinder = new LocalBinder();
    boolean notificate = false;
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION_ID = 1234;//R.string.local_service_started;
    private enum state{
        PLAYING, STOPPED
    }
    private state STATE = state.STOPPED;
    static final MediaPlayer MP = new MediaPlayer();


    @Nullable
    @Override
    public IBinder onBind(Intent intent)  {
        return mBinder;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        notificate = false;
        // Estosoluciona un nullpointer exception
        if (intent != null && intent.getExtras() != null){
            notificate = intent.getExtras().getBoolean("notificate");
        }
        return START_REDELIVER_INTENT;
    }

    /**
     * Show a notification while this service is running.
     * @param ticket Texto principal de la notacion
     * @param subticket Texto secundario bajo el ticket
     * @param text Es el mensaje que sale cuando se pone la notificacion
     * @param foreground
     */
    @SuppressWarnings("deprecation")
    private void showNotification(String ticket, String subticket, String text, boolean foreground) {
        Notification noti = new Notification.Builder(this)
                .setContentTitle(ticket)
                .setSubText(subticket)
                .setSmallIcon(android.R.drawable.arrow_down_float)
                .build();
        noti.flags |= Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR;
        //Por que se manda la notificacion desde el servicio y no desde un contexto?
        startForeground(NOTIFICATION_ID, noti);
    }

    /**
     * Hace el start del mediaplayer
     */
    public void start(Song song) throws ServiceException{
        //Log.v("service", "ejecutando el start");
        try {
            prepareToPlay(song.getPath());
            showNotification("Marandina playing:", song.getTitle(), song.getTitle(), true);
            MP.start();
            STATE = state.PLAYING;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            throw new ServiceException("No se ha podido iniciar el servicio" , e);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("No se ha podido iniciar el servicio" , e);
        }
    }

    /** Se realiza la preparacion para hacer un play: reset + setDatasource + prepare
     * @param absfilename
     * @throws ServiceException
     */
    private void prepareToPlay(String absfilename) throws ServiceException{
        try {
            MP.reset();
            MP.setDataSource(absfilename);
            MP.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new ServiceException("No se ha podido iniciar el servicio" , e);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new ServiceException("No se ha podido iniciar el servicio" , e);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            throw new ServiceException("No se ha podido iniciar el servicio" , e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("No se ha podido iniciar el servicio" , e);
        }
    }

    public void stop() {
        if (STATE == state.PLAYING) {
            MP.stop();
            Log.w(getClass().getName(), "Got to stop()!");
            STATE = state.STOPPED;
        }
    }

    public void pause() {
        MP.pause();
    }

    /** Establece el listener de lo que tiene que hacerse cuando acabe una cancion
     * p.e continuar con la siguiente de un playlist
     * @param listener
     */
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        MP.setOnCompletionListener(listener);
    }
    /**
     * Clase para que las clases externas accedan al servicio. No tiene nada mas.
     */
    class LocalBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }

    class GoToNextOnCompletionListener implements MediaPlayer.OnCompletionListener {
        public void onCompletion(MediaPlayer mp) {
            //MediaManager.setActiveSong(MediaManager
            //        .getActiveSong() + 1);
        }
    }
}


