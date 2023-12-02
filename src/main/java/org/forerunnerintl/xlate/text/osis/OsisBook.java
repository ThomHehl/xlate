package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.forerunnerintl.xlate.text.DocumentBook;
import org.forerunnerintl.xlate.text.DocumentChapter;

import java.util.Collections;
import java.util.List;

public class OsisBook extends DocumentBook {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "chapter")
    private List<OsisChapter> osisChapters;
    @JacksonXmlProperty(localName = "osisID")
    private String osisId;
    private String type;

    @Override
    public String toString() {
        return "OsisBook{" +
                "osisId='" + osisId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public List<DocumentChapter> getChapters() {
        List<DocumentChapter> result = Collections.unmodifiableList((List) getOsisChapters());
        return result;
    }

    @Override
    public void setChapters(List<DocumentChapter> chapters) {
        List<OsisChapter> chapterList = Collections.unmodifiableList((List) chapters);
        setOsisChapters(chapterList);
    }

    public List<OsisChapter> getOsisChapters() {
        return osisChapters;
    }

    public void setOsisChapters(List<OsisChapter> osisChapters) {
        this.osisChapters = osisChapters;
    }

    public String getOsisId() {
        return osisId;
    }

    public void setOsisId(String osisId) {
        this.osisId = osisId;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getUniqueID() {
        return getOsisId();
    }

    @Override
    public void setUniqueID(String uniqueID) {
        setOsisId(uniqueID);
    }
}
