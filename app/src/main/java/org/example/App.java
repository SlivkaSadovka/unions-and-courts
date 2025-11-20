package org.example;

import org.example.model.*;

import java.util.Map;


public class App {

    public static void main(String[] args) {
        // defaults
        String mode = "step";
        int steps = 30;
        double duration = 50.0;
        double delta = 0.5;
        double lambda = 0.6;
        int bufferSize = 8;
        int operators = 3;
        boolean directDispatch = false;
        double serviceMin = 1.0;
        double serviceMax = 10.0;
        long seed = 0;

        // parse rudimentary args
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--mode": if (i+1 < args.length) mode = args[++i]; break;
                case "--steps": if (i+1 < args.length) steps = Integer.parseInt(args[++i]); break;
                case "--duration": if (i+1 < args.length) duration = Double.parseDouble(args[++i]); break;
                case "--delta": if (i+1 < args.length) delta = Double.parseDouble(args[++i]); break;
                case "--lambda": if (i+1 < args.length) lambda = Double.parseDouble(args[++i]); break;
                case "--buffer-size": if (i+1 < args.length) bufferSize = Integer.parseInt(args[++i]); break;
                case "--operators": if (i+1 < args.length) operators = Integer.parseInt(args[++i]); break;
                case "--direct-dispatch": directDispatch = true; break;
                case "--service-min": if (i+1 < args.length) serviceMin = Double.parseDouble(args[++i]); break;
                case "--service-max": if (i+1 < args.length) serviceMax = Double.parseDouble(args[++i]); break;
                case "--seed": if (i+1 < args.length) seed = Long.parseLong(args[++i]); break;
                default:
                    System.err.println("Unknown arg: " + args[i]);
            }
        }

        Simulation sim = new Simulation(lambda, bufferSize, operators, directDispatch, serviceMin, serviceMax, seed);

        if ("step".equalsIgnoreCase(mode)) {
            sim.runStepMode(steps, delta);
            Map<String, Object> summary = sim.metrics.getSummary(sim.clock);
            System.out.println("---- Summary ----");
            summary.forEach((k,v)-> System.out.println(k + ": " + v));
        } else {
            Map<String, Object> stats = sim.runAutoMode(duration, delta);
            System.out.println("Автоматический режим завершен.");
            System.out.printf("Время моделирования: %.2f%n", sim.clock);
            System.out.println("Начато заявок: " + stats.get("started"));
            System.out.println("Завершено заявок: " + stats.get("completed"));
            System.out.println("Отказано: " + stats.get("rejected") + " (" + stats.get("rejectPercent") + "%)");
            System.out.printf("Среднее ожидание: %.2f%n", stats.get("avgWait"));
            System.out.printf("Среднее обслуживание: %.2f%n", stats.get("avgService"));
        }
    }
}
