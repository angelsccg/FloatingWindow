package com.angels.floatwindow.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class XNBaseAdapter<T> extends BaseAdapter {

	protected List<T> data;
	
	protected Context context;
	
	public XNBaseAdapter(List<T> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public T getItem(int position) {
		return data.get(position) ;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getView(convertView, data.get(position),position);
	}
	
	public abstract View getView(View convertView,T t,int position);

	public void setData(List<T> data) {
		this.data = data;
		this.notifyDataSetChanged();
	}
}
