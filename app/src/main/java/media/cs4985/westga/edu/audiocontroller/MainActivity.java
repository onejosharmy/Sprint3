package media.cs4985.westga.edu.audiocontroller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

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
    private MusicManager theManager;
    private EntryAdapter theAdapter;
    private ListView theListView;
    private ArrayList<Entry> theList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        //mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        FilePuller thePuller = new FilePuller();
        permissionChecker(Manifest.permission.READ_EXTERNAL_STORAGE);
        this.theManager = new MusicManager(thePuller.getAllMusic(),this);
        this.theList = new ArrayList<Entry>();

        for(File current:this.theManager.getMusicFiles()){
            int trimIndex = current.getPath().lastIndexOf("/") + 1;
            Entry theEntry = new Entry(current.getPath().substring(trimIndex));
            theList.add(theEntry);
        }

    }


    private void permissionChecker(String permission) {
        int permissionGranted = ContextCompat.checkSelfPermission(this, permission);

        if (permissionGranted == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        }

    }

    public ArrayList<Entry> getTheList() {
        return theList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public ListView getTheListView() {
        return theListView;
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

    public void doTheSearch(View view) {
        FilePuller thePuller = new FilePuller();
        this.theManager = new MusicManager(thePuller.getAllMusic(),this);
    }

    public void pausePlay(View view) {
        this.theManager.playSong();
        this.updateSongPlayingText();
    }

    private void updateSongPlayingText() {
        TextView songName = findViewById(R.id.songName);
        String trimmedName = this.theManager.getCurrentSong().getPath().substring(this.theManager.getCurrentSong().getPath().lastIndexOf("/")+1);
        songName.setText(trimmedName);
    }

    public void skipToPreviousSong(View view){
        this.theManager.prevSong();
        this.updateSongPlayingText();
    }

    public void skipToNextSong(View view){
        this.theManager.nextSong();
        this.updateSongPlayingText();
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);

            ListView theListView = (ListView) rootView.findViewById(R.id.listViewPlay);
            ArrayList<Entry> theList = new MainActivity().getTheList();
            System.out.println(theList.size());
            EntryAdapter theAdapter = new EntryAdapter(rootView.getContext(), R.layout.entry_view, theList);
            theListView.setAdapter(theAdapter);
            //TextView textView = (TextView) rootView.findViewById(R.id.playListView);
            //textView.setText(getString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           Fragment fragment = null;

           switch (position){
               case 0:
                   fragment = new FileFragment();
                   break;
               case 1:
                   fragment = new PlaylistFragment();
                   break;
               case 2:
                   fragment = new OptionsFragment();
           }
           return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
