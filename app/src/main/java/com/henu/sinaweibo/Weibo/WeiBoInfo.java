package com.henu.sinaweibo.Weibo;

import com.henu.sinaweibo.Tools.TextUtils;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.Date;
import java.util.List;

/**
 * Created by 公子 on 2017/5/13.
 */

public class WeiBoInfo {
    //微博id
    private String id;
    //发布人id
    private String userId;
    //发布人名字
    private String userName;
    //发布人头像
    private String userIcon;
    //发布时间
    private String time;
    //是否有宫格图
    private Boolean haveImages=false;
    //图片路径集合
    private List<String> picUrls;
    //文章内容
    private String text;
    //赞
    private int attitudesCount;
    //评论
    private int commentsCount;
    //转发
    private int repostsCount;

    private Status retweeted_status;

    public WeiBoInfo(){

    }

    public WeiBoInfo(Status data){
        String time = data.created_at;
        Boolean haveImgs = false;
        //判断微博中是否有宫格图
        if(data.pic_urls!=null){
            haveImgs = true;
        }
        Date date = new Date(time);
        time = TextUtils.getTimeDiff(date);
        this.setId(data.id);
        this.setUserId(data.user.id);
        this.setUserName(data.user.screen_name);
        this.setTime(time);
        this.setText(data.text);
        this.setHaveImages(haveImgs);
        this.setUserIcon(data.user.profile_image_url);
        this.setAttitudesCount(data.attitudes_count);
        this.setCommentsCount(data.comments_count);
        this.setRepostsCount(data.reposts_count);
        this.setPicUrls(data.pic_urls);
    }

    public static WeiBoInfo convertFromStatus(Status data){
        return new WeiBoInfo(data);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getHaveImages() {
        return haveImages;
    }

    public void setHaveImages(Boolean haveImages) {
        this.haveImages = haveImages;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAttitudesCount() {
        return attitudesCount;
    }

    public void setAttitudesCount(int attitudesCount) {
        this.attitudesCount = attitudesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(int repostsCount) {
        this.repostsCount = repostsCount;
    }

    public List<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<String> picUrls) {
        this.picUrls = picUrls;
    }
}
