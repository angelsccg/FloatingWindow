package com.angels.world.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.angels.library.utils.AcFileUtil;
import com.angels.library.utils.AcLogUtil;
import com.angels.library.utils.AcToastUtil;
import com.angels.library.widget.AcCustomTitleLayout;
import com.angels.world.R;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.io.File;
import java.util.List;

public class FileChangeActivity extends BaseActivity implements View.OnClickListener{
    int REQUESTCODE_FROM_ACTIVITY_01 = 1000;
    private Button btnSelect,btnChange;
    private TextView tvPath;
    private EditText etFileName;

    /**文件夹名*/
    private String pathStr;

    /**标题*/
    private AcCustomTitleLayout titleLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_change);

        initView();
        initTitleView();
    }

    private void initView() {
        btnSelect = findViewById(R.id.btn_select);
        tvPath = findViewById(R.id.tv_path);
        etFileName = findViewById(R.id.et_file_name);
        btnChange = findViewById(R.id.btn_change);
        btnSelect.setOnClickListener(this);
        btnChange.setOnClickListener(this);
    }
    private void initTitleView() {
        titleLayout = findViewById(R.id.rl_title);
        titleLayout.setTitle("文件管理");
        titleLayout.setIvLeft(R.drawable.ic_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select: {
                selectFile();
            }
            break;
            case R.id.btn_change: {
                changeFile();
            }
            break;
        }

    }

    /**修改文件夹下的文件名*/
    private void changeFile(){
        String fileName = etFileName.getText().toString();
        if(TextUtils.isEmpty(fileName.trim()) || pathStr == null || TextUtils.isEmpty(pathStr)){
            AcToastUtil.showShort(this,"文件路径或名字有误");
            return;
        }
        File pathFile = new File(pathStr);
        File[] files = pathFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            AcFileUtil.FixFileName(files[i].getAbsolutePath(),fileName+"_"+i);
        }
        AcToastUtil.showShort(getApplicationContext(), "修改成功" + pathStr);
    }
    /**
     * 选择文件夹
     * */
    private void selectFile(){
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
         withStartPath(String pathStr) 设置初始显示路径
         withIsGreater(boolean isGreater) 设置过滤方式,true(默认)为大于指定大小，false小于指定大小
         withFileSize(long size) 设置指定过滤文件大小，如果是500K则输入500*1024
         *
         * */
        new LFilePicker()
                .withActivity(this)
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY_01)
                .withStartPath("/storage/emulated/0/Download")//指定初始显示路径
                .withIsGreater(false)//过滤文件大小 小于指定大小的文件
                .withFileSize(500 * 1024 * 1024)//指定文件大小最大为500M
                .withChooseMode(false)
                .start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY_01) {
                //如果是文件选择模式，需要获取选择的所有文件的路径集合
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
//                List<String> list = data.getStringArrayListExtra("paths");
                AcLogUtil.i("3D-->选中了" + list.size() + "个文件");
                //如果是文件夹选择模式，需要获取选择的文件夹路径
                pathStr = data.getStringExtra("path");
                AcToastUtil.showShort(getApplicationContext(), "选中的路径为" + pathStr);
                AcLogUtil.i("3D-->选中的文件夹路径为" + pathStr);
//                File file = new File(list.get(0));
//                show3D(file.getAbsolutePath());
                tvPath.setText("文件夹路径：" + pathStr);
            }
        }
    }


}
