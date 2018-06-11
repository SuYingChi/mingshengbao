package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/9/1.
 */

public class PreexistenceAdapter extends BaseAdapter {

    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> detailList = new ArrayList<HashMap<String, String>>();
    public PreexistenceAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.detailList=List;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return detailList.size();
    }

    @Override
    public Object getItem(int position) {
        return detailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_preexistence_detail, null);
            holder.cn_money=(TextView) convertView.findViewById(R.id.id_tv_blance);
            holder.cn_tradeno=(TextView)convertView.findViewById(R.id.id_trade_no);
            holder.cn_time=(TextView) convertView.findViewById(R.id.id_tv_time);
            holder.text_time=(TextView)convertView.findViewById(R.id.id_text_time);
            holder.text_amount=(TextView)convertView.findViewById(R.id.id_text_amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cn_tradeno.setText(detailList.get(position).get("recordId"));
        holder.cn_time.setText(detailList.get(position).get("time"));
        holder.cn_money.setText("¥"+detailList.get(position).get("amount"));
        String flag=detailList.get(position).get("flag");
        if (flag.equals("1")){
            holder.text_time.setText("充值时间");
            holder.text_amount.setText("预存款充值");
        }else if (flag.equals("-1")){
            holder.text_time.setText("消费时间");
            holder.text_amount.setText("预存款消费");
        }
        return convertView;
    }

    class ViewHolder {
        TextView cn_tradeno;
        TextView cn_money;
        TextView cn_time;
        TextView text_time;
        TextView text_amount;
    }
}
