package org.forerunnerintl.xlate.text;

import java.util.Arrays;

public abstract class DocumentText {
    public static final String[] BOOK_ABBREVIATIONS = {"Gen", "Exod", "Lev", "Num", "Deut", "Josh", "Judg", "Ruth", "1Sam", "2Sam", "1Kgs", "2Kgs",
            "1Chr", "2Chr", "Ezra", "Neh", "Esth", "Job", "Ps", "Prov", "Eccl", "Song", "Isa", "Jer", "Lam", "Ezek", "Dan", "Hos", "Joel", "Amos", "Obad",
            "Jonah", "Mic", "Nah", "Hab", "Zeph", "Hag", "Zech", "Mal"};
    public static final String[] BOOK_NAMES = {"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel",
            "1 Kings", "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes", "Song of Songs",
            "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah",
            "Haggai", "Zechariah", "Malachi"
    };

    public static String getBookNameForAbbreviation(String abbr) {
        int idx = 0;

        while (!abbr.equals(BOOK_ABBREVIATIONS[idx])) {
            idx++;
        }

        String result = BOOK_NAMES[idx];
        return result;
    }

    public abstract SourceText getSourceText();

    public abstract void setSourceText(SourceText sourceText);
}
