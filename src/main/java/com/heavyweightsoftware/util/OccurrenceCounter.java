package com.heavyweightsoftware.util;

import java.util.HashMap;
import java.util.Map;

public class OccurrenceCounter {
    private Map<String, Integer> entries = new HashMap<>();

    public void add(String entry) {
        Integer count = entries.get(entry);
        Integer newCount = count == null ? 1 : count + 1;
        entries.put(entry, newCount);
    }

    public int getCount(String entry) {
        Integer count = entries.get(entry);
        return  count == null ? 0 : count;
    }

    public void clear() {
        entries.clear();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return entries.size();
    }
}
