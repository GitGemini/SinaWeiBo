package com.henu.sinaweibo.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.henu.sinaweibo.AppConfig.BaseActivity;
import com.henu.sinaweibo.Commnet.CommentAdapter;
import com.henu.sinaweibo.Models.UserList;
import com.henu.sinaweibo.Tools.Common;
import com.henu.sinaweibo.Tools.Constants;
import com.henu.sinaweibo.Models.CustomProgressDialog;
import com.henu.sinaweibo.R;
import com.henu.sinaweibo.Weibo.UserAdapter;
import com.henu.sinaweibo.Weibo.WeiboAdapter;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.List;

/**
 * Created by AKira on 2017/6/3.
 */

public class ShowStatusActivity extends BaseActivity {
    private TextView titleText;
    private ListView showList;
    private Dialog dialog;
    private StatusesAPI statusesAPI;
    private Context mContext;

    private String userId;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_users);

        mContext = this;

        titleText = (TextView) findViewById(R.id.titleText);
        showList = (ListView) findViewById(R.id.userList);
        dialog = CustomProgressDialog.createLoadingDialog(mContext,
                "正在拉取信息，请稍候...");

        Intent intent = getIntent();
        userId = intent.getStringExtra(Constants.USERID);
        if(userId==null) getMyData();
        else getData();
    }

    private void getMyData() {
        dialog.show();
        if(statusesAPI==null){
            statusesAPI = new StatusesAPI(mContext, Constants.APP_KEY,
                    Common.getAccessToken(mContext));
        }
        statusesAPI.userTimeline(0, 0, 50, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String s) {
                        ShowStatus(s);
                    }

            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        dialog.show();
        if(statusesAPI==null){
            statusesAPI = new StatusesAPI(mContext, Constants.APP_KEY,
                    Common.getAccessToken(mContext));
        }
        long id = Long.parseLong(userId);
        statusesAPI.userTimeline(id, 0, 0, 50, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String s) {
                ShowStatus(s);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowStatus(String s) {
        updateWidegt();
        StatusList statusList = StatusList.parse(s);
        List<Status> statuses = statusList.statusList;
        if(statuses==null){
            titleText.setText("没有微博");
            Toast.makeText(mContext, "没有微博,快去发几条吧，多发微博涨粉丝。", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        titleText.setText("我的微博");
        adapter = new WeiboAdapter(mContext, statuses);
        showList.setAdapter(adapter);
    }

    private void updateWidegt(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
