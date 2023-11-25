package org.forerunnerintl.xlate.text;

import java.util.List;

public abstract class DocumentVerse {
    public abstract void clear();
    public abstract List<DocumentNote> getNotes();

    public abstract void setNotes(List<DocumentNote> notes);

    public abstract String getUniqueId();

    public abstract void setUniqueId(String uniqueId);

    public abstract List<DocumentWord> getWords();

    public abstract void setWords(List<DocumentWord> words);
}
