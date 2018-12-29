package com.msht.minshengbao.androidShop.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeGoods_3Bean;

import java.util.ArrayList;
import java.util.List;

public class ShopMaiBianQuanQiuPagerAdapter extends PagerAdapter {

    private MyHaveHeadViewRecyclerAdapter.OnItemClickListener listener;
    private List<View> indicators = new ArrayList<>();//水平分页的指示器
    private List<ShopHomeGoods_3Bean.Goods3Bean.ItemBean> datas = new ArrayList<>();//RecyclerView数据集合
    private Context context;
    private LinearLayout llIndicators;//水平分页的容器
    private int navCount = 3;

    public void setDatas(List<ShopHomeGoods_3Bean.Goods3Bean.ItemBean> datas) {
        this.datas = datas;
    }

    public List<View> getIndicators() {
        return indicators;
    }

    public ShopMaiBianQuanQiuPagerAdapter(Context context, LinearLayout llIndicators, MyHaveHeadViewRecyclerAdapter.OnItemClickListener listener) {
        this.context = context;
        this.llIndicators = llIndicators;
        this.listener = listener;
    }

    /**
     * 计算水平分页的数量
     *
     * @return
     */
    @Override
    public int getCount() {
        int count = datas.size() % navCount;
        int divide = datas.size() / navCount;
        return count == 0 ? divide : divide + 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public RecyclerView instantiateItem(ViewGroup container, int position) {
        RecyclerView rcl = new RecyclerView(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false) {
            //与Scrollerview嵌套滑动不流畅时禁止recycview的滚动，
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setAutoMeasureEnabled(true);
        rcl.setLayoutManager(linearLayoutManager);
        rcl.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        MaiBianQuanQiuAdapter adapter = new MaiBianQuanQiuAdapter(context);
        List<ShopHomeGoods_3Bean.Goods3Bean.ItemBean> list = new ArrayList<ShopHomeGoods_3Bean.Goods3Bean.ItemBean>();
        //每页最多显示10个，小于数据集总数，且小于下一页开始的位置索引
        for (int i = position * navCount; i < (position + 1) * navCount && i < datas.size(); i++) {
            list.add(datas.get(i));
        }
        adapter.setDatas(list);
        adapter.setOnItemClickListener(listener);
        rcl.setAdapter(adapter);
        //初始化指示器。position == 0只初始化一次;且有多页；
        for (int i = 0; position == 0 && getCount() != 1 && i < getCount(); i++) {
            View indicator = new View(context);
            Drawable drawable = context.getResources().getDrawable(R.drawable.indicator_selector_black);
            indicator.setBackgroundDrawable(drawable);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(24, 24);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginEnd(10);
            }
            llIndicators.addView(indicator, params);
            indicators.add(indicator);
            indicators.get(0).setSelected(true);
        }

        container.addView(rcl);
        return rcl;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeViewAt(position);
    }

}
