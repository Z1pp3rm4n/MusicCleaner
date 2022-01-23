package com.victor.musiccleaner.metadata;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

public abstract class Song {
    public static String UNKNOWN_ARTIST = "Unknown Artist";
    public static String UNKNOWN_ALBUM  = "Unknown Album";

    protected File file;

    public static Song getSong(File f){
        String name = f.getName();
        if (name.endsWith(".mp3")) {
            return new MP3(f);
        } else if (name.endsWith(".flac")){
            return new FLAC(f);
        } else
            return null;
    }

    public Song(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public abstract Metadata retrieveMetadata () throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException;

}
