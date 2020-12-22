package com.example.android_lab1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.SENSOR_SERVICE;

public class Movement extends SurfaceView implements SurfaceHolder.Callback {
    private int screen_width, screen_height;
    private SensorManager sensor_manager;
    private Sensor sensor_accel;
    private Sensor sensor_magnet;

    private float[] sensor_accelerometer_values = new float[3];
    private float[] sensor_magnetic_values = new float[3];
    private float[] sensors_results = new float[3];

    private Updater updater;
    Context context;
    GameActivity game_activity;

    private int x_player_coordiante, y_player_coordinate;
    private int x_speed, y_speed;
    private int players_circle_radius;
    private Paint players_circle_paint;

    private int final_destination_width, final_destination_height;
    private int x_finals_destination_coordinate, y_finals_destination_coordinate;
    private Paint final_destination_paint;

    private SensorEventListener sensor_checker = new SensorEventListener() {
        // get sensor's results
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, sensor_accelerometer_values, 0, 3);
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, sensor_magnetic_values, 0, 3);
            }
        }
    };

    public Movement(Context context, GameActivity game_activity) {
        super(context);
        getHolder().addCallback(this);
        this.context = context;
        this.game_activity = game_activity;

        x_speed = 4;
        y_speed = 4;

        players_circle_radius = 32;
        players_circle_paint = new Paint();
        players_circle_paint.setColor(Color.BLUE);

        final_destination_width = players_circle_radius * 8;
        final_destination_height = players_circle_radius * 4;
        final_destination_paint = new Paint();
        final_destination_paint.setColor(Color.RED);

        sensor_manager = (SensorManager)context.getSystemService(SENSOR_SERVICE);
        sensor_accel = sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor_magnet = sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }


    void getDeviceOrientation() {
        // transform sensor results to degrees for easier operations on them
        float[] temp = new float[9];
        SensorManager.getRotationMatrix(temp, null, sensor_accelerometer_values,
                sensor_magnetic_values);
        SensorManager.getOrientation(temp, sensors_results);
        sensors_results[0] = (float) Math.toDegrees(sensors_results[0]);
        sensors_results[1] = (float) Math.toDegrees(sensors_results[1]);
        sensors_results[2] = (float) Math.toDegrees(sensors_results[2]);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // start
        Rect surface_frame = holder.getSurfaceFrame();
        screen_width = surface_frame.width();
        screen_height = surface_frame.height();

        x_player_coordiante = screen_width / 2;
        y_player_coordinate = players_circle_radius;
        setFinalDestinationParameters();

        updater = new Updater(this);
        updater.setRunning(true);
        updater.start();
    }

    protected void setFinalDestinationParameters() {
        Random random = new Random();
        x_finals_destination_coordinate = random.nextInt(screen_width -
                2 * final_destination_width);
        x_finals_destination_coordinate += final_destination_width;
        y_finals_destination_coordinate = screen_height - final_destination_height * 4;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // we will do everything in special class Updater
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        sensor_manager.unregisterListener(sensor_checker);
        updater.setRunning(false);
        while (true) {
            try {
                updater.join();
                break;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(x_player_coordiante, y_player_coordinate, players_circle_radius,
                players_circle_paint);
        canvas.drawRect(x_finals_destination_coordinate - (int)(final_destination_width / 2.0),
                y_finals_destination_coordinate + (int)(final_destination_height / 2.0),
                x_finals_destination_coordinate + (int)(final_destination_width / 2.0),
                y_finals_destination_coordinate - (int)(final_destination_height / 2.0),
                final_destination_paint);
    }

    public void updatePhysics() {
        sensor_manager.registerListener(sensor_checker, sensor_accel, SensorManager.SENSOR_DELAY_NORMAL);
        sensor_manager.registerListener(sensor_checker, sensor_magnet, SensorManager.SENSOR_DELAY_NORMAL);
        getDeviceOrientation();

        if (sensors_results[2] > 0) {
            x_player_coordiante += x_speed;
        }
        if (sensors_results[2] < 0) {
            x_player_coordiante -= x_speed;
        }
        if (sensors_results[1] < 0) {
            y_player_coordinate += y_speed;
        }
        if (sensors_results[1] > 0) {
            y_player_coordinate -= y_speed;
        }

        if (x_player_coordiante >= x_finals_destination_coordinate - final_destination_width / 2 &&
            x_player_coordiante <= x_finals_destination_coordinate + final_destination_width / 2 &&
            y_player_coordinate >= y_finals_destination_coordinate - final_destination_height / 2 &&
            y_player_coordinate <= y_finals_destination_coordinate + final_destination_height / 2) {
            exitSystem(true);
        }

        if (y_player_coordinate - players_circle_radius < 0 ||
                y_player_coordinate + players_circle_radius > screen_height) {
            exitSystem(false);
        }
        if (x_player_coordiante - players_circle_radius < 0 ||
                x_player_coordiante + players_circle_radius > screen_width) {
            exitSystem(false);
        }
    }

    protected void exitSystem(boolean flag) {
        updater.setRunning(false);
        Intent data = new Intent();
        data.putExtra("win", flag);
        game_activity.setResult(RESULT_OK,data);
        game_activity.finish();
    }
}
