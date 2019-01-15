package com.msht.minshengbao.androidShop.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeClassBean;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

public class ShopImageListPagerAdapter extends PagerAdapter {


    private  OnClickImageListener onClickImageListener;
    private  int selectedPosition;
    private List<View> indicators = new ArrayList<>();//水平分页的指示器
    private List<String> datas = new ArrayList<String>();
    private Context context;
    private LinearLayout llIndicators;//水平分页的容器



    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    public List<View> getIndicators() {
        return indicators;
    }

    public ShopImageListPagerAdapter(Context context, LinearLayout llIndicators,int selectedPosition,OnClickImageListener onClickImageListener) {
        this.context = context;
        this.llIndicators = llIndicators;
        this.selectedPosition = selectedPosition;
        this.onClickImageListener = onClickImageListener;

    }

    /**
     * 计算水平分页的数量
     *
     * @return
     */
    @Override
    public int getCount() {

        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public ImageView instantiateItem(ViewGroup container, int position) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);
        GlideUtil.loadRemoteImg(context,imageView,datas.get(position));
        //初始化指示器。position == 0只初始化一次;且有多页；
        for (int i = 0;  position==0&&getCount() != 1 && i < getCount(); i++) {
            View indicator = new View(context);
            Drawable drawable = context.getResources().getDrawable(R.drawable.indicator_selector_black);
            indicator.setBackgroundDrawable(drawable);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(24, 24);
            params.setMargins(0, 0, 10, 0);
            if(selectedPosition==i){
                indicator.setSelected(true);
            }else {
                indicator.setSelected(false);
            }
            llIndicators.addView(indicator, params);
            indicators.add(indicator);

        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImageListener.onclickImage();
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeViewAt(position);
    }
   public interface  OnClickImageListener{
        void onclickImage();
   }
}
