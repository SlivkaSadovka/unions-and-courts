package org.example.model;

public class Operator {
    public int id;
    public double busyUntil;
    private Complaint currentComplaint;

    public Operator(int id) {
        this.id = id;
        this.busyUntil = 0;
    }

    public boolean isFree(double now) {
        return now >= busyUntil;
    }

    public void takeComplaint(Complaint complaint, double now) {
        currentComplaint = complaint;
        busyUntil = now + complaint.processingTime;
    }

    public Complaint getCurrent() {
        return currentComplaint;
    }

    @Override
    public String toString() {
        if (currentComplaint != null) return "O" + id + ":" + currentComplaint;
        else return "O" + id + ":free";
    }
}
