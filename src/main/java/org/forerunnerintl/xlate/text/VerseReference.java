package org.forerunnerintl.xlate.text;

public record VerseReference(String book, String chapter, int verse) {
    @Override
    public String toString() {
        return book + " " + chapter + ":" + verse;
    }

    public String toChapterId() {
        StringBuilder sb = new StringBuilder(book);
        sb.append('.');
        sb.append(chapter);

        return sb.toString();
    }

    public String toVerseId() {
        StringBuilder sb = new StringBuilder(toChapterId());
        sb.append('.');
        sb.append(verse);

        return sb.toString();
    }
}
