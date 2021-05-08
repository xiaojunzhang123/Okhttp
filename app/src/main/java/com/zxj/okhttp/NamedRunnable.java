package com.zxj.okhttp;

public abstract class NamedRunnable implements Runnable{

    protected final String name;

    public NamedRunnable(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        String oldName = Thread.currentThread().getName();
        Thread.currentThread().setName(name);
        try {
            execute();
        } finally {
            Thread.currentThread().setName(oldName);
        }
    }

    protected abstract void execute();
}
