package com.henu.sinaweibo.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.henu.sinaweibo.Helper.DataHelper;
import com.henu.sinaweibo.LoadActivity;
import com.henu.sinaweibo.AppConfig.ActivityCollector;
import com.henu.sinaweibo.AppConfig.BaseActivity;
import com.henu.sinaweibo.Models.UserInfo;
import com.henu.sinaweibo.R;
import com.henu.sinaweibo.Tools.Common;
import com.henu.sinaweibo.Tools.Constants;
import com.henu.sinaweibo.WBAuthActivity;
import com.sina.weibo.sdk.openapi.LogoutAPI;

import java.util.List;

/**
 * Created by AKira on 2017/6/7.
 */

public class SettingActivity extends BaseActivity {
    private ListView loginUserList;
    private LinearLayout logoutLayout,addAccout;
    private List<UserInfo> userList;
    private DataHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);

        InitView();
        InitEvent();
    }

    private void InitEvent() {
        loginUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //int ID = Integer.parseInt(userList.get(position).getId());
                //Common.saveLoginID(SettingActivity.this, ID);
                ActivityCollector.finishAll();
                Intent intent = new Intent(SettingActivity.this, LoadActivity.class);
                startActivity(intent);
            }
        });
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "退出登录!", Toast.LENGTH_SHORT).show();
                SettingActivity.this.finish();
//                LogoutAPI logoutAPI = new LogoutAPI(SettingActivity.this, Constants.APP_KEY, Common.mAccessToken);
//                boolean a = dbHelper.HaveUserInfo(Common.login_user.id);
//                int count = dbHelper.DelUserInfo(Common.login_user.id);
//                if(count<1){
//                    Toast.makeText(SettingActivity.this, String.valueOf(a)+count+"从数据库中删除记录失败！", Toast.LENGTH_SHORT).show();
//                }
                /*logoutAPI.logout(new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        Toast.makeText(SettingActivity.this, "成功退出!", Toast.LENGTH_SHORT).show();
                        int count = dbHelper.DelUserInfo(Common.login_user.id);
                        if(count<1){
                            Toast.makeText(SettingActivity.this, count+"从数据库中删除记录失败！", Toast.LENGTH_SHORT).show();
                        }
                        ActivityCollector.finishAll();
                        Intent intent = new Intent(SettingActivity.this, LoadActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        Toast.makeText(SettingActivity.this, "退出失败!", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });
        addAccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.finishAll();
                Intent intent = new Intent(SettingActivity.this, WBAuthActivity.class);
                startActivity(intent);
            }
        });
    }

    private void InitView() {
        loginUserList = (ListView)findViewById(R.id.loginUserList);
        logoutLayout = (LinearLayout) findViewById(R.id.logoutLayout);
        addAccout = (LinearLayout) findViewById(R.id.addAccout);

        dbHelper = new DataHelper(SettingActivity.this);
        userList = dbHelper.GetUserList(false);
        if(userList==null){
            return;
        }
        loginUserList.setAdapter(new LoginUserAdapter(SettingActivity.this, userList));
    }
}
