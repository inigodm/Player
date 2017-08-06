/**
 * 
 */
package com.inigo.player.receivers;

import com.inigo.player.logics.playservices.MediaManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;


/**
 * Pausa la ejecucion si llega una llamada o si se quitan los cascos.
 * @author inigo
 *
 */
public class GlobalReceiver extends BroadcastReceiver {
	private static final int KEYCODE_MEDIA_PLAY = 126;
	private static final int KEYCODE_MEDIA_PAUSE = 127;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
			onRemoteControlRecived(context, intent);
		}	else {
			onPauseEventReceived(context, intent);
		}
	}
	
	private void onPauseEventReceived(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if(null == bundle)
			return;
		// Se han desconectado los auriculares??
		if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())
				|| Intent.ACTION_HEADSET_PLUG.equals(intent.getAction())
				|| bundle.getString(TelephonyManager.EXTRA_STATE).equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
			MediaManager.getInstance().pause();
		}
	}
	
	private void onRemoteControlRecived(Context context, Intent intent) {
		KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
		if (event.getAction() == KeyEvent.ACTION_DOWN)
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE: {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					MediaManager.getInstance().play();
				}
				return;
			}
			case KEYCODE_MEDIA_PLAY: {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					MediaManager.getInstance().play();
				}
				return;
			}
			case KEYCODE_MEDIA_PAUSE: {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					MediaManager.getInstance().pause();
				}
				return;
			}
			case KeyEvent.KEYCODE_MEDIA_NEXT: {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					MediaManager.getInstance().next();
				}
				return;
			}
			case KeyEvent.KEYCODE_MEDIA_PREVIOUS: {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					MediaManager.getInstance().previous();
				}
				return;
			}
			case KeyEvent.KEYCODE_MEDIA_STOP: {
				Log.v(this.getClass().getName(), "KEYCODE_MEDIA_STOP");
				return;
			}
			// Cuando se pulsa el boton de los cascos
			case KeyEvent.KEYCODE_HEADSETHOOK: {
				// Viene el evento down y el up consecutivamente, tomo el down
				// como activador
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					MediaManager.getInstance().invertPauseOrPlay();
				}
				return;
			}
		}
	}
}
