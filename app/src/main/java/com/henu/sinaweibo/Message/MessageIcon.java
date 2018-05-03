package com.henu.sinaweibo.Message;

import android.support.v4.app.Fragment;

/**
 * Created by L on 2017/5/13.
 */

public class MessageIcon  {
    private String name;
    private int imageID;
    public MessageIcon(String s, int tubiao) {
        this.name=s;
        this.imageID = tubiao;
    }
    public String getName()
    {
        return name;
    }
    public int getImageID(){
        return imageID;
    }
}
