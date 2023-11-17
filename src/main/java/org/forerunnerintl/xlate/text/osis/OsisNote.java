package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class OsisNote {
    @JacksonXmlProperty(localName = "n")
    private String noteId;
    @JacksonXmlProperty(localName = "note")
    private String text;

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
