package com.henu.sinaweibo.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.henu.sinaweibo.AppConfig.BaseActivity;
import com.henu.sinaweibo.Models.CustomProgressDialog;
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

public class ShowUserDetails extends BaseActivity implements View.OnClickListener {
    TextView yourName, yourDesc, yourVipText;
    ImageView yourIcon, yourVipIcon,backBtn;
    TextView yourStatusCount, yourFocusCount,yourFansCount;
    LinearLayout yourStatus, yourFocus,yourFans;

    private User user;
    private Dialog dialog;
    private Context context;
    private UsersAPI usersAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_detail);
        context = this;
        usersAPI = new UsersAPI(context, Constants.APP_KEY, Common.getAccessToken(context));

        getUser();
        InitView();
        InitEvent();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backBtn:
            {
                ShowUserDetails.this.finish();
            }
            break;
            case R.id.yourFocus:
            {
                Intent intent = new Intent(context, ShowUsersActivity.class);
                intent.putExtra(Constants.USERID, user.id);
                intent.putExtra(ShowUsersActivity.SHOW_TYPE, ShowUsersActivity.SHOW_FRIENDS);
                context.startActivity(intent);
            }
            break;
            case R.id.yourStatus:
            {
                Intent intent = new Intent(context, ShowStatusActivity.class);
                intent.putExtra(Constants.USERID, user.id);
                context.startActivity(intent);
            }
            break;
            case R.id.yourFans:
            {
                Intent intent = new Intent(context, ShowUsersActivity.class);
                intent.putExtra(Constants.USERID, user.id);
                intent.putExtra(ShowUsersActivity.SHOW_TYPE, ShowUsersActivity.SHOW_FANS);
                context.startActivity(intent);
            }
            break;
        }
    }

    public void updateView(){
        yourName.setText(user.screen_name);
        yourDesc.setText("简介："+user.description);
        yourStatusCount.setText(""+user.statuses_count);
        yourFocusCount.setText(""+user.friends_count);
        yourFansCount.setText(""+user.followers_count);
        AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
        asyncImageLoader.loadDrawable(user.profile_image_url, yourIcon, new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
                yourIcon.setImageDrawable(imageDrawable);
            }
        });
    }

    private void InitEvent() {
        backBtn.setOnClickListener(this);

        yourName.setOnClickListener(this);
        yourDesc.setOnClickListener(this);
        yourVipText.setOnClickListener(this);

        yourFocus.setOnClickListener(this);
        yourStatus.setOnClickListener(this);
        yourFans.setOnClickListener(this);
    }

    private void InitView() {
        backBtn = (ImageView) findViewById(R.id.backBtn);
        yourName = (TextView) findViewById(R.id.yourName);
        yourDesc = (TextView) findViewById(R.id.yourDesc);
        yourVipText = (TextView) findViewById(R.id.yourVipText);
        yourStatusCount = (TextView) findViewById(R.id.yourStatusCount);
        yourFansCount = (TextView) findViewById(R.id.yourFansCount);
        yourFocusCount = (TextView) findViewById(R.id.yourFocusCount);

        yourVipIcon = (ImageView) findViewById(R.id.yourVipIcon);
        yourIcon = (RoundImageView) findViewById(R.id.yourIconView);

        yourFocus = (LinearLayout) findViewById(R.id.yourFocus);
        yourFans = (LinearLayout) findViewById(R.id.yourFans);
        yourStatus = (LinearLayout) findViewById(R.id.yourStatus);
    }

    private void getUser() {
        dialog = CustomProgressDialog.createLoadingDialog(context, "正在获取用户信息...");
        dialog.show();
        Intent intent = getIntent();
        String idStr = intent.getStringExtra(Constants.USERID);
        long id = Long.parseLong(idStr);
        usersAPI.show(id, new RequestListener() {
            @Override
            public void onComplete(String s) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                user = User.parse(s);
                updateView();
                //Toast.makeText(mContext, "成功更新用户信息！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(context, "获取用户信息失败！"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
