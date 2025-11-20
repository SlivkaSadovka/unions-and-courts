package org.example.model;

public class Complaint {
    private final int id;
    private final int workerId;
    private final double arrivalTime;
    private final double serviceTime; // planned service duration

    public Complaint(int id, int workerId, double arrivalTime, double serviceTime) {
        this.id = id;
        this.workerId = workerId;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getId() { return this.id; }
    public double getArrivalTime() { return this.arrivalTime; }
    public double getServiceTime() { return this.serviceTime; }

    @Override
    public String toString() {
        return String.format("C%d(s=%d,t=%.2f,d=%.2f)", id, workerId, arrivalTime, serviceTime);
    }
}
