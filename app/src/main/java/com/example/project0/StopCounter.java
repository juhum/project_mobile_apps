package com.example.project0;

public class StopCounter {
    private static int stopCounter = 0;

    public static int getStopCounter() {
        return stopCounter;
    }

    public static void incrementStopCounter() {
        stopCounter++;
    }
}

