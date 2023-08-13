package com.zj.windmill.player;

import android.content.Context;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class MyStandardGSYVideoPlayer extends StandardGSYVideoPlayer {

    public MyStandardGSYVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyStandardGSYVideoPlayer(Context context) {
        super(context);
    }

    public MyStandardGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
