package com.xiaodong.pickimglib.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaodong.pickimglib.ImgPickSpec;
import com.xiaodong.pickimglib.R;
import com.xiaodong.pickimglib.view.ClipType;
import com.xiaodong.pickimglib.view.ClipViewLayout;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


/**
 * ClipImageActivity
 */
public class ClipImageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ClipImageActivity";
    private ClipViewLayout clipViewLayout1;
    private ClipViewLayout clipViewLayout2;
    private ClipViewLayout clipViewLayout3;
    private ImageView back;
    private TextView btnCancel;
    private TextView btnOk;
    // 1:qq,2:weixin
    private ClipType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImgPickSpec imgPickSpec = ImgPickSpec.newInstance();
        setTheme(imgPickSpec.themeId);
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_clip_image);
        initView();
    }

    /**
     */
    public void initView() {
        type = ImgPickSpec.newInstance().type;
        Log.i(TAG,"initView_type:"+type);
        clipViewLayout1 = (ClipViewLayout) findViewById(R.id.clipViewLayout1);
        clipViewLayout2 = (ClipViewLayout) findViewById(R.id.clipViewLayout2);
        clipViewLayout3 = (ClipViewLayout) findViewById(R.id.clipViewLayout3);
//        back = (ImageView) findViewById(R.id.iv_back);
        btnCancel = (TextView) findViewById(R.id.btn_cancel);
        btnOk = (TextView) findViewById(R.id.bt_ok);
//        back.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "image uri: "+getIntent().getData());
        Log.i(TAG,"type:"+type);
        if (type == ClipType.CIRCLE) {
            clipViewLayout1.setVisibility(View.VISIBLE);
            clipViewLayout2.setVisibility(View.GONE);
            clipViewLayout3.setVisibility(View.GONE);
            clipViewLayout1.setImageSrc(getIntent().getData());
        } else if(type == ClipType.RECTANGLE){
            clipViewLayout2.setVisibility(View.VISIBLE);
            clipViewLayout1.setVisibility(View.GONE);
            clipViewLayout3.setVisibility(View.GONE);
            clipViewLayout2.setImageSrc(getIntent().getData());
        }else if(type == ClipType.FREEDOM){
            clipViewLayout2.setVisibility(View.GONE);
            clipViewLayout1.setVisibility(View.GONE);
            clipViewLayout3.setVisibility(View.VISIBLE);
            clipViewLayout3.setImageSrc(getIntent().getData());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bt_ok){
            generateUriAndReturn();
        }else if(v.getId()==R.id.btn_cancel){
            finish();
        }
    }


    /**
     */
    private void generateUriAndReturn() {
        Bitmap zoomedCropBitmap;
        if (type == ClipType.CIRCLE) {
            zoomedCropBitmap = clipViewLayout1.clip();
        } else if(type == ClipType.RECTANGLE){
            zoomedCropBitmap = clipViewLayout2.clip();
        }else{
            zoomedCropBitmap = clipViewLayout3.clip();
        }
        if (zoomedCropBitmap == null) {
            Log.e("android", "zoomedCropBitmap == null");
            return;
        }
        Uri mSaveUri = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".jpg"));
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException ex) {
                Log.e("android", "Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Intent intent = new Intent();
            intent.setData(mSaveUri);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
