package com.Pseminar.Window.Events.WindowEvents;

import com.Pseminar.Window.Events.Event;

public class WindowResizeEvent extends Event{
    private final int width, height;

    public WindowResizeEvent(int width, int height){
        super(EventType.WINDOW_RESIZE);
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    @Override
    public String getEventDetails() {
        return "Window Close Event";
    }

    @Override
    public String toString(){
        return "WindowResizeEvent: ["+width+", "+height+"]";
    }

}
