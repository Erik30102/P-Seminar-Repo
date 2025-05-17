package com.Pseminar.Window.Events.InputEvents;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.glfw.GLFW;

import com.Pseminar.Window.Events.Event;

public class ControllerEvent extends Event{
    
    boolean Controllerbenutzung;
    
    public ControllerEvent(EventType type) {
        super(type);
        
        Controllerbenutzung = false;
        
        for(;;)
        {
            controllererkennung();
        }
        
    }

    // Erkennt ob ein controller da is und richtet diesen auch ein mit den Axen
    void controllererkennung()
    {
        if (GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1)) {
            System.out.println("Controller 1 is da mein Lieber :)");
            
            String joystickName = GLFW.glfwGetJoystickName(GLFW.GLFW_JOYSTICK_1);
            System.out.println("Joystick 1:" + joystickName);

            Controllerbenutzung = true;

            //Die Joystickfunktion return Floatbuffer und keine floats, deswegen muss das noch geändert werden
            FloatBuffer axenBuffer = GLFW.glfwGetJoystickAxes(GLFW.GLFW_JOYSTICK_1);
            axenBuffer.rewind();

            float[] axen = new float[axenBuffer.limit()];
            axenBuffer.get(axen);

            for (int i = 0; i < axen.length; i++)
            {
            System.out.println("Axe:" + i + ":" + axen[i]);
            }

            //Erkennt alle Joystick Buttons und fügt diese hinzu
            ByteBuffer bytebuffer = GLFW.glfwGetJoystickButtons(GLFW.GLFW_JOYSTICK_1);
            bytebuffer.rewind();

            byte[] buttons = new byte[bytebuffer.limit()];
            bytebuffer.get(buttons);

            for (int i = 0; i < buttons.length; i++)
            {
                System.out.println("Jimm Knopf:" + i + ":" + (buttons[i] == GLFW.GLFW_PRESS ? "Gedrückt" : "Nicht gedrückt"));
            }

            GLFW.glfwSetJoystickCallback((jid, event) -> {
                if (event == GLFW.GLFW_CONNECTED)
                {
                    System.out.println("Joystick" + jid + "verbunden");
                }
                else if(event == GLFW.GLFW_DISCONNECTED)
                {
                    System.out.println("Joystick" + jid + "nicht verbunden, duhhhh");
                }
            });

        }
    }
    
    
    @Override
    public String getEventDetails() {
        return "keyCode=";
    }

    @Override
    public String toString() {
        return "KeyEvent{" + "type=" + getType() + ", " + getEventDetails() + '}';
    }

}
