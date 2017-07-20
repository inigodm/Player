package com.inigo.player;

import android.content.ContentResolver;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.inigo.player.logics.tasks.playlistload.SongsLoader;
import com.inigo.player.models.TitleSubtitle;

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
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.inigo.player", appContext.getPackageName());
        List<TitleSubtitle> songs = new ArrayList<>();
        SongsLoader sl = new SongsLoader(appContext.getContentResolver(), songs);
        sl.run();
        assertTrue(songs.size() > 0);
    }
}
