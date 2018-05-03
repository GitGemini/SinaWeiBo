package com.henu.sinaweibo.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.henu.sinaweibo.Message.IconAdapter;
import com.henu.sinaweibo.Message.MessageIcon;
import com.henu.sinaweibo.Message.ShowMessageActivity;
import com.henu.sinaweibo.R;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    private ListView listView;
    private List<MessageIcon> iconList = new ArrayList<>();
    private static  int aite=0;
    private static  int pinglun =1;
    private static  int zanwo =2;

    private View mView;
    private Context mContext;

    final String []message = {"@我的","评论","赞"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_message, container, false);
        mContext = getActivity();

        //创建选项菜单
        listView = (ListView) mView.findViewById(R.id.listview1);
        initList();//初始化菜单数据
        IconAdapter adapter = new IconAdapter(mContext ,R.layout.message_item,iconList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ShowMessageActivity.class);
                startActivity(intent);
            }
        });
        return mView;
    }

    private void initList() {
        MessageIcon aite = new MessageIcon("@我的",R.drawable.message_at);
        iconList.add(aite);
        MessageIcon pinglun = new MessageIcon("评论",R.drawable.message_comment);
        iconList.add(pinglun);
        MessageIcon zan = new MessageIcon("赞",R.drawable.message_like);
        iconList.add(zan);
    }

}
