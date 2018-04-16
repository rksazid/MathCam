package com.cse14kuet.rksazid.mathcam;

/**
 * Created by ASUS on 28/10/2017.
 */

public class MyBounceInterpolator implements android.view.animation.Interpolator {

    private double mAmplitude = 1;
    private double mFrequency = 10;

    public MyBounceInterpolator(double mAplitude, double mFrequency) {
        this.mFrequency = mFrequency;
        this.mAmplitude = mAplitude;
    }

    @Override
    public float getInterpolation(float time) {
        return (float)(-1*Math.pow(Math.E, -time/mAmplitude)*Math.cos(mFrequency*time)+1);
    }
}
