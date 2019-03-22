package com.msht.minshengbao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hong
 * @date 2018/1/4
 */

public class WaterIncomeAdapter extends RecyclerView.Adapter<WaterIncomeAdapter.MyViewHolder> {

    private ArrayList<HashMap<String, String>> incomeList = new ArrayList<HashMap<String, String>>();

    public WaterIncomeAdapter( ArrayList<HashMap<String, String>> List) {
        super();
        this.incomeList=List;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_water_balance_detail,viewGroup,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        String type = incomeList.get(position).get("type");
        String payTypeName=incomeList.get(position).get("payTypeName");
        String createTime=incomeList.get(position).get("createTime");
        String childType=incomeList.get(position).get("childType");
        String amountText=incomeList.get(position).get("amount");
        myViewHolder.tvTime.setText(createTime);
        myViewHolder.tvPayType.setText(payTypeName);
        switch (type){
            case ConstantUtil.VALUE_ONE:
                myViewHolder.tvContent.setText("余额充值");
                amountText="+"+amountText+"元";
                myViewHolder.tvAmount.setTextColor(Color.parseColor("#FF30A949"));
                break;
            case ConstantUtil.VALUE_TWO:
                myViewHolder.tvContent.setText("扫码购水");
                amountText="-"+amountText+"元";
                myViewHolder.tvAmount.setTextColor(Color.parseColor("#FFF96331"));
                break;
            case ConstantUtil.VALUE_THREE:
                myViewHolder.tvContent.setText("余额退款");
                amountText="+"+amountText+"元";
                myViewHolder.tvAmount.setTextColor(Color.parseColor("#FF30A949"));
                break;
            case ConstantUtil.VALUE_FOUR:
                myViewHolder.tvContent.setText("活动赠送");
                amountText="+"+amountText+"元";
                myViewHolder.tvAmount.setTextColor(Color.parseColor("#FF30A949"));
                break;
            case ConstantUtil.VALUE_FIVE:
                if (childType.equals(ConstantUtil.VALUE_TWO)){
                    myViewHolder.tvContent.setText("充值退款");
                    amountText="-"+amountText+"元";
                    myViewHolder.tvAmount.setTextColor(Color.parseColor("#FFF96331"));
                  //  myViewHolder.tvAmount.setTextColor(Color.parseColor("#FF30A949"));
                }else {
                    myViewHolder.tvContent.setText("消费退款");
                    amountText="+"+amountText+"元";
                    myViewHolder.tvAmount.setTextColor(Color.parseColor("#FF30A949"));
                }
                break;
            case ConstantUtil.VALUE_SIX:
                amountText="+"+amountText+"元";
                myViewHolder.tvContent.setText("抽奖赠送");
                myViewHolder.tvAmount.setTextColor(Color.parseColor("#FF30A949"));
                break;
            case ConstantUtil.VALUE_SEVER:
                amountText="+"+amountText+"元";
                myViewHolder.tvContent.setText("活动赠送");
                myViewHolder.tvAmount.setTextColor(Color.parseColor("#FF30A949"));
                break;
            case ConstantUtil.VALUE_EIGHT:
                amountText="+"+amountText+"元";
                myViewHolder.tvContent.setText("体验充值");
                myViewHolder.tvAmount.setTextColor(Color.parseColor("#FF30A949"));
                break;
            default:
                amountText="-"+amountText+"元";
                myViewHolder.tvContent.setText("送水到家");
                myViewHolder.tvAmount.setTextColor(Color.parseColor("#FF30A949"));
                break;
        }
        myViewHolder.tvAmount.setText(amountText);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (incomeList == null) {
            return 0;
        } else {
            return incomeList.size();
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView  tvContent;
        private TextView  tvAmount;
        private TextView  tvTime;
        private TextView  tvPayType;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            tvTime =(TextView) view.findViewById(R.id.id_create_time);
            tvContent =(TextView) view.findViewById(R.id.id_tv_type);
            tvAmount =(TextView)view.findViewById(R.id.id_tv_amount);
            tvPayType=(TextView)view.findViewById(R.id.id_payType);
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
