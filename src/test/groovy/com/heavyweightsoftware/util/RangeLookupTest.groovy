package com.heavyweightsoftware.util

import spock.lang.Specification

class RangeLookupTest extends Specification {
    public static final String ENTRY1 = "Ultimate Answer"
    public static final String ENTRY2 = "Mostly Harmless"
    public static final int RANGE1_FROM = 1
    public static final int RANGE1_TO = 50
    public static final int RANGE2_FROM = 60
    public static final int RANGE2_TO = 100

    private RangeLookup<String> rangeLookup

    public static RangeLookup.RangeEntry<String> getEntry() {
        RangeLookup.RangeEntry<String> result = new RangeLookup.RangeEntry(RANGE1_FROM, RANGE1_TO, ENTRY1)
    }

    public static RangeLookup.RangeEntry<String> getEntry2() {
        RangeLookup.RangeEntry<String> result = new RangeLookup.RangeEntry(RANGE2_FROM, RANGE2_TO, ENTRY2)
    }

    void setup() {
        rangeLookup = new RangeLookup<>()
    }

    def "clear"() {
        given: "A couple of entries"
        RangeLookup.RangeEntry<String> entry1 = getEntry()
        RangeLookup.RangeEntry<String> entry2 = getEntry2()

        RangeLookup<String> rangeLookup = new RangeLookup<>()
        rangeLookup.add(entry1)
        rangeLookup.add(entry2)

        when: "clearing"
        rangeLookup.clear()

        then: "Should be empty"
        rangeLookup.isEmpty()
    }

    def "get 100 ranges"() {
        given: "A hundred ranges"
        final int increment = 100
        int from = 1;
        int to = 50;
        for (int ix = 0; ix < 100; ++ix) {
            RangeLookup.RangeEntry<String> entry = new RangeLookup.RangeEntry<>(from, to, Integer.toString(ix))
            rangeLookup.add(entry);

            from += increment
            to += increment
        }

        expect:
        RangeLookup.RangeEntry<String> rangeEntry = rangeLookup.get(value)
        String result = rangeEntry == null ? "" : rangeEntry.value()
        answer == result

        where:
        value | answer
        0 | ""
        1 | "0"
        49 | "0"
        50 | "0"
        51 | ""
        100 | ""
        101 | "1"
        500 | ""
        501 | "5"
        551 | ""
        9825 | "98"
        9851 | ""
        9925 | "99"
        9951 | ""
    }

    def "get found"() {
        given: "A couple of entries"
        RangeLookup.RangeEntry<String> entry1 = getEntry()
        RangeLookup.RangeEntry<String> entry2 = getEntry2()

        RangeLookup<String> rangeLookup = new RangeLookup<>()
        rangeLookup.add(entry1)
        rangeLookup.add(entry2)

        when: "retrieving a matching record"
        RangeLookup.RangeEntry<String> result = rangeLookup.get(42)

        then: "Should match"
        ENTRY1 == result.value()
    }

    def "get not found"() {
        given: "A couple of entries"
        RangeLookup.RangeEntry<String> entry1 = getEntry()
        RangeLookup.RangeEntry<String> entry2 = getEntry2()

        RangeLookup<String> rangeLookup = new RangeLookup<>()
        rangeLookup.add(entry1)
        rangeLookup.add(entry2)

        when: "retrieving a non-matching value"
        RangeLookup.RangeEntry<String> result = rangeLookup.get(52)

        then: "Should be null"
        null == result
    }

    def "size"() {
        given: "A couple of entries"
        RangeLookup.RangeEntry<String> entry1 = getEntry()
        RangeLookup.RangeEntry<String> entry2 = getEntry2()

        RangeLookup<String> rangeLookup = new RangeLookup<>()
        rangeLookup.add(entry1)
        rangeLookup.add(entry2)

        when: "Checking size"
        int result = rangeLookup.size()

        then: "Should match"
        2 == result
    }
}
