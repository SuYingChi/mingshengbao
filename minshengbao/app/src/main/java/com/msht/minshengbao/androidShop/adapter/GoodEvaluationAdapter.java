package com.msht.minshengbao.androidShop.adapter;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.EvaluationBean;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;


public class GoodEvaluationAdapter extends MyHaveHeadViewRecyclerAdapter<EvaluationBean.DatasBean.GoodsEvalListBean> {


    private int imagePadding = 0 ;
    private GoodEveluateListener listener;

    public GoodEvaluationAdapter(Context context,GoodEveluateListener listener) {
        super(context, R.layout.item_evaluation);
        imagePadding =(int)context.getResources().getDimension(R.dimen.dp4);
        this.listener = listener;
    }

    @Override
    public void convert(RecyclerHolder holder, final EvaluationBean.DatasBean.GoodsEvalListBean item, final int position) {
        ImageView ivPic = holder.getView(R.id.head);
        GlideUtil.loadRemoteCircleImg(context,ivPic,item.getMember_avatar());
        TextView name = holder.getView(R.id.name);
        name.setText(item.getGeval_frommembername());
        TextView time = holder.getView(R.id.time);
        time.setText(item.getGeval_addtime_date());
        LinearLayout ll = holder.getView(R.id.ll_star);
        ll.removeAllViews();
        int imageCount = Integer.valueOf(item.getGeval_scores());
        LogUtils.e("imageCount"+imageCount);
        for (int i = 0; i < imageCount; i++) {
            ImageView mImageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DimenUtil.dip2px(50), DimenUtil.dip2px(50));
            params.rightMargin = 10;
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setLayoutParams(params);
            mImageView.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
            mImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.shop_star));
            ll.addView(mImageView);
        }
        TextView content  = holder.getView(R.id.content);
        content.setText(item.getGeval_content());
        RecyclerView rcl1 = holder.getView(R.id.rcl1);
        if (rcl1.getAdapter() == null) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
            rcl1.setLayoutManager(gridLayoutManager);
            gridLayoutManager.setAutoMeasureEnabled(true);
            rcl1.setNestedScrollingEnabled(false);
            GoodEvaluationChildAdapter ad = new  GoodEvaluationChildAdapter(context);
            ad.setDatas(item.getGeval_image_240());
            ad.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int childposition) {
                    listener.onClickImage(false,position, childposition);
                }
            });
            rcl1.setAdapter(ad);
        } else if (rcl1.getAdapter() instanceof GoodEvaluationChildAdapter) {
            GoodEvaluationChildAdapter ad = (GoodEvaluationChildAdapter) rcl1.getAdapter();
            ad.notifyDataSetChanged();
        }
        if(!item.getGeval_addtime_again().equals("0")) {
            holder.setText(R.id.add_evelate, item.getGeval_addtime_again_date() + "追加评价");
        }
            holder.setText(R.id.addContent, item.getGeval_content_again());
            RecyclerView rcl2 = holder.getView(R.id.rcl2);
            if (rcl2.getAdapter() == null) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
                rcl2.setLayoutManager(gridLayoutManager);
                gridLayoutManager.setAutoMeasureEnabled(true);
                rcl2.setNestedScrollingEnabled(false);
                GoodEvaluationChildAdapter ad = new GoodEvaluationChildAdapter(context);
                ad.setDatas(item.getGeval_image_again_240());
                ad.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int childposition) {
                        listener.onClickImage(true, position, childposition);
                    }
                });
                rcl2.setAdapter(ad);
            } else if (rcl2.getAdapter() instanceof GoodEvaluationChildAdapter) {
                GoodEvaluationChildAdapter ad = (GoodEvaluationChildAdapter) rcl2.getAdapter();
                ad.notifyDataSetChanged();
            }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
    public interface GoodEveluateListener{
        void onClickImage(boolean isAddEveluate, int position, int childposition);
    }
}
