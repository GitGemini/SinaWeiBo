package com.henu.sinaweibo.Tools;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by AKira on 2017/6/10.
 */

public class ImageListener implements View.OnClickListener {

    private String picUrl;
    private Context context;

    public ImageListener(Context context,String url){
        this.picUrl = url;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
//        Toast.makeText(context, picUrl, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, ShowImageAvtivity.class);
        intent.putExtra(ShowImageAvtivity.URL_KEY, picUrl);
        context.startActivity(intent);
    }
}
