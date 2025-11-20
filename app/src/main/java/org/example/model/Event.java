package org.example.model;

public class Event {
    public final EventType type;
    public final Complaint complaint;
    public final int scheduledStep; // на каком шаге планируется событие (для информации)
    public final int originStep; // вспомогательно - откуда вызвано

    public Event(EventType type, Complaint complaint, int scheduledStep, int originStep) {
        this.type = type;
        this.complaint = complaint;
        this.scheduledStep = scheduledStep;
        this.originStep = originStep;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (sch:%d)", type, complaint, scheduledStep);
    }
}