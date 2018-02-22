package media.cs4985.westga.edu.audiocontroller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.ListActivity;
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
import java.util.Collections;
import java.util.List;


public class MainActivity extends ListActivity {

    /**


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private MusicManager theManager;
    private EntryAdapter theAdapter;
    private ListView theListView;
    private ArrayList<Entry> theList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        FilePuller thePuller = new FilePuller();
        permissionChecker(Manifest.permission.READ_EXTERNAL_STORAGE);
        this.theManager = new MusicManager(thePuller.getAllMusic(), this);
        this.theListView = (ListView) findViewById(R.id.list);
        generate();
    }

    private void generate(){

        this.theList = new ArrayList<Entry>();

        for (File current : this.theManager.getMusicFiles()) {
            int trimIndex = current.getPath().lastIndexOf("/") + 1;
            Entry theEntry = new Entry(current.getPath().substring(trimIndex));
            theList.add(theEntry);
            System.out.println(theEntry);
        }


        this.theAdapter = new EntryAdapter(MainActivity.this, R.layout.entry_view, this.theList);
        setListAdapter(this.theAdapter);
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
        this.theManager = new MusicManager(thePuller.getAllMusic(), this);
    }

    public void pausePlay(View view) {
        this.theManager.playSong();
        this.updateSongPlayingText();
    }

    private void updateSongPlayingText() {
        TextView songName = findViewById(R.id.songName);
        String trimmedName = this.theManager.getCurrentSong().getPath().substring(this.theManager.getCurrentSong().getPath().lastIndexOf("/") + 1);
        songName.setText(trimmedName);
    }

    public void skipToPreviousSong(View view) {
        this.theManager.prevSong();
        this.updateSongPlayingText();
    }

    public void skipToNextSong(View view) {
        this.theManager.nextSong();
        this.updateSongPlayingText();
    }
    /**
     * A placeholder fragment containing a simple view.
     */
}