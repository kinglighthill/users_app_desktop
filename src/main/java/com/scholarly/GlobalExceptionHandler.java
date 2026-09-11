package com.scholarly;

import java.lang.Thread;
import java.lang.Thread.UncaughtExceptionHandler;

public class GlobalExceptionHandler implements UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        System.out.println("Uncaught exception in thread: " + thread.getName());
        if (MainApplication.fileHandler != null) {
            MainApplication.logger.severe("Uncaught exception in thread: " + thread.getName());
            MainApplication.logger.severe("Exception: " + throwable.toString());
            for (StackTraceElement element : throwable.getStackTrace()) {
                MainApplication.logger.severe(element.toString());
            }
            MainApplication.fileHandler.close();
        }
    }
}
