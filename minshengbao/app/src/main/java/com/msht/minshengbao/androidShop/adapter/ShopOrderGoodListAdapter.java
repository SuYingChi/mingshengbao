package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.ShopOrdersDetailActivity;
import com.msht.minshengbao.androidShop.shopBean.ShopOrderDetailBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

public class ShopOrderGoodListAdapter extends MyHaveHeadAndFootRecyclerAdapter<ShopOrderDetailBean.DatasBean.OrderInfoBean.GoodsListBean>{
   private ClickAfterSaleListener clickAfterSaleListener;
    public ShopOrderGoodListAdapter(Context context) {
        super(context, R.layout.item_orders_info_child_list);
    }

    @Override
    public void convert(RecyclerHolder holder, ShopOrderDetailBean.DatasBean.OrderInfoBean.GoodsListBean goodsListBean, final int position) {
          holder.setImage(R.id.iv,goodsListBean.getImage_url());
          holder.setText(R.id.name,goodsListBean.getGoods_name());
          if(goodsListBean.getGoods_spec()==null||goodsListBean.getGoods_spec().toString().equals("null")){
              holder.setText(R.id.desc,"");
          }else {
              holder.setText(R.id.desc,goodsListBean.getGoods_spec().toString());
          }
         LinearLayout ll_after_sale= holder.getView(R.id.ll_after_sale);
          if(goodsListBean.getRefund()==1){
              ll_after_sale.setVisibility(View.VISIBLE);
              TextView tv = holder.getView(R.id.afterSale);
              tv.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      clickAfterSaleListener.onGotoAfterSale(position);
                  }
              });
          }else {
              ll_after_sale.setVisibility(View.GONE);
          }
          holder.setText(R.id.price, StringUtil.getPriceSpannable12String(context,goodsListBean.getGoods_price(),R.style.big_money,R.style.big_money));
          holder.setText(R.id.num,"x "+goodsListBean.getGoods_num());
          holder.getConvertView().setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  clickAfterSaleListener.onGotoGoodDetail(position);
              }
          });
    }
    public  interface ClickAfterSaleListener{
        void onGotoAfterSale(int position);

        void onGotoGoodDetail(int position);
    }
   public void  setClickAfterSaleListener(ClickAfterSaleListener clickAfterSaleListener){
       this.clickAfterSaleListener = clickAfterSaleListener;
   }
}
