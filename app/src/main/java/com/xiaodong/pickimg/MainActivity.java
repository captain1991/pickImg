package com.xiaodong.pickimg;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaodong.pickimglib.ImagePickerClip;
import com.xiaodong.pickimglib.view.ClipType;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.security.Permission;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private final int PICKIMAGE=0x01;
    private final int CLIPIMAGE=0x02;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            return;
        }
        Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(1)
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(1f)
                .imageEngine(new GlideEngine())
                .theme(R.style.Matisse_Zhihu)
                .forResult(PICKIMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if (requestCode==PICKIMAGE){//选取图片结果
                List<Uri> result = Matisse.obtainResult(data);
                Uri uri = result.get(0);
                toClicp(uri);
            }else if(requestCode==CLIPIMAGE){//图片裁剪结果
                final Uri uri = data.getData();
                Glide.with(this).load(uri).into(imageView);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //裁剪
    public void toClicp(Uri uri){
        ImagePickerClip.from(this)
                .choose(MimeType.allOf())
                .uri(uri)
                .type(ClipType.RECTANGLE)
                .overWriteWH(160,160)
                .themeId(R.style.Matisse_Zhihu)
                .forResult(CLIPIMAGE);
    }
}
