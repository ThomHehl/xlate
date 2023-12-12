package org.forerunnerintl.xlate.text;

public record WordReference(String book, int chapter, int verse, int index) {
    @Override
    public String toString() {
        return book + " " + chapter + ":" + verse + "(" + index + ")";
    }
}
