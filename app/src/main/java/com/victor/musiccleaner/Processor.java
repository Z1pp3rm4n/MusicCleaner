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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public void tidyFolder(){
        Map<File, Set<Song>> newFolders = getNewFolders();
        move(newFolders);
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
        Map<File, List<Song>> folderToSongList = scannedSongs.stream()
                .collect(Collectors.groupingBy(this::getCanonFolder));

        Map<File, Set<Song>> newFolders = new HashMap<>();
        File defaultFolder = new File(rootFolder, OTHERS);
        for (File folder: folderToSongList.keySet()) {
            List<Song> songs = folderToSongList.get(folder);
            if (songs.size() > 1) {
                newFolders.put(folder, new HashSet<>(songs));
            } else {
                newFolders.put(defaultFolder, new HashSet<>(songs));
            }
        }

        return newFolders;
    }

    /**
     * Get the canonical folder of a song (Artist + Album).
     * @param song
     * @return
     */
    private File getCanonFolder(Song song){
        String folderName = OTHERS;

        try {
            Metadata metadata = song.retrieveMetadata();
            folderName = metadata.getFolderName();
        } catch (TagException e) {
            LOGGER.warning("Cannot read song tag from file: " + song.getFile());
        } catch (ReadOnlyFileException | CannotReadException | InvalidAudioFrameException | IOException e) {
            LOGGER.warning("Cannot open file :" + song.getFile());
        }

        return new File(rootFolder,folderName);
    }

    /**
     * Create folder if it doesn't exist
     * @param folder
     */
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

    /**
     * Return all songs in a folder (recursive)
     * @param folder
     * @return
     */
    private static Set<Song> scanSongs(File folder){
        return scanFiles(folder)
                .stream()
                .map(Song::getSong)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Return all files in a folder (recursive)
     * @param folder
     * @return
     */
    private static Set<File> scanFiles(File folder){
        File[] filesList = folder.listFiles();
        Set<File> files = new HashSet<>();

        for (File f : filesList){
            if (f.isFile()) {
                files.add(f);
            } else if (f.isDirectory()){
                files.addAll(scanFiles(f));
            }
        }

        return files;
    }

}
