package com.msht.minshengbao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
 * @date 2017/3/22
 */

public class PayRecordAdapter extends RecyclerView.Adapter<PayRecordAdapter.MyViewHolder> {


    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }
    public interface ItemClickCallBack{
        /**
         * item回调
         * @param pos 下标
         */
        void onItemClick(int pos);
    }
    private ItemClickCallBack clickCallBack;
    private ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();
    public PayRecordAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.recordList=List;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pay_record,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int position) {
        myViewHolder.cnMoney.setText("¥"+recordList.get(position).get("amount"));
        myViewHolder.cnPayWay.setText(recordList.get(position).get("payMethod"));
        myViewHolder.cnTime.setText(recordList.get(position).get("payTime"));
        String writeCardState=recordList.get(position).get("writeCardState");
        String tableType=recordList.get(position).get("tableType");
        switch (tableType){
            case ConstantUtil.VALUE_ZERO:
                myViewHolder.cnWriteCardState.setText(writeCardState);
                myViewHolder.tvText1.setText("订单金额");
                myViewHolder.tvText2.setText("缴费渠道");
                myViewHolder.tvText3.setText("缴费时间");
                myViewHolder.tvText4.setVisibility(View.GONE);
                myViewHolder.cnWriteCardState.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_ONE:
                myViewHolder.cnWriteCardState.setText(writeCardState);
                myViewHolder.tvText1.setText("充值金额");
                myViewHolder.tvText2.setText("充值渠道");
                myViewHolder.tvText3.setText("充值时间");
                myViewHolder.tvText4.setText("写卡状态");
                myViewHolder.tvText4.setVisibility(View.VISIBLE);
                myViewHolder.cnWriteCardState.setVisibility(View.VISIBLE);
                break;
            case ConstantUtil.VALUE_TWO:
                myViewHolder.cnWriteCardState.setText(writeCardState);
                myViewHolder.tvText1.setText("充值金额");
                myViewHolder.tvText2.setText("支付状态");
                myViewHolder.tvText3.setText("充值时间");
                myViewHolder.tvText4.setText("上表状态");
                myViewHolder.tvText4.setVisibility(View.GONE);
                myViewHolder.cnWriteCardState.setVisibility(View.GONE);
                break;
                default:
                    myViewHolder.cnWriteCardState.setText(writeCardState);
                    myViewHolder.tvText1.setText("订单金额");
                    myViewHolder.tvText2.setText("缴费渠道");
                    myViewHolder.tvText3.setText("缴费时间");
                    myViewHolder.tvText4.setVisibility(View.GONE);
                    myViewHolder.cnWriteCardState.setVisibility(View.GONE);
                    break;
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickCallBack!=null){
                    clickCallBack.onItemClick(position);
                }
            }
        });
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        if (recordList!=null){
            return recordList.size();
        }else {
            return 0;
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cnMoney;
        TextView cnPayWay;
        TextView cnTime;
        TextView cnWriteCardState;
        TextView tvText1;
        TextView tvText2;
        TextView tvText3;
        TextView tvText4;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            cnMoney =(TextView) view.findViewById(R.id.id_amonut);
            cnPayWay =(TextView)view.findViewById(R.id.id_pay_way);
            cnTime =(TextView) view.findViewById(R.id.id_tv_time);
            cnWriteCardState =(TextView)view.findViewById(R.id.id_writecard_state);
            tvText1 =(TextView)view.findViewById(R.id.id_text1);
            tvText2 =(TextView)view.findViewById(R.id.id_text2);
            tvText3 =(TextView)view.findViewById(R.id.id_text3);
            tvText4 =(TextView)view.findViewById(R.id.id_text4);
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
