package com.msht.minshengbao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author hong
 * @date 2017/11/21
 */

public class VehicleAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public VehicleAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        this.mList=List;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.shape_ring_loading)
                .showImageForEmptyUri(R.drawable.icon_stub)
                .showImageOnFail(R.drawable.icon_stub)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                //  .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))  //设置圆形图
                .build();
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_electriom_bile, null);
            holder.storeIMG=(ImageView) convertView.findViewById(R.id.id_store_img);
            holder.cnName =(TextView) convertView.findViewById(R.id.id_store_name);
            holder.cnAddress =(TextView)convertView.findViewById(R.id.id_tv_address);
            holder.cnDistance =(TextView)convertView.findViewById(R.id.id_tv_distance) ;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String address="地址："+mList.get(position).get("address");
        String imgUrl=mList.get(position).get("imgUrl");
        holder.cnName.setText(mList.get(position).get("name"));
        holder.cnAddress.setText(address);
        holder.cnDistance.setText(mList.get(position).get("distance"));
        ImageLoader.getInstance().displayImage(imgUrl, holder.storeIMG, options, animateFirstListener);
        return convertView;
    }
    class ViewHolder {
         ImageView storeIMG;
         TextView cnName;
         TextView cnAddress;
         TextView cnDistance;

    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> DISPLAYED_IMAGES = Collections.synchronizedList(new LinkedList<String>());
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !DISPLAYED_IMAGES.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    DISPLAYED_IMAGES.add(imageUri);
                }
            }
        }
    }
}
