package com.Pseminar.Window.Events.InputEvents;

import com.Pseminar.Window.Events.Event;

public class MouseClickEvent extends Event {
    private final int x, y, button;

    public MouseClickEvent(int x, int y, int button) {
        super(EventType.MOUSE_CLICKED);
        this.x = x;
        this.y = y;
        this.button = button;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getButton() {
        return button;
    }

    @Override
    public String getEventDetails() {
        return "x=" + x + ", y=" + y + ", button=" + button;
    }

    @Override
    public String toString() {
        return "MouseClickEvent{" + "type=" + getType() + ", " + getEventDetails() + '}';
    }
}



