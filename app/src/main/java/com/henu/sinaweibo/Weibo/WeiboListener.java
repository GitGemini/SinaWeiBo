package com.henu.sinaweibo.Weibo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.henu.sinaweibo.Commnet.ShowCommentsActivity;
import com.henu.sinaweibo.Tools.Common;
import com.henu.sinaweibo.Tools.Constants;
import com.henu.sinaweibo.R;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;

/**
 * Created by AKira on 2017/6/3.
 */

public class WeiboListener implements View.OnClickListener {
    private WeiboHolder weiboHolder;
    private Context mContext;
    private StatusesAPI statusesAPI;

    public WeiboListener(Context context, WeiboHolder weiboHolder){
        this.mContext = context;
        this.weiboHolder = weiboHolder;
        statusesAPI = new StatusesAPI(mContext, Constants.APP_KEY, Common.getAccessToken(mContext));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.repostsLayout:
                {
                    String idStr = weiboHolder.wb.getId();
                    long id = Long.parseLong(idStr);
                    statusesAPI.repost(id, null, 0, new RequestListener() {
                        @Override
                        public void onComplete(String s) {
                            Toast.makeText(mContext, "转发微博成功！",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onWeiboException(WeiboException e) {
                            Toast.makeText(mContext, "转发微博失败！"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.commentsLayout:
                {
                    Intent intent = new Intent(mContext, ShowCommentsActivity.class);
                    intent.putExtra(Constants.COMMENT_ID, weiboHolder.wb.getId());
                    mContext.startActivity(intent);
                }
                break;
            case R.id.attitudesLayout:
                {
                    weiboHolder.attitudesIcon.setImageResource(R.drawable.picture_like);
                }
                break;
            default:
                break;
        }
    }


}
