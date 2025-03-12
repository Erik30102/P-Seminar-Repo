package com.Pseminar.Window.Events.InputEvents;

public class MouseClickEvent extends Event {
    private final int x, y, button;

    public MouseClickEvent(int x, int y, int button) {
        super(EventType.MOUSE_CLICKED);
        this.x = x;
        this.y = y;
        this.button = button;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getButton() { return button; }

    @Override
    public String toString() {
        return "MouseClickEvent{" + "x=" + x + ", y=" + y + ", button=" + button + '}';
    }
}
