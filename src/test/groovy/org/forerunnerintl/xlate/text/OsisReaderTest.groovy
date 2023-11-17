package org.forerunnerintl.xlate.text

import org.forerunnerintl.xlate.text.osis.OsisDocument
import org.forerunnerintl.xlate.text.osis.OsisReader
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class OsisReaderTest extends Specification {
    private Path obadiah
    private OsisReader osisReader;

    void setup() {
        URL url = getClass().getClassLoader().getResource("source-text/ot/Obad.xml")
        URI uri = url.toURI()
        obadiah = Paths.get(uri)

        osisReader = new OsisReader()
    }

    def "Read OSIS"() {
        when: "Reading the file"
        OsisDocument osisDocument = osisReader.readFile(obadiah.toFile())

        then: "Should match"
        true
    }
}
