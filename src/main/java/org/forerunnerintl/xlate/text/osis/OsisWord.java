package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import org.forerunnerintl.xlate.text.DocumentWord;

public class OsisWord extends DocumentWord {
    @JacksonXmlProperty(isAttribute = true)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true)
    private String lemma;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true)
    private String morph;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true, localName = "n")
    private String noteId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(localName = "seg")
    private String segment;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true, localName = "x-source-word")
    private String sourceWord;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true)
    private String type;
    @JacksonXmlText
    private String bodyText;

    @Override
    public void clear() {
        setNoteId(null);
    }

    @Override
    public String getBodyText() {
        return bodyText;
    }

    @Override
    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getLemma() {
        return lemma;
    }

    @Override
    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    @Override
    public String getMorph() {
        return morph;
    }

    @Override
    public void setMorph(String morph) {
        this.morph = morph;
    }

    @Override
    public String getNoteId() {
        return noteId;
    }

    @Override
    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    @Override
    public String getSourceWord() {
        return sourceWord;
    }

    @Override
    public void setSourceWord(String sourceWord) {
        this.sourceWord = sourceWord;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OsisWord{" +
                "id='" + id + '\'' +
                ", lemma='" + lemma + '\'' +
                ", sourceWord='" + sourceWord + '\'' +
                ", bodyText='" + bodyText + '\'' +
                ", noteId='" + noteId + '\'' +
                '}';
    }
}
