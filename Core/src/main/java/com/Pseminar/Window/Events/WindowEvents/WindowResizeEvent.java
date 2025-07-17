package com.Pseminar.Window.Events.WindowEvents;

import com.Pseminar.Window.Events.Event;

public class WindowResizeEvent extends Event{
    private final int width, height;

    public WindowResizeEvent(int width, int height){
        super(EventType.WINDOW_RESIZE);
        this.width = width;
        this.height = height;
    }

    /** 
     * @return int
     */
    public int getWidth(){
        return width;
    }

    /** 
     * @return int
     */
    public int getHeight(){
        return height;
    }

    /** 
     * @return String
     */
    @Override
    public String getEventDetails() {
        return "Window Close Event";
    }

    /** 
     * @return String
     */
    @Override
    public String toString(){
        return "WindowResizeEvent: ["+width+", "+height+"]";
    }

}
