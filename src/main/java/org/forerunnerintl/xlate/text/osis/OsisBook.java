package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class OsisBook {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "chapter")
    private List<OsisChapter> chapters;
    @JacksonXmlProperty(localName = "osisID")
    private String osisId;
    private String type;

    public List<OsisChapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<OsisChapter> chapters) {
        this.chapters = chapters;
    }

    public String getOsisId() {
        return osisId;
    }

    public void setOsisId(String osisId) {
        this.osisId = osisId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
