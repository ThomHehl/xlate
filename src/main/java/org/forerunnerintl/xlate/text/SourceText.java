package org.forerunnerintl.xlate.text;

public abstract class SourceText {

    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_GREEK = "gr";
    public static final String LANGUAGE_HEBREW = "he";

    public abstract void clear();

    public abstract String getLanguage();

    public abstract void setLanguage(String language);

    public abstract DocumentBook getDocumentBook();

    public abstract void setDocumentBook(DocumentBook documentBook);
}
