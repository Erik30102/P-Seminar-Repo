package com.Pseminar;

public abstract class Application {
    private static Application INSTANCE;

    public Application() {
        INSTANCE = this;
    }

    protected boolean running = true;

    public void Run() {
        OnStart();

        while (running) {
            OnUpdate();
        }

        OnDispose();
    }

    public abstract void OnStart();

    public abstract void OnUpdate();

    public abstract void OnDispose();

    public static Application GetApplication() {
        return INSTANCE;
    }
}
