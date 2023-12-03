package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.forerunnerintl.xlate.text.DocumentBook;
import org.forerunnerintl.xlate.text.SourceText;

public class OsisText extends SourceText {

    private OsisHeader header;
    @JacksonXmlProperty(isAttribute = true, localName = "lang")
    private String language;
    @JacksonXmlProperty(localName = "div")
    private OsisBook osisBook;
    @JacksonXmlProperty(isAttribute = true)
    private String osisIDWork;
    @JacksonXmlProperty(isAttribute = true)
    private String osisRefWork;

    @Override
    public void clear() {
        setHeader(null);
    }

    public OsisHeader getHeader() {
        return header;
    }

    public void setHeader(OsisHeader header) {
        this.header = header;
    }

    public String getOsisRefWork() {
        return osisRefWork;
    }

    public void setOsisRefWork(String osisRefWork) {
        this.osisRefWork = osisRefWork;
    }

    public OsisBook getOsisBook() {
        return osisBook;
    }

    public void setOsisBook(OsisBook osisBook) {
        this.osisBook = osisBook;
    }

    public String getOsisIDWork() {
        return osisIDWork;
    }

    public void setOsisIDWork(String osisIDWork) {
        this.osisIDWork = osisIDWork;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonIgnore
    @Override
    public DocumentBook getDocumentBook() {
        return getOsisBook();
    }

    @JsonIgnore
    @Override
    public void setDocumentBook(DocumentBook documentBook) {
        setOsisBook((OsisBook) documentBook);
    }
}
