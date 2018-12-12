package com.msht.minshengbao.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.functionActivity.MyActivity.ModifyAddressActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class SelectAddressAdapter extends RecyclerView.Adapter<SelectAddressAdapter.MyViewHolder>{



    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();


    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface ItemClickCallBack{
        /**
         * item回调
         * @param pos 下标
         */
        void onItemClick(int pos);
    }
    public interface OnItemClickListener{
        /**
         * item回调
         * @param position 下标
         */
        void  onItemSelectClick(int position);
    }
    private OnItemClickListener onItemClickListener;
    private ItemClickCallBack clickCallBack;
    public SelectAddressAdapter(ArrayList<HashMap<String, String>> list) {
        this.mList=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_address_manage,viewGroup,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final  int position=i;
        String name=mList.get(position).get("name");
        if (TextUtils.isEmpty(name)){
            myViewHolder.cnName.setText(mList.get(position).get("phone"));
        }else {
            myViewHolder.cnName.setText(name);
        }
        myViewHolder.cnPhone.setText(mList.get(position).get("phone"));
        myViewHolder.cnAddress.setText(mList.get(position).get("address"));
        myViewHolder.editLayout.setVisibility(View.GONE);
        myViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (onItemClickListener!=null){
                   onItemClickListener.onItemSelectClick(position);
               }
            }
        });
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickCallBack != null){
                    clickCallBack.onItemClick(position);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View editLayout;
        ImageView imgEdit;
        TextView cnAddress;
        TextView cnName;
        TextView cnPhone;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            cnName =(TextView)view.findViewById(R.id.id_tv_name);
            cnPhone =(TextView)view.findViewById(R.id.id_tv_phone);
            cnAddress = (TextView)view.findViewById(R.id.id_tv_address);
            imgEdit =(ImageView)view.findViewById(R.id.id_edit_img);
            editLayout =view.findViewById(R.id.id_edit_layout);
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
