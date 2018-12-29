package com.angels.library.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angels.library.R;

import java.util.List;

/**
 * Name: CustomDialog
 * Author: ccg
 * Comment: //TODO
 * Date: 2017-08-31 11:14
 */

public class AcCustomDialog{
    public static Dialog createToastDialog(Context context,String content) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ac_dialog_toast, null);// 得到加载view
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        tvContent.setText(content);
        Dialog dialog = new Dialog(context, R.style.AcDialogCommon);// 创建自定义样式dialog
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return dialog;
    }

    public static Dialog createMessageDialog(Context context,String content,String okName, View.OnClickListener okListener) {
        return createMessageDialog(context,content,okName,null,okListener,null);
    }
    public static Dialog createMessageDialog(Context context,String content,String okName) {
        return createMessageDialog(context,content,okName,null,null,null);
    }
    public static Dialog createMessageDialog(Context context,String content) {
        return createMessageDialog(context,content,null,null,null,null);
    }
    public static Dialog createMessageDialog(Context context,String content,String okName,String cancelName, View.OnClickListener okListener, View.OnClickListener cancelListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ac_dialog_message, null);// 得到加载view
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        tvContent.setText(content);
        Dialog dialog = new Dialog(context, R.style.AcDialogCommon);// 创建自定义样式dialog
        setBtnOk(dialog,btnOk,okName,okListener);
        setBtnCancel(dialog,btnCancel,cancelName,cancelListener);
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return dialog;
    }

    public static Dialog createListButtonDialog(Context context, List<String> contents, View.OnClickListener listener) {
        Dialog dialog = new Dialog(context, R.style.AcDialogCommon);// 创建自定义样式dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ac_dialog_list_button, null);// 得到加载view
        LinearLayout llContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        for (int i = 0; i < contents.size(); i++) {
            if(i != 0){
                View lineView = new View(context);
                lineView.setBackgroundResource(R.color.grey_line);
                lineView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,1));
                llContainer.addView(lineView);
            }
            String txt = contents.get(i);
            TextView textView = (TextView) inflater.inflate(R.layout.ac_part_textview, null);// 得到加载view
            textView.setText(txt);
            textView.setOnClickListener(listener);
            llContainer.addView(textView);
            textView.setId(i);
            textView.setTag(dialog);
        }
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return dialog;
    }

    public static Dialog createEditDialog(Context context,String hint,String okName,String cancelName, View.OnClickListener okListener, View.OnClickListener cancelListener) {
        return createEditDialog(context,hint,okName,cancelName,okListener,cancelListener,-1);
    }
    public static Dialog createEditDialog(Context context,String hint,String okName,String cancelName, View.OnClickListener okListener, View.OnClickListener cancelListener,int inputType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ac_dialog_edit, null);// 得到加载view
        EditText editText = (EditText) view.findViewById(R.id.et_txt);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        editText.setHint(hint);
        if(inputType != -1){
            editText.setInputType(inputType);
        }
        btnOk.setTag(R.id.et_txt,editText);
        btnCancel.setTag(R.id.et_txt,editText);
        Dialog dialog = new Dialog(context, R.style.AcDialogCommon);// 创建自定义样式dialog
        setBtnOk(dialog,btnOk,okName,okListener);
        setBtnCancel(dialog,btnCancel,cancelName,cancelListener);
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return dialog;
    }


        /**
     * 设置对话框内容
     * */
    public static void setBtnOk(final Dialog dialog, Button btnOk, String okName, View.OnClickListener listener){
        if(okName == null){
            if(btnOk != null){
                btnOk.setVisibility(View.GONE);
            }
            return;
        }
        btnOk.setTag(dialog);
        btnOk.setVisibility(View.VISIBLE);
        btnOk.setText(okName);
        if(listener != null){
            btnOk.setOnClickListener(listener);
        }else{
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
    /**
     * 设置对话框内容
     * */
    public static void setBtnCancel(final Dialog dialog, Button btnCancel, String cancelName, View.OnClickListener listener){
        if(cancelName == null){
            return;
        }
        btnCancel.setTag(dialog);
        btnCancel.setVisibility(View.VISIBLE);
        btnCancel.setText(cancelName);
        if(listener != null){
            btnCancel.setOnClickListener(listener);
        }else{
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
}
