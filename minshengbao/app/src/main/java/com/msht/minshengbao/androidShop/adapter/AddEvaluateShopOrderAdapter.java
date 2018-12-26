package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.MyAddEvaluateShopOrderBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

public class AddEvaluateShopOrderAdapter extends  MyHaveHeadAndFootRecyclerAdapter<MyAddEvaluateShopOrderBean>{

    private  AddEvaluateItemListener listener;

    public AddEvaluateShopOrderAdapter(Context context, AddEvaluateItemListener listener) {
        super(context, R.layout.item_add_evaluate_shop_order);
        this.listener = listener;
    }

    @Override
    public void convert(RecyclerHolder holder, final MyAddEvaluateShopOrderBean myAddEvaluateShopOrderBean, final int position) {
        holder.setIsRecyclable(false);
        holder.setImage(R.id.iv, myAddEvaluateShopOrderBean.getImaUrl());
        holder.setText(R.id.name, myAddEvaluateShopOrderBean.getName());
        holder.setText(R.id.text, myAddEvaluateShopOrderBean.getGeval_content());
        EditText et = holder.getView(R.id.et);
        if (myAddEvaluateShopOrderBean.getComment().equals("")) {
            et.setHint("已经用了一段时间了，你觉得宝贝怎么样呢");
        }else {
            et.setText(myAddEvaluateShopOrderBean.getComment());
        }
        final TextView wordNum = holder.getView(R.id.wordNum);
        wordNum.setText(String.format("%d/150", myAddEvaluateShopOrderBean.getTextNum()));
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.onTextChange(s.toString(),position);
                wordNum.setText(s.toString().length() + "/150");
            }
        });
        RecyclerView rclGrid = holder.getView(R.id.rcl_grid);
        if (rclGrid.getAdapter() == null) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
            rclGrid.setLayoutManager(gridLayoutManager);
            gridLayoutManager.setAutoMeasureEnabled(true);
            rclGrid.setNestedScrollingEnabled(false);
            EvaluatePhotoAdapter2 ad = new EvaluatePhotoAdapter2(context);
            ad.setDatas(myAddEvaluateShopOrderBean.getImagePathList());
            ad.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int childposition) {
                    listener.onClickImageContainer(position, childposition);
                }
            });
            rclGrid.setAdapter(ad);
        } else if (rclGrid.getAdapter() instanceof EvaluatePhotoAdapter2) {
            EvaluatePhotoAdapter2 ad = (EvaluatePhotoAdapter2) rclGrid.getAdapter();
            ad.notifyDataSetChanged();
        }
    }
    public interface AddEvaluateItemListener {

        void onClickImageContainer(int position, int childPosition);


        void onTextChange(String s, int position);
    }
}
