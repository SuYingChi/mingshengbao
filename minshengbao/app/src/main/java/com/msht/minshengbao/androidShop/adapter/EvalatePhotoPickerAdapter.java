package com.msht.minshengbao.androidShop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.BitmapUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;

import java.util.List;

class EvalatePhotoPickerAdapter extends BaseAdapter {
    private List<String> listPath;
    public EvalatePhotoPickerAdapter(List<String> listPath) {
        this.listPath = listPath;
    }
    @Override
    public int getCount() {
        if (listPath.size() == 5) {
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
        LogUtils.e("EvalatePhotoPickerAdapter listPath="+listPath.toString());
        RefundAllFormPhotoPickerAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic, parent, false);
            holder = new RefundAllFormPhotoPickerAdapter.ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (RefundAllFormPhotoPickerAdapter.ViewHolder) convertView.getTag();
        }

        if (position == listPath.size()) {
            holder.image.setImageResource(R.drawable.icon_addpic_unfocused);
            if (position == 5) {
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
