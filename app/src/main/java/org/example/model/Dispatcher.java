package org.example.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Dispatcher {
    private final Buffer buffer;
    private final List<Operator> operators;
    private final Metrics metrics;
    private final boolean directDispatch;

    public Dispatcher(Buffer buffer, List<Operator> operators, Metrics metrics, boolean directDispatch) {
        this.buffer = buffer;
        this.operators = operators;
        this.metrics = metrics;
        this.directDispatch = directDispatch;
    }

    public void onArrival(Complaint c, double now) {
        metrics.logGenerated(c, now);
        if (directDispatch) {
            Optional<Operator> free = findFree(now);
            if (free.isPresent()) {
                startProcessing(free.get(), c, now);
                return;
            }
        }
        // try enqueue; if capacity==0, enqueue returns false
        List<Complaint> rejected = buffer.enqueue(c);;
        if (!rejected.isEmpty()) {
            for (Complaint r : rejected) {
                metrics.logReject("of", r, now);
            }
        } else {
            metrics.logBuffered(c, now);
        }
        tryDispatch(now);
    }

    // try to assign pending complaints to free operators
    public void tryDispatch(double now) {
        List<Operator> free = collectFree(now);
        while (!free.isEmpty() && !buffer.isEmpty()) {
            Operator op = free.removeFirst();
            Complaint c = buffer.dequeue(); // LIFO
            if (c == null) break;
            startProcessing(op, c, now);
        }
    }

    private void startProcessing(Operator op, Complaint c, double now) {
        op.take(c, now);
        metrics.logStart(c, op, now);
    }

    // called by simulation to check for finished processing
    public void updateOperators(double now) {
        for (Operator op : operators) {
            Complaint finished = op.finishIfDue(now);
            if (finished != null) {
                metrics.logDone(finished, now);
                // after finishing, try dispatch again (may free up operator)
            }
        }
        tryDispatch(now);
    }

    private List<Operator> collectFree(double now) {
        List<Operator> list = new ArrayList<>();
        for (Operator op : operators) if (op.isFree(now)) list.add(op);
        // priority by operator id (lower id -> higher priority)
        list.sort(Comparator.comparingInt(Operator::getId));
        return list;
    }

    private Optional<Operator> findFree(double now) {
        for (Operator op : operators) if (op.isFree(now)) return Optional.of(op);
        return Optional.empty();
    }
}


