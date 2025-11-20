package org.example.model;
import java.util.Random;

public class Worker {
    private static final Random R = new Random();

    public final int id;
    public final String name;

    public Worker(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Complaint submitComplaint(int nextId, float now) {
        float proc = 1.0f + R.nextFloat() * 9.0f; // 1..10
        return new Complaint(nextId, id, now, proc);
    }
}