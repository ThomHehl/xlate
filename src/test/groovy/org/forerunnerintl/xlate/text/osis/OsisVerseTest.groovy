package org.forerunnerintl.xlate.text.osis

import spock.lang.Specification

class OsisVerseTest extends Specification {
    private OsisVerse verse

    public static OsisVerse getOsisVerse() {
        OsisVerse result = new OsisVerse()

        List<OsisNote> noteList = [OsisNoteTest.getOsisNote()]
        result.setOsisNotes(noteList)

        List<OsisWord> wordList = [OsisWordTest.getOsisWord()]
        result.setOsisWords(wordList)

        return result
    }

    void setup() {
        verse = getOsisVerse()
    }

    def "Clear"() {
        when: "Clearing"
        verse.clear()

        then: "Should be clear"
        verse.getOsisNotes().isEmpty()
    }

    def "getNotes"() {
        when: "Getting the notes"
        List<OsisNote> noteList = verse.getOsisNotes()

        then: "Should be correct"
        1 == noteList.size()
        OsisNoteTest.BODY_TEXT == noteList.get(0).getText()
    }

    def "setNotes append"() {
        given: "Another note"
        OsisNote note2 = OsisNoteTest.getOsisNote2()
        List<OsisNote> otherNotes = [note2]

        when: "Appending the notes"
        verse.setOsisNotes(otherNotes)

        then: "Should be appended"
        List<OsisNote> noteList = verse.getOsisNotes()
        2 == noteList.size()
        note2.text == noteList.get(1).text
    }

    def "getOsisWords"() {
        when: "Getting the words"
        List<OsisWord> wordList = verse.getOsisWords()

        then: "Should be correct"
        1 == wordList.size()
        OsisWordTest.BODY_TEXT == wordList.get(0).bodyText
    }

    def "setOsisWords append"() {
        given: "Another word"
        OsisWord word2 = OsisWordTest.getOsisWord2()
        List<OsisWord> otherWords = [word2]

        when: "Setting the words"
        verse.setOsisWords(otherWords)

        then: "Should be appended"
        List<OsisWord> wordList = verse.getOsisWords()
        2 == wordList.size()
        word2.bodyText == wordList.get(1).bodyText
    }
}
