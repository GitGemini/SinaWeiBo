package com.henu.sinaweibo.Commnet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.henu.sinaweibo.AppConfig.BaseActivity;
import com.henu.sinaweibo.Models.CustomProgressDialog;
import com.henu.sinaweibo.R;
import com.henu.sinaweibo.Tools.Common;
import com.henu.sinaweibo.Tools.Constants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;

import java.util.List;

/**
 * Created by AKira on 2017/6/13.
 */

public class ShowCommentsActivity extends BaseActivity {
    private TextView titleText;
    private SwipeRefreshLayout refreshLayout;
    private ListView showList;
    private Dialog dialog;
    private CommentsAPI commentsAPI;
    private Context mContext;

    private List<Comment> comments;
    private EditText et_comment;
    private TextView createComment;

    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_comments_acticity);

        mContext = this;
        et_comment = (EditText) findViewById(R.id.et_comment);
        createComment = (TextView)findViewById(R.id.createComment);
        createComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostComment(et_comment.getText());
            }
        });

        titleText = (TextView) findViewById(R.id.titleText);
        showList = (ListView) findViewById(R.id.showlist);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshShowList);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(comments!=null){
                    comments=null;
                    getData();
                }
            }
        });
        dialog = CustomProgressDialog.createLoadingDialog(mContext,
                "正在拉取信息，请稍候...");
        getData();
    }

    private void PostComment(CharSequence text) {
        if(text==null||text.length()<=0){
            Toast.makeText(mContext, "发表评论失败,字数太少！", Toast.LENGTH_SHORT).show();
            return;
        }
        String message = text.toString();
        if(message.length()>=140){
            Toast.makeText(mContext, "发表评论失败，字数超过140！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = getIntent();
        String idStr = intent.getStringExtra(Constants.COMMENT_ID);
        long id = Long.valueOf(idStr);
        commentsAPI.create(message, id, true, new RequestListener() {
            @Override
            public void onComplete(String s) {
                Toast.makeText(mContext, "发表评论成功！", Toast.LENGTH_SHORT).show();
                et_comment.setText("");
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(mContext, "发表评论失败！"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        dialog.show();
        if(commentsAPI==null){
            commentsAPI = new CommentsAPI(mContext, Constants.APP_KEY,
                    Common.getAccessToken(mContext));
        }
        String idStr = getIntent().getStringExtra(Constants.COMMENT_ID);
        long id = Long.valueOf(idStr);
        commentsAPI.show(id, 0, 0, 50, 1, CommentsAPI.AUTHOR_FILTER_ALL, new RequestListener() {
            @Override
            public void onComplete(String s) {
                        ShowComments(s);
                    }

            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(mContext, "拉取评论失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowComments(String s){
        updateWidegt();
        CommentList commentList = CommentList.parse(s);
        comments = commentList.commentList;
        if(comments==null){
            titleText.setText("没有相关评论");
            Toast.makeText(mContext, "暂时没有相关评论!", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        titleText.setText("评论");
        adapter = new CommentAdapter(mContext, comments);
        showList.setAdapter(adapter);
    }

    private void updateWidegt(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        refreshLayout.setRefreshing(false);
    }
}
