package com.inigo.player.logics.playservices;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by inigo on 9/08/17.
 */

public class MediaManagerConnection implements ServiceConnection{
    MediaManager mBoundService = null;
    static MediaManagerConnection instance = new MediaManagerConnection();

    public static MediaManagerConnection getInstance(){
        if (instance == null){
            instance = new MediaManagerConnection();
        }
        return instance;
    }

    /* (non-Javadoc)
     * @see android.content.ServiceConnection#onServiceConnected(android.content.ComponentName, android.os.IBinder)
     */
    public void onServiceConnected(ComponentName className, IBinder service) {
        // This is called when the connection with the service has been
        // established, giving us the service object we can use to
        // interact with the service.  Because we have bound to a explicit
        // service that we know is running in our own process, we can
        // cast its IBinder to a concrete class and directly access it.
        mBoundService = ((MediaManager.LocalBinder)service).getService();
    }

    /* (non-Javadoc)
     * @see android.content.ServiceConnection#onServiceDisconnected(android.content.ComponentName)
     */
    public void onServiceDisconnected(ComponentName className) {
        // This is called when the connection with the service has been
        // unexpectedly disconnected -- that is, its process crashed.
        // Because it is running in our same process, we should never
        // see this happen.
        mBoundService = null;
    }

    /**
     * @return the mBoundService
     */
    public MediaManager getBoundService() {
        return mBoundService;
    }

    public void doBindService(Context ctx){
        if (mBoundService == null) {
            // Empezamos el servicio de modo que continue ejecutandose hasta un stopService() o que haga falta memoria.
            ComponentName myService = ctx.startService(new Intent(ctx, com.inigo.player.logics.playservices.MediaManager.class));
            //ctx.bindService(new Intent(ctx,com.android.services.PlayerService.class),mConnection, Context.BIND_ABOVE_CLIENT | Context.BIND_IMPORTANT|Context.BIND_DEBUG_UNBIND);
            // ligamos el servicio al contexto
            ctx.bindService(new Intent(ctx, com.inigo.player.logics.playservices.MediaManager.class), this, Context.BIND_AUTO_CREATE);
            mBoundService = this.getBoundService();
        }
    }


    public void doUnbindService(Context ctx) {
        // Detach our existing connection.
        ctx.unbindService(this);
    }
}
