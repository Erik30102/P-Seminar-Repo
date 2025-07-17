package com.Pseminar.Window.Events.InputEvents;

import com.Pseminar.Window.Events.Event;

public class MouseClickEvent extends Event {
    private final int button;

    public MouseClickEvent(int button) {
        super(EventType.MOUSE_PRESSED);
        this.button = button;
    }

    /** 
     * @return int
     */
    public int getButton() {
        return button;
    }

    /** 
     * @return String
     */
    @Override
    public String getEventDetails() {
        return "button=" + button;
    }

    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "MouseClickEvent{" + "type=" + getType() + ", " + getEventDetails() + '}';
    }
}



