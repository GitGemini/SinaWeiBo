package com.henu.sinaweibo.Emotions;

import com.henu.sinaweibo.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AKira on 2017/6/9.
 */

public class EmotionUtils {
    public static Map<String, Integer> emojiMap;

    private static final String URL = "https://api.weibo.com/2/emotions.json";
    private static String mJsonInfo;

    public EmotionUtils(){
        HelpUtils helpUtils = new HelpUtils();
         // 获取Json数据
         mJsonInfo = helpUtils.getHttpString(URL);
         // 将Json数据反序列化成java对象
         List<Bean> beans = helpUtils.changeJsonToList(mJsonInfo);
         //循环遍历下载图片
         for (int i = 0; i < beans.size(); i++) {
             helpUtils.makeImage(beans.get(i), "C:/images/");
        }
    }

    static {
        emojiMap = new HashMap<String, Integer>();
        emojiMap.put("[微笑]", 0);
    }

    public static int getImgByName(String imgName) {
//         Integer integer = emojiMap.get(imgName);
         return R.drawable.emotion;
     }
}
