package com.zj.windmill.player;

import android.content.Context;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

/**
 * Manager交互层
 *
 * @author zj
 * @date 2023/08/11
 */
public abstract class MyGSYVideoPlayer extends GSYVideoPlayer {

    public MyGSYVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyGSYVideoPlayer(Context context) {
        super(context);
    }

    public MyGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGSYVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
