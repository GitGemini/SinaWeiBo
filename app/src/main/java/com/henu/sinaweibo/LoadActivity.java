package com.henu.sinaweibo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.henu.sinaweibo.Helper.AndroidHelper;
import com.henu.sinaweibo.Helper.DataHelper;
import com.henu.sinaweibo.AppConfig.BaseActivity;
import com.henu.sinaweibo.Models.UserInfo;
import com.henu.sinaweibo.Tools.Common;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LoadActivity extends BaseActivity {
    DataHelper dbHelper;
    List<UserInfo> userList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_load);

        LinearLayout layout = (LinearLayout) findViewById(R.id.main_layout);
        AndroidHelper.AutoBackground(this, layout, R.drawable.main_bg_v, R.drawable.main_bg_h);

        loadUsers();
    }

    private void loadUsers() {
        //获取账号列表
        dbHelper = new DataHelper(LoadActivity.this);
        //获取数据库用户列表
        userList = dbHelper.GetUserList(true);

        //关闭数据库
        dbHelper.Close();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Looper.prepare();
                if(userList.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoadActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("您还未创建任何账户，是否现在创建？");
                    builder.setCancelable(false);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //跳转到授权页面
                            Intent intent = new Intent(LoadActivity.this, WBAuthActivity.class);
                            startActivity(intent);
                            LoadActivity.this.finish();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //取消则关闭本程序
                            LoadActivity.this.finish();
                        }
                    });
                    builder.create().show();
                }
                else{
//                    if(Common.login_ID==-1){
//                        Common.getLoginID(LoadActivity.this);
//                    }
//                    Log.e(Constants.TAG, ""+Common.login_ID);
//                    UserInfo userInfo = userList.get(Common.login_ID-1);
                    UserInfo userInfo = userList.get(0);
                    if(Common.login_user==null){
                        Common.getUserSync(LoadActivity.this, userInfo.getUserId(),userInfo.getOAuth2AccessToken());
                    }
                    Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoadActivity.this.finish();
                }
                Looper.loop();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000);
    }
}
