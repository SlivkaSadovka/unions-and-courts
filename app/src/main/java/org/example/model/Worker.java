package org.example.model;

public class Worker {
    public int id;
    public String name;

    public Worker(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Complaint submitComplaint(double now) {
        double processingTime = 1 + Math.random() * 2; // равномерное распределение
        return new Complaint(id, now, processingTime);
    }
}
