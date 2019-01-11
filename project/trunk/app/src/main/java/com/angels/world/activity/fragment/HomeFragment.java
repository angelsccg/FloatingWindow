package com.angels.world.activity.fragment;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.angels.world.R;
import com.angels.world.activity.AppInfoActivity;
import com.angels.world.activity.BluetoothActivity;
import com.angels.world.activity.CompassActivity;
import com.angels.world.activity.D3Activity;
import com.angels.world.activity.FileActivity;
import com.angels.world.activity.MapActivity;
import com.angels.world.activity.RulerActivity;
import com.angels.world.activity.StepActivity;
import com.angels.world.activity.VoiceActivity;
import com.angels.world.activity.VoiceXFActivity;
import com.angels.world.activity.WebviewActivity;
import com.angels.world.activity.launcher.NoteWidgetActivity;
import com.angels.world.service.LiveWallpaperService;
import com.angels.world.service.WorldService;
import com.angels.world.service.WorldProtectService;
import com.angels.library.utils.AcToastUtil;
import com.angels.library.widget.AcCustomTitleLayout;

public class HomeFragment extends Fragment implements View.OnClickListener{
    /**标题栏*/
    private AcCustomTitleLayout titleLayout;
    /**菜单*/
    private LinearLayout llPet,llNote,llRuler,llInfo,llWallpaper,llMap,llEmpty,llWeb,llCompass,llStep,llBluetooth,llVoice,llVoiceXf,ll3D,llFile,llHtml;
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
        llInfo = view.findViewById(R.id.ll_info);
        llWallpaper = view.findViewById(R.id.ll_wallpaper);
        llEmpty = view.findViewById(R.id.ll_empty);
        llMap = view.findViewById(R.id.ll_map);
        llWeb = view.findViewById(R.id.ll_web);
        llCompass = view.findViewById(R.id.ll_compass);
        llStep = view.findViewById(R.id.ll_step);
        llBluetooth = view.findViewById(R.id.ll_bluetooth);
        llVoice = view.findViewById(R.id.ll_voice);
        llVoiceXf = view.findViewById(R.id.ll_voice_xf);
        ll3D = view.findViewById(R.id.ll_3d);
        llFile = view.findViewById(R.id.ll_file);
        llHtml = view.findViewById(R.id.ll_html);
        llPet.setOnClickListener(this);
        llNote.setOnClickListener(this);
        llRuler.setOnClickListener(this);
        llEmpty.setOnClickListener(this);
        llInfo.setOnClickListener(this);
        llWallpaper.setOnClickListener(this);
        llMap.setOnClickListener(this);
        llWeb.setOnClickListener(this);
        llCompass.setOnClickListener(this);
        llStep.setOnClickListener(this);
        llBluetooth.setOnClickListener(this);
        llVoice.setOnClickListener(this);
        llVoiceXf.setOnClickListener(this);
        ll3D.setOnClickListener(this);
        llFile.setOnClickListener(this);
        llHtml.setOnClickListener(this);
    }

    private void initTitleView(View view) {
        titleLayout = view.findViewById(R.id.rl_title);
        titleLayout.setTitle("安好之家");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_pet:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
                    AcToastUtil.showShort(getContext(), "请授权！");
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
                    startActivityForResult(intent, 1001);
                } else {
                    Intent i1 = new Intent(getContext(), WorldService.class);
                    getContext().startService(i1);

                    Intent i2 = new Intent(getContext(), WorldProtectService.class);
                    getContext().startService(i2);
                }
                break;
            case R.id.ll_note: {
                Intent intent = new Intent(view.getContext(), NoteWidgetActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_ruler: {
                Intent intent = new Intent(view.getContext(), RulerActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_info: {
                Intent intent = new Intent(view.getContext(), AppInfoActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_empty:
                AcToastUtil.showShort(getContext(), "空");
                break;
            case R.id.ll_wallpaper: {
                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(getContext(), LiveWallpaperService.class));
                startActivity(intent);
                break;
            }
            case R.id.ll_map: {
                Intent intent = new Intent(view.getContext(), MapActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_web: {
                Intent intent = new Intent(view.getContext(), WebviewActivity.class);
                String url = "http://www.baidu.com";
//                String url = "http://typhoon.zjwater.gov.cn/default.aspx";
//                String url = "file:///android_asset/demo.html";
                intent.putExtra(WebviewActivity.DATA_URL, url);
                startActivity(intent);
                break;
            }
            case R.id.ll_compass: {
                Intent intent = new Intent(view.getContext(), CompassActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_step: {
                Intent intent = new Intent(view.getContext(), StepActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_bluetooth: {
                Intent intent = new Intent(view.getContext(), BluetoothActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_voice: {
                Intent intent = new Intent(view.getContext(), VoiceActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_voice_xf: {
                Intent intent = new Intent(view.getContext(), VoiceXFActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_3d:{
                Intent intent = new Intent(view.getContext(), D3Activity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_file:{
                Intent intent = new Intent(view.getContext(), FileActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_html:{
                Intent intent = new Intent(view.getContext(), WebviewActivity.class);
                String url = "file:///android_asset/html/menu_list.html";
//                String url = "http://www.baidu.com";
//                String url = "http://typhoon.zjwater.gov.cn/default.aspx";
                intent.putExtra(WebviewActivity.DATA_URL, url);
                startActivity(intent);
                break;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001){

        }
    }

}
