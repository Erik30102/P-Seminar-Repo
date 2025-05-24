package com.Pseminar.Window.Events.InputEvents;

import com.Pseminar.Window.Events.Event;

public class ScrollEvent extends Event {
    private final float xOffset, yOffset;

    public ScrollEvent(float xOffset, float yOffset) {
        super(EventType.MOUSE_SCROLLED);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public float GetXOffset() {
        return xOffset;
    }
    
    public float GetYOffset() {
        return yOffset;
    }

    @Override
    public String getEventDetails() {
        return "xOffset=" + xOffset + ", yOffset=" + yOffset;
    }

    @Override
    public String toString() {
        return "ScrollEvent{" + "type=" + getType() + ", " + getEventDetails() + '}';
    }
}

