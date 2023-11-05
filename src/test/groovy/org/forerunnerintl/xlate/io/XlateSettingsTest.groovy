package org.forerunnerintl.xlate.io

import spock.lang.Specification

class XlateSettingsTest extends Specification {
    void setup() {
    }

    def "GetXlateUserDirectory"() {
        when: "Retrieving the directory"
        String result = XlateSettings.getXlateUserDirectory()

        then: "Should match"
        result.endsWith(XlateSettings.XLATE_USER_HOME)
    }
}
