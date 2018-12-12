package com.msht.minshengbao.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
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
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/11/15  
 */
public class HeadLineAdapter extends RecyclerView.Adapter<HeadLineAdapter.MyViewHolder>{
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }
    public interface ItemClickCallBack{
        /**
         * item回调
         * @param pos 下标
         */
        void onItemClick(int pos);
    }
    private ItemClickCallBack clickCallBack;
    public HeadLineAdapter(ArrayList<HashMap<String, String>> headLineList) {
        this.mList=headLineList;
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

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_head_line,viewGroup,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        final  int thisPosition=position;
        String source="来源:"+mList.get(position).get("source");
        myViewHolder.tvTitle.setText(mList.get(position).get("title"));
        myViewHolder.tvSource.setText(source);
        myViewHolder.tvPublicTime.setText(mList.get(position).get("publishTime"));
        String picUrl=mList.get(position).get("pic");
        if (TextUtils.isEmpty(picUrl)||picUrl.equals(ConstantUtil.NULL_VALUE)){
            myViewHolder.imageSource.setVisibility(View.GONE);
        }else {
            myViewHolder.imageSource.setVisibility(View.VISIBLE);
        }
        ImageLoader.getInstance().displayImage(picUrl, myViewHolder.imageSource, options, animateFirstListener);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickCallBack != null){
                    clickCallBack.onItemClick(thisPosition);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        if (mList!=null){
            return mList.size();
        }else {
            return 0;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPublicTime;
        TextView tvSource;
        TextView tvTitle;
        ImageView imageSource;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            tvPublicTime =(TextView) view.findViewById(R.id.id_time_text);
            tvSource =(TextView) view.findViewById(R.id.id_origin_text);
            tvTitle=(TextView)view.findViewById(R.id.id_title);
            imageSource=(ImageView)view.findViewById(R.id.id_source_img);
            itemView=view.findViewById(R.id.item_layout);
        }
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
