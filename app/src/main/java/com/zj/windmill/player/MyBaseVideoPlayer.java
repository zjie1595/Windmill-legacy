package com.zj.windmill.player;

import android.content.Context;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

/**
 * 播放控件处理大小屏等业务
 *
 * @author zj
 * @date 2023/08/11
 */
public abstract class MyBaseVideoPlayer extends GSYBaseVideoPlayer {

    public MyBaseVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyBaseVideoPlayer(Context context) {
        super(context);
    }

    public MyBaseVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBaseVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
