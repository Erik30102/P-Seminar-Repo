package com.Pseminar.Window.Events.InputEvents;

import com.Pseminar.Window.Events.Event;

public class MouseMoveEvent extends Event {
    private final int x, y;

    public MouseMoveEvent(int x, int y) {
        super(Event.EventType.MOUSE_MOVED);
        this.x = x;
        this.y = y;
    }

    /** 
     * @return int
     */
    public int getX() {
        return x;
    }

    /** 
     * @return int
     */
    public int getY() {
        return y;
    }

    /** 
     * @return String
     */
    @Override
    public String getEventDetails() {
        return "x=" + x + ", y=" + y;
    }

    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "MouseEvent{" + "type=" + getType() + ", " + getEventDetails() + '}';
    }
}

