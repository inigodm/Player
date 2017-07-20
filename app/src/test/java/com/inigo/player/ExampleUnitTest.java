package com.inigo.player;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.test.mock.MockContentProvider;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void globalLoadingIsCorrect(){
        OneQueryMockContentProvider cp = new OneQueryMockContentProvider();
        String[] data = {"titulo", "album", "artista", "0", "path"};
        String[] projection = new String[]{MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };
        MatrixCursor matrixCursor = new MatrixCursor(projection);
        matrixCursor.addRow(data);
    }
}

class OneQueryMockContentProvider extends MockContentProvider {
    private Cursor queryResult;

    public void addQueryResult(Cursor expectedResult) {
        this.queryResult = expectedResult;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return this.queryResult;
    }
}