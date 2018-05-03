package com.henu.sinaweibo.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.henu.sinaweibo.Models.UserInfo;

/**
 * Created by AKira on 2017/4/14.
 */

public class SqliteHelper extends SQLiteOpenHelper {
    // //用来保存 UserID、Access Token、Access Secret的表名
    public static final String TB_NAME = "users";
    private static final String CREATE_USER = "CREATE TABLE IF NOT EXISTS " + TB_NAME +"("+
            UserInfo.ID + " integar primary key,"+
            UserInfo.USERID+" varchar," +
            UserInfo.TOKEN+" varchar," +
            UserInfo.TOKENSECRET+" varchar," +
            UserInfo.EXPIRES_IN+" varchar," +
            UserInfo.USERNAME+" varchar," +
            UserInfo.USERICON+" blob" +
            ")";

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL(CREATE_USER);
        //Log.e("Database", "onCreate");
    }

    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
        //Log.e("Database", "onUpgrade");
    }

    //更新列
    public void updateColumn(SQLiteDatabase db, String oldColumn,
                             String newColumn,String typeColumn){
        try {
            db.execSQL("ALTER TABLE " + TB_NAME
            +" CHANGE " + oldColumn+ " " + newColumn +
            " " + typeColumn);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
