package com.henu.sinaweibo.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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

public class ShowUsersActivity extends BaseActivity {
    //展示好友列表
    public static final String SHOW_TYPE = "show_type";
    //无
    public static final int SHOW_NONE = 0;
    //展示粉丝列表
    public static final int SHOW_FANS = 1;
    //展示好友列表
    public static final int SHOW_FRIENDS = 2;

    private TextView titleText;
    private ListView showList;
    private Dialog dialog;
    private FriendshipsAPI friendshipsAPI;
    private Context mContext;
    private List<User> users;
    private int type;


    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_users);

        mContext = this;

        titleText = (TextView) findViewById(R.id.titleText);
        showList = (ListView) findViewById(R.id.userList);
        showList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ShowUserDetails.class);
                intent.putExtra("user_id", users.get(position).id);
                startActivity(intent);
            }
        });

        dialog = CustomProgressDialog.createLoadingDialog(mContext,
                    "正在拉取信息，请稍候...");

        Intent intent = getIntent();
        type = intent.getIntExtra(SHOW_TYPE, 0);
        getData();
    }

    private void getData() {
        switch (type){
            case SHOW_NONE:
                break;
            case SHOW_FRIENDS:
                {
                    dialog.show();
                    if(friendshipsAPI==null){
                        friendshipsAPI = new FriendshipsAPI(mContext, Constants.APP_KEY,
                                Common.getAccessToken(mContext));
                    }
                    String idStr = getIntent().getStringExtra(Constants.USERID);
                    long uid = Long.valueOf(idStr);
                    friendshipsAPI.friends(uid, 50, 0, false, new RequestListener() {
                        @Override
                        public void onComplete(String s) {
                            ShowFans(s);
                        }

                        @Override
                        public void onWeiboException(WeiboException e) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case SHOW_FANS:
                {
                    dialog.show();
                    if(friendshipsAPI==null){
                        friendshipsAPI = new FriendshipsAPI(mContext, Constants.APP_KEY,
                                Common.getAccessToken(mContext));
                    }
                    String idStr = getIntent().getStringExtra(Constants.USERID);
                    long uid = Long.valueOf(idStr);
                    friendshipsAPI.followers(uid, 50, 0, false, new RequestListener() {
                        @Override
                        public void onComplete(String s) {
                            ShowFriends(s);
                        }

                        @Override
                        public void onWeiboException(WeiboException e) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }

    private void ShowFriends(String s) {
        updateWidegt();
        UserList userList = UserList.parse(s);
        users = userList.userList;
        if(users==null){
            titleText.setText("没有关注的人");
            Toast.makeText(mContext, "没有关注的人，快去关注几个吧!", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        titleText.setText("我的关注");
        adapter = new UserAdapter(mContext, users);
        showList.setAdapter(adapter);


    }

    private void ShowFans(String s) {
        updateWidegt();
        UserList userList = UserList.parse(s);
        users = userList.userList;
        if(users==null){
            titleText.setText("没有粉丝，还需要努力哦!");
            Toast.makeText(mContext, "没有粉丝", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        titleText.setText("我的粉丝");
        adapter = new UserAdapter(mContext, users);
        showList.setAdapter(adapter);
    }

    private void updateWidegt(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
