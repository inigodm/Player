/**
 * 
 */
package services;

import java.io.IOException;
import java.util.List;

import com.inigo.player.MainActivity;
import com.inigo.player.exceptions.ServiceException;
import com.inigo.player.models.Song;

import android.R;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * @author inigo
 *
 */
public class PlayerService extends Service {
	boolean isPlaying = true;
	static List<Song> playLists;
	private final IBinder mBinder = new LocalBinder();
	boolean notificate = false;
	//private NotificationManager mNM;
	static final MediaPlayer MP = new MediaPlayer();
	static final Equalizer EQUALIZER = new Equalizer(0, MP.getAudioSessionId());
	static final BassBoost BASSBOOST = new BassBoost(0, MP.getAudioSessionId());
	static int numBands = EQUALIZER.getNumberOfBands();
	private static short[] bands = MediaManager.bands;
	static short eqMax;
	static short eqMin;
	static final short bbMin = 0;
	static final short bbMax = 1000;
	static short bbValue = 1000;
	static{
		for (int i = 0; i < 10; i++){
			bands[i] = 50;
		}
	}
	// Unique Identification Number for the Notification.
	// We use it on Notification start, and to cancel it.
	private int NOTIFICATION_ID = 1234;//R.string.local_service_started;

	/**
	 * Clase para que las clases externas accedan al servicio. No tiene nada mas.
	 */
	public class LocalBinder extends Binder {
		PlayerService getService() {
			return PlayerService.this;
		}
	}
	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	public void onCreate(){
		//Log.v("service", "oncreatestart");
		//mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		// Display a notification about us starting.  We put an icon in the status bar.
		//showNotification("Marandina","", "el texto es este", true);
		//Log.v("service", "oncreateend");
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
				.setSmallIcon(R.drawable.arrow_down_float)
				.build();
	}
	private void showNotification(String ticket, String subticket, String text, boolean foreground) {
		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.arrow_down_float, text,
				System.currentTimeMillis());
		// Creamos un intent con el ActListadoCancionesAsync pero especificando que si esta creado use el que esta creado
		Intent i = new Intent(this, MainActivity.class);
		//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| //esto tambien funcionaba...Intent.FLAG_ACTIVITY_NEW_TASK|
		//		//Intent.FLAG_ACTIVITY_CLEAR_TASK|
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// The PendingIntent to launch our activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				i, 0);//new Intent(this, ActListadoCancionesAsync.class), 0);
		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, ticket,
				subticket, contentIntent);
		/* UUUUPS! esto es a partir de la API 16
		Builder builder = new Builder(this);
		builder.setTicker(ticket);
		builder.setContentText(subticket);
		builder.setContentIntent(contentIntent);
		builder.setAutoCancel(false);
		builder.setContentTitle(text);
		notification = builder.build();*/
		notification.flags|=Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR;
		// Send the notification.
		startForeground(NOTIFICATION_ID, notification);
		//Si esta en el la pantalla se pone, sino tambien...
		//if (foreground){
		//	startForeground(NOTIFICATION_ID, notification);
		//}else{
		//	startForeground(NOTIFICATION_ID, notification);
			//mNM.notify(NOTIFICATION, notification);
		//}
	}
	
	/**
	 * Hace el start del mediaplayer 
	 */
	public void start(){
		//Log.v("service", "ejecutando el start");
		try {
			BASSBOOST.setStrength(bbValue);
			BASSBOOST.setEnabled(true);
			EQUALIZER.setEnabled(true);
			eqMax = EQUALIZER.getBandLevelRange()[1];
			eqMin = EQUALIZER.getBandLevelRange()[0];
			equalizeMusic();
			String songName = MediaManager.getCurrentSongName();
			showNotification("Marandina playing:", songName, songName, true);
			MP.start();
			isPlaying = true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			Log.e("ERROR", "ERROR 38!!!!");
			e.printStackTrace();
		} 
	}
	
	public static void equalizeMusic(){
		for (short i = 0; i < numBands; i++){
			EQUALIZER.setBandLevel(i, (short)( (eqMax/100) *bands[i] + eqMin));
		}
		 if (BASSBOOST.getStrengthSupported()){
			 BASSBOOST.setStrength(bbValue);
		 }
	}

	/** Se realiza la preparacion para hacer un play: reset + setDatasource + prepare
	 * @param absfilename
	 * @throws ServiceException 
	 */
	public void prepareToPlay(String absfilename) throws ServiceException{
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
		//Log.v("service", "ejecutando el stop");
		if (isPlaying) {
			MP.stop();
			Log.w(getClass().getName(), "Got to stop()!");
			isPlaying=false;
			//mNM.cancel(NOTIFICATION);
			//stopForeground(true);
		}
	}

	public void pause() {
		MP.pause();
	}


	/**Devuelve la posicion, en milisegundos, dentro de la cancion actual
	 * @return
	 */
	public int getCurrentPosition(){
		return  MP.getCurrentPosition();
	}

	/** Establce la posicion en milisegndos de la reproduccion actual
	 * @param progress
	 */
	public void setCurrentPosition(int progress) {
		MP.seekTo(progress);
	}

	/** Establece el listener de lo que tiene que hacerse cuando acabe una cancion
	 * p.e continuar con la siguiente de un playlist
	 * @param listener
	 */
	public void setOnCompletionListener(OnCompletionListener listener) {
		MP.setOnCompletionListener(listener);
	}

	/**
	 * @param playLists
	 */
	public void setPlaylist(ArrayList<Cancion> playLists){
		this.playLists = playLists;
	}
	
	public Equalizer getEqualizer(){
		return EQUALIZER;
	}

	public static void setBand(short band, short level) {
		bands[band]=level;
		if (EQUALIZER != null){
			EQUALIZER.setBandLevel(band, level);
			equalizeMusic();
			
		}
	}
	
	public static void setBassBoost(short level) {
		bbValue = level;
		if (BASSBOOST != null){
			equalizeMusic();
			
		}
	}
	
	public static short[] getBands(){
		return bands;
	}

	public static void setEQEnabled(boolean b) {
		if (EQUALIZER!=null){
			EQUALIZER.setEnabled(b);
		}
		if (BASSBOOST.getEnabled()){
			BASSBOOST.setEnabled(b);
		}
		
	}
	
}

class GoToNextOnCompletionListener implements OnCompletionListener {
	public void onCompletion(MediaPlayer mp) {
		MediaManager.setActiveSong(MediaManager
				.getActiveSong() + 1);
	}
}