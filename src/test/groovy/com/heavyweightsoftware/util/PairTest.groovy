package com.heavyweightsoftware.util

import spock.lang.Specification

class PairTest extends Specification {
    public static final String LEFT1 = "Answer"
    public static final int RIGHT1 = 42

    private Pair<String, Integer> pair

    void setup() {
        pair = new Pair(LEFT1, RIGHT1)
    }

    def "GetLeft"() {
        when: "Getting value"
        String result = pair.getLeft()

        then: "Should match"
        LEFT1 == result
    }

    def "GetRight"() {
        when: "Getting value"
        int result = pair.getRight()

        then: "Should match"
        RIGHT1 == result
    }
}
