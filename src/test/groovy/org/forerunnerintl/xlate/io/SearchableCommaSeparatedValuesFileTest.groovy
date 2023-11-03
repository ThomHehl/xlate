package org.forerunnerintl.xlate.io

import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class SearchableCommaSeparatedValuesFileTest extends Specification {
    public static final String FIELD_2 = "VALUE"
    public static final String[] FIELDS = [SearchableCommaSeparatedValuesFile.ID, FIELD_2];

    public static final String FILE_CSV = "csv/"
    public static final String FILE_2PARMS = "parm2.csv"

    public static final String ID_ATAT = "AT-AT"
    public static final String VALUE_ATAT = "Imperial Walker"
    public static final String ID_C3P0 = "C3P0"
    public static final String ID_SPEEDER = "T38"
    public static final String VALUE_C3P0 = "Protocol Droid"
    public static final String VALUE_SPEEDER = "Land Speeder"

    private SearchableCommaSeparatedValuesFile csv
    private String testData

    void setup() {
        URL url = getClass().getClassLoader().getResource(FILE_CSV)
        if (url == null) {
            throw new NullPointerException("Unable to find csv path")
        }
        testData = Paths.get(url.toURI()).toAbsolutePath().toString()
    }

    def "error with 0 field names"() {
        given: "not enough fields"
        Path csvFile = getCsvFile()
        String[] fields = new String[0];

        when: "creating"
        csv = new SearchableCommaSeparatedValuesFile(csvFile, fields)

        then: "should throw exception"
        NullPointerException npe = thrown()
        npe != null
    }

    def "error with only 1 field names"() {
        given: "not enough fields"
        Path csvFile = getCsvFile()
        String[] fields = ["Abbot"];

        when: "creating"
        csv = new SearchableCommaSeparatedValuesFile(csvFile, fields)

        then: "should throw exception"
        NullPointerException npe = thrown()
        npe != null
    }

    def "error with no ID field"() {
        given: "fields without ID"
        Path csvFile = getCsvFile()
        String[] fields = ["Abbot", "Costello"];

        when: "creating"
        csv = new SearchableCommaSeparatedValuesFile(csvFile, fields)

        then: "should throw exception"
        IllegalArgumentException iae = thrown()
        iae != null
    }

    def "search not found"() {
        given: "a file"
        Path csvFile = getCsvFile()
        csv = new SearchableCommaSeparatedValuesFile(csvFile, FIELDS)

        when: "creating"
        String[] result = csv.searchById("R@D@")

        then: "should return array of null strings"
        result.length == FIELDS.length
        result[0] == null
        result[1] == null
    }

    def "search AT-AT"() {
        given: "a file"
        Path csvFile = getCsvFile()
        csv = new SearchableCommaSeparatedValuesFile(csvFile, FIELDS)

        when: "creating"
        String[] result = csv.searchById(ID_ATAT)

        then: "should return the record"
        result.length == FIELDS.length
        result[0] == ID_ATAT
        result[1] == VALUE_ATAT
    }

    def "search C3P0"() {
        given: "a file"
        Path csvFile = getCsvFile()
        csv = new SearchableCommaSeparatedValuesFile(csvFile, FIELDS)

        when: "creating"
        String[] result = csv.searchById(ID_C3P0)

        then: "should return the record"
        result.length == FIELDS.length
        result[0] == ID_C3P0
        result[1] == VALUE_C3P0
    }

    def "search land speeder"() {
        given: "a file"
        Path csvFile = getCsvFile()
        csv = new SearchableCommaSeparatedValuesFile(csvFile, FIELDS)

        when: "creating"
        String[] result = csv.searchById(ID_SPEEDER)

        then: "should return the record"
        result.length == FIELDS.length
        result[0] == ID_SPEEDER
        result[1] == VALUE_SPEEDER
    }

    Path getCsvFile(String fileName = FILE_2PARMS) {
        Path result = Paths.get(testData, fileName)
        return result
    }
}
