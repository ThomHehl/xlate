package com.heavyweightsoftware.util

import spock.lang.Specification

class OccurrenceCounterTest extends Specification {
    public static final String KEY1 = "Ford"
    public static final String KEY2 = "Arthur"
    public static final String KEY3 = "Trillian"

    private OccurrenceCounter counter;

    void setup() {
        counter = new OccurrenceCounter()
    }

    def "Clear"() {
        given: "A counter with some entries"
        counter.add(KEY1)
        counter.add(KEY2)
        counter.add(KEY3)

        when: "Clearing it"
        counter.clear()

        then: "Should be empty"
        counter.isEmpty()
    }

    def "getCount"() {
        given: "A counter with some entries"
        counter.add(KEY1)
        counter.add(KEY2)
        counter.add(KEY3)

        counter.add(KEY2)
        counter.add(KEY3)

        counter.add(KEY3)

        when: "Getting counts"
        int count1 = counter.getCount(KEY1)
        int count2 = counter.getCount(KEY2)
        int count3 = counter.getCount(KEY3)
        int count4 = counter.getCount("Marvin")

        then: "Should match"
        1 == count1
        2 == count2
        3 == count3
        0 == count4
    }
}
