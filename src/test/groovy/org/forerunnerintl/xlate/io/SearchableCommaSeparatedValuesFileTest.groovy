package org.forerunnerintl.xlate.io

import spock.lang.Specification

class SearchableCommaSeparatedValuesFileTest extends Specification {
    public static final String FILE_CSV = "csv/"
    public static final String FILE_2PARMS = "parm2.csv"

    private SearchableCommaSeparatedValuesFile csv
    private File testData

    void setup() {
        URL url = getClass().getClassLoader().getResource(FILE_CSV)
        if (url == null) {
            throw new NullPointerException("Unable to find csv path")
        }
        String path = url.getFile()
        testData = new File(path)
    }

    def "error with 0 field names"() {
        given: "not enough fields"
        File csvFile = new File(testData, FILE_2PARMS)
        String[] fields = new String[0];

        when: "creating"
        csv = new SearchableCommaSeparatedValuesFile(csvFile, fields)

        then: "should throw exception"
        NullPointerException npe = thrown()
        npe != null
    }
}
