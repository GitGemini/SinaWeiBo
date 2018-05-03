package com.henu.sinaweibo.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.henu.sinaweibo.Models.UserInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AKira on 2017/4/14.
 */

public class DataHelper {
    //数据库名称
    private static String DB_NAME = "sinaweibo.db";
    //数据库版本
    private static int DB_VERSION = 2;
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;

    public DataHelper(Context context){
        dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }
    public void Close(){
        db.close();
        dbHelper.close();
    }

    //获取users表中的UserID、Access Token、Access Secret的记录
    public List<UserInfo> GetUserList(Boolean isSimple)
    {
        List<UserInfo> userList = new ArrayList<UserInfo>();
        Cursor cursor = db.query(SqliteHelper.TB_NAME, null,null,null,null,null,UserInfo.ID+" DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()&&(cursor.getString(1)!=null)) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(cursor.getString(0));
            userInfo.setUserId(cursor.getString(1));
            userInfo.setToken(cursor.getString(2));
            userInfo.setTokenSecert(cursor.getString(3));
            userInfo.setExpires_in(cursor.getString(4));
            if (!isSimple) {
                if (cursor.getString(5)!=null && cursor.getBlob(6)!=null){
                    userInfo.setUserName(cursor.getString(5));
                    ByteArrayInputStream stream = new ByteArrayInputStream(cursor.getBlob(6));
                    Drawable icon = Drawable.createFromStream(stream, "image");
                    userInfo.setUserIcon(icon);
                }
            }
            userList.add(userInfo);
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    //判断users表中是否包含某个UserID的记录
    public Boolean HaveUserInfo(String UserId){
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper.TB_NAME,null,UserInfo.USERID+"="+UserId, null, null,null,null);
        b = cursor.moveToFirst();
        //Log.e("HaveUserInfo", b.toString());
        cursor.close();
        return b;
    }

    //判断users表中是否包含某个UserName的记录
    public UserInfo GetUserByName(String userName){
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper.TB_NAME,null,UserInfo.USERNAME+"= '"+userName+"'", null, null,null,null);
        b = cursor.moveToFirst();
        //Log.e("GetUserByName", b.toString());
        if(b!=false){
            UserInfo userInfo = new UserInfo();
            userInfo.setId(cursor.getString(0));
            userInfo.setUserId(cursor.getString(1));
            userInfo.setToken(cursor.getString(2));
            userInfo.setTokenSecert(cursor.getString(3));
            userInfo.setExpires_in(cursor.getString(4));
            userInfo.setUserName(cursor.getString(5));
            ByteArrayInputStream stream = new ByteArrayInputStream(cursor.getBlob(6));
            Drawable icon = Drawable.createFromStream(stream, "image");
            userInfo.setUserIcon(icon);
            cursor.close();
            return userInfo;
        }
        return null;
    }

    //更新users表的记录，根据UserId更新用户昵称和用户图标
    public int UpdateUserInfo(String userName, Bitmap userIcon, String userId){
        ContentValues values = new ContentValues();
        values.put(UserInfo.USERNAME, userName);
        //BLOB类型
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        //将Bitmap压缩成PNG编码，质量为100%存储
        userIcon.compress(Bitmap.CompressFormat.PNG, 100, os);
        values.put(UserInfo.USERICON, os.toByteArray());
        int id = db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID +"="+userId, null);
        //Log.e("UpdateUserInfo", id+"");
        return id;
    }

    //更新users表的记录
    public int UpdateUserInfo(UserInfo user){
        ContentValues values = new ContentValues();
        values.put(UserInfo.USERID, user.getUserId());
        values.put(UserInfo.TOKEN, user.getToken());
        values.put(UserInfo.TOKENSECRET, user.getTokenSecert());
        values.put(UserInfo.EXPIRES_IN, user.getExpires_in());
        int id = db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID +"="+user.getUserId(), null);
        //Log.e("UpdateUserInfo", id+"");
        return id;
    }

    //添加users表的记录
    public Long SaveUserInfo(UserInfo user){
        ContentValues values = new ContentValues();
        values.put(UserInfo.USERID, user.getUserId());
        values.put(UserInfo.TOKEN, user.getToken());
        values.put(UserInfo.TOKENSECRET, user.getTokenSecert());
        values.put(UserInfo.EXPIRES_IN, user.getExpires_in());
        long uid = db.insert(SqliteHelper.TB_NAME,UserInfo.ID,values);
        //Log.e("SaveUserInfo", uid+"");
        return uid;
    }

    //删除users表的记录
    public int DelUserInfo(String userId){
        int count = db.delete(SqliteHelper.TB_NAME, UserInfo.ID +"="+ userId, null);
        //Log.e("sinaweibo","DelUserInfo: "+count);
        return count;
    }
}
