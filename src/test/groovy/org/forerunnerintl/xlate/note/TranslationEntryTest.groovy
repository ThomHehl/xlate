package org.forerunnerintl.xlate.note

import org.forerunnerintl.xlate.text.osis.OsisDocument
import org.forerunnerintl.xlate.text.osis.OsisDocumentTest
import spock.lang.Specification

class TranslationEntryTest extends Specification {
    public static final String ALT1 = "Therefore"
    public static final String ALT2 = "Also"
    public static final String KEY = "H2199"
    public static final String PRIMARY = "was"

    private TranslationEntry translationEntry

    public static final TranslationEntry getTranslationEEntry() {
        TranslationEntry result = new TranslationEntry()

        result.setKey(KEY)
        result.setPrimary(PRIMARY)
        String[] alts = [ALT1, ALT2]
        result.setAlternates(alts)

        return result
    }

    void setup() {
        translationEntry = new TranslationEntry()
    }

    def "BuildKey"() {
        given: "Key information"
        OsisDocument doc = OsisDocumentTest.getOsisDocument()
        String wordRef = "c/6213 a"

        when: "Building the key"
        String result = TranslationEntry.buildKey(doc, wordRef)

        then: "Should be correct"
        "Hc/6213 a" == result
    }

    def "GetAlternatesAsString multiple"() {
        given: "Some alternates"
        String alts = "was|should be"

        when: "Setting alternates"
        translationEntry.setAlternatesAsString(alts)

        then: "Should match"
        alts == translationEntry.getAlternatesAsString()
    }
}
