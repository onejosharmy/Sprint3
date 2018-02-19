package media.cs4985.westga.edu.audiocontroller;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by onejo on 2/19/2018.
 */

public class FilePuller {
    private ArrayList<String> allFiles;
    private File[] files;

    public FilePuller() {
        this.allFiles = new ArrayList<String>();
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
}

