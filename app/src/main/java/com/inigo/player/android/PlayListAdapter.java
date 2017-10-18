package com.inigo.player.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.inigo.player.R;
import com.inigo.player.logics.playservices.MediaManager;
import com.inigo.player.models.Song;

import java.util.List;

/**
 * Created by inigo on 17/07/17.
 */

public class PlayListAdapter extends ArrayAdapter<Song> {
    public PlayListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public PlayListAdapter(Context context, int resource, List<Song> songs) {
        super(context, resource, songs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.playlist_item, null);
        }
        Song song = getItem(position);
        if (song != null) {
            TextView title = v.findViewById(R.id.LblTitulo);
            TextView subtitle = v.findViewById(R.id.LblSubTitulo);
            if (title != null) {
                title.setText(song.getTitle());
            }
            if (subtitle != null) {
                subtitle.setText(song.getSubtitle());
            }
        }
        return v;
    }
}