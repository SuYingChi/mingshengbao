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
 * Created by hong on 2017/3/22.
 */

public class PayRecordAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();
    public PayRecordAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.recordList=List;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return recordList.size();
    }
    @Override
    public Object getItem(int position) {
        return recordList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_pay_record, null);
            holder.cn_money=(TextView) convertView.findViewById(R.id.id_amonut);
            holder.cn_payway=(TextView)convertView.findViewById(R.id.id_pay_way);
            holder.cn_time=(TextView) convertView.findViewById(R.id.id_tv_time);
            holder.cn_writecard_state=(TextView)convertView.findViewById(R.id.id_writecard_state);
            holder.tv_text1=(TextView)convertView.findViewById(R.id.id_text1);
            holder.tv_text2=(TextView)convertView.findViewById(R.id.id_text2);
            holder.tv_text3=(TextView)convertView.findViewById(R.id.id_text3);
            holder.tv_text4=(TextView)convertView.findViewById(R.id.id_text4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cn_money.setText("¥"+recordList.get(position).get("amount")+"元");
        holder.cn_time.setText(recordList.get(position).get("pay_time"));
        holder.cn_payway.setText(recordList.get(position).get("pay_method"));
        String writecard_state=recordList.get(position).get("writecard_state");
        if (!writecard_state.equals("0")){
            holder.cn_writecard_state.setText(writecard_state);
            holder.tv_text1.setText("充值金额");
            holder.tv_text2.setText("充值渠道");
            holder.tv_text3.setText("充值时间");
            holder.tv_text4.setVisibility(View.VISIBLE);
            holder.cn_writecard_state.setVisibility(View.VISIBLE);
        }else {
            holder.cn_writecard_state.setText(writecard_state);
            holder.tv_text1.setText("缴费金额");
            holder.tv_text2.setText("缴费渠道");
            holder.tv_text3.setText("缴费时间");
            holder.tv_text4.setVisibility(View.GONE);
            holder.cn_writecard_state.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder {
        TextView cn_money;
        TextView cn_payway;
        TextView cn_time;
        TextView cn_writecard_state;
        TextView tv_text1;
        TextView tv_text2;
        TextView tv_text3;
        TextView tv_text4;
    }
}
