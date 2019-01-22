package com.msht.minshengbao.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.Model.InsuranceBusinessModel;
import com.msht.minshengbao.MyApplication;
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
 * @date 2019/1/17  
 */
public class InsuranceBusinessAdapter extends RecyclerView.Adapter<InsuranceBusinessAdapter.MyViewHolder>{
    private List<InsuranceBusinessModel.DataBean> dataList=null;

    private OnItemMainClickListener mListener;
    public void setOnItemClickListener(OnItemMainClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemMainClickListener {
        /**
         * hui
         * @param view
         * @param mainPosition
         * @param secondPosition
         */
        void onItemClick(View view, int mainPosition,int secondPosition);
    }
    public InsuranceBusinessAdapter(List<InsuranceBusinessModel.DataBean> data){
        this.dataList=data;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_insurance_business_main_list,viewGroup,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final int position=i;
        InsuranceBusinessModel.DataBean dataBean=dataList.get(i);
        myViewHolder.tvTitle.setText(dataBean.getType());
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getMsbApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myViewHolder.mRecyclerView.setLayoutManager(layoutManager);
        SecondAdapter secondAdapter=new SecondAdapter(dataBean.getChild());
        myViewHolder.mRecyclerView.setAdapter(secondAdapter);
        secondAdapter.setOnItemClickListener(new SecondAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int secondPosition) {
                if (mListener!=null){
                    mListener.onItemClick(view,position,secondPosition);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        if (dataList!=null){
            return dataList.size();
        }else {
            return 0;
        }

    }
    /**自定义的ViewHolder，持有每个Item的的所有界面元素 */
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        RecyclerView mRecyclerView;
        private MyViewHolder(View view){
            super(view);
            tvTitle=(TextView)view.findViewById(R.id.id_title) ;
            mRecyclerView=(RecyclerView)view.findViewById(R.id.id_child_list);
        }
    }

    /**
     * 次级列表的适配器
     */
    private static class SecondAdapter extends RecyclerView.Adapter<SecondViewHolder> {
        private DisplayImageOptions options;
        private List<InsuranceBusinessModel.DataBean.ChildBean> secondList=new ArrayList<InsuranceBusinessModel.DataBean.ChildBean>();
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        /**
         *
         * @param list
         * @param
         */
        public SecondAdapter(List<InsuranceBusinessModel.DataBean.ChildBean>  list) {
            this.secondList=list;
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

        private  OnItemClickListener listener;
        public void setOnItemClickListener( OnItemClickListener listener) {
            this.listener = listener;
        }

        public interface OnItemClickListener {
            /**
             * hui
             * @param view
             * @param secondPosition
             */
            void onItemClick(View view, int secondPosition);
        }

        @NonNull
        @Override
        public SecondViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insurance_business_child_list,parent,false);
            return new SecondViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull SecondViewHolder holder,  int position) {
            final  int pos=position;
            InsuranceBusinessModel.DataBean.ChildBean childBean=secondList.get(position);
            String amountText=childBean.getAmount()+"元起  >>";
            holder.tvTitle.setText(childBean.getTitle());
            holder.tvEnterpriseName.setText(childBean.getCompany());
            holder.tvDesc.setText(childBean.getDesc());
            holder.tvAmount.setText(amountText);
            ImageLoader.getInstance().displayImage(childBean.getImg(), holder.imageView, options, animateFirstListener);
            holder.layoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null){
                        listener.onItemClick(view,pos);
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            if (secondList!=null){
                return secondList.size();
            }else {
                return 0;
            }
        }
    }
    private static class SecondViewHolder extends RecyclerView.ViewHolder {
         TextView  tvEnterpriseName;
         TextView  tvDesc;
         TextView  tvTitle;
         TextView  tvAmount;
         ImageView imageView;
         View      layoutView;

        private SecondViewHolder(View itemView) {
            super(itemView);
            tvEnterpriseName=(TextView)itemView.findViewById(R.id.id_enterprise_name);
            tvDesc=(TextView)itemView.findViewById(R.id.id_desc) ;
            tvTitle=(TextView)itemView.findViewById(R.id.id_title);
            tvAmount=(TextView)itemView.findViewById(R.id.id_amount);
            imageView=(ImageView)itemView.findViewById(R.id.id_image);
            layoutView=itemView.findViewById(R.id.id_item_layout);

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
