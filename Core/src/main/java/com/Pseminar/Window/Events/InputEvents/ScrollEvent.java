package com.Pseminar.Window.Events.InputEvents;

import com.Pseminar.Window.Events.Event;

public class ScrollEvent extends Event {
    private final float scrollAmount;

    public ScrollEvent(float scrollAmount) {
        super(EventType.MOUSE_SCROLLED);
        this.scrollAmount = scrollAmount;
    }

    public float getScrollAmount() {
        return scrollAmount;
    }

    @Override
    public String getEventDetails() {
        return "scrollAmount=" + scrollAmount;
    }

    @Override
    public String toString() {
        return "ScrollEvent{" + "type=" + getType() + ", " + getEventDetails() + '}';
    }
}

