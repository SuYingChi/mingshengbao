package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.VariableUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/4/28.
 */

public class AddrManageAdapter extends BaseAdapter {
    private Context mContext = null;
    private LayoutInflater inflater;
    public MyWorkOrderAdapter.OnItemSelectListener listener;
    public void SetOnItemSelectListener(MyWorkOrderAdapter.OnItemSelectListener listener){
        this.listener=listener;
    }
    public interface OnItemSelectListener{
        void ItemSelectClick(View view,int thisposition);
    }
    private ArrayList<HashMap<String, String>> addrList = new ArrayList<HashMap<String, String>>();
    public AddrManageAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        inflater=LayoutInflater.from(context);
        this.mContext=context;
        this.addrList=List;
    }
    @Override
    public int getCount() {
        if (addrList!=null){
            return addrList.size();
        }else {
            return 0;
        }
    }
    @Override
    public Object getItem(int position) {
        if (addrList!=null){
            return addrList.get(position);
        }else {
            return null;
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int thisposition=position;
        Holder holder;
        if(convertView==null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_address_manage, null);
            holder.cn_addre = (TextView) convertView.findViewById(R.id.id_tv_address);
            holder.img_edit=(ImageView)convertView.findViewById(R.id.id_edit_img);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.cn_addre.setText(addrList.get(position).get("address"));
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.ItemSelectClick(v,thisposition);
                }
            }
        });
        return convertView;
    }
    class Holder{
        ImageView   img_edit;
        TextView    cn_addre;
    }
}
