package org.forerunnerintl.xlate.text.osis


import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class OsisReaderTest extends Specification {
    private Path obadiah
    private OsisReader osisReader

    Path getObadiah() {
        URL url = getClass().getClassLoader().getResource("source-text/ot/Obad.xml")
        URI uri = url.toURI()
        Path obadiah = Paths.get(uri)
        return obadiah
    }

    void setup() {
        obadiah = getObadiah()
        osisReader = new OsisReader()
    }

    def "Read OSIS"() {
        when: "Reading the file"
        OsisDocument osisDocument = osisReader.readPath(obadiah)
        OsisBook book = osisDocument.getSourceText().getOsisBook()

        then: "Should match"
        book.chapters.size() == 1
        OsisVerse verse = book.getOsisChapters().get(0).getOsisVerses().get(0)
        verse.words.size() == 18
        "2377" == verse.words.get(0).lemma
        "l/4421" == verse.words.get(17).lemma
    }
}
