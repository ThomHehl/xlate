package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import org.forerunnerintl.xlate.text.DocumentWord;

public class OsisWord extends DocumentWord {
    @JacksonXmlProperty(isAttribute = true)
    private String id;
    @JacksonXmlProperty(isAttribute = true)
    private String lemma;
    @JacksonXmlProperty(isAttribute = true)
    private String morph;
    @JacksonXmlProperty(isAttribute = true, localName = "n")
    private String noteId;
    @JacksonXmlProperty(isAttribute = true, localName = "x-source-word")
    private String sourceWord;
    @JacksonXmlProperty(isAttribute = true)
    private String type;
    @JacksonXmlText
    private String bodyText;

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
}
