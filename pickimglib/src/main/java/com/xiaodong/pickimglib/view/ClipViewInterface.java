package com.xiaodong.pickimglib.view;

import android.graphics.Rect;

public interface ClipViewInterface  {
    void setClipBorderWidth(int s);
    void setmHorizontalPadding(float mHorizontalPadding);
    void setClipType(ClipType type);
    Rect getClipRect();
}
