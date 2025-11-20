package org.example.model;

public class Complaint {
    private static int counter = 0;
    public int id;
    public int workerId;
    public double arrivalTime;
    public double processingTime;

    public Complaint(int workerId, double arrivalTime, double processingTime) {
        this.id = counter++;
        this.workerId = workerId;
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
    }

    @Override
    public String toString() {
        return "C" + id + "(W" + workerId + ")";
    }
}

