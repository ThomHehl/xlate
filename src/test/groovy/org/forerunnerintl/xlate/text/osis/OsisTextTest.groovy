package org.forerunnerintl.xlate.text.osis

import spock.lang.Specification

class OsisTextTest extends Specification {
    public static final String LANG_HEBREW = "he";

    private OsisText osisText;

    public static OsisText getOsisText() {
        OsisText result = new OsisText()

        result.setHeader(OsisHeaderTest.getOsisHeader())
        result.setLanguage(LANG_HEBREW)

        return result
    }

    def setup() {
        osisText = getOsisText()
    }

    def "Clear"() {
        when: "Clearing"
        osisText.clear()

        then: "Should be correct"
        null == osisText.getHeader()
        LANG_HEBREW == osisText.getLanguage()
    }
}
