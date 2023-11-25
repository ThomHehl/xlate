package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import org.forerunnerintl.xlate.text.DocumentNote;

public class OsisNote extends DocumentNote {
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

    @Override
    public String getNoteId() {
        return noteId;
    }

    @Override
    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public OsisReading getReading() {
        return reading;
    }

    public void setReading(OsisReading reading) {
        this.reading = reading;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
