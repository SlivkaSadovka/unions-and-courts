package org.example.model;

import java.util.Random;

public class Generator {
    private final float lambda;
    private float nextTime = 0;
    private final Random rnd = new Random();

    public Generator(float lambda) {
        this.lambda = lambda;
    }

    public Complaint generatePoisson(float now, Worker worker, int nextId) {
        if (nextTime == 0) {
            nextTime = (float)(-Math.log(1 - rnd.nextFloat()) / lambda);
        }
        if (now >= nextTime) {
            // генерируем заявку
            float arrival = nextTime;
            nextTime = now + (float)(-Math.log(1 - rnd.nextFloat()) / lambda);
            return worker.submitComplaint(nextId, arrival);
        }
        return null;
    }
}

