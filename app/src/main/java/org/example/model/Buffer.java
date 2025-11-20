package org.example.model;

import java.util.*;

public class Buffer {
    protected int size;
    protected Deque<Complaint> stack;

    public Buffer(int size) {
        this.size = size;
        this.stack = new ArrayDeque<>();
    }

    public boolean enqueue(Complaint complaint) {
        if (stack.size() >= size) {
            stack.removeLast(); // выбрасываем старую заявку
        }
        stack.push(complaint);
        return true;
    }

    public Complaint dequeue() {
        return stack.poll();
    }

    public boolean isFull() {
        return stack.size() >= size;
    }

    public List<Complaint> getAll() {
        return new ArrayList<>(stack);
    }

    public int size() {
        return stack.size();
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}