package org.forerunnerintl.xlate.text;

public abstract class DocumentWord {
    public abstract String getBodyText();

    public abstract void setBodyText(String bodyText);

    public abstract String getId();

    public abstract void setId(String id);

    public abstract String getLemma();

    public abstract void setLemma(String lemma);

    public abstract String getMorph();

    public abstract void setMorph(String morph);

    public abstract String getNoteId();

    public abstract void setNoteId(String noteId);

    public abstract String getSourceWord();

    public abstract void setSourceWord(String sourceWord);

    public abstract String getType();

    public abstract void setType(String type);
}
