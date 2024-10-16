package com.Sandbox;

import com.Pseminar.Application;
import com.Pseminar.Logger;

public class SandboxApplication extends Application {

    private int TestNumber = 0;

    public static void main(String[] args) {
        new SandboxApplication().Run();
    }

    @Override
    public void OnStart() {
        Logger.error("test error");
        Logger.info("test info");
        Logger.warm("test warning");
    }

    @Override
    public void OnUpdate() {
        TestNumber++;
        if(TestNumber >= 2000) {
            this.running = false;
        }
    }

    @Override
    public void OnDispose() {
        System.out.println("Wow es hat bis: " + this.TestNumber + " hochgezählt");
    }
}
