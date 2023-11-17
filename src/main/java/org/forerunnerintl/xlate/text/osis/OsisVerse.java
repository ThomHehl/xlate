package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class OsisVerse {
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<OsisNote> note;
    @JacksonXmlProperty(localName = "osisID")
    private String osisId;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "w")
    private List<OsisWord> words;

    public List<OsisNote> getNote() {
        return note;
    }

    public void setNote(List<OsisNote> note) {
        this.note = note;
    }

    public String getOsisId() {
        return osisId;
    }

    public void setOsisId(String osisId) {
        this.osisId = osisId;
    }

    public List<OsisWord> getWords() {
        return words;
    }

    public void setWords(List<OsisWord> words) {
        this.words = words;
    }
}
