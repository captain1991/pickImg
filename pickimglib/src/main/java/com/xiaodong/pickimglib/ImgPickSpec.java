package com.xiaodong.pickimglib;

import android.support.annotation.StyleRes;

/**
 * Created by yxd on 2017/8/16.
 */

public class ImgPickSpec {
    @StyleRes
    public int themeId;
    public int type=2;//1圆形 2方形

    private static ImgPickSpec mImgPickSpec;
    private ImgPickSpec(){}

//    public static ImgPickSpec getInstance(){
//        if(mImgPickSpec == null){
//            synchronized(ImgPickSpec.class){
//                if(mImgPickSpec==null){
//                    mImgPickSpec = new ImgPickSpec();
//                }
//            }
//        }
//        return mImgPickSpec;
//    }
    //推荐使用静态内部类创建单例
    public static ImgPickSpec newInstance(){
        return InstanceHolder.INSTANCE;
    }

    public static ImgPickSpec getCleanInstance(){
        newInstance().reset();
        return newInstance();
    }

    private void reset(){
        themeId = 0;
        type = 2;
    }

    private static final class InstanceHolder{
        private static final ImgPickSpec INSTANCE = new ImgPickSpec();
    }

}
