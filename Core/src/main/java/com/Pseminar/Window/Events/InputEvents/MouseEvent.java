package com.Pseminar.Window.Events.InputEvents;

import com.Pseminar.Window.Events.Event;

public class MouseEvent extends Event {
    private final int x, y;

    public MouseEvent(EventType type, int x, int y) {
        super(type);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String getEventDetails() {
        return "x=" + x + ", y=" + y;
    }

    @Override
    public String toString() {
        return "MouseEvent{" + "type=" + getType() + ", " + getEventDetails() + '}';
    }
}

