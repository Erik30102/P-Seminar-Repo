package com.Pseminar.Window.Events.InputEvents;

import com.Pseminar.Window.Events.Event;

public class CharEvent extends Event {
    private int keycode;

	public CharEvent(int keycode) {
        super(EventType.CHAR_EVENT);
		this.keycode = keycode;
	}

	/**
	 * @return Key Code for the given KeyPressed Event in EL.KEY_XX
	 */
	public int GetKeyCode() {
		return this.keycode;
	}

	/** 
	 * @return String
	 */
	@Override
	public String toString() {
		return "CharEvent for KeyCode: " + keycode;
	}

    /** 
	 * @return String
	 */
	@Override
    public String getEventDetails() {
		return "CharEvent";
    }
    
}
