package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.StoreClassBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class StoreClassAdapter extends HaveHeadRecyclerAdapter<StoreClassBean>{
    private StoreClassAdapterInterface storeClassAdapterInterface;

    public StoreClassAdapter(Context context, int layoutId, List<StoreClassBean> datas) {
        super(context, layoutId, datas);
    }
   public void  setStoreClassAdapterInterface(StoreClassAdapterInterface storeClassAdapterInterface){
       this.storeClassAdapterInterface = storeClassAdapterInterface;
    }
    @Override
    public void convert(RecyclerHolder holder, final StoreClassBean storeClassBean, int position) {
        holder.setText(R.id.title,storeClassBean.getTopBean().getName());
        holder.getView(R.id.title_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeClassAdapterInterface.onCLick(storeClassBean.getTopBean().getId());
            }
        });
        RecyclerView recyclerView = holder.getView(R.id.rcl);
        if(recyclerView.getAdapter()==null) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
            gridLayoutManager.setAutoMeasureEnabled(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(gridLayoutManager);
            StoreChildAdpater childAdapter = new StoreChildAdpater(context, R.layout.item_text2, storeClassBean.getChildList());
            childAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position2) {
                    storeClassAdapterInterface.onCLick(storeClassBean.getChildList().get(position2).getId());
                }
            });
            recyclerView.setAdapter(childAdapter);
        }else {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
   public interface StoreClassAdapterInterface{
        void onCLick(String id);
   }
}
