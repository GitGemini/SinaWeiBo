package com.henu.sinaweibo.Weibo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sina.weibo.sdk.openapi.models.Status;

import java.util.List;

/**
 * Created by 公子 on 2017/5/16.
 */

public class WeiboAdapter extends BaseAdapter {
    //保存当前上下文
    Context mContext;
    //存储所有微博信息
    private List<Status> statusList;

    //获取列表总行数
    public WeiboAdapter(Context mContext, List<Status> statusList) {
        this.mContext = mContext;
        this.statusList = statusList;
    }

    @Override
    public int getCount() {
        return statusList.size();
    }

    //取得当前选中的微博
    @Override
    public Object getItem(int i) {
        return statusList.get(i);
    }

    //取得当前的位置
    @Override
    public long getItemId(int i) {
        return i;
    }

    //得到每一行的视图
    @Override
    public View getView(int postion, View convertView, ViewGroup parent) {
        WeiboHolder wh = new WeiboHolder(mContext, convertView, statusList.get(postion));
        return wh.getWeiboView();
    }
}

