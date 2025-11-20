package org.example.model;

import java.util.*;

public class Metrics {

    private final List<String> eventLog = new ArrayList<>();

    public int generated = 0;
    public int buffered = 0;
    public int rejected = 0;
    public int completed = 0;

    public void log(String s) {
        eventLog.add(s);
    }

    public List<String> flushEvents() {
        List<String> copy = new ArrayList<>(eventLog);
        eventLog.clear();
        return copy;
    }

    public float rejectPercent() {
        int total = generated;
        return total == 0 ? 0 : (float) rejected / total * 100f;
    }
}

