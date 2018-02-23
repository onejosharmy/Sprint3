package media.cs4985.westga.edu.audiocontroller;

import java.io.File;

/**
 * Created by Dallas Wyant on 2/21/2018.
 */

public class Entry implements Comparable<Entry> {
    private String name;
    private File song;
    private int position;
    private boolean isPlaylist;


    public Entry(String name, int position, boolean isPlaylist) {
        this.name = name;
        this.song = null;
        this.position = position;
        this.isPlaylist = isPlaylist;
    }

    public Entry(String name, File song, boolean isPlaylist) {
        this.name = name;
        this.song = song;
        this.isPlaylist = isPlaylist;
        this.position = 0;
    }

    public int getPosition() { return this.position;
    }

    public String getName() {
        return this.name;
    }

    public File getSongFile() {
        return this.song;
    }

    public boolean isPlaylist() {
        return this.isPlaylist;
    }

    @Override
    public int compareTo(Entry that) {
        return this.name.compareTo(that.getName());
    }
}
