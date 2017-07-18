package services;

import java.util.ArrayList;

import com.inigo.customItems.Cancion;
import com.inigo.customItems.Itemizable;
import com.inigo.customItems.PlayList;
import com.inigo.customexceptions.ServiceException;
import com.inigo.services.PlayerService;
import com.inigo.services.PlayerServiceConnection;
import com.inigo.tasks.SongFinderTask;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.audiofx.Equalizer;
import android.util.Log;

public class MediaManager {
	static boolean isPaused = true;
	public static final ArrayList<Cancion> PLAYLISTS = new ArrayList<Cancion>();
	public static final ArrayList<Cancion> SONGS = new ArrayList<Cancion>();
	public static final ArrayList<PlayList> ARTISTS = new ArrayList<PlayList>();
	public static final ArrayList<PlayList> ALBUMS = new ArrayList<PlayList>();
	static final ArrayList<PlayList> CUSTOMPLAYLISTS = new ArrayList<PlayList>();
	public static final ArrayList<PlayList> SONGSPLAYLIST = new ArrayList<PlayList>();
	static final ArrayList<Itemizable> EDITPLAYLISTITEMIZABLES = new ArrayList<Itemizable>();
	static Context context = null;
	
	public static final int TYPEARTIST = 0;
	public static final int TYPEALBUM = 1;
	public static final int TYPESONG = 2;
	public static final int TYPEPLAYLIST = 3;
	public static final int TYPEEDITPLAYLISTADDSONGS = 4;
	public static final int TYPEEDITPLAYLISTREMOVESONGS = 5;
	private static final boolean TYPEREMOVE = false;
	private static final boolean TYPEADD = true;
	private static boolean isBound = true;
	private static PlayerServiceConnection mConnection;// = new PlayerServiceConnection();
	private static PlayerService playerService;
	public static final short[] bands = new short[10];
	static{
		for (short i = 0; i < 10; i++){
			bands[i] = 100;
		}
	}
	static int type = -1;
	static boolean isAdd = false;
	static int artist = -1;
	static int album = -1;
	static int song = -1;
	static int playlist = -1;
	static Cancion currentCancion;
	static String currentSongName;
	static CharSequence currentSongLength;
	static int currentSongDuration;
	static String currentPosition;
	static int currentPositionInMilis;
	/**
	 * @return the type 
	 */
	public static int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public static void setType(int type) {
		MediaManager.type = type;
		MediaManager.isAdd = MediaManager.TYPEREMOVE;
		if (type == MediaManager.TYPEEDITPLAYLISTADDSONGS){
			MediaManager.isAdd = MediaManager.TYPEADD;
			MediaManager.type = MediaManager.TYPESONG;
		}else if (type == MediaManager.TYPEEDITPLAYLISTREMOVESONGS){
			MediaManager.isAdd = MediaManager.TYPEREMOVE;
			MediaManager.type = MediaManager.TYPESONG;
		}
	}
	/**
	 * @return the addOrRemove
	 */
	public static boolean isAdding() {
		return isAdd;
	}
	/**
	 * @param addOrRemove the addOrRemove to set
	 */
	public static void setAdding(boolean addOrRemove) {
		MediaManager.isAdd = addOrRemove;
	}
	/**
	 * @return the song
	 */
	public static int getSong() {
		return song;
	}
	/**
	 * @return the artist
	 */
	public static int getArtist() {
		return artist;
	}

	/**
	 * @param artist the artist to set
	 */
	public static void setArtist(int artist) {
		MediaManager.artist = artist;
	}

	/**
	 * @return the album
	 */
	public static int getAlbum() {
		return album;
	}

	/**
	 * @param album the album to set
	 */
	public static void setAlbum(int album) {
		MediaManager.album = album;
	}
	
	/**
	 * @param index
	 */
	public static void removeCustomPlaylist(PlayList playlist){
		CUSTOMPLAYLISTS.remove(playlist);
	}

	static int activeSong = 0;
	static boolean isPlaying = false;
	static String actualPath="";
	
