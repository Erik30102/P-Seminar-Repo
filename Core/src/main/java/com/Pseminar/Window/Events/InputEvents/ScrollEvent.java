package com.Pseminar.Window.Events.InputEvents;

import com.Pseminar.Window.Events.Event;

public class ScrollEvent extends Event {
    private final float xOffset, yOffset;

    public ScrollEvent(float xOffset, float yOffset) {
        super(EventType.MOUSE_SCROLLED);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /** 
     * @return float
     */
    public float GetXOffset() {
        return xOffset;
    }
    
    /** 
     * @return float
     */
    public float GetYOffset() {
        return yOffset;
    }

    /** 
     * @return String
     */
    @Override
    public String getEventDetails() {
        return "xOffset=" + xOffset + ", yOffset=" + yOffset;
    }

    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "ScrollEvent{" + "type=" + getType() + ", " + getEventDetails() + '}';
    }
}

