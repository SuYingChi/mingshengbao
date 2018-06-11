
package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.VariableUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;


public class SwipeAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 
     */
    private int mRightWidth = 0;

    private JSONArray mjsonArray;

    private ArrayList<HashMap<String, String>> houseList = new ArrayList<HashMap<String, String>>();
    /**
     * 单击事件监听器
     */
    public IOnItemButtonClickListener mListener = null;

    public interface IOnItemButtonClickListener {
        void onButtonClick(View v, int position);
    }

    public void setOnDelectListener(IOnItemButtonClickListener delectlistener){
        this.mListener=delectlistener;
    }
    /**

     */
    public SwipeAdapter(Context ctx, ArrayList<HashMap<String, String>> houseLists) {
        mContext = ctx;
        houseList=houseLists;
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
        ViewHolder item;
        final int thisPosition = position;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_customerno, parent, false);
            item = new ViewHolder();
            item.item_customer=(TextView)convertView.findViewById(R.id.id_customerText);
            item.item_left_txt = (TextView)convertView.findViewById(R.id.id_address_text);
            item.item_delect=(ImageView) convertView.findViewById(R.id.id_btn_contast);
            convertView.setTag(item);
        } else {                // 有直接获得ViewHolder
            item = (ViewHolder)convertView.getTag();
        }
        item.item_left_txt.setText(houseList.get(position).get("name"));
        item.item_customer.setText(houseList.get(position).get("customerNo"));
        if (VariableUtil.Boolselect){
            item.item_delect.setVisibility(View.VISIBLE);
        }else {
            item.item_delect.setVisibility(View.GONE);
        }
        item.item_delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onButtonClick(v, thisPosition);
                }
            }
        });
        return convertView;
    }
    private class ViewHolder {
        ImageView item_delect;
        TextView  item_customer;
        TextView  item_left_txt;
    }
}
