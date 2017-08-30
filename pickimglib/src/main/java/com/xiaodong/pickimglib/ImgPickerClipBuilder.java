package com.xiaodong.pickimglib;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.xiaodong.pickimglib.activity.ClipImageActivity;
import com.zhihu.matisse.MimeType;

import java.util.Set;

/**
 * Created by yxd on 2017/8/16.
 */

public final class ImgPickerClipBuilder {
    private final ImagePickerClip imagePickerClip;
    private final Set<MimeType> mMimeType;
    private int mMaxSelectable;
    private boolean mCountable;
    private ImgPickSpec mImgPickSpec;
    @StyleRes
    private int themeId;
    private int type=2;//1圆形 2方形
    private Uri uri;
    public ImgPickerClipBuilder(ImagePickerClip imagePickerClip, Set<MimeType> mMimeType) {
        this.imagePickerClip = imagePickerClip;
        this.mMimeType = mMimeType;
        this.mImgPickSpec=ImgPickSpec.getCleanInstance();
    }

    /**
     * Show a auto-increased number or a check mark when user select a photo.
     *
     * @param countable true for a auto-increased number from 1, false for a check mark. Default
     *                  value is false.
     * @return {@link ImgPickerClipBuilder} for fluent API.
     */
    public ImgPickerClipBuilder countable(boolean countable) {
        mCountable = countable;
        return this;
    }

    public ImgPickerClipBuilder maxSelectable(int maxSelectable ){
        mMaxSelectable = maxSelectable;
        return this;
    }

    public ImgPickerClipBuilder themeId(int themeId){
        this.themeId = themeId;
        return this;
    }

    public ImgPickerClipBuilder type(int type){
        this.type = type;
        return this;
    }

    public ImgPickerClipBuilder uri(Uri uri){
        this.uri = uri;
        return this;
    }

    public void forResult(int requestCode){
        Activity activity = imagePickerClip.getActivity();
        if(activity==null){
            return;
        }
        if (themeId == 0) {
            themeId = com.zhihu.matisse.R.style.Matisse_Zhihu;
        }
        mImgPickSpec.type=type;
        mImgPickSpec.themeId=themeId;
        Intent intent = new Intent(activity,ClipImageActivity.class);
        intent.setData(uri);
        Fragment fragment = imagePickerClip.getFragment();
        if(fragment!=null){
            fragment.startActivityForResult(intent,requestCode);
        }else {
            activity.startActivityForResult(intent,requestCode);
        }
//
    }

}
