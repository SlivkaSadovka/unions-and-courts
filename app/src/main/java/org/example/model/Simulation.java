package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Simulation {
    private final Generator generator;
    private final Buffer buffer;
    private final List<Operator> operators;
    private final Dispatcher dispatcher;
    public final Metrics metrics;
    public double clock = 0.0;
    private double delta = 0.1;

    public Simulation(double lambda, int bufferSize, int numOperators, boolean directDispatch, double serviceMin, double serviceMax, long rngSeed) {
        this.generator = new Generator(lambda, rngSeed, serviceMin, serviceMax);
        this.buffer = new Buffer(bufferSize);
        this.operators = new ArrayList<>();
        for (int i = 0; i < numOperators; i++) operators.add(new Operator(i + 1));
        this.metrics = new Metrics();
        this.dispatcher = new Dispatcher(buffer, operators, metrics, directDispatch);
    }

    public void tick(double deltaTime) {
        clock += deltaTime;
        List<Complaint> arrivals = generator.generate(clock);
        for (Complaint c : arrivals) {
            dispatcher.onArrival(c, clock);
        }
        dispatcher.updateOperators(clock);
    }

    public void snapshotAndPrint() {
        metrics.recordSnapshot(clock, buffer, operators);
        String events = String.join("; ", metrics.getEvents());
        String buf = buffer.size() + " -> " + buffer.snapshotIds();
        String ops = operators.stream().map(op -> op.describe(clock)).collect(Collectors.joining("; "));
        String reject = String.format("%.2f", metrics.rejectionPercent());
        System.out.printf("%6.2f | %-60s | %-20s | %-35s | %6s%n", clock, events, buf, ops, reject);
        metrics.getEvents().clear();
    }

    public void runStepMode(int steps, double deltaTime) {
        this.delta = deltaTime;
        Map<String, Object> lastSummary;
        System.out.printf("%6s | %-60s | %-20s | %-35s | %6s%n", "t", "Events", "Buffer", "Operators", "%rej");
        System.out.println("-".repeat(140));
        for (int i = 0; i < steps; i++) {
            tick(deltaTime);
            snapshotAndPrint();
        }
        metrics.getSummary(clock);
    }

    public Map<String, Object> runAutoMode(double duration, double deltaTime) {
        this.delta = deltaTime;
        while (clock < duration) {
            tick(deltaTime);
            metrics.recordSnapshot(clock, buffer, operators);
        }

        while (!buffer.isEmpty() || operators.stream().anyMatch(op -> !op.isFree(clock))) {
            tick(deltaTime);
            metrics.recordSnapshot(clock, buffer, operators);
        }
        for (Operator op : operators) metrics.getOperatorUtil().put(op.getId(), op.utilization(clock));
        return metrics.getSummary(clock);
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }
}


