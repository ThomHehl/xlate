package org.forerunnerintl.xlate.note;

import org.forerunnerintl.xlate.text.osis.OsisDocument;

import java.util.Arrays;

public class TranslationEntry {
    private String key;
    private String primary;
    private String[] alternates;

    public TranslationEntry() {}

    public TranslationEntry(String key, String primary, String alternatesString) {
        this.key = key;
        this.primary = primary;
        setAlternatesAsString(alternatesString);
    }

    public TranslationEntry(String primary, String alts) {
        setPrimary(primary);
        setAlternatesAsString(alts);
    }

    public static String buildKey(OsisDocument doc, String wordRef) {
        StringBuilder sb = new StringBuilder();

        String prefix = doc.getOsisText().getLanguage();
        prefix = prefix.substring(0, 1).toUpperCase();
        sb.append(prefix);

        sb.append(wordRef);

        return sb.toString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String[] getAlternates() {
        return alternates;
    }

    public void setAlternates(String[] alternates) {
        this.alternates = alternates;
    }

    public String getAlternatesAsString() {
        StringBuilder sb = new StringBuilder();
        for (String alt : getAlternates()){
            sb.append(alt);
            sb.append("|");
        }
        if (!sb.isEmpty()) {
            sb.setLength(sb.length() -1);
        }

        return sb.toString();
    }

    public void setAlternatesAsString(String alternatesAsString) {
        String parts[] = alternatesAsString.split("\\|");
        setAlternates(parts);
    }

    @Override
    public String toString() {
        return "TranslationEntry{" +
                "key='" + key + '\'' +
                ", primary='" + primary + '\'' +
                ", alternates=" + Arrays.toString(alternates) +
                '}';
    }
}
