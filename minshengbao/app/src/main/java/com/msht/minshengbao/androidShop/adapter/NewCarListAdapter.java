package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopCarBean;
import com.msht.minshengbao.androidShop.shopBean.ShopStoreBean;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import org.json.JSONObject;

import java.util.List;

public class NewCarListAdapter  extends HaveHeadAndFootRecyclerAdapter<ShopCarBean>{


    public void setCarListListener(CarListListener carListListener) {
        this.carListListener = carListListener;
    }

    private CarListListener carListListener;
    public NewCarListAdapter(Context context, int layoutId, List<ShopCarBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerHolder holder, ShopCarBean shopCarBean, final int position) {
        holder.setIsRecyclable(false);
        NewCarListChildAdapter childAdapter = new NewCarListChildAdapter(context, R.layout.item_child_car_list, shopCarBean.getDatasBean().getGoodBeanList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        //自适应自身高度
        linearLayoutManager.setAutoMeasureEnabled(true);
        RecyclerView rcl = holder.getView(R.id.rcl);
        rcl.setLayoutManager(linearLayoutManager);
        childAdapter.setCarListChildListener(new NewCarListChildAdapter.CarListChildListener() {
            @Override
            public void onGoodItemCheckChange(int chilposition, boolean isCheck) {
                carListListener.onGoodItemCheckChange(chilposition,position,isCheck);
            }

            @Override
            public void onCheckStoreItemChange(boolean isCheck) {
                carListListener.onStoreCheckChange(position,isCheck);
            }

            @Override
            public void onModifyItemNum(int chilposition, int num) {
                carListListener.onModifyItemNum(chilposition,position,num);
            }

            @Override
            public void onGotoGoodDetail(String goodid) {
                carListListener.onGotoGoodDetail(goodid);
            }
        });
        childAdapter.setHead_layoutId(R.layout.item_chid_car_list_head,new ShopCarBean.DatasBean.goodBean(shopCarBean.getStoreName(),shopCarBean.getStoreId(),shopCarBean.isCheckStore(),shopCarBean.isEdit()));
        rcl.setAdapter(childAdapter);
    }
    public interface CarListListener {


        void onGoodItemCheckChange(int chilposition, int position, boolean isCheck);

        void onStoreCheckChange(int position, boolean isCheck);

        void onModifyItemNum(int chilposition, int position, int num);

        void onGotoGoodDetail(String goodid);
    }
}
