package org.example.model;

public class Dispatcher {

    private final Buffer buffer;
    private final Operator[] operators;
    private final Metrics metrics;

    public Dispatcher(Buffer buffer, Operator[] operators, Metrics metrics) {
        this.buffer = buffer;
        this.operators = operators;
        this.metrics = metrics;
    }

    public void onArrival(Complaint c) {
        metrics.generated++;
        metrics.log("GENERATED #" + c.id + " from W" + c.workerId);

        if (!buffer.enqueue(c)) {
            metrics.rejected++;
            metrics.log("REJECT #" + c.id);
        } else {
            metrics.buffered++;
            metrics.log("BUFFERED #" + c.id);
        }
    }

    public void tryDispatch(float now) {
        for (Operator op : operators) {
            if (op.isFree(now)) {
                Complaint next = buffer.dequeue();
                if (next == null) return;

                op.takeComplaint(next, now);
                metrics.log("SENT TO OPERATOR " + op.id + " complaint #" + next.id);
            }
        }
    }
}
