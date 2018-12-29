package com.angels.world.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.angels.library.utils.AcAppInfoUtil;
import com.angels.world.R;

public class AppInfoActivity extends BaseActivity implements View.OnClickListener,View.OnLongClickListener{

    private EditText etPackage;
    private Button btnOk;
    private TextView tvSign;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);

        initView();


    }

    private void initView() {
        etPackage = findViewById(R.id.et_package);
        tvSign = findViewById(R.id.tv_sign);
        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        tvSign.setTextIsSelectable(true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
                String packageName = etPackage.getText().toString();
                if(!TextUtils.isEmpty(packageName)){
                    String sign1 = AcAppInfoUtil.getSingInfo(getApplicationContext(), packageName, AcAppInfoUtil.SHA1);
                    String sign2 = AcAppInfoUtil.getSign(getApplicationContext(), packageName);
                    tvSign.setText(
                            "SHA1:"+sign1
                            +"\n 签名:" + sign2
                    );
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_sign:
//                ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
//                cmb.setText(content.trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
//                cm.getText();//获取粘贴信息
//                break;
        }
        return false;
    }
}
