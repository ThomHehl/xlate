package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class OsisNote {
    private String catchWord;
    @JacksonXmlProperty(localName = "n")
    private String noteId;
    @JacksonXmlText
    private String text;
    private String type;
    @JacksonXmlProperty(localName = "rdg")
    private OsisReading reading;

    public String getCatchWord() {
        return catchWord;
    }

    public void setCatchWord(String catchWord) {
        this.catchWord = catchWord;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public OsisReading getReading() {
        return reading;
    }

    public void setReading(OsisReading reading) {
        this.reading = reading;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
