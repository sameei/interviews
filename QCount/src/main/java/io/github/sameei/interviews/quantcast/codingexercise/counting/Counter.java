package io.github.sameei.interviews.quantcast.codingexercise.counting;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Counter {
    private final HashMap<String, Integer> underlay;
    private final int defaultValue;

    public Counter(HashMap<String, Integer> initialState, int defaultValue) {
        this.underlay = initialState;
        this.defaultValue = defaultValue;
    }

    public Counter() {
        this(new HashMap<>(), 0);
    }

    public Iterable<String> keys() {
        return underlay.keySet();
    }

    public int get(String key) {
        return underlay.getOrDefault(key, defaultValue);
    }

    public int plusAndGet(String key, int value) {
        int current = underlay.getOrDefault(key, defaultValue);
        int newValue = current + value;
        underlay.put(key, newValue);
        return newValue;
    }

    public int incAndGet(String key) {
        return plusAndGet(key, 1);
    }

    public Counter plus(String key, int value) {
        plusAndGet(key, value);
        return this;
    }

    public Counter inc(String key) {
        incAndGet(key);
        return this;
    }

    public int size() {
        return underlay.size();
    }

    public Map<String, Integer> asMap() {
        return underlay;
    }

    public List<Pair<String, Integer>> onlyMaximums() {
        int max = -1;
        for(Map.Entry<String, Integer> e: underlay.entrySet()) {
            max = Math.max(e.getValue(), max);
        }
        ArrayList<Pair<String, Integer>> maxs = new ArrayList<>();
        for(Map.Entry<String, Integer> e: underlay.entrySet()) {
            if (e.getValue() == max) maxs.add(Pair.with(e.getKey(), max));
        }
        return maxs;
    }
}
