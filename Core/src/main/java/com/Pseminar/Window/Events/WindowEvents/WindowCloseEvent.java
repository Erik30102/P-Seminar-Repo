package com.Pseminar.Window.Events.WindowEvents;

import com.Pseminar.Window.Events.Event;

public class WindowCloseEvent extends Event{

public WindowCloseEvent(){
    super(EventType.WINDOW_CLOSE);
}

public String toString(){
    return "WindowCloseEvent";
}

@Override
    public String getEventDetails() {
        return "Window Close Event";
    }

}
