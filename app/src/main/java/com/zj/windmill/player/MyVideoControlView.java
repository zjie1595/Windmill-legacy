package com.zj.windmill.player;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shuyu.gsyvideoplayer.video.base.GSYVideoControlView;

/**
 * 播放器控件UI控制层
 *
 * @author zj
 * @date 2023/08/11
 */
public abstract class MyVideoControlView extends GSYVideoControlView {

    public MyVideoControlView(@NonNull Context context) {
        super(context);
    }

    public MyVideoControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyVideoControlView(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }
}
