package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.forerunnerintl.xlate.text.DocumentChapter;
import org.forerunnerintl.xlate.text.DocumentVerse;

import java.util.Collections;
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

    public List<OsisVerse> getOsisVerses() {
        return verse;
    }

    public void setOsisVerses(List<OsisVerse> osisVerses) {
        this.verse = osisVerses;
    }

    @Override
    public String getUniqueId() {
        return getOsisId();
    }

    @Override
    public void setUniqueId(String uniqueId) {
        setOsisId(uniqueId);
    }

    @Override
    public List<DocumentVerse> getVerses() {
        List<DocumentVerse> result = Collections.unmodifiableList((List) getOsisVerses());
        return result;
    }

    @Override
    public void setVerses(List<DocumentVerse> verses) {
        List<OsisVerse> verseList = Collections.unmodifiableList((List) verses);
        setOsisVerses(verseList);
    }
}
