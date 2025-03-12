package com.Pseminar.Window.Events.InputEvents;

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
    public String toString() {
        return "KeyEvent{" + "type=" + getType() + ", keyCode=" + keyCode + '}';
    }
}
