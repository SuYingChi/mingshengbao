package com.msht.minshengbao.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/9/4  
 */
public class CustomerNoAdapter  extends RecyclerView.Adapter<CustomerNoAdapter.MyViewHolder>{
    private ArrayList<HashMap<String, String>> houseList = new ArrayList<HashMap<String, String>>();
    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }
    public interface ItemClickCallBack{
        /**
         * item回调
         * @param pos
         */
        void onItemClick(int pos);
    }
    private ItemClickCallBack clickCallBack;
    public CustomerNoAdapter(ArrayList<HashMap<String, String>> recordList) {
        this.houseList=recordList;
    }
    @Override
    public CustomerNoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customerno_record,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(CustomerNoAdapter.MyViewHolder holder, int position) {
        final  int thisPosition=position;
        holder.addressWord.setText(houseList.get(position).get("name"));
        holder.itemCustomerNo.setText(houseList.get(position).get("customerNo"));
        holder.radioButton.setVisibility(View.INVISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickCallBack != null){
                    clickCallBack.onItemClick(thisPosition);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        if (houseList!=null){
           return houseList.size();
        }else {
            return 0;
        }
    }
    /**自定义的ViewHolder，持有每个Item的的所有界面元素 */
    public  static class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView addressWord;
        TextView itemCustomerNo;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            radioButton=(RadioButton)view.findViewById(R.id.id_radio);
            addressWord = (TextView) view.findViewById(R.id.id_address_text);
            itemCustomerNo = (TextView) view.findViewById(R.id.id_customerText);
            itemView=view.findViewById(R.id.item_left);
        }
    }
}
