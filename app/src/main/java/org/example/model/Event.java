package org.example.model;

public class Event {
    public double time;
    public String description;

    public Event(double time, String description) {
        this.time = time;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%.2f: %s", time, description);
    }
}
