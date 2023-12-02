package org.forerunnerintl.xlate.text.osis

import spock.lang.Specification

class OsisWordTest extends Specification {
    public static final String      BODY_TEXT = "Mostly Harmless"
    public static final String      BODY_TEXT2 = "Pan-Galactic Gargle Blaster"
    public static final String      SOURCE_WORD = "Earth"
    public static final String      SOURCE_WORD2 = "Cocktail"

    private OsisWord osisWord

    public static OsisWord getOsisWord() {
        OsisWord result = new OsisWord()

        result.setBodyText(BODY_TEXT)
        result.setSourceWord(SOURCE_WORD)

        return result
    }

    public static OsisWord getOsisWord2() {
        OsisWord result = new OsisWord()

        result.setBodyText(BODY_TEXT2)
        result.setSourceWord(SOURCE_WORD2)

        return result
    }

    void setup() {
        osisWord = getOsisWord()
    }

    def "GetSourceWord"() {
        when: "Retrieving the source word"
        String word = osisWord.getSourceWord()

        then: "Should match"
        SOURCE_WORD == word
    }
}
