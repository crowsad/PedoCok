package com.github.gist.pedocok;

import android.hardware.SensorEvent;

// Will listen to step alerts
public interface StepListener {

    void onSensorChanged(SensorEvent event);

    public void step(long timeNs);

}