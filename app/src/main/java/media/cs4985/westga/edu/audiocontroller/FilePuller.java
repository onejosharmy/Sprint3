package media.cs4985.westga.edu.audiocontroller;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by onejo on 2/19/2018.
 */

public class FilePuller {
    private ArrayList<String> allFiles;
    private File[] files;
    private ArrayList<File> allMusic;

    public FilePuller() {
        this.allFiles = new ArrayList<String>();
        this.allMusic = new ArrayList<File>();
    }

    public void addFiles(File directory) {

        if (directory.isDirectory()) {
            this.files = directory.listFiles();
        }
        for (File current : files) {
            if (current.isFile()) {
                this.allFiles.add(current.getAbsolutePath());
            }
            if (current.isDirectory()) {
                addFiles(current);
            }
        }
    }

    public String getList(){
        String response = "";
        for(String current : this.allFiles){
            response += current.toString()+"/n";
        }
        return response;
    }

    public ArrayList<File> getAllMusic(){
        System.out.println(Environment.getExternalStorageDirectory());
        this.getAllFiles(Environment.getExternalStorageDirectory(), "mp3");
        this.getAllFiles(Environment.getExternalStorageDirectory(), "ogg");
        this.getAllFiles(Environment.getExternalStorageDirectory(), "flac");
        return this.allMusic;
    }

    private void getAllFiles(File currentDirectory, String searchPattern) {
        File[] fileList = currentDirectory.listFiles();
        if(fileList == null){
            System.out.println("null list o files");
        } else {
            for (File current : fileList) {
                if (current.isDirectory()) {
                    getAllFiles(current, searchPattern);
                }
                String path = current.getAbsolutePath().toLowerCase();
                if (current.isFile() && (path.contains(searchPattern.toLowerCase()))) {
                    System.out.println(current.getPath());
                    this.allMusic.add(current);
                }
            }
    }}
}

