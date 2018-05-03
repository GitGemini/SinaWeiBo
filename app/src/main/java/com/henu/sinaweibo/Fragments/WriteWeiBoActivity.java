package com.henu.sinaweibo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.henu.sinaweibo.AppConfig.BaseActivity;
import com.henu.sinaweibo.R;
import com.henu.sinaweibo.Tools.Common;
import com.henu.sinaweibo.Tools.Constants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * created by mqh
 */

public class WriteWeiBoActivity extends BaseActivity {
    private ImageView mCancel;//取消返回按钮
    private TextView mSendTv;//发送
    private EditText mEditText;//微博内容编辑
    private TextView mLimitTextView;//剩余字数说明
    private LinearLayout mRepostlayout; //发送布局
    private ImageView repostImg;//发送图片
    private TextView repostName;//发送的名字
    private TextView repostContent;//发送的内容
    private ImageButton mPhotoButton;//拍照按钮
    public Uri imageUri;
    public static final int TAKE_PHOTO = 1;
    private RelativeLayout mPhoto;

    private Context mContext;
    private StatusesAPI statusesAPI;

    /**
     * 最多输入140个字符
     */
    private static final int TEXT_LIMIT = 140;

    /**
     * 在只剩下10个字可以输入的时候，做提醒
     */
    private static final int TEXT_REMIND = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_write_weibo);

        mContext = this;
        statusesAPI = new StatusesAPI(mContext, Constants.APP_KEY, Common.getAccessToken(mContext));

        InitView();
        setListeners();

    }

    private void InitView() {
        mSendTv = (TextView)findViewById(R.id.idea_send);
        mCancel = (ImageView) findViewById(R.id.idea_cancal);
        mEditText = (EditText) findViewById(R.id.idea_content);
        mLimitTextView = (TextView) findViewById(R.id.limitTextView);
        mRepostlayout = (LinearLayout) findViewById(R.id.repost_layout);
        repostImg = (ImageView) findViewById(R.id.repost_img);
        repostName = (TextView) findViewById(R.id.repost_name);
        repostContent = (TextView) findViewById(R.id.repost_content);

        mPhotoButton = (ImageButton)findViewById(R.id.imageButton_choose_photo);
        mPhoto = (RelativeLayout) findViewById(R.id.photo_relativelayout);
    }

    //设置监听事件
    private void setListeners()
    {
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建File对象，用于存储拍照后的照片，手机SD卡的应用关联缓存目录下
               /* 把图片命名为image.jpg，调用getExternalCacheDir()这个方法可以得到这个这个目录*/
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    /*调用FileProvider的getUriForFile方法将File对象转换成一个封装过的Uri对象。
                    getUriForFile接收3个参数，第一个参数要求传入Context对象，第二个参数可以
                    是任意唯一的字符串，第三个参数则是我们刚刚创建的File对象。*/
                    imageUri = FileProvider.getUriForFile(WriteWeiBoActivity.this,
                            "com.example.cameraalbumtest.fileprovider", outputImage);
                } else {
                   /* 如果运行设备的版本低于Android7.0，就调用Uri的fromFile()方法将File对象
                     转换为Uri对象，这个Uri对象标识着这张图片的真实路径*/
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
               /* 将这个Intent的action指定为android.media.action.IMAGE_CAPTURE，再调用
                        Intent的putExtra方法指定图片的输出地址。*/
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }

        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //点击返回按钮则销毁此Activity
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            private CharSequence inputString;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                inputString = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                //根据输入的字数设置提示的颜色
                setLimitTextColor(mLimitTextView, inputString.toString());
            }
        });
        mSendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果微博内容超过字数限制
                if (mEditText.getText().toString().length() > TEXT_LIMIT) {
                    Toast.makeText(WriteWeiBoActivity.this,"文本超出限制" + TEXT_LIMIT + "个字！请做调整",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else if(mEditText.getText().length() == 0 && mPhoto.getVisibility() == View.GONE)
                {
                    //如果微博内容为空且没有图片
                    Toast.makeText(WriteWeiBoActivity.this,"微博内容为空,请输入内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    mRepostlayout.setVisibility(View.VISIBLE); //发布时的示例
                    repostContent.setText(mEditText.getText());
                    CreateStatus(mEditText.getText());
//                    Toast.makeText(mContext,"发送成功",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void CreateStatus(Editable text) {
        if(imageUri==null){
            statusesAPI.update(text.toString(), "0.0", "0.0", new RequestListener() {
                @Override
                public void onComplete(String s) {
                    Toast.makeText(WriteWeiBoActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    Toast.makeText(mContext,"发送失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Bitmap bitmap;
            try{
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            }catch (IOException e){
                bitmap = null;
            }
            if(bitmap==null){
                Toast.makeText(mContext,"图片加载失败！",Toast.LENGTH_SHORT).show();
                return;
            }
            statusesAPI.upload(text.toString(), bitmap, "0.0", "0.0", new RequestListener() {
                @Override
                public void onComplete(String s) {
                    Toast.makeText(mContext,"发送成功",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    Toast.makeText(mContext,"发送失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片显示出来 动态创建
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        mPhoto.setVisibility(View.VISIBLE);
                        final ImageView iconx = new ImageView(WriteWeiBoActivity.this);
                        iconx.setImageResource(R.drawable.ic_android_black_24dp);//删除图片的图标
                        //清除图标的布局
                        final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams
                                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        //图片的布局
                        final RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams
                                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        lp1.width = WriteWeiBoActivity.this.getWindowManager().getDefaultDisplay().getWidth() / 4;
                        //动态创建图片
                        ImageView imageView = new ImageView(WriteWeiBoActivity.this);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);

                        mPhoto.addView(imageView, lp1);
                        mPhoto.addView(iconx, lp);

                        //发送成功时的示例图片
                        repostImg.setImageBitmap(bitmap);
                        repostImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        iconx.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPhoto.removeAllViews(); //照片布局清除所有的视图
                                mPhoto.setVisibility(View.GONE);
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    //设置剩余字数提示的颜色
    public void setLimitTextColor(TextView limitTextView, String content) {
        long length = content.length();
        if (length > TEXT_LIMIT) {
            long outOfNum = length - TEXT_LIMIT;
            limitTextView.setTextColor(getResources().getColor(R.color.limittext_text_outofrange));
            limitTextView.setText("-" + outOfNum + "");
        } else if (length == TEXT_LIMIT) {
            limitTextView.setText(0 + "");
            limitTextView.setTextColor(getResources().getColor(R.color.limittext_text_warning));
        } else if (TEXT_LIMIT - length <= TEXT_REMIND) {
            limitTextView.setText(TEXT_LIMIT - length + "");
            limitTextView.setTextColor(getResources().getColor(R.color.limittext_text_warning));
        } else {
            limitTextView.setText("");
        }
    }

}
