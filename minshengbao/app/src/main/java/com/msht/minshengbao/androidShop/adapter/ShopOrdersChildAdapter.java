package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.MyExtendOrderGoodsBean;
import com.msht.minshengbao.androidShop.shopBean.OrdersItem;
import com.msht.minshengbao.androidShop.shopBean.ZengpingBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ShopOrdersChildAdapter extends MyHaveHeadAndFootRecyclerAdapter<MyExtendOrderGoodsBean> {


    private String payAmount;
    private String addTime;
    private String paySn;
    private List<ZengpingBean> zengpings;

    public void setListener(OrderListener listener) {
        this.listener = listener;
    }

    private OrderListener listener;

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public void setPaySn(String paySn) {
        this.paySn = paySn;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    private String shipping_fee;
    private int totalNum = 0;

    private String storeName;

    private String state_desc;
    private List<TextView> btns;


    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public void setState_desc(String state_desc) {
        this.state_desc = state_desc;
    }

    public void setBtns(List<TextView> btns) {
        this.btns = btns;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public ShopOrdersChildAdapter(Context context, String storeName, String payAmount, String addTime, String paySn, String shipping_fee, String state_desc, List<TextView> btns, int totalNum, List<ZengpingBean> zengpings) {
        super(context, R.layout.item_orders_child_list);

        this.storeName = storeName;
        this.payAmount = payAmount;
        this.addTime = addTime;
        this.paySn = paySn;
        this.shipping_fee = shipping_fee;
        this.state_desc = state_desc;
        this.btns = btns;
        this.totalNum = totalNum;
        this.zengpings = zengpings;
    }

    @Override
    public void convert(RecyclerHolder holder, MyExtendOrderGoodsBean extendOrderGoodsBean , int position) {
        if (holder.getItemViewType() == Integer.MIN_VALUE) {
            TextView tvStore = holder.getView(R.id.store);
            tvStore.setText(storeName);
            holder.setText(R.id.order_state, state_desc);
        } else if (holder.getItemViewType() == Integer.MAX_VALUE) {
            if (zengpings.size()>0) {
                holder.getView(R.id.ll_zengping).setVisibility(View.VISIBLE);
                StringBuilder sb = new StringBuilder();
                for(ZengpingBean zengping:zengpings){
                    sb.append(zengping.getGoods_name()+"x"+zengping.getGoods_num());
                }
                holder.setText(R.id.zengpingName,"贈品  "+sb.toString());
            }else {
                holder.getView(R.id.ll_zengping).setVisibility(View.GONE);
            }
            TextView tvTotalNum = holder.getView(R.id.total_num);
            tvTotalNum.setText("共" + totalNum + "件商品，合计");
            TextView tvPrice = holder.getView(R.id.price);
            if(!TextUtils.isEmpty(payAmount)&&Double.valueOf(payAmount)>0) {
                holder.getView(R.id.liu_yan).setVisibility(View.VISIBLE);
                tvPrice.setText(StringUtil.getPriceSpannable12String(context, payAmount + "", R.style.big_money, R.style.big_money));
                holder.setText(R.id.deleiver, "(含运费" + StringUtil.getPriceSpannable12String(context, shipping_fee, R.style.big_money, R.style.big_money) + ")");
            }else {
                holder.getView(R.id.liu_yan).setVisibility(View.GONE);
            }
            LinearLayout btn_container = holder.getView(R.id.btn_container);
            btn_container.removeAllViews();
            View view = new View(context);
            view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            List<TextView> left = new ArrayList<TextView>();
            List<TextView> right = new ArrayList<TextView>();
            for (TextView btn : btns) {
                ViewGroup p = (ViewGroup) btn.getParent();
                if (p != null) {
                    p.removeAllViewsInLayout();
                }
                if(btn.getText().toString().contains("退款")||btn.getText().toString().contains("二维码")){
                   left.add(btn);
                }else{
                    right.add(btn);
                }
            }
            for(TextView btn :left){
                btn_container.addView(btn);
            }
            ViewGroup p = (ViewGroup) view.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
            btn_container.addView(view);
            for(TextView btn :right){
                btn_container.addView(btn);
            }
        } else if (holder.getItemViewType() == 100) {
            holder.setImage(R.id.iv, extendOrderGoodsBean.getGoods_image_url());
            holder.setText(R.id.name, extendOrderGoodsBean.getGoods_name());
            holder.setText(R.id.desc, extendOrderGoodsBean.getGoods_spec()==null?"":extendOrderGoodsBean.getGoods_spec().toString());
            holder.setText(R.id.price, StringUtil.getPriceSpannable12String(context, extendOrderGoodsBean.getGoods_price(), R.style.big_money, R.style.big_money));
            holder.setText(R.id.num, "X" + extendOrderGoodsBean.getGoods_num());
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGoDetail();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Integer.MIN_VALUE;
        } else if (position == datas.size() + 1) {
            return Integer.MAX_VALUE;
        } else if (position > 0 && position <= datas.size()) {
            return 100;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return datas.size() + 2;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (getItemViewType(position) == Integer.MIN_VALUE) {
            convert(holder, null, 0);
        } else if (getItemViewType(position) == Integer.MAX_VALUE) {
            convert(holder, null, datas.size() + 1);
        } else if (getItemViewType(position) == 100) {
            convert(holder, datas.get(position-1), position-1);
        }
    }

    public void setZengpings(List<ZengpingBean> zengpings) {
        this.zengpings = zengpings;
    }

    public interface OrderListener{
        void onGoDetail();
   }
}
