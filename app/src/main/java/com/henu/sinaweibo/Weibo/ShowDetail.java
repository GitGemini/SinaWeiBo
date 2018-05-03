package com.henu.sinaweibo.Weibo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.henu.sinaweibo.AppConfig.BaseActivity;
import com.henu.sinaweibo.Commnet.CommentAdapter;
import com.henu.sinaweibo.Tools.Common;
import com.henu.sinaweibo.Tools.Constants;
import com.henu.sinaweibo.Models.CustomProgressDialog;
import com.henu.sinaweibo.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.List;

/**
 * Created by AKira on 2017/6/6.
 */

public class ShowDetail extends BaseActivity {
    //UI
    private FrameLayout weiboFrame,commentFrame;
    private ListView showCommentsList;

    //API
    private StatusesAPI statusAPI;
    private CommentsAPI commentAPI;

    //数据
    private Status status;
    private List<Comment> comments;
    private long statusID;
    private Dialog dialog;

    //上下文
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weibo_detail);

        InitView();
        loadView();
    }

    private void loadView() {
        if(dialog==null){
            dialog = CustomProgressDialog.createLoadingDialog(ShowDetail.this,
                    "正在拉取信息，请稍候...");
        }
        dialog.show();
        statusAPI.show(statusID, new RequestListener() {
            @Override
            public void onComplete(String s) {
                statusComplete(s);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(mContext, "获取微博信息失败!"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        commentAPI.show(statusID, 0, 0, 50, 1, 0, new RequestListener() {
            @Override
            public void onComplete(String s) {
                commentsComplete(s);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(mContext, "获取评论信息失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitView() {
        Intent intent = getIntent();
        String idstr = intent.getStringExtra(Constants.STATUS_ID);
        statusID = Long.parseLong(idstr);

        mContext = ShowDetail.this;
        Oauth2AccessToken mAccessToken = Common.getAccessToken(mContext);
        statusAPI = new StatusesAPI(mContext, Constants.APP_KEY, mAccessToken);
        commentAPI = new CommentsAPI(mContext, Constants.APP_KEY, mAccessToken);

        weiboFrame = (FrameLayout) findViewById(R.id.weiboFrame);
        commentFrame = (FrameLayout) findViewById(R.id.commentFrame);
        showCommentsList = (ListView) findViewById(R.id.showCommentsList);
    }

    private void statusComplete(String s) {
        status = Status.parse(s);
        if(status==null){
            Toast.makeText(mContext, "获取微博信息失败!", Toast.LENGTH_SHORT).show();
            return;
        }
        WeiboHolder wbHolder =new WeiboHolder(mContext, null, status);
        View view = wbHolder.getWeiboView();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        weiboFrame.addView(view, params);
    }
    private void commentsComplete(String s) {
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        CommentList commentList = CommentList.parse(s);
        comments = commentList.commentList;
        if(comments==null){
            Toast.makeText(mContext, "获取评论信息失败!", Toast.LENGTH_SHORT).show();
        }
        CommentAdapter adapter = new CommentAdapter(mContext, comments);
        showCommentsList.setAdapter(adapter);
    }

}
