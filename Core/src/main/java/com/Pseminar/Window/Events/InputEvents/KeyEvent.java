package com.Pseminar.Window.Events.InputEvents;

import com.Pseminar.Window.Events.Event;

public class KeyEvent extends Event {
    private final int keyCode;

    public KeyEvent(EventType type, int keyCode) {
        super(type);
        this.keyCode = keyCode;
    }

    /** 
     * @return int
     */
    public int getKeyCode() {
        return keyCode;
    }

    /** 
     * @return String
     */
    @Override
    public String getEventDetails() {
        return "keyCode=" + keyCode;
    }

    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "KeyEvent{" + "type=" + getType() + ", " + getEventDetails() + '}';
    }
}


