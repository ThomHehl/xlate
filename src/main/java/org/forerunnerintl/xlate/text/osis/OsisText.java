package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class OsisText {
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_GREEK = "gr";
    public static final String LANGUAGE_HEBREW = "he";

    private OsisHeader header;
    private String lang;
    @JacksonXmlProperty(localName = "div")
    private OsisBook osisBook;
    private String osisIDWork;
    private String osisRefWork;

    public OsisHeader getHeader() {
        return header;
    }

    public void setHeader(OsisHeader header) {
        this.header = header;
    }
    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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
}
