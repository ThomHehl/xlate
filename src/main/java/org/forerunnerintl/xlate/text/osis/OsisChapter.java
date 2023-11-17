package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.forerunnerintl.xlate.text.DocumentChapter;

import java.util.List;

public class OsisChapter extends DocumentChapter {
    @JacksonXmlProperty(localName = "osisID")
    private String osisId;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<OsisVerse> verse;

    public String getOsisId() {
        return osisId;
    }

    public void setOsisId(String osisId) {
        this.osisId = osisId;
    }

    public List<OsisVerse> getVerse() {
        return verse;
    }

    public void setVerse(List<OsisVerse> verse) {
        this.verse = verse;
    }
}
