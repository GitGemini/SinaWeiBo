package com.henu.sinaweibo.Message;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.henu.sinaweibo.Models.CustomProgressDialog;
import com.henu.sinaweibo.R;
import com.henu.sinaweibo.Tools.Common;
import com.henu.sinaweibo.Tools.Constants;
import com.henu.sinaweibo.Weibo.WeiboAdapter;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import java.util.List;

/**
 * Created by AKira on 2017/6/3.
 */

public class ShowMessageActivity extends Activity {

    private TextView titleText;
    private ListView showList;
    private Dialog dialog;
    private StatusesAPI statusesAPI;
    private Context mContext;

    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_users);

        mContext = this;
        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText("提到我的微博");
        showList = (ListView)findViewById(R.id.showlist);

        getMessages();
    }

    private void getMessages() {
        if(statusesAPI==null){
            statusesAPI = new StatusesAPI(mContext, Constants.APP_KEY,
                    Common.getAccessToken(mContext));
        }
        if(dialog==null){
            dialog = CustomProgressDialog.createLoadingDialog(mContext,
                    "正在拉取信息，请稍候...");
        }
        dialog.show();
        statusesAPI.mentions(0, 0, 50, 1, 0, 1, 1, true, new RequestListener() {
            @Override
            public void onComplete(String s) {
                ShowMessages(s);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Log.e(Constants.TAG, e.getMessage());
                Toast.makeText(mContext, "拉取评论失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void ShowMessages(String s){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        StatusList statusList = StatusList.parse(s);
        List<Status> statuses = statusList.statusList;
        if(statuses==null){
            Toast.makeText(mContext, "没有提到我的微博", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        adapter = new WeiboAdapter(mContext, statuses);
        showList.setAdapter(adapter);
    }
}
