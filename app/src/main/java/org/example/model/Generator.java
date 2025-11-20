package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {
    private final double lambda;
    private final Random rnd;
    private double nextArrival = Double.NaN;
    private int counter = 0;
    private final double serviceMin;
    private final double serviceMax;

    public Generator(double lambda, long seed, double serviceMin, double serviceMax) {
        this.lambda = lambda;
        this.rnd = (seed == 0 ? new Random() : new Random(seed));
        this.serviceMin = serviceMin;
        this.serviceMax = serviceMax;
    }

    private void scheduleNext(double now) {
        if (Double.isNaN(nextArrival)) {
            nextArrival = now + sampleExpInterval();
        }
    }

    private double sampleExpInterval() {
        if (lambda <= 0.0) return Double.POSITIVE_INFINITY;
        double u = rnd.nextDouble();
        while (u <= 0.0) u = rnd.nextDouble();
        return -Math.log(u) / lambda;
    }

    private double sampleService() {
        return serviceMin + rnd.nextDouble() * (serviceMax - serviceMin);
    }

    public List<Complaint> generate(double now) {
        List<Complaint> produced = new ArrayList<>();
        scheduleNext(now);
        while (!Double.isNaN(nextArrival) && nextArrival <= now) {
            double arrivalTime = nextArrival;
            double serviceTime = sampleService();
            Complaint c = new Complaint(counter++, 1, arrivalTime, serviceTime);
            produced.add(c);
            nextArrival = arrivalTime + sampleExpInterval();
        }
        return produced;
    }
}

