package com.msht.minshengbao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Bean.HotRecommendBean;
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
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/2/26  
 */
public class HotRecommendAdapter extends RecyclerView.Adapter<HotRecommendAdapter.MyViewHolder>{
    private Context context;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
   // private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private List<HotRecommendBean.DataBean> mList=null;
    private HotRecommendBean.DataBean dataBean;
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
    public  HotRecommendAdapter(Context context, List<HotRecommendBean.DataBean> List) {
        super();
       // this.mList=List;
        this.mList=List;
        this.context=context;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hot_recomment,viewGroup,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
       // HotRecommendBean.DataBean dataBean=mList.get(i).
        String imgUrl=mList.get(i).getImg();
        String title=mList.get(i).getTitle();
        String desc=mList.get(i).getDesc();
        desc=desc.replace("\\n", "\n");
        myViewHolder.tvCategoryName.setText(title);
        myViewHolder.tvDescribe.setText(desc);
        ImageLoader.getInstance().displayImage(imgUrl, myViewHolder.categoryImg, options, animateFirstListener);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickCallBack!=null){
                    clickCallBack.onItemClick(i);
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
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView categoryImg;
        TextView tvDescribe;
        View     itemView;
        private MyViewHolder(View view){
            super(view);
            tvCategoryName =(TextView)view.findViewById(R.id.id_hot_category) ;
            categoryImg=(ImageView)view.findViewById(R.id.id_category_img);
            tvDescribe=(TextView)view.findViewById(R.id.id_hot_describe);
            itemView=view.findViewById(R.id.id_itemView);
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
