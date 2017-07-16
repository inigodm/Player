/**
 * 
 */
package services;

import com.inigo.android.os.MediaManager;
import com.inigo.interfaces.IPlayesServiceOwner;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * @author inigo
 *
 */
public class PlayerServiceConnection implements ServiceConnection {
	PlayerService mBoundService;
	IPlayesServiceOwner ctx;
	public PlayerServiceConnection(Context ctx) {
		this.ctx = (IPlayesServiceOwner)ctx;
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
		//Log.v("service", "Service connection starts...");
        mBoundService = ((PlayerService.LocalBinder)service).getService();
        //Log.v("service", "Service connection ends with " + mBoundService.toString());
        MediaManager.refreshServiceBind();
        ctx.setOnCompletionGoToNext();
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
	public PlayerService getBoundService() {
		//Log.v("service", "Se pide el servicio bounded" + mBoundService);
		return mBoundService;
	}
}
