package org.forerunnerintl.xlate.text.osis

import spock.lang.Specification

class OsisDocumentTest extends Specification {
    public static final String SCHEMA_LOC = "http://www.bibletechnologies.net/2003/OSIS/namespace http://www.bibletechnologies.net/osisCore.2.1.1.xsd<"

    private OsisDocument osisDocument

    public static OsisDocument getOsisDocument() {
        OsisDocument result = new OsisDocument()

        result.setOsisText(OsisTextTest.getOsisText())
        result.setSchemaLocation(SCHEMA_LOC)

        return result
    }

    void setup() {
        osisDocument = getOsisDocument()
    }

    def "GetSchemaLocation"() {
        when: "Retrieving schema location"
        String result = osisDocument.getSchemaLocation()

        then: "Should be correct"
        SCHEMA_LOC == result
    }
}
