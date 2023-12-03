package org.forerunnerintl.xlate.text.osis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forerunnerintl.xlate.text.DocumentText;
import org.forerunnerintl.xlate.text.SourceText;

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

    @JsonIgnore
    @Override
    public SourceText getSourceText() {
        return getOsisText();
    }

    @JsonIgnore
    @Override
    public void setSourceText(SourceText sourceText) {
        setOsisText((OsisText) sourceText);
    }
}
