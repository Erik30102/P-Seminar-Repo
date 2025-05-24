package com.Pseminar.Window.Events.InputEvents;

import com.Pseminar.Window.Events.Event;

public class MouseReleaseEvent extends Event {
    private final int button;

    public MouseReleaseEvent(int button) {
        super(EventType.MOUSE_RELEASED);
        this.button = button;
    }

    public int getButton() {
        return button;
    }

    @Override
    public String getEventDetails() {
        return "button=" + button;
    }

    @Override
    public String toString() {
        return "MouseClickEvent{" + "type=" + getType() + ", " + getEventDetails() + '}';
    }
}



