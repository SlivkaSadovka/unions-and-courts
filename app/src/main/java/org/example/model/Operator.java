package org.example.model;

import java.util.Optional;

public class Operator {
    private final int id;
    private Complaint current = null;
    private double busyUntil = 0.0;
    private double totalBusyTime = 0.0;

    public Operator(int id) {
        this.id = id;
    }

    public boolean isFree(double now) {
        return current == null || now >= busyUntil;
    }

    public void take(Complaint c, double now) {
        this.current = c;
        this.busyUntil = now + c.getServiceTime();
    }

    // if finishing now, return finished complaint; else null
    public Complaint finishIfDue(double now) {
        if (current != null && now >= busyUntil) {
            Complaint finished = current;
            totalBusyTime += finished.getServiceTime();
            current = null;
            return finished;
        }
        return null;
    }

    public Optional<Complaint> peek() {
        return Optional.ofNullable(current);
    }

    public double utilization(double totalTime) {
        if (totalTime <= 0) return 0.0;
        return Math.min(1.0, totalBusyTime / totalTime);
    }

    public int getId() { return this.id; }

    public String describe(double now) {
        if (current == null) return String.format("O%d:free", id);
        else return String.format("O%d:#%d→%.2f", id, current.getId(), busyUntil);
    }
}


