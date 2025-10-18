package com.example.termproject_game;

import android.os.Handler;

public class TimerThread extends Thread {
    private long startTime;
    private Handler timerHandler;
    private long baseTime;
    private int sleep;

    public TimerThread(Handler handler) {
        startTime = System.currentTimeMillis();
        timerHandler = handler;
        this.baseTime = CGD.BASE_TIME;
        this.sleep = CGD.THREAD_SLEEP;
    }

    public TimerThread(Handler handler, long setTime) {
        startTime = System.currentTimeMillis();
        timerHandler = handler;
        this.baseTime = 1000 * setTime;
        this.sleep = CGD.THREAD_SLEEP;
    }

    public TimerThread(Handler handler, long setTime, int sleep) {
        startTime = System.currentTimeMillis();
        timerHandler = handler;
        this.baseTime = 1000 * setTime;
        this.sleep = sleep;
    }

    public void run() {
        try {
            if(baseTime == 0){
                while (!Thread.currentThread().isInterrupted()) {       // 정시간
                    Thread.sleep(sleep);

                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime;

                    timerHandler.obtainMessage(0, elapsedTime).sendToTarget();
                }
            } else {
                while (!Thread.currentThread().isInterrupted()) {       // 역시간
                    Thread.sleep(sleep);

                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime;
                    long outputTime = baseTime - elapsedTime;

                    timerHandler.obtainMessage(0, outputTime).sendToTarget();
                }
            }

        } catch (InterruptedException e) {
            return;
        }
    }
}
