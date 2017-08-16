package com.xiaodong.pickimglib;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.zhihu.matisse.MimeType;

import java.lang.ref.WeakReference;
import java.util.Set;

/**
 * Created by yxd on 2017/8/16.
 */

public class ImagePickerClip {
    private WeakReference<Activity> mContext;
    private WeakReference<Fragment> mFragment;

    public ImagePickerClip(Activity mContext) {
        this.mContext = new WeakReference<Activity>(mContext);
        mFragment = null;
    }

    public ImagePickerClip(Activity mContext, Fragment mFragment) {
        this.mContext =  new WeakReference<Activity>(mContext);
        this.mFragment = new WeakReference<Fragment>(mFragment);
    }
    /**
     * Start ImagePickerClip from an Activity.
     * <p>
     * This Activity's {@link Activity#onActivityResult(int, int, Intent)} will be called when user
     * finishes selecting.
     *
     * @param activity Activity instance.
     * @return Matisse instance.
     */
    public static ImagePickerClip from(Activity activity){
        return new ImagePickerClip(activity);
    }

    /**
     * Start Fragment from a Fragment.
     * <p>
     * This Fragment's {@link Fragment#onActivityResult(int, int, Intent)} will be called when user
     * finishes selecting.
     *
     * @param fragment Fragment instance.
     * @return Matisse instance.
     */
    public static ImagePickerClip from(Fragment fragment){
        return new ImagePickerClip(fragment.getActivity(),fragment);
    }

   /**
     * MIME types the selection constrains on.
     * <p>
     * Types not included in the set will still be shown in the grid but can't be chosen.
     *
     * @param mimeType MIME types set user can choose from.
     * @return {@link ImgPickerClipBuilder} to build select specifications.
     * @see MimeType
     * @see ImgPickerClipBuilder
     */
    public ImgPickerClipBuilder choose(Set<MimeType> mimeType) {
        return new ImgPickerClipBuilder(this, mimeType);
    }

    public ImgPickerClipBuilder builder(Set<MimeType> mimeType) {
        return new ImgPickerClipBuilder(this, mimeType);
    }

    @Nullable
    Activity getActivity(){
        return mContext.get();
    }

    @Nullable
    Fragment getFragment(){
        if(mFragment!=null) {
            return mFragment.get();
        }
        return null;
    }
}
