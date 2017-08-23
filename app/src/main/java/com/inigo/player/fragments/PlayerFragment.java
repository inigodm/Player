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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;

/**
     * A placeholder fragment containing a simple view.
     */
    public class PlayerFragment extends Fragment implements StatusListener{
        View rootView = null;
        PlayListLoader tskPLLoader;
        List<Song> datos = new ArrayList<>();
        @BindView(R.id.btnPause) Button pause;
        @BindView(R.id.btnPlay)Button play;
        @BindView(R.id.LstListadoPL)ListView playlist;
        @BindView(R.id.lblSongName)TextView songName;
        @BindView(R.id.lblTotalTime)TextView duration;
        @BindView(R.id.lblCurrentTime)TextView current;
        private Unbinder unbinder;

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
            unbinder = ButterKnife.bind(this, rootView);
            MediaManager.getInstance().setSongs(initData());
            MediaManager.getInstance().subscribe(this);
            setSpinnerVisible(true);
            return rootView;
        }

        @Override public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        @OnItemClick(R.id.LstListadoPL)
        public void onItemClick(int next ){
            MediaManager.getInstance().play(next);
        }

        @OnClick(R.id.btnPlay)
        public void play(){
            MediaManager.getInstance().play();
        }

        @OnClick(R.id.btnPause)
        public void pause(){
            MediaManager.getInstance().pause();
        }

        @OnClick(R.id.btnStop)
        public void stop(){
            MediaManager.getInstance().stop();
        }

        @OnClick(R.id.btnNext)
        public void next(){
            MediaManager.getInstance().next();
        }

        @OnClick(R.id.btnAnterior)
        public void previous(){
            MediaManager.getInstance().previous();
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