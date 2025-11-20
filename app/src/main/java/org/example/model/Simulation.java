package org.example.model;

import java.util.*;

public class Simulation {

    public float clock = 0;
    private final float dt;

    private final Buffer buffer;
    private final Operator[] operators;
    private final Dispatcher dispatcher;
    private final Generator generator;
    private final Worker[] workers;

    private final Metrics metrics = new Metrics();

    private int nextId = 1;

    public Simulation(float lambda, int workersCount, int bufferSize, int operatorsCount, float dt) {

        this.dt = dt;

        workers = new Worker[workersCount];
        for (int i = 0; i < workersCount; i++)
            workers[i] = new Worker(i + 1, "W" + (i + 1));

        generator = new Generator(lambda);
        buffer = new Buffer(bufferSize);

        operators = new Operator[operatorsCount];
        for (int i = 0; i < operatorsCount; i++)
            operators[i] = new Operator(i + 1);

        dispatcher = new Dispatcher(buffer, operators, metrics);
    }

    public void tick() {
        clock += dt;

        // 1. Generation
        for (Worker w : workers) {
            Complaint c = generator.generatePoisson(clock, w, nextId);
            if (c != null) {
                nextId++;
                dispatcher.onArrival(c);
            }
        }

        // 2. Dispatch to free operators
        dispatcher.tryDispatch(clock);

        // 3. Finishing
        for (Operator op : operators) {
            Complaint done = op.finishComplaint(clock);
            if (done != null) {
                metrics.completed++;
                metrics.log("DONE #" + done.id);
            }
        }
    }

    // Пошаговый режим (календарь событий)
    public void printSnapshot() {
        List<String> events = metrics.flushEvents();

        System.out.printf("t=%.2f | Events: ", clock);
        if (events.isEmpty()) System.out.print("—");
        else System.out.print(String.join("; ", events));

        System.out.print(" | Buffer(" + buffer.length() + "): ");
        for (Complaint c : buffer.view()) System.out.print("#" + c.id + " ");

        System.out.print(" | Operators: ");
        for (Operator op : operators) {
            if (op.getCurrent() == null)
                System.out.print("O" + op.id + "=free ");
            else
                System.out.print("O" + op.id + "=#" + op.getCurrent().id + " ");
        }

        System.out.printf(" | Reject=%.2f%%\n", metrics.rejectPercent());
    }

    // Автоматический режим — без вывода
    public void runAuto(float until) {
        while (clock < until) tick();
    }

    // Итоговая статистика
    public void printStats() {
        System.out.println("----- FINAL STATS -----");
        System.out.println("Generated: " + metrics.generated);
        System.out.println("Buffered: " + metrics.buffered);
        System.out.println("Completed: " + metrics.completed);
        System.out.println("Rejected: " + metrics.rejected);
        System.out.printf("Reject %% = %.2f\n", metrics.rejectPercent());
    }
}
