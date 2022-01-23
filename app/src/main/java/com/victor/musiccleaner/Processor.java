package com.victor.musiccleaner;

import android.widget.ProgressBar;

import com.victor.musiccleaner.metadata.Song;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scanner {
    private final static Logger LOGGER = Logger.getLogger(Scanner.class.getName());
    private final static String OTHERS = "Others";
    private ProgressBar moveProgressBar;
    private final File root;

    public Scanner(File root) {
        this.root = root;
    }

    private void move (Map<File,Song> folderToSong){
        for (File folder : folderToSong.keySet()){
            createFolder(folder);
            moveProgressBar.setPro
        }
    }

    private void createFolder(File folder){
        if (folder.mkdirs()) {
            LOGGER.log(Level.INFO, "Folder: " + folder + " created");
        } else {
            LOGGER.log(Level.INFO, "Folder: " + folder + " already exists / couldn't be created");
        }
    }

    /**
     * Move a song to a new folder
     * @param song
     * @param folder
     */
    private void moveToFolder(Song song, @NotNull File folder){
        File path = song.getFile();
        File newPath = new File(folder, path.getName());
        path.renameTo(newPath);
        song.setFile(newPath);
        LOGGER.log(Level.INFO, "File: " + path + " moved to folder: " + folder);
    }



    private static Set<File> allMusicFilesR (File dir){
        File[] filesList = dir.listFiles();
        Set<File> musicFiles = new HashSet<>();

        for (File f : filesList){
            if (isMusicFile(f)) {
                musicFiles.add(f);
            }
            if (f.isDirectory()){
                musicFiles.addAll(allMusicFilesR(f));
            }
        }

        return musicFiles;
    }

    private static boolean isMusicFile (File file){
        String name = file.getName();
        return file.isFile()
                && (name.endsWith(".flac") || name.endsWith(".mp3"));
    }
}
