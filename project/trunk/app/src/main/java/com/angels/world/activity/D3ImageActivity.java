package com.angels.world.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.angels.library.utils.AcImageUtil;
import com.angels.world.R;

public class D3ImageActivity extends BaseActivity{

    // 当前显示的bitmap对象
    private static Bitmap bitmap;
    // 图片容器
    private ImageView imageView;
    // 开始按下位置
    private int startX;
    // 当前位置
    private int currentX;
    // 当前图片的编号
    private int scrNum;
    // 图片的总数
    private static int maxNum = 52;
    // 资源图片集合
    private int[] srcs = new int[] { R.drawable.p1, R.drawable.p2,
            R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6,
            R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10,
            R.drawable.p11, R.drawable.p12, R.drawable.p13, R.drawable.p14,
            R.drawable.p15, R.drawable.p16, R.drawable.p17, R.drawable.p18,
            R.drawable.p19, R.drawable.p20, R.drawable.p21, R.drawable.p22,
            R.drawable.p23, R.drawable.p24, R.drawable.p25, R.drawable.p26,
            R.drawable.p27, R.drawable.p28, R.drawable.p29, R.drawable.p30,
            R.drawable.p31, R.drawable.p32, R.drawable.p33, R.drawable.p34,
            R.drawable.p35, R.drawable.p36, R.drawable.p37, R.drawable.p38,
            R.drawable.p39, R.drawable.p40, R.drawable.p41, R.drawable.p42,
            R.drawable.p43, R.drawable.p44, R.drawable.p45, R.drawable.p46,
            R.drawable.p47, R.drawable.p48, R.drawable.p49, R.drawable.p50,
            R.drawable.p51, R.drawable.p52 };
    // 资源图片集合
    private int[] srcsHome = new int[] { R.drawable.home_1, R.drawable.home_2,
            R.drawable.home_3, R.drawable.home_4, R.drawable.home_5, R.drawable.home_6,
            R.drawable.home_7, R.drawable.home_8, R.drawable.home_9, R.drawable.home_10,
            R.drawable.home_11, R.drawable.home_12, R.drawable.home_13, R.drawable.home_14,
            R.drawable.home_15, R.drawable.home_16, R.drawable.home_17, R.drawable.home_18,
            R.drawable.home_19, R.drawable.home_20, R.drawable.home_21, R.drawable.home_22,
            R.drawable.home_23, R.drawable.home_24, R.drawable.home_25, R.drawable.home_26,
            R.drawable.home_27, R.drawable.home_28, R.drawable.home_29, R.drawable.home_30,
            R.drawable.home_31, R.drawable.home_32, R.drawable.home_33, R.drawable.home_34,
            R.drawable.home_35, R.drawable.home_36, R.drawable.home_37, R.drawable.home_38,
            R.drawable.home_39, R.drawable.home_40, R.drawable.home_41, R.drawable.home_42,
            R.drawable.home_43, R.drawable.home_44, R.drawable.home_45, R.drawable.home_46,
            R.drawable.home_47, R.drawable.home_48, R.drawable.home_49, R.drawable.home_50,
            R.drawable.home_51, R.drawable.home_52, R.drawable.home_53, R.drawable.home_54};
    /*
            R.drawable.home_55, R.drawable.home_56, R.drawable.home_57, R.drawable.home_58*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d3_image);

        imageView = (ImageView) findViewById(R.id.imageView);
        bitmap = BitmapFactory.decodeResource(getResources(),
                srcsHome[0]);
        bitmap = AcImageUtil.comp(bitmap);
        imageView.setImageBitmap(bitmap);
        // 初始化当前显示图片编号
        scrNum = 1;

        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        currentX = (int) event.getX();
                        // 判断手势滑动方向，并切换图片
                        if (currentX - startX > 5) {
                            modifySrcR();
                        } else if (currentX - startX < -5) {
                            modifySrcL();
                        }
                        // 重置起始位置
                        startX = (int) event.getX();

                        break;

                }

                return true;
            }

        });

    }

    // 向右滑动修改资源
    private void modifySrcR() {

        if (scrNum > maxNum) {
            scrNum = 1;
        }

        if (scrNum > 0) {
            bitmap = BitmapFactory.decodeResource(getResources(),
                    srcsHome[scrNum - 1]);
            bitmap = AcImageUtil.comp(bitmap);
            imageView.setImageBitmap(bitmap);
            scrNum++;
        }

    }

    // 向左滑动修改资源
    private void modifySrcL() {
        if (scrNum <= 0) {
            scrNum = maxNum;
        }

        if (scrNum <= maxNum) {
            bitmap = BitmapFactory.decodeResource(getResources(),
                    srcsHome[scrNum - 1]);
            bitmap = AcImageUtil.comp(bitmap);
            imageView.setImageBitmap(bitmap);
            scrNum--;
        }
    }


}
