package com.inigo.player;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inigo.player.android.PlayListAdapter;
import com.inigo.player.exceptions.ServiceException;
import com.inigo.player.logics.playservices.MediaManager;
import com.inigo.player.logics.tasks.PlayListLoader;
import com.inigo.player.logics.tasks.StatusListener;
import com.inigo.player.logics.tasks.playlistload.SongsLoader;
import com.inigo.player.models.Song;
import com.inigo.player.models.Status;
import com.inigo.player.models.TitleSubtitle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private static final String ARG_STRING= "section_string";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), new Bundle());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlayerFragment extends Fragment implements StatusListener{
        View rootView = null;
        PlayListLoader tskPLLoader;
        List<TitleSubtitle> datos = new ArrayList<>();
        View selectedView = null;
        Button pause;
        Button play;
        ListView playlist;
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
            play.setVisibility((!status.isPlaying() && !status.isPaused()) ? View.VISIBLE : View.GONE);
            pause.setVisibility((status.isPlaying() && !status.isPaused()) ? View.VISIBLE : View.GONE);
        }

        public List<TitleSubtitle> initData(){
            datos.clear();
            tskPLLoader = new PlayListLoader(this, new SongsLoader(this.getContext().getContentResolver(), datos));
            tskPLLoader.execute();
            return datos;
        }

        public List<TitleSubtitle> setData(List<TitleSubtitle> newData){
            datos.clear();
            datos.addAll(newData);
            return datos;
        }

        public void setSpinnerVisible(boolean spinnerVisible){
            LinearLayout ll = rootView.findViewById(R.id.lytContenedor);
            LinearLayout pl = rootView.findViewById(R.id.LstListadoPLLayout);
            ll.setVisibility(spinnerVisible ? View.VISIBLE : View.GONE);
            pl.setVisibility(!spinnerVisible ? View.VISIBLE : View.GONE);
        }

        public void fillPlayList(View rootView, List<TitleSubtitle> datos){
            PlayListAdapter pla = new PlayListAdapter(getContext(), R.id.LstListadoPL, datos);
            playlist.setAdapter(pla);
        }

        public void refreshPlaylist() {
            fillPlayList(rootView, datos);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        Bundle bundle;
        List<Fragment> pf = new ArrayList<>(3) ;

        public SectionsPagerAdapter(FragmentManager fm, Bundle bundle) {
            super(fm);
            this.bundle = bundle;
            pf.add(PlayerFragment.newInstance(bundle));
            pf.add(new LoadingFragment());
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (pf.get(position) == null){
                pf.set(position, PlayerFragment.newInstance(bundle));
            }
            return pf.get(position);
        }

        @Override
        public int getCount(){
            return pf.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Title";
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class LoadingFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public LoadingFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static LoadingFragment newInstance(Bundle bundle) {
            return new LoadingFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.loading, container, false);
            //final EditText txtNombre = (EditText) rootView.findViewById(R.id.txtNombre);
            return rootView;
        }
    }
}
