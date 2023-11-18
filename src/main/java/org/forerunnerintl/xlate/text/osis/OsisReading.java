package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class OsisReading {
    @JacksonXmlProperty(localName = "w")
    private OsisWord word;
    private String type;

    public OsisWord getWord() {
        return word;
    }

    public void setWord(OsisWord word) {
        this.word = word;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
