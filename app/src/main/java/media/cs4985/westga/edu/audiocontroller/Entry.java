package media.cs4985.westga.edu.audiocontroller;

/**
 * Created by Dallas Wyant on 2/21/2018.
 */

public class Entry implements Comparable<Entry> {
    private String name;
    private boolean isDirectory;
    private String path;
    private String size;
    private int children;

    public Entry(String name) {
        this.name = name;
    }

    public int getChildren() {
        return children;
    }

    public Entry(String name, String path, boolean isDirectory, String fileSize) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.path = path;
        this.size = fileSize;
    }

    public String getSizeText() {
        return this.size;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public boolean isDirectory() {
        return this.isDirectory;
    }

    @Override
    public int compareTo(Entry that) {
        return this.name.compareTo(that.getName());
    }
}
