package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
    public OnSelectDelectListener delectlistener;
    public OnItemSelectListener listener;
    public OnItemCheckedListener checkedListener;
    public void setOnItemCheckedListener(OnItemCheckedListener listener){
        this.checkedListener=listener;
    }
    public void setOnItemSelectListener(OnItemSelectListener listener){
        this.listener=listener;
    }
    public void setOnDelectListener(OnSelectDelectListener delectlistener){
        this.delectlistener=delectlistener;
    }
    public interface OnItemCheckedListener{
        void ItemCheckedClick(View view,int thisposition);
    }
    public interface OnSelectDelectListener{
        void ItemDelectClick(View view,int thisposition);
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
            holder.box_setdefault=(CheckBox)convertView.findViewById(R.id.id_box_setdefault);
            holder.cn_addre = (TextView) convertView.findViewById(R.id.id_tv_address);
            holder.cn_name=(TextView)convertView.findViewById(R.id.id_tv_name);
            holder.cn_phone=(TextView)convertView.findViewById(R.id.id_tv_phone);
            holder.tv_edit=(TextView)convertView.findViewById(R.id.id_et_edittext);
            holder.tv_delect=(TextView)convertView.findViewById(R.id.id_tv_delect);
            holder.img_delect=(ImageView)convertView.findViewById(R.id.id_delect_img);
            holder.img_edit=(ImageView)convertView.findViewById(R.id.id_edit_img);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        String flag=addrList.get(position).get("flag");
        holder.cn_addre.setText(addrList.get(position).get("address"));
        holder.cn_phone.setText(addrList.get(position).get("phone"));
        holder.cn_name.setText(addrList.get(position).get("name"));
        if (flag.equals("1")){
            holder.box_setdefault.setChecked(true);
            holder.box_setdefault.setText("默认地址");
            holder.box_setdefault.setTextColor(0xfff96331);
        }else {
            holder.box_setdefault.setTextColor(0xff555555);
            holder.box_setdefault.setText("设为默认");
            holder.box_setdefault.setChecked(false);
        }
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.ItemSelectClick(v,thisposition);
                }
            }
        });
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.ItemSelectClick(v,thisposition);
                }
            }
        });
        holder.img_delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delectlistener!=null){
                    delectlistener.ItemDelectClick(v,thisposition);
                }
            }
        });
        holder.tv_delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delectlistener!=null){
                    delectlistener.ItemDelectClick(v,thisposition);
                }
            }
        });
        holder.box_setdefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedListener!=null){
                    checkedListener.ItemCheckedClick(v,thisposition);
                }
            }
        });
        return convertView;
    }
    class Holder{
        CheckBox    box_setdefault;
        ImageView   img_edit;
        ImageView   img_delect;
        TextView    tv_edit;
        TextView    tv_delect;
        TextView    cn_name;
        TextView    cn_phone;
        TextView    cn_addre;

    }
}
