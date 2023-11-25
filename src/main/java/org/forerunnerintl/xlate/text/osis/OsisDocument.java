package org.forerunnerintl.xlate.text.osis;

import org.forerunnerintl.xlate.text.DocumentText;

public class OsisDocument extends DocumentText {
    private String schemaLocation;

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public OsisText getOsisText() {
        return (OsisText) getSourceText();
    }

    public void setOsisText(OsisText sourceText) {
        setSourceText(sourceText);
    }
}
