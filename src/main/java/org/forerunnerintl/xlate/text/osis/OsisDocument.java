package org.forerunnerintl.xlate.text.osis;

import org.forerunnerintl.xlate.text.DocumentText;

public class OsisDocument extends DocumentText {
    private OsisText osisText;
    private String schemaLocation;

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public OsisText getOsisText() {
        return osisText;
    }

    public void setOsisText(OsisText osisText) {
        this.osisText = osisText;
    }
}
