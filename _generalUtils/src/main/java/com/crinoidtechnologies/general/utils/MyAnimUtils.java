package com.crinoidtechnologies.general.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by ${Vivek} on 1/29/2016 for MissApp.Be careful
 */
public class MyAnimUtils {

    public static Animation getAnimation(Context context, int animId, int startDelay, int duration) {
        Animation animation = AnimationUtils.loadAnimation(context, animId);
        animation.setDuration(duration);
        animation.setStartOffset(startDelay);
        return animation;
    }

    public static void playAnimationOnView(Context context, View view, int animId, int startDelay, int duration) {
        view.startAnimation(getAnimation(context, animId, startDelay, duration));
    }
}
