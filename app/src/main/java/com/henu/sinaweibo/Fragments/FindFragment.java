package com.henu.sinaweibo.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.henu.sinaweibo.Models.CustomProgressDialog;
import com.henu.sinaweibo.R;
import com.henu.sinaweibo.Tools.Common;
import com.henu.sinaweibo.Tools.Constants;
import com.henu.sinaweibo.Weibo.ShowDetail;
import com.henu.sinaweibo.Weibo.WeiboAdapter;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import java.util.List;

/**
 * Created by hppc on 2017/5/8.
 */


public class FindFragment extends Fragment {
    private EditText mEtSearchContent; //搜索输入框
    private SwipeRefreshLayout refreshLayout;
    private ListView mListViewResult;//搜索列表结果
    private ImageView mIvClear;//清除搜索内容的图标
    private TextView mSearchResult;
    // 用于获取微博信息流等操作的API
    private StatusesAPI mStatusesAPI;
    private List<Status> statuses;

    private Dialog dialog;
    private View mView;
    private Context mContext;

    private static final int DO_SEARCH = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_find, container, false);
        mContext = getActivity();
        initViews();
        setListeners();
        loadList();
        return mView;
    }

    private void loadList() {
        if(dialog==null){
            dialog = CustomProgressDialog.createLoadingDialog(mContext,
                    "正在拉取信息，请稍候...");
        }
        dialog.show();
        mStatusesAPI.publicTimeline(50, 1, false, new RequestListener() {
            @Override
            public void onComplete(String s) {
                searchWeiboInfo(s);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Log.e(Constants.TAG, e.getMessage());
            }
        });
    }

    private void initViews() {
        //获取当前已保存过的Token
        Oauth2AccessToken mAccessToken = Common.getAccessToken(mContext);
        mStatusesAPI = new StatusesAPI(mContext, Constants.APP_KEY, mAccessToken);
        mIvClear = (ImageView) mView.findViewById(R.id.iv_clear_content);
        mSearchResult = (TextView)mView.findViewById(R.id.search_result_text);
        mEtSearchContent = (EditText) mView.findViewById(R.id.et_search_content);
        mListViewResult = (ListView) mView.findViewById(R.id.lv_search_result);
        refreshLayout = (SwipeRefreshLayout)mView.findViewById(R.id.ll_search_result);
    }

    private void setListeners() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(statuses!=null)  statuses.clear();
                loadList();
            }
        });
        //如果不想使用边写边搜索的话可以使用回车键按下
        mEtSearchContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER)//如果点击回车键
                {
                    //开始搜索
                    loadList();
                    return true;
                }
                return false;
            }
        });

        //搜索输入框的监听事件
        /*mEtSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //文字改变前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {//相关课程listview隐藏
                    mIvClear.setVisibility(View.GONE);
                } else if (mIvClear.getVisibility() == View.GONE) {
                    mIvClear.setVisibility(View.VISIBLE);//相关listview显示
                }
                // mHandler.sendEmptyMessageDelayed(DO_SEARCH, 1000);//延迟搜索，在用户输入的时候就进行搜索，但是考虑到用户流量问题，延迟一秒
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mIvClear.setVisibility(View.GONE);
                }
            }
        });*/

        //为清除图标设置监听器事件
        mIvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearContent();//清除搜索框的内容
            }
        });
        //设置列表项的点击事件
        mListViewResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 2017/5/30  这里写跳转语句 跳转到每一项的详细显示界面
                //获得视图绑定的tag信息
                String idstr = statuses.get(position).id;
                //启动查看微博详细信息的视图
                Intent intent = new Intent(mContext, ShowDetail.class);
                //将微博的id绑定到b中发送给下一个activity
                intent.putExtra(Constants.STATUS_ID, idstr);
                startActivity(intent);
            }
        });
    }

    /**
     * 搜索微博
     *
     * @param WeiBoInfos 微博总集合
     * @param str        搜索关键字
     * @return 是否搜索到微博
     */
    private void searchWeiboInfo(String s) {
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        StatusList statusList = StatusList.parse(s);
        statuses = statusList.statusList;
        WeiboAdapter adapter = new WeiboAdapter(mContext, statuses);
        mListViewResult.setAdapter(adapter);
        refreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
        if (statuses.size() == 0) {
            mSearchResult.setText("没有找到相关微博，试试其他关键词吧");
        } else {
            mSearchResult.setText("搜索结果");
        }
    }

    /**
     * 清空输入框
     */
    public void clearContent() {
        mEtSearchContent.setText("");
        mIvClear.setVisibility(View.GONE);
    }
}





