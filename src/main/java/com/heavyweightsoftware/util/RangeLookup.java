package com.heavyweightsoftware.util;

import java.util.*;

public class RangeLookup<Type> {
    private List<RangeEntry<Type>> entryList = new ArrayList<>();

    public RangeEntry<Type> get(int value) {
        RangeEntry<Type> result = null;

        int min = 0;
        int max = size() - 1;

        while (max >= min) {
            int pos = (min + max) / 2;
            RangeEntry<Type> entry = entryList.get(pos);

            if (entry.includes(value)) {
                result = entry;
                max = -1;
            } else if (entry.isGreaterThan(value)) {
                max = pos - 1;
            } else {
                min = pos + 1;
            }
        }

        return result;
    }

    public void add(RangeEntry<Type> newEntry) {
        entryList.add(newEntry);
        Collections.sort(entryList);
    }

    public void clear() {
        entryList.clear();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return entryList.size();
    }

    public static record RangeEntry<Type>(int start, int end, Type value) implements Comparable<RangeEntry<Type>>{
        @Override
        public int compareTo(RangeEntry<Type> entry) {
            int result = start - entry.start();
            if (result == 0) {
                result = end - entry.end();
            }

            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RangeEntry<?> that)) return false;
            return compareTo((RangeEntry<Type>) o) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }

        public boolean includes(int val) {
            return val >= start && val <= end;
        }

        /**
         * Is the entire range less than the specified value?
         * @param val the value
         * @return true if both start and end are less than the value
         */
        public boolean isLessThan(int val) {
            return start < val && end < val;
        }

        /**
         * Is the entire range greater than the specified value?
         * @param val the value
         * @return true if both start and end are greater than the value
         */
        public boolean isGreaterThan(int val) {
            return start > val && end > val;
        }
    }
}
