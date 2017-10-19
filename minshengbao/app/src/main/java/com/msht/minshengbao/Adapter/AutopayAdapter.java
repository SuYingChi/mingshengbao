package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/2/15.
 */

public class AutopayAdapter extends BaseAdapter {
    private Context mContext = null;

    /**
     *
     */
    private int mRightWidth = 0;

    private JSONArray mjsonArray;

    private ArrayList<HashMap<String, String>> List = new ArrayList<HashMap<String, String>>();

    /**
     * 单击事件监听器
     */
    private IOnItemButtonClickListener mListener = null;

    public interface IOnItemButtonClickListener {
        void onDelectClick(View v, int position);
    }

    public AutopayAdapter(Context ctx, ArrayList<HashMap<String, String>> houseLists, IOnItemButtonClickListener iOnItem) {
        mContext = ctx;
        List=houseLists;
        mListener = iOnItem;
    }
    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int position) {
        return List.get(position);
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
            item.item_customer=(TextView)convertView.findViewById(R.id.id_customerText);
            item.item_left_txt = (TextView)convertView.findViewById(R.id.item_left_txt);
            item.item_delect=(ImageView)convertView.findViewById(R.id.id_delect_img);
            convertView.setTag(item);
        } else {                // 有直接获得ViewHolder
            item = (ViewHolder)convertView.getTag();
        }
        item.item_left_txt.setText(List.get(position).get("address"));
        String customers=List.get(position).get("customerNo");
        item.item_customer.setText(customers);
        item.item_delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDelectClick(v, thisPosition);
                }

            }
        });
        return convertView;
    }
    private class ViewHolder {
        ImageView item_delect;
        TextView item_customer;
        TextView item_left_txt;

    }
}
