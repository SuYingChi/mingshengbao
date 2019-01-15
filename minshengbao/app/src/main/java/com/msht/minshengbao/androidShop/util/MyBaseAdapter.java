package com.msht.minshengbao.androidShop.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.msht.minshengbao.R;
import com.zhy.autolayout.utils.AutoUtils;



import java.util.List;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    protected Context context;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;
  /*  protected INCOnDel incOnDel;
    protected INCOnEdit incOnEdit;*/
    protected String monetary_unit;

    public MyBaseAdapter(Context context, List<T> datas, int layoutId) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
        this.monetary_unit = context.getResources().getString(R.string.monetary_unit);
    }

    protected MyBaseAdapter(Context context, int layoutId) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.monetary_unit = context.getResources().getString(R.string.monetary_unit);
    }

  /*  *//***
     * 针对地址列表
     *//*
    protected MyBaseAdapter(Context context, int layoutId, INCOnDel incOnDel, INCOnEdit incOnEdit) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.monetary_unit = context.getResources().getString(R.string.monetary_unit);
    }*/

    public List<T> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(context, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position),position);

        //新增
        AutoUtils.autoSize(holder.getConvertView());

        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t,int position);

}
