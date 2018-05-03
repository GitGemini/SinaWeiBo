package com.henu.sinaweibo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.henu.sinaweibo.Helper.DataHelper;
import com.henu.sinaweibo.AppConfig.BaseActivity;
import com.henu.sinaweibo.Models.UserInfo;
import com.henu.sinaweibo.Tools.Common;
import com.henu.sinaweibo.Tools.Constants;
import com.sina.weibo.sdk.auth.*;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

import java.net.URL;

/**
 * Created by AKira on 2017/5/1.
 */

public class WBAuthActivity extends BaseActivity {
    DataHelper dbHelper;
    //保存当前授权成功的用户信息
    UserInfo userInfo;
    //保存当前用户的Access_Token
    String token;
    //保存当前用户的UserID
    String user_id;
    //保存当前的上下文
    Context mContext;

    AuthInfo mAuthInfo;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    Oauth2AccessToken mAccessToken;
    SsoHandler mSsoHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        userInfo = new UserInfo();
        dbHelper = new DataHelper(this);
        mAuthInfo = new AuthInfo(mContext, Constants.APP_KEY,
                Constants.REDIRECT_URL,Constants.SCOPE);
        mSsoHandler = new SsoHandler(WBAuthActivity.this, mAuthInfo);
        mSsoHandler.authorize(new AuthListener());
        //mSsoHandler.authorizeWeb(new AuthListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    class AuthListener implements WeiboAuthListener{

        @Override
        public void onComplete(Bundle bundle) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
            if(mAccessToken.isSessionValid()){
                //保存Token
                token = bundle.getString("access_token", "");
                String expires_in = bundle.getString("expires_in", "");
                user_id = bundle.getString("uid", "");
                userInfo.setToken(token);
                userInfo.setUserId(user_id);
                userInfo.setTokenSecert(Constants.APP_SECRET);
                if(dbHelper.HaveUserInfo(user_id)) {
                    //更新用户信息
                    dbHelper.UpdateUserInfo(userInfo);
                }else {
                    //添加用户信息
                    dbHelper.SaveUserInfo(userInfo);
                }
                //获得用户详细信息
                getUserDetail(mContext, user_id);
                Toast.makeText(WBAuthActivity.this,"授权可用:"+token,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WBAuthActivity.this, MainActivity.class);
                startActivity(intent);
                WBAuthActivity.this.finish();
            }else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = bundle.getString("code", "false");
                Toast.makeText(WBAuthActivity.this,"授权不可用:"+code,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(WBAuthActivity.this,"授权出错:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            WBAuthActivity.this.finish();
        }

        @Override
        public void onCancel() {
            Toast.makeText(WBAuthActivity.this,"授权取消",Toast.LENGTH_SHORT).show();
            WBAuthActivity.this.finish();
        }
    }

    public void getData(final String s){
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
            try {
                User user = User.parse(s);
                String NickName = user.screen_name;
                Bitmap bm = Common.getBm(new URL(user.profile_image_url));
                Common.login_user = user;
                int id = dbHelper.UpdateUserInfo(NickName, bm, user_id);
                Common.saveLoginID(WBAuthActivity.this, id);
                dbHelper.Close();
            }catch (Exception e){
                //Log.e("sinaweibo", "Exception:"+e.getMessage());
            }
            }
        };
        new Thread(mRunnable).start();
    }

    //调用API，获得用户信息
    public void getUserDetail(Context mContext, String user_id){
        UsersAPI usersAPI = new UsersAPI(mContext, Constants.APP_KEY, mAccessToken);
        //此处应把uid转换为long
        //public void show(long uid, RequestListener listener)
        //public void show(String screen_name, RequestListener listener)
        long uid;
        try{
            uid = Long.parseLong(user_id);
        }catch (Exception e){
            uid = 0;
        }
        usersAPI.show(uid, new RequestListener() {
            @Override
            public void onComplete(String s) {
                getData(s);
            }

            @Override
            public void onWeiboException(WeiboException e) {
            }
        });
    }
}

