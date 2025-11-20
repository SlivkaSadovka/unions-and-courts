package org.example.model;

public class Generator {
    private double lambda;

    public Generator(double lambda) {
        this.lambda = lambda;
    }

    public Complaint generatePoisson(double now, Worker worker) {
        double u = Math.random();
        double interArrival = -Math.log(1 - u) / lambda;
        return worker.submitComplaint(now + interArrival);
    }
}
