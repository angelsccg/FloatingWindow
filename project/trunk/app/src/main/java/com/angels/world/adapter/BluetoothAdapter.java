package com.angels.world.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angels.world.R;

import java.util.List;

/**
 * Created by chencg on 2017/9/7.
 */

public class BluetoothAdapter extends XNBaseAdapter<BluetoothDevice> {

    public BluetoothAdapter(List<BluetoothDevice> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(View convertView, BluetoothDevice device, final int position) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bluetooth, null);
            holder.tvName = (TextView)convertView.findViewById(R.id.tv_name);
            holder.tvMac = (TextView)convertView.findViewById(R.id.tv_mac);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tvName.setText(device.getName());
        holder.tvMac.setText(device.getAddress());
        return convertView;
    }

    private final class ViewHolder {
        TextView tvName;
        TextView tvMac;
    }

}
