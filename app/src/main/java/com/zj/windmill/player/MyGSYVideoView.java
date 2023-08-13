package com.zj.windmill.player;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shuyu.gsyvideoplayer.video.base.GSYVideoView;

/**
 * 播放控件UI层
 *
 * @author zj
 * @date 2023/08/11
 */
public abstract class MyGSYVideoView extends GSYVideoView {

    public MyGSYVideoView(@NonNull Context context) {
        super(context);
    }

    public MyGSYVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGSYVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyGSYVideoView(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }
}
