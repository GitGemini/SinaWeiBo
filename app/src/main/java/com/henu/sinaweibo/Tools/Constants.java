package com.henu.sinaweibo.Tools;

/**
 * Created by AKira on 2017/5/1.
 */

public interface Constants {
    public static final String TAG = "sinaweibo";

    public static final String APP_KEY      = "1748471742";		   // 应用的APP_KEY
    public static String APP_SECRET = "db7cbfcb94873937d2d36ae63f562d04";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";// 应用的回调页
    public static final String SCOPE = 							   // 应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public static final String COMMENT_ID = "comment_id";
    public static final String STATUS_ID = "status_id";
    public static final String USERID = "user_id";

}
