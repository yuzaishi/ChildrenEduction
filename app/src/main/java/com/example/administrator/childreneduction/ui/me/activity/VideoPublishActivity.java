package com.example.administrator.childreneduction.ui.me.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.childreneduction.R;
import com.example.administrator.childreneduction.bmob.VedioTable;
import com.example.administrator.childreneduction.model.Content;
import com.example.administrator.childreneduction.model.LoginInfo;
import com.example.administrator.childreneduction.ui.me.iview.VideoPublishUI;
import com.example.administrator.childreneduction.ui.me.presenter.VideoPublishPresenter;
import com.example.administrator.childreneduction.utils.SharePrefernceUtils;
import com.example.baselibrary.base.BaseActivity;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/5/20.
 */

public class VideoPublishActivity extends BaseActivity implements VideoPublishUI{

    private TextView mTvActivityVideoBack;
    private TextView mTvActivityVideoPub;
    private EditText mEdtActivityVideoTilte;
    private EditText mEdtActivityVideoContebt;
    private ImageView mImgActVideoRecode;

    private VideoPublishPresenter mPresenter;
    private Gson mGson;
    private SharePrefernceUtils mPrefernceUtils;
    private String mVideoPath;

    @Override
    public int setLayout() {
        return R.layout.activity_videopublish;
    }

    @Override
    public void initView() {
        mTvActivityVideoBack = (TextView) findViewById(R.id.tv_activity_video_back);
        mTvActivityVideoPub = (TextView) findViewById(R.id.tv_activity_video_pub);
        mEdtActivityVideoTilte = (EditText) findViewById(R.id.edt_activity_video_tilte);
        mEdtActivityVideoContebt = (EditText) findViewById(R.id.edt_activity_video_contebt);
        mImgActVideoRecode = (ImageView) findViewById(R.id.img_act_video_recode);

        initData();
        setListener();
    }

    private void initData(){
        mPrefernceUtils=new SharePrefernceUtils(getApplicationContext(),Content.SP_NAME);
        mPresenter=new VideoPublishPresenter(this);
        mGson=new Gson();
    }
    /**
     * 设置监听
     */
    private void setListener(){
        //发布
        mTvActivityVideoPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mPrefernceUtils.getString(Content.SP_NAME);
                LoginInfo loginInfo = mGson.fromJson(string, LoginInfo.class);
                String title = mEdtActivityVideoTilte.getText().toString().trim();
                String content = mEdtActivityVideoContebt.getText().toString().trim();
                if (title.length()==0 || content.length()==0){
                    Toast.makeText(VideoPublishActivity.this,"请添加标题或内容！",Toast.LENGTH_LONG);
                    return;
                }
                if (mVideoPath==null || mVideoPath.length()==0){
                    Toast.makeText(VideoPublishActivity.this,"请添加视频！",Toast.LENGTH_LONG);
                    return;
                }
                VedioTable vedioTable=new VedioTable();
                vedioTable.setU_id(loginInfo.getId());
                vedioTable.setU_name(loginInfo.getName());
                vedioTable.setV_coll("0");
                vedioTable.setV_comm("0");
                vedioTable.setV_title(title);
                vedioTable.setV_content(content);
                vedioTable.setV_url(mVideoPath);

                //发布
                mPresenter.pub_video(VideoPublishActivity.this,vedioTable);
            }
      });
        //视频
        mImgActVideoRecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoPublishActivity.this, VideoActivity.class);
                startActivityForResult(intent,Content.REQUEST_VIDEO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== Content.REQUEST_VIDEO && resultCode== Content.RESULT_VIDEO){
            String path = data.getStringExtra("PATH");
            System.out.println("path+fragment"+path);
            if (path!=null){
                mPresenter.upload_video(VideoPublishActivity.this,path);
            }
        }
    }

    @Override
    public void upload_video_ok(String link) {
        if (link!=null){
            mVideoPath=link;
        }
    }

    @Override
    public void upload_video_fail() {

    }

    @Override
    public void pub_video_ok() {
        Toast.makeText(VideoPublishActivity.this,"发布成功！",Toast.LENGTH_LONG);
        finish();
    }

    @Override
    public void pub_video_fail() {
        Toast.makeText(VideoPublishActivity.this,"发布失败！",Toast.LENGTH_LONG);
    }
}
