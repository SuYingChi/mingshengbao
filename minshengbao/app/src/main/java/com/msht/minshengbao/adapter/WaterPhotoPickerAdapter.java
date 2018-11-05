package com.msht.minshengbao.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.BitmapUtil;

import java.util.ArrayList;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/10/14  
 */
public class WaterPhotoPickerAdapter extends BaseAdapter {
    private ArrayList<String> listPath;
    public WaterPhotoPickerAdapter(ArrayList<String> listPath) {
        this.listPath = listPath;
    }
    @Override
    public int getCount() {
        if (listPath.size() == 3) {
            return listPath.size();
        } else {
            return listPath.size() + 1;
        }
    }
    @Override
    public Object getItem(int position) {
        return listPath.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_gridview, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.id_malfunction_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == listPath.size()) {
            holder.image.setImageResource(R.drawable.pickphoto_default_xh);
            if (position == 3) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(listPath.get(position), 500, 500));
        }
        return convertView;
    }
    public static class ViewHolder {
        ImageView image;
    }
}
