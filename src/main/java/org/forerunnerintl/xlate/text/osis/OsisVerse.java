package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.forerunnerintl.xlate.text.DocumentNote;
import org.forerunnerintl.xlate.text.DocumentVerse;
import org.forerunnerintl.xlate.text.DocumentWord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OsisVerse extends DocumentVerse {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "note")
    private List<OsisNote> osisNotes;
    @JacksonXmlProperty(localName = "osisID")
    private String uniqueId;
    @JacksonXmlProperty(localName = "seg")
    private String segment;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "w")
    private List<OsisWord> osisWords;

    @Override
    public void clear() {
        if (osisNotes == null) {
            osisNotes = new ArrayList<>();
        }

        if (osisWords == null) {
            osisWords = new ArrayList<>();
        }
    }

    @JsonIgnore
    @Override
    public List<DocumentNote> getNotes() {
        List<DocumentNote> result = Collections.unmodifiableList((List) getOsisNotes());
        return result;
    }

    @JsonIgnore
    @Override
    public void setNotes(List<DocumentNote> notes) {
        List<OsisNote> noteList = Collections.unmodifiableList((List) notes);
        setOsisNotes(noteList);
    }

    public synchronized List<OsisNote> getOsisNotes() {
        return osisNotes == null ? osisNotes = new ArrayList<>() : osisNotes;
    }

    public synchronized void setOsisNotes(List<OsisNote> osisNotes) {
        if (this.osisNotes == null) {
            this.osisNotes = new ArrayList<>();
        }

        this.osisNotes.addAll(osisNotes);
    }

    @JsonIgnore
    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @JsonIgnore
    @Override
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @JsonIgnore
    @Override
    public List<DocumentWord> getWords() {
        List<DocumentWord> result = (List) osisWords;
        return result;
    }

    @JsonIgnore
    @Override
    public void setWords(List<DocumentWord> words) {
        List<OsisWord> wordList = (List) words;
        setOsisWords(wordList);
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public synchronized List<OsisWord> getOsisWords() {
        return osisWords == null ? osisWords = new ArrayList<>() : osisWords;
    }

    public synchronized void setOsisWords(List<OsisWord> words) {
        if (osisWords == null) {
            osisWords = new ArrayList<>();
        }

        this.osisWords.addAll(words);
    }

    @Override
    public String toString() {
        return "OsisVerse{" +
                "uniqueId='" + uniqueId + '\'' +
                '}';
    }
}
