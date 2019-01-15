package com.msht.minshengbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.VariableUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hong
 * @date 2017/2/15
 */

public class AutomaticPayAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public void setOnItemDeleteButtonClickListener(IOnItemButtonClickListener listener){
        this.mListener=listener;
    }
    /**
     * 单击事件监听器
     */
    private IOnItemButtonClickListener mListener = null;

    public interface IOnItemButtonClickListener {
        /**
         * 删除
         * @param v
         * @param position
         */
        void onDeleteClick(View v, int position);
    }

    public AutomaticPayAdapter(Context ctx, ArrayList<HashMap<String, String>> houseLists) {
        mContext = ctx;
        mList =houseLists;
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
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder item;
        final int thisPosition = position;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_autopay_address, parent, false);
            item = new ViewHolder();
            item.itemCustomer =(TextView)convertView.findViewById(R.id.id_customerText);
            item.itemLeftTxt = (TextView)convertView.findViewById(R.id.item_left_txt);
            item.itemDelete =(ImageView)convertView.findViewById(R.id.id_delect_img);
            convertView.setTag(item);
        } else {                // 有直接获得ViewHolder
            item = (ViewHolder)convertView.getTag();
        }
        item.itemLeftTxt.setText(mList.get(position).get("address"));
        String customers= mList.get(position).get("customerNo");
        item.itemCustomer.setText(customers);
        if (VariableUtil.boolSelect){
            item.itemDelete.setVisibility(View.VISIBLE);
        }else {
            item.itemDelete.setVisibility(View.GONE);
        }
        item.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDeleteClick(v, thisPosition);
                }

            }
        });
        return convertView;
    }
    private class ViewHolder {
        ImageView itemDelete;
        TextView itemCustomer;
        TextView itemLeftTxt;

    }
}
