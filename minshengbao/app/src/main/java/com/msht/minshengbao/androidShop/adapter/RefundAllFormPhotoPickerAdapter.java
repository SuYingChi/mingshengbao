package com.msht.minshengbao.androidShop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.BitmapUtil;

import java.util.List;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

public class RefundAllFormPhotoPickerAdapter extends BaseAdapter {
    private List<String> listPath;
    public RefundAllFormPhotoPickerAdapter(List<String> listPath) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == listPath.size()) {
            holder.image.setImageResource(R.drawable.icon_addpic_unfocused);
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