	public static void doBindService(Context ctx){
		mConnection = new PlayerServiceConnection(ctx);
	    // Empezamos el servicio de modo que continue ejecutandose hasta un stopService() o que haga falta memoria.
		ComponentName myService = ctx.startService(new Intent(ctx,com.inigo.services.PlayerService.class));
	    //ctx.bindService(new Intent(ctx,com.android.services.PlayerService.class), 
	    //		mConnection, Context.BIND_ABOVE_CLIENT | Context.BIND_IMPORTANT|Context.BIND_DEBUG_UNBIND);
		// ligamos el servicio al contexto
	    ctx.bindService(new Intent(ctx,com.inigo.services.PlayerService.class), 
	    		mConnection, Context.BIND_AUTO_CREATE);
	    playerService = mConnection.getBoundService();
	    //Log.v("service", "obteniendo el player del bind: " + playerService);
		isBound = true;
		context = ctx;
	}
	
	public static void refreshServiceBind(){
		playerService = mConnection.getBoundService();
	}
	

	public static void doUnbindService(Context ctx) {
	    if (isBound) {
	        // Detach our existing connection.
	        ctx.unbindService(mConnection);
	        isBound = false;
	        playerService = null;
	        // Log.v("service", "desbndando la activity");
	    }
	}
	public static void setOnCompletionListener(OnCompletionListener listener){
		playerService.setOnCompletionListener(listener);
		//mp.setOnCompletionListener(listener);
	}
	
	/**
	 * @return the isPaused
	 */
	public static boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * @return the isPlaying
	 */
	public static boolean isPlaying() {
		return isPlaying;
	}
	/**
	 * @return the activeSong
	 */
	public static int getActiveSong() {
		return activeSong;
	}

	/**
	 * @param activeSong the activeSong to set
	 */
	public static void setActiveSong(int activeSong) {
		// Si se cambia de cancion se resetea el pause
		if(MediaManager.isPlaying()) {
		    MediaManager.pauseMedia();
		}
		if (MediaManager.activeSong != activeSong) {
			isPaused = false;
		}
		if ((activeSong > PLAYLISTS.size() - 1) || (activeSong < 0)) {
			activeSong = 0;
		}
		MediaManager.activeSong = activeSong;
		Log.d("DEBUGEANDO", activeSong + "=" + PLAYLISTS.get(activeSong).getTitle());
		updateSongStaticInfo();
	}


