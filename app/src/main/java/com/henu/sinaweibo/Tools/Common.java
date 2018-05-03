package com.henu.sinaweibo.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;

import com.henu.sinaweibo.Helper.DataHelper;
import com.henu.sinaweibo.Models.UserInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by AKira on 2017/5/6.
 */

public class Common {
    public static User login_user = null;
    public static Oauth2AccessToken mAccessToken = null;
    public static int login_ID = -1;

    /*此方法会报错：
    android.os.NetworkOnMainThreadException
    */
    public static Bitmap getBm(URL aUrl){
        URLConnection conn;
        Bitmap bm = null;
        try{
            conn = aUrl.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        }catch (Exception e){
            Log.e(Constants.TAG,3+e.toString());

            e.printStackTrace();
        }
        return bm;
    }

    public static void getUserSync(Context mContext,String userId, Oauth2AccessToken accessToken){
        UsersAPI usersAPI = new UsersAPI(mContext, Constants.APP_KEY, accessToken);
        long uid;
        try{
            uid = Long.parseLong(userId);
        }catch (Exception e){
            uid = 0;
        }
        String s = usersAPI.showSync(uid);
        login_user = User.parse(s);
        mAccessToken = accessToken;
    }
    private static final String KEY_LOGIN_ID = "login_id";
    public static void saveLoginID(Context context, int id){
        SharedPreferences.Editor editor = context.getSharedPreferences(KEY_LOGIN_ID, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_LOGIN_ID, id);
        editor.commit();
    }

    public static void getLoginID(Context context){
        SharedPreferences pref = context.getSharedPreferences(KEY_LOGIN_ID, Context.MODE_PRIVATE);
        login_ID = pref.getInt(KEY_LOGIN_ID, 0);
    }

    public static Oauth2AccessToken getAccessToken(Context context){
        if(mAccessToken!=null){
            return mAccessToken;
        }
        DataHelper dataHelper = new DataHelper(context);
        List<UserInfo> userList = dataHelper.GetUserList(true);
        if(userList==null){
            return null;
        }
        UserInfo userInfo = userList.get(0);
        return userInfo.getOAuth2AccessToken();
    }
}
