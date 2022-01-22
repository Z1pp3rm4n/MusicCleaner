package com.victor.musiccleaner.metadata;

import java.io.File;

import lombok.Getter;
import lombok.Setter;


public class Metadata {
    private String artist;
    private String album;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFolderName () {
        return artist + File.separator + album;
    }
}
