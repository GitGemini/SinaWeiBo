package com.henu.sinaweibo.Weibo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sina.weibo.sdk.openapi.models.User;

import java.util.List;

/**
 * Created by AKira on 2017/6/7.
 */

public class UserAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> users;

    public UserAdapter(Context context, List<User> users){
        this.mContext = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserHolder uh = new UserHolder(mContext, convertView, users.get(position));
        return uh.getView();
    }
}
