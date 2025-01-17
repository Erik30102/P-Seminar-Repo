package com.Pseminar.Window.Events;

public abstract class Event {

    public enum EventType{
        WINDOW_RESIZE, WINDOW_CLOSE
    }

    private final EventType type;

    public Event(EventType type){
        this.type = type;
    }

    public EventType getType(){
        return type;
    }

    public abstract String toString();

}
