package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metrics {
    private int totalGenerated = 0;
    private int totalStarted = 0;
    private int totalCompleted = 0;
    private int totalRejected = 0;
    private final List<String> events = new ArrayList<>();
    private final List<double[]> queueHistory = new ArrayList<>(); // [time, queueLength]
    private final List<double[]> rejectionHistory = new ArrayList<>(); // [time, rejectPercent]
    private final Map<Integer, Double> operatorUtil = new HashMap<>();
    private final Map<Integer, Double> callStartTime = new HashMap<>();
    private final List<Double> waitTimes = new ArrayList<>();
    private final List<Double> serviceTimes = new ArrayList<>();

    public List<String> getEvents() {
        return events;
    }

    public Map<Integer, Double> getOperatorUtil() {
        return operatorUtil;
    }

    public void logGenerated(Complaint c, double time) {
        totalGenerated++;
        events.add(String.format("generated#%d", c.getId()));
    }

    public void logBuffered(Complaint c, double time) {
        events.add(String.format("buffered#%d", c.getId()));
    }

    public void logReject(String reason, Complaint c, double time) {
        totalRejected++;
        events.add(String.format("reject(%s)%s", reason, c == null ? "" : "#" + c.getId()));
    }

    public void logStart(Complaint c, Operator op, double time) {
        totalStarted++;
        callStartTime.put(c.getId(), time);
        double wait = time - c.getId();
        waitTimes.add(wait);
        events.add(String.format("start#%d@O%d", c.getId(), op.getId()));
    }

    public void logDone(Complaint c, double time) {
        totalCompleted++;
        double start = callStartTime.getOrDefault(c.getId(), time);
        serviceTimes.add(Math.max(0.0, time - start));
        events.add(String.format("done#%d", c.getId()));
    }

    public void recordSnapshot(double time, Buffer buffer, List<Operator> ops) {
        queueHistory.add(new double[]{time, buffer.size()});
        rejectionHistory.add(new double[]{time, rejectionPercent()});
        for (Operator op : ops) {
            operatorUtil.put(op.getId(), op.utilization(time)); // approximate at this time
        }
    }

    public double rejectionPercent() {
        int total = totalStarted + totalRejected;
        if (total == 0) return 0.0;
        return ((double) totalRejected / total) * 100.0;
    }

    public Map<String, Object> getSummary(double simTime) {
        double avgWait = waitTimes.stream().mapToDouble(d -> d).average().orElse(0.0);
        double avgService = serviceTimes.stream().mapToDouble(d -> d).average().orElse(0.0);
        Map<String, Object> out = new HashMap<>();
        out.put("time", simTime);
        out.put("generated", totalGenerated);
        out.put("started", totalStarted);
        out.put("completed", totalCompleted);
        out.put("rejected", totalRejected);
        out.put("rejectPercent", rejectionPercent());
        out.put("avgWait", avgWait);
        out.put("avgService", avgService);
        out.put("operatorUtil", new HashMap<>(operatorUtil));
        return out;
    }
}

