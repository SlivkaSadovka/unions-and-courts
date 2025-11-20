package org.example.model;

public class Operator {

    public final int id;
    private float busyUntil = 0;
    private Complaint current = null;

    public Operator(int id) {
        this.id = id;
    }

    public boolean isFree(float now) {
        return now >= busyUntil;
    }

    public void takeComplaint(Complaint c, float now) {
        current = c;
        busyUntil = now + c.processingTime;
    }

    public Complaint finishComplaint(float now) {
        if (current != null && now >= busyUntil) {
            Complaint done = current;
            current = null;
            return done;
        }
        return null;
    }

    public Complaint getCurrent() {
        return current;
    }
}
