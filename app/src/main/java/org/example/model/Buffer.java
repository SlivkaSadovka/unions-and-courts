package org.example.model;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
    public final int size;
    private final Queue<Complaint> stack = new LinkedList<>();

    public Buffer(int size) {
        this.size = size;
    }

    public boolean enqueue(Complaint c) {
        if (isFull()) return false;
        stack.add(c);
        return true;
    }

    public Complaint dequeue() {
        return stack.poll();
    }

    public boolean isFull() {
        return stack.size() >= size;
    }

    public int length() {
        return stack.size();
    }

    public Queue<Complaint> view() {
        return stack;
    }
}
