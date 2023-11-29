package com.heavyweightsoftware.util

import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class StringHelperTest extends Specification {
    void setup() {
    }

    def "FileToArray"() {
        given: "A text file"
        URL url = getClass().getClassLoader().getResource("testdata/starships.txt")
        URI uri = url.toURI()
        Path starships = Paths.get(uri)

        when: "Reading tro a list"
        List<String> starshipList = StringHelper.pathToArray(starships)

        then: "Should have the correct list"
        13 == starshipList.size()
        "Constellation" == starshipList.get(0)
        "Farragut" == starshipList.get(6)
        "Yorktown" == starshipList.get(12)
    }
}
