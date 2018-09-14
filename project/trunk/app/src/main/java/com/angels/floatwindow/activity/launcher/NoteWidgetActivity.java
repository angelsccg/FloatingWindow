package com.angels.floatwindow.activity.launcher;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.angels.floatwindow.R;
import com.angels.floatwindow.activity.BaseActivity;
import com.angels.floatwindow.adapter.NoteAdapter;
import com.angels.floatwindow.bd.NoteDBManager;
import com.angels.floatwindow.mode.Note;
import com.angels.floatwindow.utils.AppUtil;
import com.angels.library.utils.AcAppUtil;
import com.angels.library.widget.AcCustomTitleLayout;

import java.util.ArrayList;
import java.util.List;

public class NoteWidgetActivity extends BaseActivity implements View.OnClickListener{

    /**标题栏*/
    private AcCustomTitleLayout titleLayout;
    private EditText etContent;
    /**是否可编辑*/
    private boolean isEdit = false;

    private Note note;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_widget);

        initView();
        initTitleView();
    }

    private void initView() {

        etContent = findViewById(R.id.et_content);
        etContent.setFocusable(false);
        etContent.setOnClickListener(this);

        note = NoteDBManager.queryNewNote();
        if(note.getId() == null){
            NoteDBManager.addNote(note);
            note = NoteDBManager.queryNewNote();
        }else {
            etContent.setText(note.getContent());
        }
    }

    private void initTitleView() {
        titleLayout = findViewById(R.id.rl_title);
        titleLayout.setTitle("备忘录");
        titleLayout.setIvLeft(R.drawable.ic_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleLayout.setTvRight("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEdit){
                    isEdit = true;
                    etContent.setFocusable(true);
                    titleLayout.setTvRight("完成",null);
                    etContent.setCursorVisible(true);
                    etContent.setFocusableInTouchMode(true);
                    etContent.requestFocus();
                    AcAppUtil.openKeybord(etContent,view.getContext());
                }else {
                    isEdit = false;
                    etContent.setFocusable(false);
                    titleLayout.setTvRight("编辑",null);

                    String content = etContent.getText().toString();
                    if(!TextUtils.isEmpty(content)){
                        note.setContent(content);
                        NoteDBManager.updateNote(note);
//                    etContent.setText("");
                        AcAppUtil.closeKeybord(etContent);

                        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                        sendBroadcast(intent);

                        NoteWidgetActivity.this.finish();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_content:
                if(!isEdit){
                    isEdit = true;
                    etContent.setFocusable(true);
                    titleLayout.setTvRight("完成",null);
                    etContent.setCursorVisible(true);
                    etContent.setFocusableInTouchMode(true);
                    etContent.requestFocus();
                    AcAppUtil.openKeybord(etContent,this);
                }
                break;
        }
    }
}
