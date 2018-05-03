package com.henu.sinaweibo.Commnet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.henu.sinaweibo.R;
import com.henu.sinaweibo.Tools.ImageUtils;
import com.henu.sinaweibo.Tools.TextUtils;
import com.sina.weibo.sdk.openapi.models.Comment;

import java.util.Date;

/**
 * Created by AKira on 2017/6/3.
 */

public class CommentHodler {
    private ImageView commentIcon;
    private TextView commentName,commentTime,commentText;

    private Context mContext;
    private View mView;
    private Comment comment;

    public CommentHodler(Context context, View convertView, Comment comment){
        this.mContext = context;
        this.comment = comment;
        if(convertView==null){
            mView = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
        }else {
            mView = convertView;
        }

        InitView();
    }

    public View getView(){
        commentName.setText(comment.user.name);
        String time = TextUtils.getTimeDiff(new Date(comment.created_at));
        commentTime.setText(time);
        commentText.setText(TextUtils.getWeiBoContent(mContext ,comment.text, commentText), TextView.BufferType.SPANNABLE);
        ImageUtils.loadDrawable(comment.user.profile_image_url, commentIcon);
        return mView;
    }

    private void InitView() {
        commentIcon = (ImageView)mView.findViewById(R.id.commentIcon);
        commentTime = (TextView)mView.findViewById(R.id.commentTime);
        commentText = (TextView)mView.findViewById(R.id.commentText);
        commentName = (TextView)mView.findViewById(R.id.commentName);
    }
}
