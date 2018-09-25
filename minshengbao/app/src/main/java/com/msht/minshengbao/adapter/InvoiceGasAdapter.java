package com.msht.minshengbao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class InvoiceGasAdapter extends RecyclerView.Adapter<InvoiceGasAdapter.MyViewHolder>{

    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }
    public void setItemRadioClickBack(ItemRadioClickBack itemRadioClickBack){
        this.itemRadioClickBack=itemRadioClickBack;
    }
    public interface ItemRadioClickBack{
        /**
         * item回调
         * @param pos 下标
         */
        void onItemRadioBack(int pos);
    }
    public interface ItemClickCallBack{
        /**
         * item回调
         * @param pos 下标
         */
        void onItemClick(int pos);
    }
    private ItemClickCallBack clickCallBack;
    private ItemRadioClickBack itemRadioClickBack;
    public InvoiceGasAdapter(ArrayList<HashMap<String, String>> list) {
        this.mList=list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice_gas,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final  int thisPosition=position;
        String mGasNum="缴费气量"+mList.get(position).get("rechargeGas")+"NM³";
        holder.tvDate.setText(mList.get(position).get("paymentDate"));
        holder.tvGasNum.setText(mGasNum);
        holder.tvAmount.setText(mList.get(position).get("amount"));
        holder.itemCustomerNo.setText(mList.get(position).get("customerNo"));
        holder.radioButton.setChecked(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickCallBack != null){
                    clickCallBack.onItemClick(thisPosition);
                }
            }
        });
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemRadioClickBack != null){
                    itemRadioClickBack.onItemRadioBack(thisPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList!=null){
            return mList.size();
        }else {
            return 0;
        }
    }

    /**自定义的ViewHolder，持有每个Item的的所有界面元素 */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView tvDate;
        TextView itemCustomerNo;
        TextView tvGasNum;
        TextView tvAmount;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            radioButton=(RadioButton)view.findViewById(R.id.id_radio);
            tvDate = (TextView) view.findViewById(R.id.id_date);
            itemCustomerNo = (TextView) view.findViewById(R.id.id_tv_orderNo);
            tvGasNum=(TextView)view.findViewById(R.id.id_gas_num) ;
            tvAmount=(TextView)view.findViewById(R.id.id_tv_amount) ;
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
