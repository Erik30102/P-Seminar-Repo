package com.Pseminar.Window.Events.InputEvents;

import com.Pseminar.Window.Events.Event;

public class KeyEvent extends Event {
    private final int keyCode;

    public KeyEvent(EventType type, int keyCode) {
        super(type);
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    @Override
    public String getEventDetails() {
        return "keyCode=" + keyCode;
    }

    @Override
    public String toString() {
        return "KeyEvent{" + "type=" + getType() + ", " + getEventDetails() + '}';
    }
}


