package com.msht.minshengbao.adapter;

import android.content.Context;
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
 * Created by hong on 2017/3/17.
 */
public class GetAddressAdapter extends BaseAdapter {
    private Context mContext;
    private int thisPos;
    private LayoutInflater mInflater = null;
    public  ItemRadioButtonClickListener mListener = null;
    public void setRadioButtonClickListener(ItemRadioButtonClickListener listener){
        this.mListener=listener;
    }
    public interface ItemRadioButtonClickListener {
        void onRadioButtonClick(View v, int position);
    }
    private ArrayList<HashMap<String, String>> houseList = new ArrayList<HashMap<String, String>>();
    public GetAddressAdapter(Context context, ArrayList<HashMap<String, String>> mList, int pos) {
        super();
        this.mContext = context;
        this.houseList=mList;
        this.thisPos =pos;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return houseList.size();
    }

    @Override
    public Object getItem(int position) {
        return houseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int thisPosition = position;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_customerno_record, null);
            holder.radioButton=(RadioButton)convertView.findViewById(R.id.id_radio);
            holder.addressWord = (TextView) convertView.findViewById(R.id.id_address_text);
            holder.itemCustomerNo = (TextView) convertView.findViewById(R.id.id_customerText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemCustomerNo.setText(houseList.get(position).get("customerNo"));
        holder.addressWord.setText(houseList.get(position).get("name"));
        if (thisPos ==position){
            holder.radioButton.setChecked(true);
        }else {
            holder.radioButton.setChecked(false);
        }
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRadioButtonClick(v, thisPosition);
                }
            }
        });
        return convertView;
    }
    class ViewHolder {
        RadioButton radioButton;
        TextView addressWord;
        TextView itemCustomerNo;
    }
}
