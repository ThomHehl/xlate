package org.forerunnerintl.xlate.io

import org.forerunnerintl.xlate.text.TextFormat
import spock.lang.Specification

class ProjectSettingsImplTest extends Specification {
    private ProjectSettingsImpl projectSettings

    void setup() {
        projectSettings = new ProjectSettingsImpl();
    }

    def "NT format"() {
        when: "Setting the format"
        projectSettings.setNewTestamentSourceFormat(TextFormat.SBL)

        then: "Should match"
        TextFormat.SBL == projectSettings.getNewTestamentSourceFormat()
    }

    def "OT format"() {
        when: "Setting the format"
        projectSettings.setOldTestamentSourceFormat(TextFormat.OSIS)

        then: "Should match"
        TextFormat.OSIS == projectSettings.getOldTestamentSourceFormat()
    }
}
