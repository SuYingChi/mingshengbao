package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.MyEvaluateShopOrderBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

public class EvaluateShopOrderAdapter extends MyHaveHeadAndFootRecyclerAdapter<MyEvaluateShopOrderBean> {

    private EvaluateItemListener listener;

    public EvaluateShopOrderAdapter(Context context, EvaluateItemListener listener) {
        super(context, R.layout.item_evaluate_shop_order);
        this.listener = listener;
    }

    @Override
    public void convert(RecyclerHolder holder, final MyEvaluateShopOrderBean evaluateShopOrderBean, final int position) {
        //recyclerview item里有edittext 和recyclerview,在键盘弹出收起时子recyclerview界面出现混乱，只能暂时禁用复用解决问题
        holder.setIsRecyclable(false);
        holder.setImage(R.id.iv, evaluateShopOrderBean.getImaUrl());
        holder.setText(R.id.name, evaluateShopOrderBean.getName());
        EditText et = holder.getView(R.id.et);
        final TextView wordNum = holder.getView(R.id.wordNum);
        if (evaluateShopOrderBean.getEvaluateText().equals("")) {
            et.setHint("你觉得宝贝怎么样呢");
        }else{
            et.setText(evaluateShopOrderBean.getEvaluateText());
        }
        wordNum.setText(String.format("%d/150", evaluateShopOrderBean.getTextNum()));
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.onTextchanged(s.toString(),position);
                wordNum.setText(s.toString().length() + "/150");
            }
        });
        RecyclerView startRcl = holder.getView(R.id.start_rcl);
        RecyclerView rclGrid = holder.getView(R.id.rcl_grid);
        CheckBox cb = holder.getView(R.id.cb_niming);
        cb.setChecked(evaluateShopOrderBean.isNiming());
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.isNiming(position, isChecked);
            }
        });
        if (startRcl.getAdapter() == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
            linearLayoutManager.setAutoMeasureEnabled(true);
            startRcl.setNestedScrollingEnabled(false);
            startRcl.setLayoutManager(linearLayoutManager);
            EvaluateStartsAdapter adapter = new EvaluateStartsAdapter(context, evaluateShopOrderBean.getStarts());
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int childPosition) {
                    listener.onClickStar(position, childPosition);
                }
            });
            startRcl.setAdapter(adapter);
        } else {
            EvaluateStartsAdapter evaluateStartsAdapter = (EvaluateStartsAdapter) startRcl.getAdapter();
            evaluateStartsAdapter.setStarts(evaluateShopOrderBean.getStarts());
            evaluateStartsAdapter.notifyDataSetChanged();
        }
        if (rclGrid.getAdapter() == null) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
            rclGrid.setLayoutManager(gridLayoutManager);
            gridLayoutManager.setAutoMeasureEnabled(true);
            rclGrid.setNestedScrollingEnabled(false);
            EvaluatePhotoAdapter2 ad = new EvaluatePhotoAdapter2(context);
            ad.setDatas(evaluateShopOrderBean.getImagePathList());
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

    public interface EvaluateItemListener {

        void onClickStar(int position, int starNum);

        void onClickImageContainer(int position, int childPosition);

        void isNiming(int itemPosition, boolean isChecked);

        void onTextchanged(String s,int positon);
    }
}
