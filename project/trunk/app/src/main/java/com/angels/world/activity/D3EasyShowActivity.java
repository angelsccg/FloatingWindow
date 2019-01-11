package com.angels.world.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.angels.library.utils.AcLogUtil;
import com.angels.library.utils.AcPathUtil;
import com.angels.library.utils.AcToastUtil;
import com.angels.world.R;
import com.dyman.easyshow3d.ModelFactory;
import com.dyman.easyshow3d.bean.ModelObject;
import com.dyman.easyshow3d.imp.ModelLoaderListener;
import com.dyman.easyshow3d.view.ShowModelView;
import com.leon.lfilepickerlibrary.LFilePicker;

import java.io.File;
import java.util.List;

public class D3EasyShowActivity extends BaseActivity implements View.OnClickListener {

    private ShowModelView showModelView;
    int REQUESTCODE_FROM_ACTIVITY = 1000;
    ModelLoaderListener modelLoaderListener = new ModelLoaderListener() {
        @Override
        public void loadedUpdate(float progress) {
            AcLogUtil.i("3D-->模型解析进度： " + progress);
        }

        @Override
        public void loadedFinish(ModelObject modelObject) {
            if (modelObject != null) {
                //  解析完成，显示模型
                showModelView.setModelObject(modelObject);
            }
        }

        @Override
        public void loaderCancel() {
        }

        @Override
        public void loaderFailure() {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_show_3d);

        Button btn = findViewById(R.id.btn_select);
        btn.setOnClickListener(this);

        showModelView = findViewById(R.id.showModelView);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select: {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                //intent.setType(“image/*”);//选择图片
//                //intent.setType(“audio/*”); //选择音频
//                //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
//                //intent.setType(“video/*;image/*”);//同时选择视频和图片
//                intent.setType("*/*");//无类型限制
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
////                startActivityForResult(intent, 1);
//                startActivityForResult(Intent.createChooser(intent, "选择文件"), 1);
                select();
            }
            break;
            case 1: {

            }
            break;
        }
    }

    private void select(){
        /**
         * withActivity(Activity activity) 绑定Activity
         withFragment(Fragment fragment) 绑定Fragment
         withSupportFragment(Fragment supportFragment) 绑定V4包Fragment
         withRequestCode(int requestCode) 设置请求码
         withTitle(String title) 设置标题文字
         withTitleColor(String color) 设置标题文字颜色
         withBackgroundColor(String color) 设置标题背景颜色
         withIconStyle(int style) 设置列表图标样式
         withBackIcon(int backStyle) 设置返回图标样式
         withFileFilter(String[] arrs) 设置文件类型过滤器
         withMutilyMode(boolean isMutily) 设置多选或单选模式
         withAddText(String text) 设置多选模式选中文字
         withNotFoundBooks(String text) 设置没有选中文件时的提示信息
         withMaxNum(int num)           设置最大可选文件数量
         withChooseMode(boolean chooseMode) 设置文件夹选择模式,true(默认)为选择文件，false为选择文件夹
         withStartPath(String path) 设置初始显示路径
         withIsGreater(boolean isGreater) 设置过滤方式,true(默认)为大于指定大小，false小于指定大小
         withFileSize(long size) 设置指定过滤文件大小，如果是500K则输入500*1024
         *
         * */
        new LFilePicker()
                .withActivity(this)
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                .withStartPath("/storage/emulated/0/Download")//指定初始显示路径
                .withIsGreater(false)//过滤文件大小 小于指定大小的文件
                .withFileSize(500 * 1024 * 1024)//指定文件大小最大为500K
                .withChooseMode(false)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                //如果是文件选择模式，需要获取选择的所有文件的路径集合
                //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                List<String> list = data.getStringArrayListExtra("paths");
                AcToastUtil.showShort(getApplicationContext(), "选中了" + list.size() + "个文件");
                //如果是文件夹选择模式，需要获取选择的文件夹路径
                String path = data.getStringExtra("path");
                AcToastUtil.showShort(getApplicationContext(), "选中的路径为" + path);
                AcLogUtil.i("3D-->选中的文件路径为" + list.get(0));
                AcLogUtil.i("3D-->选中的文件夹路径为" + path);
//                File file = new File(list.get(0));
//                show3D(file.getAbsolutePath());
                show3D(list.get(0));
            }
        }
    }

    private void show3D(String filePath) {
        //filePath为模型的文件路径，自动区分Obj、Stl、3ds等格式进行解析
        ModelFactory.decodeFile(D3EasyShowActivity.this, filePath, modelLoaderListener);
    }

}
