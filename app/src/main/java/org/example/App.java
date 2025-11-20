package org.example;
import org.example.model.Simulation;

public class App {

    public static void main(String[] args) {

        // -------------------------
        // DEFAULT PARAMETERS
        // -------------------------
        String mode = "step";
        int steps = 20;
        float duration = 50;
        float delta = 0.5f;
        int bufferSize = 8;
        int operators = 3;
        int workers = 3;
        float lambda = 0.6f;

        // -------------------------
        // PARSE ARGS
        // -------------------------
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {

                case "--mode": mode = args[++i]; break;
                case "--steps": steps = Integer.parseInt(args[++i]); break;
                case "--duration": duration = Float.parseFloat(args[++i]); break;
                case "--delta": delta = Float.parseFloat(args[++i]); break;

                case "--buffer-size": bufferSize = Integer.parseInt(args[++i]); break;
                case "--operators": operators = Integer.parseInt(args[++i]); break;
                case "--workers": workers = Integer.parseInt(args[++i]); break;

                case "--lambda": lambda = Float.parseFloat(args[++i]); break;

                default:
                    System.out.println("Unknown argument: " + args[i]);
                    return;
            }
        }

        Simulation sim = new Simulation(lambda, workers, bufferSize, operators, delta);

        // step mode
        if (mode.equals("step")) {

            System.out.println(">> STEP MODE");
            System.out.printf("steps=%d, dt=%.3f, buffer=%d, ops=%d\n",
                    steps, delta, bufferSize, operators);

            for (int i = 0; i < steps; i++) {
                sim.tick();
                sim.printSnapshot();  // calendar table per iteration
            }

            System.out.println("---- END OF STEP MODE ----");
            sim.printStats();
            return;
        }

        // auto mode
        if (mode.equals("auto")) {

            System.out.println(">> AUTO MODE");
            System.out.printf("duration=%.2f, dt=%.3f, buffer=%d, ops=%d\n",
                    duration, delta, bufferSize, operators);

            sim.runAuto(duration);
            sim.printStats();
            return;
        }

        System.out.println("ERROR: Unknown mode: " + mode);
    }
}