package com.Pseminar.Window.Events;

public abstract class Event {
    public enum EventType {
        WINDOW_CLOSE,
        WINDOW_RESIZE,
        MOUSE_MOVED,
        MOUSE_CLICKED,
        MOUSE_SCROLLED,
        KEY_PRESSED,
        KEY_RELEASED
    }

    private final EventType type;

    protected Event(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public abstract String getEventDetails();

    @Override
    public String toString() {
        return "Event{type=" + type + ", details=" + getEventDetails() + '}';
    }
}


