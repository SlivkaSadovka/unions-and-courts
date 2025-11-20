package org.example.model;

import java.util.*;

public class Dispatcher {
    private Buffer buffer;
    private List<Operator> operators;
    private boolean directDispatch;

    public Dispatcher(Buffer buffer, List<Operator> operators, boolean directDispatch) {
        this.buffer = buffer;
        this.operators = operators;
        this.directDispatch = directDispatch;
    }

    public void onArrival(Complaint complaint, double now) {
        if (directDispatch) {
            tryDispatchDirect(complaint, now);
        } else {
            buffer.enqueue(complaint);
            tryDispatch(now);
        }
    }

    private void tryDispatch(double now) {
        for (Operator op : operators) {
            if (op.isFree(now)) {
                Complaint c = buffer.dequeue();
                if (c != null) {
                    op.takeComplaint(c, now);
                }
            }
        }
    }

    private void tryDispatchDirect(Complaint complaint, double now) {
        for (Operator op : operators) {
            if (op.isFree(now)) {
                op.takeComplaint(complaint, now);
                return;
            }
        }
        buffer.enqueue(complaint); // если все заняты, кладём в буфер
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public List<Operator> getOperators() {
        return operators;
    }
}
