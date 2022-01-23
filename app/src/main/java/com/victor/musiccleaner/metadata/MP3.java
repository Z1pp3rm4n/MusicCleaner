package com.victor.musiccleaner.metadata;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

public class MP3 extends Song {
    public MP3(File file) {
        super(file);
    }

    @Override
    public Metadata retrieveMetadata() throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException {
        return null;
    }
}
