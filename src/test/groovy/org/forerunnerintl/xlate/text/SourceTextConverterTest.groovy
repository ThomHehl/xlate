package org.forerunnerintl.xlate.text

import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class SourceTextConverterTest extends Specification {
    public static final String TEXT_DIR = "text"
    private SourceTextConverter sourceTextConverter

    Path getObadiah() {
        URL url = getClass().getClassLoader().getResource("source-text/ot/Obad.xml")
        URI uri = url.toURI()
        Path obadiah = Paths.get(uri)
        return obadiah
    }

    static Path outputPathFromSource(Path source) {
        Path root = source.getParent().getParent().getParent()
        Path textRoot = Paths.get(root.toFile().getAbsolutePath(), TEXT_DIR)
        File textRootFile = textRoot.toFile()
        if (!textRootFile.exists()) {
            if (!textRootFile.mkdirs()) {
                throw new IllegalStateException()
            }
        }

        String filename = source.toFile().getName()
        Path result = Paths.get(textRootFile.getAbsolutePath(), filename)
        return result
    }

    void setup() {
        sourceTextConverter = SourceTextConverter.getConverter(TextFormat.OSIS, TextFormat.OSIS)
    }

    def "Convert"() {
        given: "An OSIS text path"
        Path input = getObadiah()
        Path output = outputPathFromSource(input)

        when: "Converting"
        sourceTextConverter.convert(input, output)

        then: "Should be correct"
        true
    }

}
