package com.xiaodong.pickimglib;

import android.support.annotation.StyleRes;

import com.xiaodong.pickimglib.view.ClipType;

/**
 * Created by yxd on 2017/8/16.
 */

public class ImgPickSpec {
    @StyleRes
    public int themeId;
    public ClipType type;//1圆形 2方形 3自由调整方形
    public int width = 200;//目标宽
    public int height = 200;//目标高

    private static ImgPickSpec mImgPickSpec;
    private ImgPickSpec(){}

//    public static ImgPickSpec newInstance(){
//        if(mImgPickSpec == null){
//            synchronized(ImgPickSpec.class){
//                if(mImgPickSpec==null){
//                    mImgPickSpec = new ImgPickSpec();
//                }
//            }
//        }
//        return mImgPickSpec;
//    }
    //use this method create instance
    public static ImgPickSpec newInstance(){
        return InstanceHolder.INSTANCE;
    }

    public static ImgPickSpec getCleanInstance(){
        newInstance().reset();
        return newInstance();
    }

    private void reset(){
        themeId = 0;
        type = ClipType.RECTANGLE;
    }

    private static final class InstanceHolder{
        private static final ImgPickSpec INSTANCE = new ImgPickSpec();
    }

}
