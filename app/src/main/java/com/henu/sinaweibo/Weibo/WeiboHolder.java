package com.henu.sinaweibo.Weibo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.henu.sinaweibo.R;
import com.henu.sinaweibo.Tools.ImageUtils;
import com.henu.sinaweibo.Tools.TextUtils;
import com.sina.weibo.sdk.openapi.models.Status;

/**
 * Created by 公子 on 2017/5/25.
 */ //用于存放微博视图元素的类
public class WeiboHolder{
    //用户昵称
    private TextView wbuser;
    //发布时间
    private TextView wbtime;
    //内容
    private TextView wbtext;
    //用户图标
    private ImageView wbicon;
    //网格布局
    private GridLayout layout;
    //
    private LinearLayout repostsLayout,commentsLayout,attitudesLayout;
    //转发数，评论数，点赞数
    public TextView reportsCount,commentsCount,attitudesCount;
    //点赞图标
    public ImageView attitudesIcon;

    public WeiBoInfo wb;
    private Context mContext;
    private View view;
    private WeiboListener weiboListener;


    public WeiboHolder(Context context,View convertView, Status status){
        if(convertView==null){
            this.view = LayoutInflater.from(context).inflate(R.layout.weibo, null);
        }else {
            this.view = convertView;
        }

        this.wb = WeiBoInfo.convertFromStatus(status);
        this.mContext = context;
        weiboListener = new WeiboListener(context, this);

        InitView();
        InitEvent();
    }

    private void InitEvent() {
        this.attitudesLayout.setOnClickListener(weiboListener);
        this.commentsLayout.setOnClickListener(weiboListener);
        this.repostsLayout.setOnClickListener(weiboListener);
    }


    private void InitView() {
        //取得界面的元素
        this.wbicon = (ImageView) view.findViewById(R.id.wbicon);
        this.wbtext = (TextView) view.findViewById(R.id.wbtext);
        this.wbtime = (TextView) view.findViewById(R.id.wbtime);
        this.wbuser = (TextView) view.findViewById(R.id.wbuser);
        this.layout = (GridLayout) view.findViewById(R.id.gridLaout);
        this.repostsLayout = (LinearLayout)view.findViewById(R.id.repostsLayout);
        this.commentsLayout = (LinearLayout)view.findViewById(R.id.commentsLayout);
        this.attitudesLayout = (LinearLayout)view.findViewById(R.id.attitudesLayout);
        this.reportsCount = (TextView) view.findViewById(R.id.repostsCount);
        this.commentsCount = (TextView) view.findViewById(R.id.commentsCount);
        this.attitudesCount = (TextView) view.findViewById(R.id.attitudesCount);
        this.attitudesIcon = (ImageView) view.findViewById(R.id.attitudesIcon);
    }

    public View getWeiboView(){
        //取得微博信息
        if (wb != null) {
            //将微博的id保存到标签汇总
            view.setTag(wb.getId());
            wbuser.setText(wb.getUserName());
            wbtime.setText(wb.getTime());
            wbtext.setText(TextUtils.getWeiBoContent(mContext, wb.getText(), wbtext), TextView.BufferType.SPANNABLE);
            reportsCount.setText(""+wb.getRepostsCount());
            commentsCount.setText(""+wb.getCommentsCount());
            attitudesCount.setText(""+wb.getAttitudesCount());
            layout.removeAllViews();
            //取得用户图标
            ImageUtils.loadDrawable(wb.getUserIcon(), wbicon);
            //如果微博内有宫格图，则显示图片
            if (wb.getHaveImages()) {
                ImageUtils.SetNineImages(mContext, this.layout, wb.getPicUrls());
            }
        }
        return view;
    }
}
