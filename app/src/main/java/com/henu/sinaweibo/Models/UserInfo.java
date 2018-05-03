package com.henu.sinaweibo.Models;

import android.graphics.drawable.Drawable;
import android.util.Log;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by AKira on 2017/4/14.
 */

public class UserInfo {
    public final static String ID ="ID";
    public final static String USERID ="USERID";
    public final static String USERNAME ="USERNAME";
    public final static String TOKEN ="TOKEN";
    public final static String EXPIRES_IN ="EXPIRES_IN";
    public final static String TOKENSECRET ="TOKENSECRET";
    public final static String USERICON ="USERICON";

    //用户在数据库中的id，递增
    private String id;
    //用户微博id
    private String userId;

    //用户微博昵称
    private String userName;
    //用户的Access_Token,为经过授权后的Token,用于读取用户数据等操作
    private String token;
    //App的App_Secert
    private String tokenSecert;

    private String expires_in;
    //用户图标
    private Drawable userIcon;

    //取得id
    public String getId() {
        return userId;
    }
    //设置id
    public void setId(String id) {
        this.id = id;
    }
    //取得用户微博id
    public String getUserId() {
        return userId;
    }
    //设置用户微博id
    public void setUserId(String userId) {
        this.userId = userId;
    }
    //取得用户微博昵称
    public String getUserName() {
        return userName;
    }
    //设置用户微博昵称
    public void setUserName(String userName) {
        this.userName = userName;
    }
    //取得Access_Token
    public String getToken() {  return token; }
    //设置Access_Token
    public void setToken(String token) { this.token = token;  }
    //取得App_Secert
    public String getTokenSecert() {
        return tokenSecert;
    }
    //设置App_Secert
    public void setTokenSecert(String tokenSecert) {
        this.tokenSecert = tokenSecert;
    }
    //取得用户图标
    public Drawable getUserIcon() {
        return userIcon;
    }
    //设置用户图标
    public void setUserIcon(Drawable userIcon) {
        this.userIcon = userIcon;
    }
    //取得用户期限
    public String getExpires_in() {
        return expires_in;
    }
    //设置用户期限
    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }



    public Oauth2AccessToken getOAuth2AccessToken(){
        Oauth2AccessToken mAccessToken = new Oauth2AccessToken(token, expires_in);
        return  mAccessToken;
    }
}
