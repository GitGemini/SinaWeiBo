package com.henu.sinaweibo.Setting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.henu.sinaweibo.Models.UserInfo;

import java.util.List;

/**
 * Created by AKira on 2017/6/7.
 */

public class LoginUserAdapter extends BaseAdapter {

    private List<UserInfo> userInfos;
    private Context mContext;

    public LoginUserAdapter(Context context, List<UserInfo> userInfos){
        this.userInfos = userInfos;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return userInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return userInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LoginUserHolder luh = new LoginUserHolder(mContext, convertView, userInfos.get(position));
        return luh.getView();
    }
}
