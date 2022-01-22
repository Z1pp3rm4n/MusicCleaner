package com.victor.musiccleaner.metadata;

import com.victor.musiccleaner.util.Strings;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

import java.io.File;
import java.io.IOException;

public class FLAC extends Song {
    public FLAC(File file) {
        super(file);
    }

    public Metadata retrieveMetadata() throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException {
        AudioFile flacFile = AudioFileIO.read(file);
        Metadata metadata = new Metadata();

        try {
            FlacTag flacTag = (FlacTag) flacFile.getTag();
            VorbisCommentTag vorbisTag = flacTag.getVorbisCommentTag();

            String albumArtistField = vorbisTag.getFirst(VorbisCommentFieldKey.ALBUMARTIST);

            String artist = Strings.stripIllegalCharacters(Strings.isNullOrEmpty(vorbisTag.getFirst(VorbisCommentFieldKey.ALBUMARTIST)) ? vorbisTag.getFirst(VorbisCommentFieldKey.ARTIST) : vorbisTag.getFirst(VorbisCommentFieldKey.ALBUMARTIST));
            String album = Strings.stripIllegalCharacters(vorbisTag.getFirst(VorbisCommentFieldKey.ALBUM));
            metadata.setArtist(Strings.isNullOrEmpty(artist) ? UNKNOWN_ARTIST : artist.trim());
            metadata.setAlbum(Strings.isNullOrEmpty(album) ? UNKNOWN_ALBUM : album.trim());
        } catch (KeyNotFoundException e) {
            metadata.setArtist(UNKNOWN_ARTIST);
            metadata.setAlbum(UNKNOWN_ALBUM);
        }

        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        // TODO
    }
}
