package org.example.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class Buffer {
    private final int capacity;
    private final Deque<Complaint> stack;

    public Buffer(int capacity) {
        this.capacity = Math.max(0, capacity);
        this.stack = new ArrayDeque<>();
    }

    public List<Complaint> enqueue(Complaint c) {
        List<Complaint> rejected = new ArrayList<>();
        if (capacity == 0) {
            rejected.add(c);
            return rejected;
        }
        if (stack.size() >= capacity) {

            while (stack.size() >= capacity) {
                Complaint oldest = stack.removeLast();
                rejected.add(oldest);
            }
        }
        stack.addFirst(c);
        return rejected;
    }

    public Complaint dequeue() {
        return stack.pollFirst();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public boolean isFull() {
        return stack.size() >= capacity;
    }

    public int size() {
        return stack.size();
    }

    public List<Integer> snapshotIds() {
        return stack.stream().map(Complaint::getId).collect(Collectors.toList());
    }

    public boolean remove(Complaint c) {
        return stack.remove(c);
    }
}

