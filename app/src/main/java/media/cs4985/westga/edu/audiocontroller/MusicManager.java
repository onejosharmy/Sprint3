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
    private ArrayList<File> musicFiles;
    private int currentIndex;
    private Context theContext;
    private MediaPlayer djJazzyJeff;
    private boolean isPaused;
    private File currentSong;
    public MusicManager(Context theContext){
        this.currentIndex=0;
        this.theContext = theContext;
        this.djJazzyJeff = new MediaPlayer();
        this.isPaused = false;

        this.djJazzyJeff.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if(MusicManager.this.currentIndex>=MusicManager.this.musicFiles.size()){
                    System.out.println("NO MORE SONGS");

                } else {
                MusicManager.this.currentIndex++;
                try {
                    mp.stop();
                    mp.reset();
                    mp.setDataSource(MusicManager.this.musicFiles.get(MusicManager.this.currentIndex).getAbsolutePath());
                    mp.prepare();
                    mp.start();
                } catch (Exception e){
                    System.out.println("DATA SOURCE NOT SET");
                    System.out.println(e.getStackTrace());
                }
            }}
        });

    }

    public File getCurrentSong() {
        return currentSong;
    }

    public void playPause(){
        if (this.djJazzyJeff.isPlaying()) {
            this.djJazzyJeff.pause();
            this.isPaused = true;
        } else if (this.isPaused){
            this.djJazzyJeff.start();
            this.isPaused = false;
        } else {
            File songToPlay = this.currentSong;
            try {
                this.djJazzyJeff.setDataSource(songToPlay.getAbsolutePath());
                this.djJazzyJeff.prepare();
                this.djJazzyJeff.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void playSong(File song) {
        if(this.djJazzyJeff.isPlaying()){
            this.djJazzyJeff.stop();
            this.djJazzyJeff.reset();
        }
            File songToPlay = song;
            try {
                this.djJazzyJeff.setDataSource(songToPlay.getAbsolutePath());
                this.djJazzyJeff.prepare();
                this.djJazzyJeff.start();
                this.currentSong = song;
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    public void nextSong(File song) {
        if (this.currentIndex == this.musicFiles.size()) {
            System.out.println("NO MORE SONGS");
        } else {
            this.currentIndex++;
            File songToPlay = song;

            try {
                this.djJazzyJeff.stop();
                this.djJazzyJeff.reset();
                this.djJazzyJeff.setDataSource(songToPlay.getAbsolutePath());
                this.djJazzyJeff.prepare();
                this.djJazzyJeff.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public void prevSong(File song) {
        if (this.currentIndex == 0) {
            System.out.println("NO PREV SONG");
        } else {
            this.currentIndex--;
            File songToPlay = song;

            try {
                this.djJazzyJeff.stop();
                this.djJazzyJeff.reset();
                this.djJazzyJeff.setDataSource(songToPlay.getAbsolutePath());
                this.djJazzyJeff.prepare();
                this.djJazzyJeff.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
