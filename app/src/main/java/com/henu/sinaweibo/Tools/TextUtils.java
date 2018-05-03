package com.henu.sinaweibo.Tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.henu.sinaweibo.Emotions.EmotionUtils;
import com.henu.sinaweibo.R;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AKira on 2017/6/4.
 */

public class TextUtils {
    // 定义正则表达式
    private static final String AT = "@[\u4e00-\u9fa5\\w]+";// @人
    private static final String TOPIC = "#[\u4e00-\u9fa5\\w]+#";// ##话题
    private static final String EMOJI = "\\[[\u4e00-\u9fa5\\w]+\\]";// 表情
    private static final String URL = "http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";// url
    private static final String REGEX = "("+AT+")"+"|"+"("+TOPIC+")"+"|"+"("+EMOJI+")"+"|"+"("+URL+")";

    /**
     2      * 设置微博内容样式
     3      * @param context
     4      * @param source
     5      * @param textView
     6      * @return
     7      */
     public static SpannableString getWeiBoContent(final Context context, String source, TextView textView) {
         String str = "...全文";
         int index = source.lastIndexOf(str);

         SpannableString spannableString = new SpannableString(source);
         if(index!=-1){
             ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
             spannableString.setSpan(span, index, index+str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
         }

         //设置正则
         Pattern pattern = Pattern.compile(REGEX);
         Matcher matcher = pattern.matcher(spannableString);

         if (matcher.find()) {
             // 要实现文字的点击效果，这里需要做特殊处理
             textView.setMovementMethod(LinkMovementMethod.getInstance());
             // 重置正则位置
             matcher.reset();
         }

         while (matcher.find()) {
             // 根据group的括号索引，可得出具体匹配哪个正则(0代表全部，1代表第一个括号)
             final String at = matcher.group(1);
             final String topic = matcher.group(2);
             String emoji = matcher.group(3);
             final String url = matcher.group(4);

             // 处理@符号
             if (at != null) {
                 //获取匹配位置
                 int start = matcher.start(1);
                 int end = start + at.length();
                 MyClickableSpan clickableSpan = new MyClickableSpan() {

                     @Override
                     public void onClick(View widget) {
                         //这里需要做跳转用户的实现，先用一个Toast代替
                         Toast.makeText(context, "点击了用户：" + at, Toast.LENGTH_SHORT).show();
                     }
                 };
                 spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
             }

             // 处理话题##符号
             if (topic != null) {
                 int start = matcher.start(2);
                 int end = start + topic.length();
                 MyClickableSpan clickableSpan = new MyClickableSpan() {

                     @Override
                     public void onClick(View widget) {
                        Toast.makeText(context, "点击了话题：" + topic, Toast.LENGTH_SHORT).show();
                     }
                 };
                 spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
             }

             if (emoji != null) {
                 int start = matcher.start(3);
                 int end = start + emoji.length();
                 int ResId = EmotionUtils.getImgByName(emoji);
                 Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), ResId);
                 if (bitmap != null) {
                     // 获取字符的大小
                     int size = (int) textView.getTextSize();
                     // 压缩Bitmap
                     bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                     // 设置表情
                     ImageSpan imageSpan = new ImageSpan(context, bitmap);
                     spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                 }
             }

             // 处理url地址
             if (url != null) {
                 final int start = matcher.start(4);
                 int end = start + url.length();
                 MyClickableSpan clickableSpan = new MyClickableSpan() {
                     @Override
                     public void onClick(View widget) {
//                    Toast.makeText(context, "点击了网址：" + url, Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(Intent.ACTION_VIEW);
                         intent.setData(Uri.parse(url));
                         context.startActivity(intent);
                     }
                 };
                 spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }

    //取得传入时间与当前时间的时间差
    public static String getTimeDiff(Date date){
        Calendar cal = Calendar.getInstance();
        long diff = 0;
        Date dnow = cal.getTime();
        String str = "";
        diff = dnow.getTime() - date.getTime();
        if(diff>86400000){
            str = (int)Math.floor(diff/86400000f)+"天前";
        }else if (diff > 3600000){
            str = (int)Math.floor(diff/3600000f)+"小时前";
        }else if (diff > 60000) {
            str = (int) Math.floor(diff / 60000f) + "分钟前";
        }else {
            str = (int) Math.floor(diff / 1000) + "秒前";
        }
        return str;
    }
}
