package com.henu.sinaweibo.Weibo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.henu.sinaweibo.R;
import com.henu.sinaweibo.Tools.ImageUtils;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by AKira on 2017/6/7.
 */

public class UserHolder {
    private User user;
    private ImageView userIcon;
    private TextView userName,userDesc;

    private Context mContext;
    private View mView;

    public UserHolder(Context context, View convertView, User user){
        this.mContext = context;
        this.user = user;
        if(convertView==null){
            mView = LayoutInflater.from(context).inflate(R.layout.user_item, null);
        }else {
            mView = convertView;
        }

        InitView();
    }

    public View getView(){
        userName.setText(user.name);
        userDesc.setText(user.description);
        ImageUtils.loadDrawable(user.profile_image_url, userIcon);
        return mView;
    }

    private void InitView() {
        userName = (TextView)mView.findViewById(R.id.userName);
        userDesc = (TextView)mView.findViewById(R.id.userDesc);
        userIcon = (ImageView) mView.findViewById(R.id.userIcon);
    }
}
