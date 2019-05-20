package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ClassDetailRightBean;
import com.msht.minshengbao.androidShop.shopBean.ShopOrderDetailBean;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

public class ClassDetailRightAdapter extends MyHaveHeadViewRecyclerAdapter<ClassDetailRightBean.DatasBean.GoodsListBean> {
    private AddCarListener addCarListener;

    public ClassDetailRightAdapter(Context context) {
        super(context, R.layout.item_class_detail_right);
    }

    @Override
    public void convert(RecyclerHolder holder, final ClassDetailRightBean.DatasBean.GoodsListBean goodsListBean, final int position) {
        ImageView iv = holder.getView(R.id.iv);
        GlideUtil.loadRemoteImg(context, iv, goodsListBean.getGoods_image_url());
        TextView tv = holder.getView(R.id.name);
        tv.setText(goodsListBean.getGoods_name().trim());
        if (TextUtils.isEmpty(goodsListBean.getGoods_jingle())) {
            holder.getView(R.id.jingle).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.jingle).setVisibility(View.VISIBLE);
            holder.setText(R.id.jingle, goodsListBean.getGoods_jingle());
        }
        if (TextUtils.isEmpty(goodsListBean.getGoods_salenum())) {
            holder.getView(R.id.sale_count).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.sale_count).setVisibility(View.VISIBLE);
            holder.setText(R.id.sale_count, "已售" + goodsListBean.getGoods_salenum() + "件");
        }
        if(TextUtils.isEmpty(goodsListBean.getDelivery_desc())){
            holder.getView(R.id.isziti).setVisibility(View.GONE);
        }else {
            holder.setText(R.id.isziti, goodsListBean.getDelivery_desc());
        }
        if (goodsListBean.isGroup_flag()) {
            holder.getView(R.id.promotion_type).setVisibility(View.VISIBLE);
            holder.setText(R.id.promotion_type, "团购");
        } else if (goodsListBean.isXianshi_flag()) {
            holder.getView(R.id.promotion_type).setVisibility(View.VISIBLE);
            holder.setText(R.id.promotion_type, "限时折扣");
        } else if (goodsListBean.isIf_pintuan()) {
            holder.getView(R.id.promotion_type).setVisibility(View.VISIBLE);
            holder.setText(R.id.promotion_type, "拼团");
        } else if (goodsListBean.isSpike_flag()) {
            holder.getView(R.id.promotion_type).setVisibility(View.VISIBLE);
            holder.setText(R.id.promotion_type, "秒杀");
        } else {
            holder.getView(R.id.promotion_type).setVisibility(View.INVISIBLE);
        }
        TextView tvPrice = holder.getView(R.id.price);
        tvPrice.setText(context.getResources().getString(R.string.monetary_unit) + goodsListBean.getGoods_price());
        ImageView car = holder.getView(R.id.car);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCarListener.addCar(goodsListBean);
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    public interface AddCarListener {
        void addCar(ClassDetailRightBean.DatasBean.GoodsListBean good);
    }

    public void setOnAddCarListener(AddCarListener addCarListener) {
        this.addCarListener = addCarListener;
    }
}
