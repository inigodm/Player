package com.inigo.player;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.inigo.player.async.tasks.PlayListLoader;
import com.inigo.player.logics.playservices.MediaManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void testSongLoader() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.inigo.player", appContext.getPackageName());
        List<Song> songs = new ArrayList<>();
        PlayListLoader sl = new PlayListLoader(appContext.getContentResolver(), songs);
        sl.doInBackground("");
        assertTrue(songs.size() > 0);
    }

    @Test
    public void testMediaManager() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        MediaManager mm = MediaManager.getInstance();
        List<Song> songs = new ArrayList<>();
        mm.setSongs(songs);
        mm.play();
        assertFalse(mm.isPlaying());
        PlayListLoader sl = new PlayListLoader(appContext.getContentResolver(), songs);
        sl.doInBackground("");
        mm.play();
        assertTrue(mm.isPlaying());
        mm.stop();
        assertFalse(mm.isPlaying());
        mm.play();
        assertTrue(mm.isPlaying());
        mm.pause();
        assertTrue(mm.isPlaying());
        mm.next();
        assertTrue(mm.isPlaying());
    }
}
