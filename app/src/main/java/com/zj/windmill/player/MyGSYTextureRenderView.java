package com.zj.windmill.player;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shuyu.gsyvideoplayer.video.base.GSYTextureRenderView;

/**
 * 渲染层
 *
 * @author zj
 * @date 2023/08/11
 */
public abstract class MyGSYTextureRenderView extends GSYTextureRenderView {
    public MyGSYTextureRenderView(@NonNull Context context) {
        super(context);
    }

    public MyGSYTextureRenderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGSYTextureRenderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
