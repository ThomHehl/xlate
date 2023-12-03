package org.forerunnerintl.xlate.controller

import com.heavyweightsoftware.util.Pair
import spock.lang.Specification

class EditorControllerImplTest extends Specification {
    void setup() {
    }

    def "BuildBookList"() {
        when: "Building the book list"
        List<Pair<String, String>> bookList = EditorControllerImpl.buildBookList()

        then: "Should be correct"
        39 == bookList.size()
        Pair<String, String> book = bookList.get(0)
        "Gen" == book.getLeft()
        "Genesis" == book.getRight()
    }

    def "splitReference"() {
        given: "A reference"
        String ref = "John 3"

        when: "Splitting the reference"
        String[] result = EditorControllerImpl.splitReference(ref)

        then: "Should be correct"
        2 == result.length
        "John" == result[0]
        "3" == result[1]
    }
}
