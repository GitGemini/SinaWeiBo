package com.henu.sinaweibo.Tools;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by AKira on 2017/5/25.
 */
public class AsyncImageLoader{
    //SoftReference是软应用，是为了更好使系统回收变量
    private HashMap<String, SoftReference<Drawable>> imageCache;
    public AsyncImageLoader(){
        imageCache = new HashMap<String, SoftReference<Drawable>>();
    }

    public Drawable loadDrawable(final String imageUrl,final ImageView imageView,
                                 final ImageCallback imageCallcack){
        if (imageCache.containsKey(imageUrl)){
            //从缓存中获取
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            Drawable drawable = softReference.get();
            if(drawable !=null){
                return drawable;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message){
                //调用接口显示图片
                imageCallcack.imageLoaded((Drawable) message.obj,imageView,imageUrl);
            }
        };
        //建立一个新的线程下载图片
        new Thread(){
            @Override
            public void run() {
                Drawable drawable = loadImageFromUrl(imageUrl);
                //将图片放入缓存
                imageCache.put(imageUrl,new SoftReference<Drawable>(drawable));
                Message message = handler.obtainMessage(0,drawable);
                //发送消息更新界面
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }

    //从网上下载图片
    public static Drawable loadImageFromUrl(String url){
        URL m;
        InputStream i = null;
        try {
            m = new URL(url);
            //取得图片的数据转换成InputStream
            i = (InputStream) m.getContent();
        }catch (Exception e){
            e.printStackTrace();
        }
        //生成一个Drawable对象
        Drawable d = Drawable.createFromStream(i,"src");
        return d;
    }

    //回调函数
    public interface ImageCallback{
        public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl);

    }
}
