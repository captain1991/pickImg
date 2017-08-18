# pickImg
图片选取器
gradle
compile 'com.yxd.util:pickClipImg:1.0.0'

并且同时在项目中引入glide库
 compile 'com.github.bumptech.glide:glide:3.7.0'
 
跳转到选择图片界面代码如下
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
                .forResult(PICKIMAGE)
                
并且在onActivityResult中做相应的处理
  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if (requestCode==PICKIMAGE){//选取图片结果与上文中PICKIMAGE相同
                List<Uri> result = Matisse.obtainResult(data);
             ｝
         ｝
         
如果选择一张图片并且需要进行裁剪，可做如下操作
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
    
     //裁剪
    public void toClicp(Uri uri){
        ImagePickerClip.from(this)
                .choose(MimeType.allOf())
                .uri(uri)
                .type(1)
                .themeId(R.style.Matisse_Zhihu)
                .forResult(CLIPIMAGE);
    }
    
只需要这么简单的几步就可以完成图片的选取并进行裁剪

 其中的图片选择器使用的是知乎的库。
