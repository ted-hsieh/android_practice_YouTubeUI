package com.example.tedhsieh.praticeyoutube.Views;

import android.view.TextureView;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class VideoViewSizeAnimation extends Animation{

    private TextureView video_textureview;
    private float MaxDy;
    private float smallviewPercentage;
    private boolean isToSmallView;
    private float startPercentage;
    private float endPercentage;

    public VideoViewSizeAnimation (TextureView video_textureview, float MaxDy,float smallviewPercentage,
                                   float currentPercentage ,boolean isToSmallView,boolean isForwardAnimation, long Duration){
        this.video_textureview = video_textureview;
        this.MaxDy = MaxDy;
        this.smallviewPercentage = smallviewPercentage;
        this.isToSmallView = isToSmallView;

        if(isForwardAnimation)
            startPercentage = currentPercentage;
        else
            startPercentage = 1.0f - currentPercentage;
        endPercentage =  1.0f;

        setInterpolator(new LinearInterpolator());

        setDuration(Duration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        float percentage = Math.abs(endPercentage - startPercentage) * interpolatedTime;
        float currentPercentage = startPercentage + percentage;
        float normalized_dy = isToSmallView ? currentPercentage : (1 - currentPercentage);

        float scale_factor = (1 - (normalized_dy * (1.0f- smallviewPercentage)) );
        float tx = (1 - scale_factor) * video_textureview.getWidth() / 2 * 0.95f;
        float ty = normalized_dy * MaxDy;

        video_textureview.setScaleX(scale_factor);
        video_textureview.setScaleY(scale_factor);

        video_textureview.setTranslationX(tx);
        video_textureview.setTranslationY(ty);
    }

}
