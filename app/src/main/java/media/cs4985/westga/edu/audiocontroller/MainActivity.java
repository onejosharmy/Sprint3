package media.cs4985.westga.edu.audiocontroller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.app.ListActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends ListActivity {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private MusicManager theManager;
    private EntryAdapter theAdapter;
    private ListView theListView;
    private boolean isEditing;
    private boolean isAdding;
    private boolean isDeleting;
    private ArrayList<Entry> theCurrentList;
    private ArrayList<Entry> theCurrentPlaylistThatIsPlaying;
    private ArrayList<Entry> thePlaylistNameList;
    private ArrayList<ArrayList<Entry>> arraylistPlaylistList;
    private int songIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FilePuller thePuller = new FilePuller();
        permissionChecker(Manifest.permission.READ_EXTERNAL_STORAGE);
        this.theManager = new MusicManager(this);
        this.theListView = (ListView) findViewById(android.R.id.list);
        this.thePlaylistNameList = new ArrayList<>();
        this.arraylistPlaylistList = new ArrayList<>();
        this.isEditing = false;
        this.isAdding = false;
        this.isDeleting = false;
        generate(new Entry("dummy", 0, false));
        this.songIndex = 0;
        this.theCurrentPlaylistThatIsPlaying = new ArrayList<>();

    }

    private ArrayList<Entry> FilesToEntriesArraylist(ArrayList<File> files) {
        ArrayList<Entry> theEntriesPlease = new ArrayList<>();
        for (File current : files) {
            int trimIndex = current.getPath().lastIndexOf("/") + 1;
            Entry theEntry = new Entry(current.getPath().substring(trimIndex), current, false);
            theEntriesPlease.add(theEntry);
            System.out.println(theEntry);
        }
        return theEntriesPlease;
    }


    private void generate(Entry selected) {

        this.theCurrentList = new ArrayList<Entry>();
        if (this.thePlaylistNameList.size() == 0) {
            FilePuller thePuller = new FilePuller();
            ArrayList<File> musicFiles = thePuller.getAllMusic();
            this.theCurrentList = this.FilesToEntriesArraylist(musicFiles);
            this.thePlaylistNameList.add(new Entry("Default Playlist", 0, true));
            this.arraylistPlaylistList.add(theCurrentList);
            this.theCurrentList = thePlaylistNameList;
            Button button = findViewById(R.id.button);
            Button button1 = findViewById(R.id.button1);
            Button button2 = findViewById(R.id.button2);
            button.setVisibility(View.VISIBLE);
            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
        } else if (selected.isPlaylist()) {
            this.theCurrentList = this.arraylistPlaylistList.get(selected.getPosition());
            this.theCurrentPlaylistThatIsPlaying = this.theCurrentList;
            Button button = findViewById(R.id.button);
            Button button1 = findViewById(R.id.button1);
            Button button2 = findViewById(R.id.button2);
            button.setVisibility(View.INVISIBLE);
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
        } else if (this.isEditing) {
            this.theCurrentList = this.arraylistPlaylistList.get(0);
            this.thePlaylistNameList.add(new Entry("NewPlaylist" + this.thePlaylistNameList.size(), this.thePlaylistNameList.size(), true));
            this.arraylistPlaylistList.add(new ArrayList<Entry>());
            Button button = findViewById(R.id.button);
            Button button1 = findViewById(R.id.button1);
            Button button2 = findViewById(R.id.button2);
            button.setVisibility(View.VISIBLE);
            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
        } else {
            this.theCurrentList = this.thePlaylistNameList;
            Button button = findViewById(R.id.button);
            Button button1 = findViewById(R.id.button1);
            Button button2 = findViewById(R.id.button2);
            button.setVisibility(View.VISIBLE);
            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
        }

        this.theAdapter = new EntryAdapter(MainActivity.this, R.layout.entry_view, this.theCurrentList);
        setListAdapter(this.theAdapter);
    }


    private void permissionChecker(String permission) {
        int permissionGranted = ContextCompat.checkSelfPermission(this, permission);

        if (permissionGranted == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        }

    }

    public ArrayList<Entry> getTheCurrentList() {
        return theCurrentList;
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

    public void pausePlay(View view) {
        this.theManager.playPause();
        this.updateSongPlayingText();
        //this.updateSongTimeText();
    }

    private void updateSongPlayingText() {
        TextView songName = findViewById(R.id.songName);
        String trimmedName = this.theManager.getCurrentSong().getPath().substring(this.theManager.getCurrentSong().getPath().lastIndexOf("/") + 1);
        songName.setText(trimmedName);

    }

    private void updateSongTimeText() {
        TextView songTime = findViewById(R.id.songTime);
        int timeText = this.theManager.getSongTime();
        songTime.setText(timeText);
    }

    public void skipToPreviousSong(View view) {
        if (this.songIndex <= 0) {

        } else {
            this.songIndex--;
            File songToPlay = this.theCurrentPlaylistThatIsPlaying.get(songIndex).getSongFile();
            this.theManager.playSong(songToPlay, theCurrentPlaylistThatIsPlaying, this.songIndex);
            this.updateSongPlayingText();
            //this.updateSongTimeText();
        }
    }

    public void skipToNextSong(View view) {
        if (this.songIndex >= this.theCurrentPlaylistThatIsPlaying.size() - 1) {

        } else {
            this.songIndex++;
            File songToPlay = this.theCurrentPlaylistThatIsPlaying.get(songIndex).getSongFile();
            this.theManager.playSong(songToPlay, this.theCurrentPlaylistThatIsPlaying, this.songIndex);
            this.updateSongPlayingText();
            //this.updateSongTimeText();
        }
    }

    public void addButtonClicked(View view) {
        if (!this.isAdding) {
            this.isAdding = true;
            this.theCurrentList = this.arraylistPlaylistList.get(0);
            Button button = findViewById(R.id.button);
            Button button1 = findViewById(R.id.button1);
            Button button2 = findViewById(R.id.button2);
            button.setVisibility(View.INVISIBLE);
            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
        }
        this.theAdapter = new EntryAdapter(MainActivity.this, R.layout.entry_view, this.theCurrentList);
        setListAdapter(this.theAdapter);
    }

    public void removeButtonClicked(View view) {
        if (!this.isDeleting) {
            this.isDeleting = true;
            this.theCurrentList = this.theCurrentPlaylistThatIsPlaying;
            Button button = findViewById(R.id.button);
            Button button1 = findViewById(R.id.button1);
            Button button2 = findViewById(R.id.button2);
            button.setVisibility(View.INVISIBLE);
            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
        }
        this.theAdapter = new EntryAdapter(MainActivity.this, R.layout.entry_view, this.theCurrentList);
        setListAdapter(this.theAdapter);
    }

    public void buttonClicked(View view) {
        if (!this.isEditing) {
            Button button = findViewById(R.id.button);
            button.setText("CONFIRM");
            this.isEditing = true;
            this.generate(new Entry("dummy", null, false));
        } else {
            Button button = findViewById(R.id.button);
            button.setText("Add Playlist");
            this.isEditing = false;
            Entry trickyEntry = new Entry("tricky", null, false);
            this.generate(trickyEntry);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.isEditing) {
            this.thePlaylistNameList.remove(this.thePlaylistNameList.size() - 1);
            this.arraylistPlaylistList.remove(this.arraylistPlaylistList.size() - 1);
            this.isEditing = false;
        }
        Entry trickyEntry = new Entry("tricky", null, false);
        this.generate(trickyEntry);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Entry entry = this.theAdapter.getItem(position);
        if (entry.isPlaylist()) {
            Button button = findViewById(R.id.button);
            button.setVisibility(View.INVISIBLE);
            Entry trickyEntry = new Entry("tricky", position, true);
            this.generate(trickyEntry);
        } else if (this.isEditing) {
            this.arraylistPlaylistList.get(this.arraylistPlaylistList.size() - 1).add(entry);
            Toast.makeText(this, entry.getName() + " added", Toast.LENGTH_SHORT).show();
        } else if (this.isAdding) {
            this.theCurrentPlaylistThatIsPlaying.add(entry);
            this.isAdding = false;
            this.theCurrentList = this.theCurrentPlaylistThatIsPlaying;
            Button button = findViewById(R.id.button);
            Button button1 = findViewById(R.id.button1);
            Button button2 = findViewById(R.id.button2);
            button.setVisibility(View.INVISIBLE);
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            this.theAdapter = new EntryAdapter(MainActivity.this, R.layout.entry_view, this.theCurrentList);
            setListAdapter(this.theAdapter);
        } else if (this.isDeleting) {
            this.theCurrentPlaylistThatIsPlaying.remove(entry);
            this.isDeleting = false;
            this.theCurrentList = theCurrentPlaylistThatIsPlaying;
            Button button = findViewById(R.id.button);
            Button button1 = findViewById(R.id.button1);
            Button button2 = findViewById(R.id.button2);
            button.setVisibility(View.INVISIBLE);
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            this.theAdapter = new EntryAdapter(MainActivity.this, R.layout.entry_view, this.theCurrentList);
            setListAdapter(this.theAdapter);
        } else {
            File newFile = entry.getSongFile();
            this.theCurrentPlaylistThatIsPlaying = this.theCurrentList;
            this.songIndex = position;
            this.theManager.playSong(newFile, this.theCurrentPlaylistThatIsPlaying, this.songIndex);

            TextView songName = findViewById(R.id.songName);
            songName.setText(entry.getName());

            TextView songTime = findViewById(R.id.songTime);
            songTime.setText(theManager.getSongTime());
        }
    }
}