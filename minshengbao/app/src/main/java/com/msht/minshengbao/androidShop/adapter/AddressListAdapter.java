package com.msht.minshengbao.androidShop.adapter;


import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopAddressListBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;


public class AddressListAdapter extends MyHaveHeadViewRecyclerAdapter<ShopAddressListBean.DatasBean.AddressListBean> {


    private OnAddressItemClickListner onAddressItemClickListner;
    private int checkPosition = 0;

    public AddressListAdapter(Context context, OnAddressItemClickListner onAddressItemClickListner) {
        super(context, R.layout.item_address);
        this.onAddressItemClickListner = onAddressItemClickListner;
    }

    @Override
    public void convert(RecyclerHolder holder, final ShopAddressListBean.DatasBean.AddressListBean item, final int position) {
        TextView name = holder.getView(R.id.name);
        name.setText(item.getTrue_name());
        TextView tvPhone_num = holder.getView(R.id.phone_num);
        tvPhone_num.setText(item.getMob_phone());
        TextView tvLocation = holder.getView(R.id.location);
        tvLocation.setText(String.format("%s%s", item.getArea_info(), item.getAddress()));
        final CheckBox checkBox = holder.getView(R.id.select_default_address);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //选中后不可更改
                    checkBox.setClickable(false);
                } else {
                    //取消选中后，可以更改
                    checkBox.setClickable(true);
                }
                onAddressItemClickListner.onItemCheckedChange(position, isChecked);
            }
        });

        if (item.getIs_default().equals("1")) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
        TextView tvEdit = holder.getView(R.id.edit_address);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddressItemClickListner.onItemEdit(position);
            }
        });
        TextView tvDelete = holder.getView(R.id.delete_address);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddressItemClickListner.onItemDeleted(position);
            }
        });
    }

    public interface OnAddressItemClickListner {
        void onItemCheckedChange(int position, boolean isChecked);

        void onItemEdit(int position);

        void onItemDeleted(int position);
    }
}
