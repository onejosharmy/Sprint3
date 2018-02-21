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
    public MusicManager(ArrayList<File> songs, Context theContext){
        this.musicFiles = songs;
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

    public File getCurrentSong(){
        return this.musicFiles.get(currentIndex);
    }

    public void playSong() {
        if (this.djJazzyJeff.isPlaying()) {
            this.djJazzyJeff.pause();
            this.isPaused = true;
        } else if (this.isPaused){
            this.djJazzyJeff.start();
            this.isPaused = false;
        } else {
            File songToPlay = this.musicFiles.get(currentIndex);

                try {
                    this.djJazzyJeff.setDataSource(songToPlay.getAbsolutePath());
                    this.djJazzyJeff.prepare();
                    this.djJazzyJeff.start();

                } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }



}
