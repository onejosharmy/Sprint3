package media.cs4985.westga.edu.audiocontroller;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Dallas Wyant on 2/21/2018.
 */

public class MusicManager {
    private ArrayList<Entry> thePlaylist;
    private int currentIndex;
    private Context theContext;
    private MediaPlayer djJazzyJeff;
    private boolean isPaused;
    private File currentSong;
    private int songTime;

    public MusicManager(Context theContext) {
        this.currentIndex = 0;
        this.theContext = theContext;
        this.djJazzyJeff = new MediaPlayer();
        this.isPaused = false;
        this.thePlaylist = new ArrayList<>();
        this.currentIndex = 0;
        this.currentSong = null;
        this.djJazzyJeff.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if (MusicManager.this.currentIndex >= MusicManager.this.thePlaylist.size()) {
                    System.out.println("NO MORE SONGS");
                } else {
                    MusicManager.this.currentIndex++;
                    try {
                        mp.stop();
                        mp.reset();
                        mp.setDataSource(MusicManager.this.thePlaylist.get(currentIndex).getSongFile().getAbsolutePath());
                        mp.prepare();
                        mp.start();
                        MusicManager.this.currentSong = MusicManager.this.thePlaylist.get(currentIndex).getSongFile();
                    } catch (Exception e) {
                        System.out.println("DATA SOURCE NOT SET");
                        System.out.println(e.getStackTrace());
                    }
                }
            }
        });

    }

    public File getCurrentSong() {
        return currentSong;
    }

    public int getSongTime() {
        return this.songTime;
    }

    public void playPause() {
        if (this.djJazzyJeff.isPlaying()) {
            this.djJazzyJeff.pause();
            this.isPaused = true;
        } else if (this.isPaused) {
            this.djJazzyJeff.start();
            this.isPaused = false;
        } else {
            File songToPlay = this.currentSong;
            try {
                this.djJazzyJeff.setDataSource(songToPlay.getAbsolutePath());
                this.djJazzyJeff.prepare();
                this.djJazzyJeff.start();
                this.songTime = djJazzyJeff.getDuration();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void playSong(File song, ArrayList<Entry> playlist, int songIndex) {
        if (this.djJazzyJeff.isPlaying()) {
            this.djJazzyJeff.stop();
            this.djJazzyJeff.reset();
        }
        File songToPlay = song;
        try {
            this.djJazzyJeff.setDataSource(songToPlay.getAbsolutePath());
            this.djJazzyJeff.prepare();
            this.djJazzyJeff.start();
            this.currentSong = song;
            this.songTime = this.djJazzyJeff.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.thePlaylist = playlist;
        this.currentIndex = songIndex;
    }
}
