package com.henu.sinaweibo.Tools;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by AKira on 2017/6/8.
 */

public class MyClickableSpan extends ClickableSpan {

    /*private String content;

    public MyClickableSpan(String content){
        this.content = content;
    }*/

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(Color.BLUE);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {

    }
}
