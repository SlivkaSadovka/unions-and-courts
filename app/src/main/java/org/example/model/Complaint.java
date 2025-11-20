package org.example.model;

public class Complaint {
    public final int id;
    public final int workerId;
    public final float arrivalTime;
    public final float processingTime;

    public Complaint(int id, int workerId, float arrivalTime, float processingTime) {
        this.id = id;
        this.workerId = workerId;
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
    }
}
