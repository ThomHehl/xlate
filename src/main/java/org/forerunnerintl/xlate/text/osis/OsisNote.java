package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import org.forerunnerintl.xlate.text.DocumentNote;
import org.forerunnerintl.xlate.text.DocumentNoteType;

public class OsisNote extends DocumentNote {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String catchWord;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true, localName = "n")
    private String noteId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true, localName = "osisID")
    private String osisId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true)
    private String osisRef;
    @JacksonXmlText
    private String text;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true)
    private DocumentNoteType type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(localName = "rdg")
    private OsisReading reading;

    @Override
    public String toString() {
        return "OsisNote{" +
                "catchWord='" + catchWord + '\'' +
                ", noteId='" + noteId + '\'' +
                ", osisId='" + osisId + '\'' +
                ", osisRef='" + osisRef + '\'' +
                ", text='" + text + '\'' +
                ", type=" + type +
                ", reading=" + reading +
                '}';
    }

    public OsisNote() { }

    public OsisNote(String text) {
        this.text = text;
    }

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

    public String getOsisId() {
        return osisId;
    }

    public void setOsisId(String osisId) {
        this.osisId = osisId;
    }

    public String getOsisRef() {
        return osisRef;
    }

    public void setOsisRef(String osisRef) {
        this.osisRef = osisRef;
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
    public DocumentNoteType getType() {
        return type;
    }

    @Override
    public void setType(DocumentNoteType type) {
        this.type = type;
    }
}
