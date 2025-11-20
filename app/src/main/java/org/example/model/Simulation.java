package org.example.model;

import java.util.*;

public class Simulation {
    private Generator generator;
    private Dispatcher dispatcher;
    private List<Worker> workers;
    private double clock;
    private List<Event> calendar;
    private int rejected;

    public Simulation(Generator generator, Dispatcher dispatcher, List<Worker> workers) {
        this.generator = generator;
        this.dispatcher = dispatcher;
        this.workers = workers;
        this.clock = 0;
        this.calendar = new ArrayList<>();
        this.rejected = 0;
    }

    public void tick() {
        for (Worker w : workers) {
            Complaint c = generator.generatePoisson(clock, w);
            if (dispatcher.getBuffer().isFull()) rejected++;
            dispatcher.onArrival(c, clock);
            calendar.add(new Event(clock, "Complaint generated: " + c));
        }
        clock += 1;
        logState();
    }

    private void logState() {
        System.out.println("Time: " + clock);
        System.out.println("Buffer: " + dispatcher.getBuffer());
        System.out.println("Operators: " + dispatcher.getOperators());
        System.out.println("Rejected: " + rejected);
        System.out.println("------");
    }

    public void run(int steps) {
        for (int i = 0; i < steps; i++) tick();
    }
}
