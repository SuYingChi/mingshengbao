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
 * Created by hong on 2018/4/3.
 */

public class EstateAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private final int RESOURCE = R.layout.item_estate_name;
    public OnItemSelectListener listener;
    public interface OnItemSelectListener{
        void ItemSelectClick(View view,int thisposition);
    }
    public void setOnItemSelectListener(OnItemSelectListener listener){
        this.listener=listener;
    }

    public EstateAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        inflater=LayoutInflater.from(context);
        this.mList=List;
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = inflater.inflate(RESOURCE, null);
            vh.tv_title = (TextView) convertView.findViewById(R.id.id_estate);
            vh.tv_address=(TextView) convertView.findViewById(R.id.id_address);
            vh.radioButton =(RadioButton)convertView.findViewById(R.id.id_RadioButton);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_title.setText(mList.get(position).get("name"));
        vh.tv_address.setText(mList.get(position).get("address"));
        vh.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.ItemSelectClick(v,position);
                }
            }
        });
        return convertView;
    }
     class ViewHolder{
         TextView tv_title;
         TextView tv_address;
         RadioButton radioButton;
    }
}
