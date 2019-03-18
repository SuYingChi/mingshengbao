package com.msht.minshengbao.androidShop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopCarBean;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

import java.util.List;

public class NewCarListChildAdapter extends HaveHeadAndFootRecyclerAdapter<ShopCarBean.DatasBean.goodBean>{

    private CarListChildListener carListChildListener;

    public NewCarListChildAdapter(Context context, int layoutId, List<ShopCarBean.DatasBean.goodBean> datas) {
        super(context, layoutId, datas);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void convert( RecyclerHolder holder, final ShopCarBean.DatasBean.goodBean datasBean, final int position) {
          if(holder.getItemViewType()==Integer.MIN_VALUE){
              holder.setText(R.id.store,datasBean.getStoreName());
              CheckBox cbSelectStore = holder.getView(R.id.select_store);
              cbSelectStore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                  @Override
                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                      carListChildListener.onCheckStoreItemChange(isChecked);
                  }
              });
              cbSelectStore.setChecked(datasBean.isCheckStore());
          }else {
              holder.setText(R.id.name,datasBean.getGoodName());
              TextView tvJingle =  holder.getView(R.id.jingle);
              if(TextUtils.isEmpty(datasBean.getGoodGuige())){
                  tvJingle.setVisibility(View.GONE);
              }else {
                  tvJingle.setVisibility(View.VISIBLE);
                  tvJingle.setText(datasBean.getGoodGuige());
              }
                 String goodsStorage=datasBean.getGoodStorageNum();
              if (TextUtils.isEmpty(goodsStorage)) {
                  goodsStorage = "商品已下架或不支持购买";
              }
              holder.setText(R.id.remain_num,String.format("库存量：%s件", goodsStorage));
              holder.setText(R.id.price, StringUtil.getPriceSpannable12String(context, datasBean.getGoodPrice(), R.style.small_money, R.style.small_money));
              if(datasBean.isEdite()){
                  holder.getView(R.id.price).setVisibility(View.INVISIBLE);
              }else {
                  holder.getView(R.id.price).setVisibility(View.VISIBLE);
              }
              if("1".equals(datasBean.getIsPickupSelf())){
                  holder.getView(R.id.ispickupself).setVisibility(View.VISIBLE);
              }else {
                  holder.getView(R.id.ispickupself).setVisibility(View.INVISIBLE);
              }
               final CheckBox cbSelect = holder.getView(R.id.select);
              cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                  @Override
                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                      carListChildListener.onGoodItemCheckChange(position-1,isChecked);
                  }
              });
              cbSelect.setChecked(datasBean.isSelected());
              final TextView tvNum = holder.getView(R.id.number);
              tvNum.setText(String.format("%d", datasBean.getGoodNum()));
              holder.setImage(R.id.iv,datasBean.getGoodImage());
              LinearLayout tvReduce = holder.getView(R.id.ll_reduce);
              tvReduce.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      int goodsStorageNum;
                      int num = datasBean.getGoodNum();
                       String goodsStorage = datasBean.getGoodStorageNum();
                      if (TextUtils.isEmpty(goodsStorage)) {
                          goodsStorageNum = -1;
                      } else {
                          goodsStorageNum = Integer.valueOf(goodsStorage);
                      }
                      if (goodsStorageNum == -1) {
                          PopUtil.showComfirmDialog(context, "提示", "商品已经下架", null, "好", null, null, true);
                      } else if (num ==1) {
                          PopUtil.showComfirmDialog(context, "提示", "请选择至少一件该商品", null, "好", null, null, true);
                      } else {
                          tvNum.setText(String.valueOf(num -1+ ""));
                              carListChildListener.onModifyItemNum(position-1,num-1);
                          }
                      }
              });
              LinearLayout tvPlus = holder.getView(R.id.ll_plus);
              tvPlus.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      int goodsStorageNum;
                      int num = datasBean.getGoodNum();
                      String goodsStorage = datasBean.getGoodStorageNum();
                      if (TextUtils.isEmpty(goodsStorage)) {
                          goodsStorageNum = -1;
                      } else {
                          goodsStorageNum = Integer.valueOf(goodsStorage);
                      }
                      if (goodsStorageNum == -1) {
                          PopUtil.showComfirmDialog(context, "提示", "商品已经下架", null, "好", null, null, true);
                      } else if (num >= goodsStorageNum) {
                          PopUtil.showComfirmDialog(context, "提示", "库存量不足", null, "好", null, null, true);
                      }else {
                          tvNum.setText(String.valueOf(num +1+ ""));
                          carListChildListener.onModifyItemNum(position-1,num+1);
                      }
                  }
              });
              holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      carListChildListener.onGotoGoodDetail(datasBean.getGoodId());
                  }
              });
          }
    }

    public void setCarListChildListener(CarListChildListener carListChildListener) {
        this.carListChildListener = carListChildListener;
    }
    public interface CarListChildListener {

        void onGoodItemCheckChange(int position,boolean isCheck);

        void onCheckStoreItemChange(boolean isCheck);

        void onModifyItemNum(int position,int num);

        void onGotoGoodDetail(String goodid);
    }
}
