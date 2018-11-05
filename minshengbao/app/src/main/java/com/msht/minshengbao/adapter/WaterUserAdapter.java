package com.msht.minshengbao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.VariableUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/10/10  
 */
public class WaterUserAdapter extends RecyclerView.Adapter<WaterUserAdapter.MyViewHolder>{
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public WaterUserAdapter(ArrayList<HashMap<String, String>> list) {
        this.mList=list;
    }
    public void setDeleteItemClickBack(ItemDeleteClickBack itemDeleteClickBack){
        this.itemDeleteClickBack=itemDeleteClickBack;
    }
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
    public interface ItemDeleteClickBack{
        /**
         *  删除item回调
         * @param pos 下标
         */
        void onItemDeleteClick(int pos);
    }
    private ItemClickCallBack clickCallBack;
    private ItemRadioClickBack itemRadioClickBack;
    private ItemDeleteClickBack itemDeleteClickBack;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_water_user_list,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final  int thisPosition=position;
        holder.tvPhone.setText(mList.get(position).get("account"));
        String isDefault=mList.get(position).get("isDefault");
        holder.radioButton.setEnabled(false);
        if (isDefault.equals(ConstantUtil.VALUE_ONE)){
            holder.radioButton.setChecked(true);
        }else {
            holder.radioButton.setChecked(false);
        }
        if (VariableUtil.deleteFlag==1){
            if (position==0){
                holder.deleteImg.setVisibility(View.GONE);
                holder.radioButton.setVisibility(View.VISIBLE);
            }else {
                holder.radioButton.setVisibility(View.INVISIBLE);
                holder.deleteImg.setVisibility(View.VISIBLE);
            }
        }else {
            holder.radioButton.setVisibility(View.VISIBLE);
            holder.deleteImg.setVisibility(View.GONE);
        }
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
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemDeleteClickBack!=null){
                    itemDeleteClickBack.onItemDeleteClick(thisPosition);
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
        ImageView   deleteImg;
        TextView    tvPhone;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            radioButton=(RadioButton)view.findViewById(R.id.id_radio_isLastLogin);
            tvPhone = (TextView) view.findViewById(R.id.id_tv_phone);
            deleteImg=(ImageView)view.findViewById(R.id.id_delete_img);
            itemView=view.findViewById(R.id.id_item_layout);
        }
    }
}
