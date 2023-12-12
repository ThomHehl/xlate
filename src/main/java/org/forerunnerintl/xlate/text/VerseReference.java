package org.forerunnerintl.xlate.text;

public record VerseReference(String book, String chapter, int verse) {
    @Override
    public String toString() {
        return book + " " + chapter + ":" + verse;
    }
}
