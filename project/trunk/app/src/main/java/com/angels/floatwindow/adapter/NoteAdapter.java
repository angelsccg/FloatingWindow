package com.angels.floatwindow.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angels.floatwindow.R;
import com.angels.floatwindow.mode.Note;

import java.util.List;

/**
 * Created by chencg on 2017/9/7.
 */

public class NoteAdapter extends XNBaseAdapter<Note> {


    public NoteAdapter(List<Note> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(View convertView, Note note, int position) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_note, null);
            holder.tvContent = convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tvContent.setText((position + 1) + ", " + note.getContent());
        return convertView;
    }

    private final class ViewHolder {
        TextView tvContent;
    }

}
