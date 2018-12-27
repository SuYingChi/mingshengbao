package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
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
        final ImageView iv = holder.getView(R.id.iv);
        iv.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = iv.getLayoutParams();
                params.width = (int)(DimenUtil.getScreenWidth()*0.25);
                int vw = params.width - iv.getPaddingLeft() - iv.getPaddingRight();
                params.height = vw + iv.getPaddingTop() + iv.getPaddingBottom();
                iv.setLayoutParams(params);
                GlideUtil.loadByImageView(context,iv,goodsListBean.getGoods_image_url());
            }
        });
        TextView tv = holder.getView(R.id.name);
        tv.setText(goodsListBean.getGoods_name());
        TextView tvPrice = holder.getView(R.id.price);
        tvPrice.setText(context.getResources().getString(R.string.monetary_unit)+"  "+goodsListBean.getGoods_price());
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

    public interface AddCarListener{
        void addCar(ClassDetailRightBean.DatasBean.GoodsListBean good);
    }

   public void setOnAddCarListener(AddCarListener addCarListener){
        this.addCarListener = addCarListener;
   }
}
