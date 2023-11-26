package org.forerunnerintl.xlate.text.osis;

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
        } else {
            osisNotes.clear();
        }
    }

    public List<OsisNote> getOsisNotes() {
        return osisNotes;
    }

    @Override
    public List<DocumentNote> getNotes() {
        List<DocumentNote> result = Collections.unmodifiableList((List) getOsisNotes());
        return result;
    }

    @Override
    public void setNotes(List<DocumentNote> notes) {
        List<OsisNote> noteList = Collections.unmodifiableList((List) notes);
        setOsisNotes(noteList);
    }

    public void setOsisNotes(List<OsisNote> osisNotes) {
        this.osisNotes = osisNotes;
    }

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public List<DocumentWord> getWords() {
        List<DocumentWord> result = (List) osisWords;
        return result;
    }

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

    public List<OsisWord> getOsisWords() {
        return osisWords;
    }

    public void setOsisWords(List<OsisWord> words) {
        this.osisWords = words;
    }
}
