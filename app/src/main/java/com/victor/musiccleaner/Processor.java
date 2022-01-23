package com.victor.musiccleaner;

import android.widget.ProgressBar;

import com.victor.musiccleaner.metadata.Metadata;
import com.victor.musiccleaner.metadata.Song;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Processor {
    private final static Logger LOGGER = Logger.getLogger(Processor.class.getName());
    private final static String OTHERS = "Others";
    private ProgressBar moveProgressBar;

    /** Locations of to-be-processed songs */
    private final File rootFolder;
    private final Set<Song> scannedSongs;

    public Processor(File rootFolder) {
        this.rootFolder = rootFolder;
        this.scannedSongs = scanSongs(rootFolder);
    }
    


    private void move (Map<File, Set<Song>> folderToSongs){
        for (File folder : folderToSongs.keySet()){
            createFolder(folder);

            Set<Song> songs = folderToSongs.get(folder);
            for (Song song : songs){
                // TODO: update progress bar
                moveToFolder(song,folder);
            }
        }
    }

    private Map<File, Set<Song>> getNewFolders(){
        Map<File, >
        return  null;
    }

    private File getNewFolder(Song song){
        String folderName = OTHERS;

        try {
            Metadata metadata = song.retrieveMetadata();
            folderName = metadata.getFolderName();
        } catch (TagException e) {
            LOGGER.log(Level.WARNING, "Cannot read song tag from file: " + song.getFile());
        } catch (ReadOnlyFileException | CannotReadException | InvalidAudioFrameException | IOException e) {
            LOGGER.log(Level.WARNING, "Cannot open file :" + song.getFile());
        }

        return new File(rootFolder,folderName);
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

    private static Set<Song> scanSongs (File folder){
        return null; // TODO
    }

    private static Set<File> allMusicFilesR (File folder){
        File[] filesList = folder.listFiles();
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
