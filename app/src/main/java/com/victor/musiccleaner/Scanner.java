package com.victor.musiccleaner;

import com.victor.musiccleaner.metadata.Song;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;

public class Scanner {
    private final static Logger LOGGER = Logger.getLogger(Scanner.class.getName());
    private final File root;

    public Scanner(File root) {
        this.root = root;
    }

    private void move (Song song, Path newPath){
        try {
            File file = song.getFile();
        }
    }

    private Map<String,File> dirToFile(Set<Song> songs){
        return null;
    }

    private



    static Set<File> allMusicFilesR (File dir){
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
