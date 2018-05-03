package com.henu.sinaweibo.Tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.henu.sinaweibo.Helper.AndroidHelper;
import com.henu.sinaweibo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by AKira on 2017/6/10.
 */

public class ImageUtils {
    private static final String thumbnail_pic  = "thumbnail";
    private static final String bmiddle_pic  = "bmiddle";
    private static final String original_pic  = "large";

    public static void SetNineImages(Context context,GridLayout layout, List<String> urls){
        //设置网格布局
        int row, col;
        if (urls.size() <= 3) {
            row = 1;
            col = urls.size();
        } else if(urls.size()==4){
            row = (urls.size() / 3) + 1;
            col = 3;
        }else{
            row = (urls.size() / 3) + 1;
            col = 3;
        }
        layout.setRowCount(row);
        layout.setColumnCount(col);
        //从list集合中取出宫格图地址，下载添加到动态生成的imageView中
        for (String url : urls) {
            //创建ImageView控件
            ImageView imageViews = new ImageView(context);
            String picUrl = url.replace(thumbnail_pic, bmiddle_pic);

            Point p = AndroidHelper.getScreen((Activity) context);
            p.x = p.x - 60;
            if (urls.size() == 1) {
                imageViews.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageViews.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(p.x / 3, p.x / 3);
                GridLayout.LayoutParams gl = new GridLayout.LayoutParams(ll);
                gl.rightMargin = 10;
                gl.topMargin = 5;
                gl.bottomMargin = 5;
                imageViews.setLayoutParams(gl);
                imageViews.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            //将ImageView控件添加到网格布局中
            loadDrawable(picUrl, imageViews);
            String originalUrl = picUrl.replace(bmiddle_pic, original_pic);
            ImageListener listener = new ImageListener(context, originalUrl);
            imageViews.setOnClickListener(listener);
            layout.addView(imageViews);
        }
    }

    //下载图片并添加到ImageView控件中
    /*
    * 使用自定义AsyncImageLoader加载图片
    * 快速滑动时会有OOM
    * 弃用
    */
    public static void loadDrawableA(ImageView imageViews, String picUrl) {
        AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
        Drawable cachedImage = asyncImageLoader.loadDrawable(picUrl, imageViews, new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
                imageView.setImageDrawable(imageDrawable);
            }
        });
        if (cachedImage == null) {
            imageViews.setImageResource(R.drawable.default_icon);
        } else {
            imageViews.setImageDrawable(cachedImage);
        }
    }

    //下载图片并添加到ImageView控件中
    /*
    * 使用开源框架Universal-Image-Loader加载图片
    * 加载多图使用
    */
    public static void loadDrawable(String picUrl, ImageView imageViews) {
        DisplayImageOptions options =new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(picUrl, imageViews, options);
    }
}
