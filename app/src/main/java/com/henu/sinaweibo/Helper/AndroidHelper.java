package com.henu.sinaweibo.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.view.Display;
import android.view.View;

/**
 * Created by AKira on 2017/4/14.
 */

public class AndroidHelper {
    //获取屏幕方向
    public static int ScreenOrient(Activity acvtivity){
        int orient = acvtivity.getRequestedOrientation();
        if((orient == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) ||
                (orient == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)){
            return  orient;
        }
        //宽>高为横屏,反之为竖屏
        Display display = acvtivity.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        orient = (p.x < p.y)?ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        return  orient;
    }

    //自动切换屏幕背景
    public static void  AutoBackground(Activity activity, View view, int background_v, int background_h){
        int orient = ScreenOrient(activity);
        if(orient == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){//横向
            view.setBackgroundResource(background_h);
        }else {
            view.setBackgroundResource(background_v);
        }

    }
    public static Point getScreen(Activity acvtivity){
        Display display = acvtivity.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        return p;
    }
}
