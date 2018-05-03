package com.henu.sinaweibo.Fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.henu.sinaweibo.Models.RoundImageView;
import com.henu.sinaweibo.R;
import com.henu.sinaweibo.Setting.SettingActivity;
import com.henu.sinaweibo.Tools.AsyncImageLoader;
import com.henu.sinaweibo.Tools.Common;
import com.henu.sinaweibo.Tools.Constants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

public class MeFragment extends Fragment implements View.OnClickListener {
    TextView txtAddFri,txtSetting;
    TextView myName,myDesc,myVipText;
    ImageView myIcon,myVipIcon;
    TextView myStatusCount,myFocusCount,myFansCount;
    LinearLayout myStatus,myFocus,myFans;

    View mView;
    private Context mContext;
    private UsersAPI usersAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        mView = view;
        mContext = getActivity();
        usersAPI = new UsersAPI(mContext, Constants.APP_KEY, Common.getAccessToken(mContext));

        InitView();
        InitEvent();
        updateView();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            updateView();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtAddFri:
                break;
            case R.id.txtSetting:
                {
                    Intent intent = new Intent(mContext, SettingActivity.class);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.myFocus:
                {
                    Intent intent = new Intent(mContext, ShowUsersActivity.class);
                    intent.putExtra(Constants.USERID, Common.login_user.id);
                    intent.putExtra(ShowUsersActivity.SHOW_TYPE, ShowUsersActivity.SHOW_FRIENDS);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.myStatus:
                {
                    Intent intent = new Intent(mContext, ShowStatusActivity.class);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.myFans:
                {
                    Intent intent = new Intent(mContext, ShowUsersActivity.class);
                    intent.putExtra(Constants.USERID, Common.login_user.id);
                    intent.putExtra(ShowUsersActivity.SHOW_TYPE, ShowUsersActivity.SHOW_FANS);
                    mContext.startActivity(intent);
                }
                break;
        }

    }

    public void updateView(){
        updateUser();
        User user = Common.login_user;
        myName.setText(user.screen_name);
        if(user.description==null||user.description.length()==0){
            myDesc.setText("简介：暂无简介");
        }
        myDesc.setText("简介："+user.description);
        myStatusCount.setText(""+user.statuses_count);
        myFocusCount.setText(""+user.friends_count);
        myFansCount.setText(""+user.followers_count);
        AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
        asyncImageLoader.loadDrawable(user.profile_image_url, myIcon, new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
                myIcon.setImageDrawable(imageDrawable);
            }
        });
    }

    public void updateUser(){
        User user = Common.login_user;
        if(user==null){
            return;
        }
        long id = Long.parseLong(user.id);
        usersAPI.show(id, new RequestListener() {
            @Override
            public void onComplete(String s) {
                Common.login_user = User.parse(s);
                //Toast.makeText(mContext, "成功更新用户信息！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(mContext, "用户信息更新失败！"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitEvent() {
        txtAddFri.setOnClickListener(this);
        txtSetting.setOnClickListener(this);
        myName.setOnClickListener(this);
        myDesc.setOnClickListener(this);
        myVipText.setOnClickListener(this);
        myStatusCount.setOnClickListener(this);
        myFansCount.setOnClickListener(this);
        myFocusCount.setOnClickListener(this);
        myFocus.setOnClickListener(this);
        myStatus.setOnClickListener(this);
        myFans.setOnClickListener(this);
    }

    private void InitView() {
        txtAddFri = (TextView)mView.findViewById(R.id.txtAddFri);
        txtSetting = (TextView)mView.findViewById(R.id.txtSetting);
        myName = (TextView)mView.findViewById(R.id.myName);
        myDesc = (TextView)mView.findViewById(R.id.myDesc);
        myVipText = (TextView)mView.findViewById(R.id.myVipText);
        myStatusCount = (TextView)mView.findViewById(R.id.myStatusCount);
        myFansCount = (TextView)mView.findViewById(R.id.myFansCount);
        myFocusCount = (TextView)mView.findViewById(R.id.myFocusCount);

        myVipIcon = (ImageView)mView.findViewById(R.id.myVipIcon);
        myIcon = (RoundImageView)mView.findViewById(R.id.myIconView);

        myFocus = (LinearLayout)mView.findViewById(R.id.myFocus);
        myFans = (LinearLayout)mView.findViewById(R.id.myFans);
        myStatus = (LinearLayout)mView.findViewById(R.id.myStatus);
    }
}
