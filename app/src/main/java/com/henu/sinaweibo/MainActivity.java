package com.henu.sinaweibo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.henu.sinaweibo.Fragments.FindFragment;
import com.henu.sinaweibo.Fragments.HomeFragment;
import com.henu.sinaweibo.Fragments.MeFragment;
import com.henu.sinaweibo.Fragments.MessageFragment;
import com.henu.sinaweibo.Fragments.WriteWeiBoActivity;
import com.henu.sinaweibo.AppConfig.ActivityCollector;

/**
 * Created by AKira on 2017/4/30.
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    Fragment frag1,frag2,frag3,frag4;
    ImageView img1,img2,img3,img4;
    LinearLayout tab1,tab2,tab3,tab4,tab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_main);
        ActivityCollector.addActivity(this);

        InitView();
        InitEvent();
        setSelect(0);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_01:
                setSelect(0);
                break;
            case R.id.tab_02:
                setSelect(1);
                break;
            case R.id.tab_03:
                setSelect(2);
                break;
            case R.id.tab_04:
                setSelect(3);
                break;
            case R.id.tab_add:
                {
                    Intent intent = new Intent(MainActivity.this, WriteWeiBoActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void setSelect(int i) {
        //0、获取事务管理器
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //1、重置图片
        clearIcon();
        //2、隐藏Fragment
        hideFragment(transaction);
        //3、改变内容

        switch (i){
            case 0:
                if(frag1 == null){
                    frag1 = new HomeFragment();
                    transaction.add(R.id.contentFrag, frag1);
                }
                img1.setImageResource(R.drawable.tab_home_h);
                transaction.show(frag1);
                break;
            case 1:
                if(frag2 == null){
                    frag2 = new MessageFragment();
                    transaction.add(R.id.contentFrag, frag2);
                }
                img2.setImageResource(R.drawable.tab_message_h);
                transaction.show(frag2);
                break;
            case 2:
                if(frag3 == null){
                    frag3 = new FindFragment();
                    transaction.add(R.id.contentFrag, frag3);
                }
                img3.setImageResource(R.drawable.tab_find_h);
                transaction.show(frag3);
                break;
            case 3:
                if(frag4== null){
                    frag4 = new MeFragment();
                    transaction.add(R.id.contentFrag, frag4);
                }
                img4.setImageResource(R.drawable.tab_me_h);
                transaction.show(frag4);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if(frag1!=null){
            transaction.hide(frag1);
        }
        if(frag2!=null){
            transaction.hide(frag2);
        }
        if(frag3!=null){
            transaction.hide(frag3);
        }
        if(frag4!=null){
            transaction.hide(frag4);
        }
    }

    private void clearIcon() {
        img1.setImageResource(R.drawable.tab_home);
        img2.setImageResource(R.drawable.tab_message);
        img3.setImageResource(R.drawable.tab_find);
        img4.setImageResource(R.drawable.tab_me);
    }

    private void InitView() {
        tab1 = (LinearLayout)findViewById(R.id.tab_01);
        tab2 = (LinearLayout)findViewById(R.id.tab_02);
        tab3 = (LinearLayout)findViewById(R.id.tab_03);
        tab4 = (LinearLayout)findViewById(R.id.tab_04);
        tab_add = (LinearLayout)findViewById(R.id.tab_add);

        img1 = (ImageView) findViewById(R.id.homeIcon);
        img2 = (ImageView) findViewById(R.id.messageIcon);
        img3 = (ImageView) findViewById(R.id.findIcon);
        img4 = (ImageView) findViewById(R.id.meIcon);
    }

    private void InitEvent() {
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);
        tab_add.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
