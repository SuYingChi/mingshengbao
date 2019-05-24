package com.msht.minshengbao.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.custom.ButtonUI.ButtonM;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/3/13  
 */
public class RedPacketAdapter extends RecyclerView.Adapter<RedPacketAdapter.MyViewHolder>{
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }
    public void setButtonCallBack(ItemClickButtonCallBack clickButtonCallBack){
        this.clickButtonCallBack=clickButtonCallBack;
    }
    public interface ItemClickCallBack{
        /**
         * item回调
         * @param pos 下标
         */
        void onItemClick(int pos);
    }
    public interface ItemClickButtonCallBack{
        /**
         * item回调
         * @param pos 下标
         */
        void onItemButtonClick(int pos);
    }
    private ItemClickCallBack clickCallBack;
    private ItemClickButtonCallBack clickButtonCallBack;

    public RedPacketAdapter(ArrayList<HashMap<String, String>> list) {
        super();
        this.mList=list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_water_red_packet,viewGroup,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        String name=mList.get(i).get("name");
        String status= mList.get(i).get("status");
        String scope = mList.get(i).get("scope");
        String conditionType =mList.get(i).get("conditionType");
        String conditionAmount="满"+mList.get(i).get("conditionAmount")+"可用";
        String discountAmount=mList.get(i).get("discountAmount");
        String days=mList.get(i).get("days")+"天后过期";
        String endTime=mList.get(i).get("endTime");
        String usageTime=mList.get(i).get("usageTime");
        String isVisible=mList.get(i).get("isVisible");
        String type=mList.get(i).get("type");
        myViewHolder.tvName.setText(name);
        myViewHolder.tvDiscountAmount.setText(discountAmount);
        myViewHolder.tvCondition.setText(conditionAmount);
        myViewHolder.tvUseDescribe.setText(mList.get(i).get("description"));
        if (!TextUtils.isEmpty(status)){
            switch (status){
                case ConstantUtil.VALUE_ZERO:
                    if (type.equals(ConstantUtil.VALUE_ONE)){
                        myViewHolder.tvRmbMark.setTextColor(Color.parseColor("#ff656565"));
                        myViewHolder.tvName.setTextColor(Color.parseColor("#ff656565"));
                        myViewHolder.tvDiscountAmount.setTextColor(Color.parseColor("#ff656565"));
                    }else {
                        myViewHolder.tvRmbMark.setTextColor(Color.parseColor("#FFE43F3E"));
                        myViewHolder.tvName.setTextColor(Color.parseColor("#FFE43F3E"));
                        myViewHolder.tvDiscountAmount.setTextColor(Color.parseColor("#FFE43F3E"));
                    }
                    myViewHolder.tvDays.setText(days);
                    break;
                case ConstantUtil.VALUE_ONE:
                    myViewHolder.tvRmbMark.setTextColor(Color.parseColor("#ff656565"));
                    myViewHolder.tvName.setTextColor(Color.parseColor("#ff656565"));
                    myViewHolder.tvDiscountAmount.setTextColor(Color.parseColor("#ff656565"));
                    myViewHolder.tvDays.setText(usageTime);
                    break;
                case ConstantUtil.VALUE_TWO:
                    myViewHolder.tvRmbMark.setTextColor(Color.parseColor("#ff656565"));
                    myViewHolder.tvName.setTextColor(Color.parseColor("#ff656565"));
                    myViewHolder.tvDiscountAmount.setTextColor(Color.parseColor("#ff656565"));
                    myViewHolder.tvDays.setText(endTime);
                    break;
                    default:
                        myViewHolder.tvDays.setText(days);
                        break;
            }
        }else {
            myViewHolder.tvDays.setText(days);
        }
        /*if (status.equals(ConstantUtil.VALUE_ZERO)){
            if (type.equals(ConstantUtil.VALUE_ONE)){
                myViewHolder.tvName.setTextColor(Color.parseColor("#ff656565"));
                myViewHolder.tvDiscountAmount.setTextColor(Color.parseColor("#ff656565"));
            }else {
                myViewHolder.tvName.setTextColor(Color.parseColor("#FFE43F3E"));
                myViewHolder.tvDiscountAmount.setTextColor(Color.parseColor("#FFE43F3E"));
            }
        }else {
            myViewHolder.tvName.setTextColor(Color.parseColor("#ff656565"));
            myViewHolder.tvDiscountAmount.setTextColor(Color.parseColor("#ff656565"));
        }*/
        if (isVisible.equals(ConstantUtil.VALUE_ONE)){
            if (status.equals(ConstantUtil.VALUE_ZERO)){
                myViewHolder.btnUse.setVisibility(View.VISIBLE);
            }else {
                myViewHolder.btnUse.setVisibility(View.GONE);
            }
        }else {
            myViewHolder.btnUse.setVisibility(View.GONE);
        }
        if (conditionType.equals(ConstantUtil.VALUE_ZERO)){
            myViewHolder.tvCondition.setVisibility(View.INVISIBLE);
        }else {
            myViewHolder.tvCondition.setVisibility(View.VISIBLE);
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickCallBack!=null){
                    clickCallBack.onItemClick(i);
                }
            }
        });
        if (type.equals(ConstantUtil.VALUE_ZERO)){
            if (VariableUtil.mPos==i){
                myViewHolder.itemView.setBackgroundResource(R.drawable.water_coupon_press_xh);
            }else {
                myViewHolder.itemView.setBackgroundResource(R.drawable.water_coupon_normal_xh);
            }
        }
        myViewHolder.btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickButtonCallBack!=null){
                    clickButtonCallBack.onItemButtonClick(i);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        } else {
            return mList.size();
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView  tvName;
        private TextView  tvDiscountAmount;
        private TextView  tvCondition;
        private TextView  tvDays;
        private TextView  tvUseDescribe;
        private TextView  tvRmbMark;
        private ButtonM   btnUse;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            tvName =(TextView) view.findViewById(R.id.id_title_name);
            tvDiscountAmount =(TextView) view.findViewById(R.id.id_discount_amount);
            tvCondition =(TextView)view.findViewById(R.id.id_use_limit);
            tvDays=(TextView)view.findViewById(R.id.id_effective_text);
            tvUseDescribe=(TextView)view.findViewById(R.id.id_use_describe);
            btnUse=(ButtonM)view.findViewById(R.id.id_use_btn) ;
            tvRmbMark=(TextView)view.findViewById(R.id.id_Rmb_mark);
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
