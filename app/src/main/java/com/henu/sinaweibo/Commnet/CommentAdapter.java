package com.henu.sinaweibo.Commnet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sina.weibo.sdk.openapi.models.Comment;

import java.util.List;

/**
 * Created by AKira on 2017/6/3.
 */

public class CommentAdapter extends BaseAdapter {

    //保存当前上下文
    Context mContext;
    //存储所有微博信息
    private List<Comment> commentList;

    //获取列表总行数
    public CommentAdapter(Context mContext, List<Comment> commentList) {
        this.mContext = mContext;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentHodler commentHodler = new CommentHodler(mContext, convertView, commentList.get(position));
        return commentHodler.getView();
    }
}
