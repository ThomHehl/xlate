package org.forerunnerintl.xlate.text

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class SourceTextConverterTest extends Specification {
    public static final String TEXT_DIR = "text"
    private SourceTextConverter sourceTextConverter

    Path getObadiah() {
        URL url = getClass().getClassLoader().getResource("source-text/ot/ObadShort.xml")
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

    def "Jackson Serializer"() {
        given: "A sentence"
        Sentence sentence = new Sentence("Don't Panic!")

        when: "Converting to XML"
        XmlMapper xmlMapper = new XmlMapper()
        String xml = xmlMapper.writeValueAsString(sentence)

        then: "Should be correct"
        xml == "<Sentence numWords=\"2\"><w boldface=\"false\">Don't</w><w boldface=\"true\">Panic!</w></Sentence>"
    }

    static class Sentence
    {
        @JacksonXmlProperty(isAttribute=true)
        public int numWords

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(isAttribute = false, localName = "w")
        public List<Word> wordList

        Sentence(String sentenceText) {
            wordList = new ArrayList<>()

            boolean boldFlag = false
            String[] words = sentenceText.split(" ")
            for (String myWord : words) {
                Word word = new Word()
                word.bodyText = myWord

                word.bold  = boldFlag
                boldFlag = !boldFlag

                wordList.add(word)
            }

            numWords = wordList.size()
        }
    }

    static class Word
    {
        @JacksonXmlProperty(isAttribute=true, localName="boldface")
        public boolean bold

        @JacksonXmlText
        public String bodyText
    }
}
