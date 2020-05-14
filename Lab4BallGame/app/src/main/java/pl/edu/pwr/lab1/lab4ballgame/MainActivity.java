package pl.edu.pwr.lab1.lab4ballgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private CanvasView canvas;
    private int circle_radious = 30;
    private float X_circle_coordinate;
    private float Y_circle_coordinate;

    private Timer timer;
    private Handler handler;

    private SensorManager sensorManager;
    private Sensor gyroscope_sensor;

    private float Xsensor;
    private float Ysensor;
    private float Zsensor;
    private long lastSensorUpdateTime = 0;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscope_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, gyroscope_sensor,SensorManager.SENSOR_DELAY_NORMAL);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int screenWidth = size.x;
        int screenHeight = size.y;

        X_circle_coordinate = screenWidth / 2 - circle_radious;
        Y_circle_coordinate = screenHeight / 2 - circle_radious;

        canvas = new CanvasView(MainActivity.this);
        setContentView(canvas);

        handler= new Handler() {
            public  void handleMessage (Message message)
            {
                canvas.invalidate();
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            float lastX = 0;
            float lastY = 0;
            @Override
            public void run() {
                if(X_circle_coordinate - circle_radious > 0 && Y_circle_coordinate - circle_radious > 0)
                {
                    if (Xsensor > 1 || Xsensor < -1)
                        lastX = Xsensor;

                    if (lastX != 0)
                        if(lastX < 0)
                            Y_circle_coordinate += 5* lastX;
                        else
                            Y_circle_coordinate -= 5* lastX;

                    if (Ysensor > 1 || Ysensor < -1)
                       lastY = Ysensor;

                    if (lastY != 0)
                        if(lastY < 0  )
                            X_circle_coordinate -=5 * lastY;
                        else
                            X_circle_coordinate +=5* lastY;
                }else {
                    timer.cancel();
                    Toast.makeText(MainActivity.this, "Game Over!", Toast.LENGTH_LONG);
                }

                System.out.println("X:"+ X_circle_coordinate);
                System.out.println("Y:"+ Y_circle_coordinate);

                handler.sendEmptyMessage(0);
            }
        },0,100);


    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if(mySensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            // We limit the times values change to 100ms
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastSensorUpdateTime) > 100)
            {
                lastSensorUpdateTime = currentTime;
                Xsensor = event.values[0];
                Ysensor = event.values[1];
                Zsensor = event.values[2];
            }
        }
    }


    // Unused
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private class CanvasView extends View{
        private Paint pen;

        public CanvasView(Context context) {
            super(context);
            setFocusable(true);

            pen = new Paint();
        }

        public void onDraw(Canvas screen){
            pen.setStyle(Paint.Style.FILL);
            pen.setAntiAlias(true);
            pen.setTextSize(30f);

            screen.drawCircle(X_circle_coordinate,Y_circle_coordinate, circle_radious, pen);
        }
    }
}