	public static final String[] retrieveSongMetadata(String absfilename){
		String[] res = null;
		try {
			MediaMetadataRetriever mmdr = new MediaMetadataRetriever();
			mmdr.setDataSource(absfilename);
			
			res = new String[]{nz(mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)),
					nz(mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)),
					nz(mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)),
					nz(mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)),
					nz(mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)),
					nz(mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))};
			mmdr.release();
		} catch (IllegalArgumentException e) {
			Log.e("", absfilename);
			throw e;
		}
		return res;
	}
	
	public static final String nz(String value){
		//return value == null?"no data":value;
		return value == null?"no data":value;
		/*String decomposed = Normalizer.normalize(value, Form.NFD);
        // removing diacritics
        String removed = decomposed.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		return removed;*/
		//return Normalizer.normalize(value == null?"no data":value, Form.NFC).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		//return Normalizer.normalize(value == null?"no data":value, Form.NFKC).replaceAll("[\\p{Latin}+\\p{Common}+\\p{Inherited}+]", "");
	}
	
	public static final void playMedia(String absfilename){
			try {
				actualPath = absfilename;
				if (!isPaused || !isPlaying){
					playerService.prepareToPlay(absfilename);
					//mp.reset();
					//mp.setDataSource(absfilename);
					//mp.prepare();
				}
				isPlaying = true;
				playerService.start();
				playerService.setPlaylist(PLAYLISTS);
				//mp.start();
				isPaused = false;
			} catch (ServiceException e) {
				e.printStackTrace();
			}
	}
	
	public static final void pauseMedia(){
		playerService.pause();
		//mp.pause();
		isPaused = true;
	}
	
	public static final void playMedia(){
		playMedia(activeSong);
	}
	
	public static final void playMedia(Cancion can){
		playMedia(can.getPath() + "/" + can.getFileName());
	}
	
	public static final void playNext(){
		playerService.stop();
		//mp.stop();
		if (activeSong < PLAYLISTS.size()-1){
			activeSong++;
			playMedia(activeSong);
		}else{
			activeSong = 0;
		}
	}
	
	public static final void playMedia(int index){
		activeSong = index;
		playMedia(PLAYLISTS.get(index).getPath() + "/" + PLAYLISTS.get(index).getFileName());
	}
	
	/**
	 * Actualiza los datos estaticos de la cancion: nombre, duracion.... 
	 */
	public static void updateSongStaticInfo(){
		currentSongName = "";
		currentCancion = PLAYLISTS.get(activeSong);
		currentSongName = currentCancion.getTitle();
		currentSongLength = currentCancion.getDurationInMinutes();
		currentSongDuration = currentCancion.getDuration();
	}
	
	/**
	 *  Actualiza los datos de tiempo transcurrido
	 */
	public static void updateSongDinamicInfo(){
		int act = playerService.getCurrentPosition()/1000;
		String temp = (int)(act)%60 + "";
		temp = "00".substring(0, 2-temp.length()) + temp;
		currentPosition = (int)act/60 + ":" + temp;
		if (playerService == null){
			refreshServiceBind();
		}
		currentPositionInMilis = playerService.getCurrentPosition();
	
	}
	
	public static final Cancion getCurrentCancion(){
		return currentCancion;
	}
	
	public static final String getCurrentSongName(){
		return currentSongName;
	}

	public static final CharSequence getCurrentSongLength() {
		return currentSongLength;
	}
	
	public static int getCurrentSongDuration() {
		return currentSongDuration;
	}
	
	public static String getCurrentPosition(){
		return currentPosition;
	}
	
	public static int getCurrentPositionInMilis(){
		return currentPositionInMilis;
	}

	public static void setPaused(boolean b) {
		isPaused = b;
	}

	public static void setCurrentPositionInMilis(int progress) {
		playerService.setCurrentPosition(progress);
		//mp.seekTo(progress);
	}
	
	public static void reset(){
		if (isPlaying){
			playerService.stop();
			//mp.stop();
		}
		isPlaying = false;
		isPaused = true;
		PLAYLISTS.clear();
		activeSong = 0;
		isPlaying = false;
		SongFinderTask.reset();
		SongFinderTask.setRethrowMoreSearchs(true);
		//int i = 1/0;
	}

	public static void stopPlaying() {
		if (isPlaying){
			playerService.stop();
			//context.stopService(new Intent(context,com.android.services.PlayerService.class));
			//mp.stop();	
			isPlaying = false;
		}
	}

	public static void stopOrStartPlaying(Cancion can) {
		if (actualPath.equals(can.getPath() + "/" + can.getFileName())){
			stopPlaying();
		}else{
			playMedia(can);
		}
		
	}
	
	public static final void playMediaAndActualizeActiveSong(String pathcan){
		//activeSong = playLists.indexOf(can);
		playMedia(pathcan);
	}
	public static void setSong(int id_song) {
		song = id_song;
	}
	/**
	 * @return the playlist
	 */
	public static int getPlaylist() {
		return playlist;
	}
	/**
	 * @param playlist the playlist to set
	 */
	public static void setPlaylist(int playlist) {
		MediaManager.playlist = playlist;
	}
	
	//TODO: Inmplementar
	public static int getNumberOfSongsOfPlayList(int id_playlist) {
		return 0;
	}

	/**
	 * @return the EDITPLAYLISTITEMIZABLES
	 */
	public static ArrayList<Itemizable> getItemizables() {
		return EDITPLAYLISTITEMIZABLES;
	}
	
	/** Establece el valor adecuado para la banda del ecualizador
	 * @param band
	 * @param progress
	 */
	public static void setBand(short band, short level) {
		PlayerService.setBand(band, level);
	}
	
	public static Equalizer getEqualizer() {
		if (playerService != null){
			return playerService.getEqualizer();
		}else{
			return null;
		}
	}
	public static void setBoostStrength(short progress) {
		PlayerService.setBassBoost(progress);
	}
	public static void setEQEnabled(boolean b) {
		PlayerService.setEQEnabled(b);
	}
}
