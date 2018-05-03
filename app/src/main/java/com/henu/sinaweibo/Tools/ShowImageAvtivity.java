package com.henu.sinaweibo.Tools;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.boycy815.pinchimageview.PinchImageView;
import com.henu.sinaweibo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by AKira on 2017/6/10.
 */

public class ShowImageAvtivity extends Activity {

    public static final String URL_KEY = "url_key";


    private PinchImageView imageView;
    private String picUrl;
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_image_activity);
        InitView();
        loadImage();
    }

    private void loadImage() {
        if(!dialog.isShowing()){
            dialog.show();
        }
        DisplayImageOptions options =new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(picUrl, imageView, options);
        dialog.dismiss();
    }

    private void InitView() {
        imageView = (PinchImageView)findViewById(R.id.showbigImage);
        dialog = new ProgressDialog(this);
        InitDialog();
        Intent intent = getIntent();
        picUrl= intent.getStringExtra(URL_KEY);
    }

    private void InitDialog() {
        dialog.setTitle("提示");
        dialog.setMessage("正在加载图片,请稍候...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //dialog.setCancelable(false);
    }
}
