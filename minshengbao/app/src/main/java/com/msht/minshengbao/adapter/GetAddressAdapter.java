package com.msht.minshengbao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hong
 * @date 2017/3/17
 */
public class GetAddressAdapter extends RecyclerView.Adapter<GetAddressAdapter.MyViewHolder> {
    private int thisPos;
    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }
    public  ItemRadioButtonClickListener mListener = null;
    public void setRadioButtonClickListener(ItemRadioButtonClickListener listener){
        this.mListener=listener;
    }
    public interface ItemRadioButtonClickListener {
        /**
         * 回调
         * @param v
         * @param position
         */
        void onRadioButtonClick(View v, int position);
    }
    public interface ItemClickCallBack{
        /**
         * 回调
         * @param v
         * @param pos
         */
        void onItemClick(View v,int pos);
    }
    private ItemClickCallBack clickCallBack;
    private ArrayList<HashMap<String, String>> houseList = new ArrayList<HashMap<String, String>>();
    public GetAddressAdapter(Context context, ArrayList<HashMap<String, String>> mList, int pos) {
        super();
        this.houseList=mList;
        this.thisPos =pos;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customerno_record,viewGroup,false);
        return new GetAddressAdapter.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final int thisPosition = i;
        myViewHolder.itemCustomerNo.setText(houseList.get(i).get("customerNo"));
        myViewHolder.addressWord.setText(houseList.get(i).get("name"));
        if (thisPos ==i){
            myViewHolder.radioButton.setChecked(true);
        }else {
            myViewHolder.radioButton.setChecked(false);
        }
        myViewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRadioButtonClick(v, thisPosition);
                }
            }
        });
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickCallBack != null) {
                    clickCallBack.onItemClick(view,thisPosition);
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
        if (houseList!=null){
            return houseList.size();
        }else {
            return 0;
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView addressWord;
        TextView itemCustomerNo;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            radioButton=(RadioButton)view.findViewById(R.id.id_radio);
            addressWord = (TextView)view.findViewById(R.id.id_address_text);
            itemCustomerNo = (TextView)view.findViewById(R.id.id_customerText);
            itemView=view.findViewById(R.id.item_left);
        }
    }
}
