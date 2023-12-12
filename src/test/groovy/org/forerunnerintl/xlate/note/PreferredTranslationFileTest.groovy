package org.forerunnerintl.xlate.note

import com.heavyweightsoftware.io.KeyedSequentialFile
import org.forerunnerintl.xlate.io.ProjectFiles
import org.forerunnerintl.xlate.io.ProjectSettings
import spock.lang.Specification

class PreferredTranslationFileTest extends Specification {

    private KeyedSequentialFile keyedSequentialFile
    private PreferredTranslationFile preferredTranslationFile

    public PreferredTranslationFile getPreferredTranslationFile(KeyedSequentialFile ksds) {
        ProjectSettings projectSettings = Stub()

        ProjectFiles projectFiles = Stub()
        projectFiles.getPreferredTranslation() >> ksds

        PreferredTranslationFile result = new PreferredTranslationFile(projectSettings, projectFiles)
        return result
    }

    void setup() {
        keyedSequentialFile = Stub()
        preferredTranslationFile = getPreferredTranslationFile(keyedSequentialFile)
    }

    def "get"() {
        given: "A record"
        final String key = "H4242"
        String[] record = [key, "primary|alt1|alt2"]
        keyedSequentialFile.get(key) >> record

        when: "Getting a record"
        TranslationEntry entry = preferredTranslationFile.get(key)

        then: "Should be correct"
        key == entry.getKey()
        "primary" == entry.getPrimary()
        String[] alts = entry.getAlternates()
        2 == alts.length
        "alt1" == alts[0]
        "alt2" == alts[1]
    }

    def "get not found"() {
        given: "A record"
        final String key = "H4242"
        keyedSequentialFile.get(key) >> null

        when: "Getting a record"
        TranslationEntry entry = preferredTranslationFile.get(key)

        then: "Should be null"
        null == entry
    }

    def "store insert"() {
        given: "A TranslationEntry"
        TranslationEntry entry = TranslationEntryTest.getTranslationEEntry()
        final String key = entry.getKey()
        keyedSequentialFile.get(key) >> null

        when: "Storing an entry"
        preferredTranslationFile.store(entry)

        then: "Should be inserted"
//        1 * keyedSequentialFile.get(key)
//        1 * keyedSequentialFile.insert_
        true
    }

    def "store update"() {
        given: "A TranslationEntry"
        TranslationEntry entry = TranslationEntryTest.getTranslationEEntry()
        TranslationEntry oldEntry = TranslationEntryTest.getTranslationEEntry()
        oldEntry.setPrimary("answer")
        String[] record = PreferredTranslationFile.buildRecordFromEntry(oldEntry)
        keyedSequentialFile.get(entry.getKey()) >> record

        when: "Storing an entry"
        preferredTranslationFile.store(entry)

        then: "Should be inserted"
//        1 * keyedSequentialFile.get(entry.getKey()) << oldEntry
//        1 * keyedSequentialFile.update(entry.getKey(), _ as String)
        true
    }

    def "BuildEntryFromRecord full"() {
        given: "A record"
        String[] record = ["key", "primary|alt1|alt2"]

        when: "Converting to TranslationEntry"
        TranslationEntry entry = PreferredTranslationFile.buildEntryFromRecord(record)

        then: "Should be correct"
        "key" == entry.getKey()
        "primary" == entry.getPrimary()
        String[] alts = entry.getAlternates()
        2 == alts.length
        "alt1" == alts[0]
        "alt2" == alts[1]
    }

    def "BuildEntryFromRecord single alt"() {
        given: "A record"
        String[] record = ["key", "primary|alt1"]

        when: "Converting to TranslationEntry"
        TranslationEntry entry = PreferredTranslationFile.buildEntryFromRecord(record)

        then: "Should be correct"
        "key" == entry.getKey()
        "primary" == entry.getPrimary()
        String[] alts = entry.getAlternates()
        1 == alts.length
        "alt1" == alts[0]
    }

    def "BuildEntryFromRecord primary only"() {
        given: "A record"
        String[] record = ["key", "primary"]

        when: "Converting to TranslationEntry"
        TranslationEntry entry = PreferredTranslationFile.buildEntryFromRecord(record)

        then: "Should be correct"
        "key" == entry.getKey()
        "primary" == entry.getPrimary()
        String[] alts = entry.getAlternates()
        0 == alts.length
    }

    def "buildRecordFromEntry full"() {
        given: "A TranslationEntry"
        TranslationEntry entry = TranslationEntryTest.getTranslationEEntry()

        when: "Converting to record"
        String[] record = PreferredTranslationFile.buildRecordFromEntry(entry)

        then: "Should be correct"
        TranslationEntryTest.KEY + "       " == record[0]
        String[] parts = record[1].split("\\|")
        3 == parts.length
        TranslationEntryTest.PRIMARY == parts[0]
        TranslationEntryTest.ALT1 == parts[1]
        TranslationEntryTest.ALT2 == parts[2]
    }
}
