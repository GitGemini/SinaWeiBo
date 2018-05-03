package com.henu.sinaweibo.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.henu.sinaweibo.Helper.DataHelper;
import com.henu.sinaweibo.Models.CustomProgressDialog;
import com.henu.sinaweibo.Models.UserInfo;
import com.henu.sinaweibo.R;
import com.henu.sinaweibo.Tools.Common;
import com.henu.sinaweibo.Tools.Constants;
import com.henu.sinaweibo.WBAuthActivity;
import com.henu.sinaweibo.Weibo.ShowDetail;
import com.henu.sinaweibo.Weibo.WeiboAdapter;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;

import org.json.JSONObject;

import java.util.List;


public class HomeFragment extends Fragment {

    //存储所有微博信息
    private List<Status> statuses;
    //载入动画
    SwipeRefreshLayout refreshLayout;
    ImageButton refreshBtn;
    TextView showName;
    ListView msglist;
    private Dialog dialog;
    private WeiboAdapter adapter;
    // 用于获取微博信息流等操作的API
    private StatusesAPI mStatusesAPI;
    //承载FramentActivity的activity
    private FragmentActivity mActivity;

    private static int page = 1;
    private View footView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mActivity = getActivity();

        InitView(view);
        InitEvent();

        //初始化界面
        loadList();
        return view;
    }

    private void InitEvent() {
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadList();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(statuses!=null)  statuses.clear();
                loadList();
            }
        });
    }

    private void InitView(View view) {
        refreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        showName = (TextView) view.findViewById(R.id.showName);
        refreshBtn = (ImageButton) view.findViewById(R.id.refreshBtn);
        msglist = (ListView) view.findViewById(R.id.Msglist);
        footView = mActivity.getLayoutInflater().inflate(R.layout.foot_load_more, null);
        Log.e(Constants.TAG, footView.toString());
        msglist.addFooterView(footView);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMore();
            }
        });
    }

    private void loadMore() {
        if(dialog==null){
            dialog = CustomProgressDialog.createLoadingDialog(mActivity,
                    "正在拉取信息，请稍候...");
        }
        dialog.show();
        mStatusesAPI.friendsTimeline(0, 0, 50, ++page, false, 1, false, mListener);
    }

    //初始化界面显示
    private void loadList(){
        page = 1;
        //如果当前用户没有登录直接返回
        if(Common.login_user==null){
            //Log.e(Constants.TAG, "no login_user");
            return;
        }
        else {
            //取得当前登录的用户
            User user = Common.login_user;
            //显示当前用户名称
            showName.setText(user.screen_name);
            // 对statusAPI实例化
            mStatusesAPI = new StatusesAPI(mActivity, Constants.APP_KEY, Common.getAccessToken(mActivity));
            if(dialog==null){
                dialog = CustomProgressDialog.createLoadingDialog(mActivity,
                        "正在拉取信息，请稍候...");
            }
            dialog.show();
            mStatusesAPI.friendsTimeline(0, 0, 50, page, false, 1, false, mListener);
        }
    }
    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            if (!TextUtils.isEmpty(response)) {
                try {
                    if (response.startsWith("{\"statuses\"")) {
                        JSONObject json = new JSONObject(response);
                        if (json.has("error") && json.getString("error_code") == "21315") {
                            Toast.makeText(mActivity, "授权过期，请重新授权！", Toast.LENGTH_LONG).show();
                            //如果过期则引导用户重新授权
                            Intent intent = new Intent(mActivity, WBAuthActivity.class);
                            startActivity(intent);
                            mActivity.finish();
                        } else if (json.has("error")) {
                            Toast.makeText(mActivity, "出错了，错误代码为：" + json.getString("error_code"), Toast.LENGTH_LONG).show();
                            //重新登录
                            Intent intent = new Intent(mActivity, WBAuthActivity.class);
                            startActivity(intent);
                            mActivity.finish();
                        } else {
                            //取得statuses信息
                            StatusList statusList = StatusList.parse(response);
                            statuses = statusList.statusList;
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

                //判断微博列表是否为空
                if(statuses!=null){
                    //创建微博信息列表的适配器
                    adapter = new WeiboAdapter(mActivity, statuses);
                    msglist.setAdapter(adapter);
                    refreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();

                    //设置列表的按键监听器
                    msglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //获得视图绑定的tag信息
                            String id = statuses.get(i).id;
                            //启动查看微博详细信息的视图
                            Intent intent = new Intent(mActivity,ShowDetail.class);
                            //将微博的id绑定到b中发送给下一个activity
                            intent.putExtra(Constants.STATUS_ID, id);
                            startActivity(intent);
                        }
                    });
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            //Log.e(Constants.TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(mActivity, info.toString(), Toast.LENGTH_LONG).show();
        }
    };

}
