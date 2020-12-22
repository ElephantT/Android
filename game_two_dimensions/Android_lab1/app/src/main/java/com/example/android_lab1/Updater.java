package com.example.android_lab1;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

public class Updater extends Thread {

    private long time;
    private final int fps = 60;
    private boolean toRun = false;
    private Movement movement;
    private SurfaceHolder surface_holder;

    public Updater(Movement movement) {
        this.movement = movement;
        surface_holder = this.movement.getHolder();
    }

    public void setRunning(boolean run) {
        toRun = run;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        Canvas canvas;
        while (toRun) {
            long cTime = System.currentTimeMillis();

            if ((cTime - time) <= (1000 / fps)) {
                canvas = null;
                try {
                    canvas = surface_holder.lockCanvas(null);
                    movement.updatePhysics();
                    movement.onDraw(canvas);
                } finally {
                    if (canvas != null) {
                        surface_holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
            time = cTime;
        }
    }
}
