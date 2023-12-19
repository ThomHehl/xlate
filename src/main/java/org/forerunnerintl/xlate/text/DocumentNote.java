package org.forerunnerintl.xlate.text;

public abstract class DocumentNote {
    public static final String NOTE_SEPARATOR = "note ";
    public static final String REFERENCE_SEPARATOR = "!";

    public abstract String getNoteId();

    public abstract void setNoteId(String noteId);

    public abstract String getText();

    public abstract void setText(String text);

    public abstract DocumentNoteType getType();

    public abstract void setType(DocumentNoteType type);
}
