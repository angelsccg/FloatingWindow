package com.angels.world.activity;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.angels.world.R;
import com.angels.world.activity.fragment.HomeFragment;
import com.angels.world.activity.fragment.MyFragment;
import com.angels.world.widget.MainPopupWindow;
import com.angels.library.utils.AcLogUtil;
import com.angels.library.utils.AcToastUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    /**首页*/
    private LinearLayout llTabHome;
    /**我的*/
    private LinearLayout llTabMy;
    private ImageView ivAdd;

    private MainPopupWindow pop;

    /**
     * 首页
     */
    public HomeFragment homeFragment;
    /**
     * 我的
     */
    public MyFragment myFragment;
    /**
     * 当前Fragment
     */
    private Fragment currentFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initFragment();

    }

    private void initView() {
        llTabHome = findViewById(R.id.ll_tab_home);
        llTabMy = findViewById(R.id.ll_tab_my);
        ivAdd = findViewById(R.id.iv_add);
        llTabHome.setOnClickListener(this);
        llTabMy.setOnClickListener(this);
        ivAdd.setOnClickListener(this);

        pop = new MainPopupWindow(this,new MainPopupWindow.OnPopWindowClickListener(){

            @Override
            public void onPopWindowClickListener(View view) {
                switch (view.getId()){
                    case R.id.iv_push_student:
                        AcToastUtil.showShort(view.getContext(),"学生");
                        break;
                    case R.id.iv_push_teacher:
                        AcToastUtil.showShort(view.getContext(),"老师");
                        break;
                }
            }
        });
    }

    private void initFragment() {
        homeFragment = new HomeFragment();
        switchFragment(homeFragment, "homeFragment");
        initTabButtom(llTabHome);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_tab_home://主页
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                switchFragment(homeFragment, "homeFragment");
                initTabButtom(llTabHome);
                break;
            case R.id.ll_tab_my://我的
                if (myFragment == null) {
                    myFragment = new MyFragment();
                }
                switchFragment(myFragment, "myFragment");
                initTabButtom(llTabMy);
                break;
            case R.id.iv_add://添加
                pop.show();
                break;
        }
    }

    public void switchFragment(Fragment fragment, String fragmentTag) {
        if (currentFragment == fragment || fragment == null) {
            return;
        }
        AcLogUtil.i("switchFragment:"+fragmentTag);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (currentFragment == null) {
            ft.add(R.id.fl_content, fragment, fragmentTag).commit();
        } else if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            ft.hide(currentFragment).add(R.id.fl_content, fragment, fragmentTag).commit();
        } else {
            ft.hide(currentFragment).show(fragment).commit();
        }
        currentFragment = fragment;
    }

    private void initTabButtom(LinearLayout ll){
        llTabHome.setSelected(false);
        llTabMy.setSelected(false);
        ll.setSelected(true);
    }

}
