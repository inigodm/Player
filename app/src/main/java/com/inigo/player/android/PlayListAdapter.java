package com.inigo.player.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.inigo.player.R;
import com.inigo.player.models.TitleSubtitle;

import java.util.List;

/**
 * Created by inigo on 17/07/17.
 */

public class PlayListAdapter extends ArrayAdapter<TitleSubtitle> {
    public PlayListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public PlayListAdapter(Context context, int resource, List<TitleSubtitle> songs) {
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
        TitleSubtitle song = getItem(position);
        if (song != null) {
            TextView title = (TextView) v.findViewById(R.id.LblTitulo);
            TextView subtitle = (TextView) v.findViewById(R.id.LblSubTitulo);
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