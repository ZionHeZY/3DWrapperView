package com.hezy.sensorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by HeZY on 2021/9/14.
 */
public class SensorView extends FrameLayout implements SensorEventListener {
    private static final int BACKGROUND_VIEW = 1001;
    private static final int FOREFROUND_VIEW = 1002;
    private static final double DIRECTION_LEFT = 1f;
    private static final double DIRECTION_RIGHT = -1f;
    private static double time = 0.02;
    private double backgroundScale;
    private double foregroundScale;
    private int currentView;
    private float maxAngleX = 40;
    private float maxAngleY = 30;
    private Scroller scroller;
    private SensorManager sensorManager;
    private double backScaleMaxX;
    private double foreScaleMaxX;
    private double foreScalMaxY;
    private double backScalMaxY;
    private double offsetRate;
    private double angleX;
    private double angleY;
    private Context ctx;

    public SensorView(@NonNull Context context) {
        this(context, null);
    }

    public SensorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SensorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SensorView);

        switch (typedArray.getInt(R.styleable.SensorView_sensorStyle, 0)) {
            case 0x10:
                currentView = FOREFROUND_VIEW;
                foregroundScale = typedArray.getFloat(R.styleable.SensorView_sensorScale, 1.1f);
                break;

            case 0x20:
                currentView = BACKGROUND_VIEW;
                backgroundScale = typedArray.getFloat(R.styleable.SensorView_sensorScale, 1.3f);
                break;
            default:

                break;

        }
        initView(context);
    }

    public float getMaxAngleX() {
        return maxAngleX;
    }

    public void setMaxAngleX(float maxAngleX) {
        this.maxAngleX = maxAngleX;
    }

    public float getMaxAngleY() {
        return maxAngleY;
    }

    public void setMaxAngleY(float maxAngleY) {
        this.maxAngleY = maxAngleY;
    }

    private void initView(Context context) {
        scroller = new Scroller(context);
        ctx = context;
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            Sensor gyrosopeSenor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            Sensor accelermeterSenor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            sensorManager.registerListener(this, gyrosopeSenor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = px2dip(ctx, getMeasuredWidth());
        int height = px2dip(ctx, getMeasuredHeight());

        backScaleMaxX = (backgroundScale - 1) * width / 2;
        foreScaleMaxX = (foregroundScale - 1) * width / 2;
        foreScalMaxY = (foregroundScale - 1) * height / 2;
        backScalMaxY = (backgroundScale - 1) * height / 2;
        offsetRate = (foregroundScale - 1) / (backgroundScale - 1);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            angleY += sensorEvent.values[0] * time;
            angleX += sensorEvent.values[1] * time;

            double degreeY = handleY((float) Math.toDegrees(angleY));
            double degreeX = handleX((float) Math.toDegrees(angleX));

            int scrollX = (int) ((degreeX / maxAngleX) * (backScaleMaxX));
            int scrollY = (int) ((degreeY / maxAngleY) * (backScalMaxY));

            if (currentView == FOREFROUND_VIEW) {
                scrollX = (int) (-scrollX * offsetRate);
                scrollY = (int) (-scrollY * offsetRate);
            }
            smoothScroll(scrollX, scrollY);
        }
    }

    public void smoothScroll(int destX, int destY) {
        int scrollY = scroller.getFinalY();
        int deltaY = destY - scrollY;

        int scrollX = scroller.getFinalX();
        int deltaX = destX - scrollX;

        scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), deltaX, deltaY, 200);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private float handleX(float tranX) {
        if (tranX > 0) {
            return Math.min(tranX, maxAngleX);
        } else {
            return Math.max(tranX, -maxAngleX);
        }
    }

    private float handleY(float tranY) {
        if (tranY > 0) {
            return Math.min(tranY, maxAngleY);
        } else {
            return Math.max(tranY, -maxAngleY);
        }
    }

    public void unregister() {
        sensorManager.unregisterListener(this);
    }
}
