package org.forerunnerintl.xlate.text.osis

import spock.lang.Specification

class OsisNoteTest extends Specification {
    public static final String BODY_TEXT = "The Hitchhiker's guide to the galaxy is a wholly remarkable book."
    public static final String BODY_TEXT2 = "Don't Panic!"

    private OsisNote osisNote

    public static OsisNote getOsisNote() {
        OsisNote result = new OsisNote()

        result.setText(BODY_TEXT)

        return result
    }

    public static OsisNote getOsisNote2() {
        OsisNote result = new OsisNote()

        result.setText(BODY_TEXT2)

        return result
    }

    void setup() {
        osisNote = getOsisNote()
    }

    def "GetNoteId"() {
        when: "Retrieving body text"
        String text = osisNote.getText()

        then: "Should be correct"
        BODY_TEXT == text
    }
}
