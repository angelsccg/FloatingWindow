package com.angels.world.activity.fragment;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.angels.world.R;
import com.angels.world.activity.MapActivity;
import com.angels.world.activity.RulerActivity;
import com.angels.world.activity.WebviewActivity;
import com.angels.world.activity.launcher.NoteWidgetActivity;
import com.angels.world.bd.NoteDBManager;
import com.angels.world.service.ServiceFloat;
import com.angels.world.service.ServiceFloatProtect;
import com.angels.library.utils.AcAppUtil;
import com.angels.library.utils.AcToastUtil;
import com.angels.library.widget.AcCustomTitleLayout;

public class HomeFragment extends Fragment implements View.OnClickListener{
    /**标题栏*/
    private AcCustomTitleLayout titleLayout;
    /**菜单*/
    private LinearLayout llPet,llNote,llRuler,llEmpty;
    private  View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != view){
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        }else {
            view = inflater.inflate(R.layout.fragment_home,null);
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        initTitleView(view);
        llPet = view.findViewById(R.id.ll_pet);
        llNote = view.findViewById(R.id.ll_note);
        llRuler = view.findViewById(R.id.ll_ruler);
        llEmpty = view.findViewById(R.id.ll_empty);
        llPet.setOnClickListener(this);
        llNote.setOnClickListener(this);
        llRuler.setOnClickListener(this);
        llEmpty.setOnClickListener(this);


        Button btn01 = view.findViewById(R.id.btn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        Button btn02 = view.findViewById(R.id.btn02);
        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WebviewActivity.class);
                String url = "file:///android_asset/study/study02_rem.html";
//                String url = "http://typhoon.zjwater.gov.cn/default.aspx";
                intent.putExtra(WebviewActivity.DATA_URL,url);
                startActivity(intent);
            }
        });

    }

    private void initTitleView(View view) {
        titleLayout = view.findViewById(R.id.rl_title);
        titleLayout.setTitle("安好之家");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_pet:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
                    AcToastUtil.showShort( getContext(),"请授权！");
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
                    startActivityForResult(intent, 1001);
                }else {
                    Intent i1 = new Intent(getContext(), ServiceFloat.class);
                    getContext().startService(i1);

                    Intent i2 = new Intent(getContext(), ServiceFloatProtect.class);
                    getContext().startService(i2);
                }
                break;
            case R.id.ll_note:
                Intent intent = new Intent(view.getContext(), NoteWidgetActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_ruler:
                Intent intent2 = new Intent(view.getContext(), RulerActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll_empty:
                break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001){

        }
    }
}
