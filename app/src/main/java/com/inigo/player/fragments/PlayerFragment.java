package com.inigo.player.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.inigo.player.R;
import com.inigo.player.android.PlayListAdapter;
import com.inigo.player.async.tasks.PlayListLoader;
import com.inigo.player.logics.StatusListener;
import com.inigo.player.logics.playservices.MediaManager;
import com.inigo.player.models.Song;
import com.inigo.player.models.Status;

import java.util.ArrayList;
import java.util.List;

/**
     * A placeholder fragment containing a simple view.
     */
    public class PlayerFragment extends Fragment implements StatusListener{
        View rootView = null;
        PlayListLoader tskPLLoader;
        List<Song> datos = new ArrayList<>();
        Button pause;
        Button play;
        ListView playlist;
        TextView songName;
        TextView duration;
        TextView current;
        public PlayerFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlayerFragment newInstance(Bundle bundle) {
            PlayerFragment fragment = new PlayerFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_play, container, false);
            MediaManager.getInstance().setSongs(initData());
            MediaManager.getInstance().subscribe(this);
            setSpinnerVisible(true);
            pause = rootView.findViewById(R.id.btnPause);
            play = rootView.findViewById(R.id.btnPlay);
            playlist = rootView.findViewById(R.id.LstListadoPL);
            songName = rootView.findViewById(R.id.lblSongName);
            duration = rootView.findViewById(R.id.lblTotalTime);
            current = rootView.findViewById(R.id.lblCurrentTime);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaManager.getInstance().play();
                }
            });
            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaManager.getInstance().pause();
                }
            });
            Button stop = rootView.findViewById(R.id.btnStop);
            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaManager.getInstance().stop();
                }
            });
            Button next = rootView.findViewById(R.id.btnNext);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaManager.getInstance().next();
                }
            });
            Button prev = rootView.findViewById(R.id.btnAnterior);
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaManager.getInstance().previous();
                }
            });
            final ListView playlist = rootView.findViewById(R.id.LstListadoPL);
            playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int next = adapterView.getPositionForView(view);
                    MediaManager.getInstance().play(next);
                }
            });
            return rootView;
        }

        @Override
        public void onUpdatedMediaPlayerStatus(Status status) {
            boolean mostrarPause = status.isPlaying() && !status.isPaused();
            play.setVisibility(mostrarPause ? View.GONE : View.VISIBLE);
            pause.setVisibility(mostrarPause ? View.VISIBLE : View.GONE);
            songName.setText(status.getAuthor() + "-" + status.getTitle());
            duration.setText(status.getDuration());
        }

        public List<Song> initData(){
            datos.clear();
            tskPLLoader = new PlayListLoader(this, datos);
            tskPLLoader.execute();
            return datos;
        }

        public void setSpinnerVisible(boolean spinnerVisible){
            LinearLayout ll = rootView.findViewById(R.id.lytContenedor);
            LinearLayout pl = rootView.findViewById(R.id.LstListadoPLLayout);
            ll.setVisibility(spinnerVisible ? View.VISIBLE : View.GONE);
            pl.setVisibility(!spinnerVisible ? View.VISIBLE : View.GONE);
        }

        public void fillPlayList(List<Song> datos){
            PlayListAdapter pla = new PlayListAdapter(getContext(), R.id.LstListadoPL, datos);
            playlist.setAdapter(pla);
        }

        public void refreshPlaylist() { fillPlayList(datos); }
    }