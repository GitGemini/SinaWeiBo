package com.henu.sinaweibo.Setting;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.henu.sinaweibo.Models.UserInfo;
import com.henu.sinaweibo.R;
import com.henu.sinaweibo.Tools.AsyncImageLoader;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by AKira on 2017/6/7.
 */


public class LoginUserHolder {
    private UserInfo userInfo;
    private ImageView userIcon;
    private TextView userName;

    private Context mContext;
    private View mView;

    public LoginUserHolder(Context context, View convertView, UserInfo userInfo){
        this.mContext = context;
        this.userInfo = userInfo;
        if(convertView==null){
            mView = LayoutInflater.from(context).inflate(R.layout.login_user_item, null);
        }else {
            mView = convertView;
        }

        InitView();
    }

    public View getView(){
        userName.setText(userInfo.getUserName());
        userIcon.setImageDrawable(userInfo.getUserIcon());
        return mView;
    }

    private void InitView() {
        userName = (TextView)mView.findViewById(R.id.LoginUserName);
        userIcon = (ImageView) mView.findViewById(R.id.LoginUserIcon);
    }
}
