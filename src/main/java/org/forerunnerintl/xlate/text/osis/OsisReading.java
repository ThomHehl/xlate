package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class OsisReading {
    @JacksonXmlProperty(localName = "w")
    private OsisWord word;
    private String type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(localName = "seg")
    private String segment;

    @JacksonXmlText
    private String bodyText;
    public OsisReading() { }

    public OsisReading(String text) {
        bodyText = text;
    }

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

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }
}
