package com.msht.minshengbao.adapter;

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
 * Created by hong on 2018/1/4.
 */

public class WaterIncomeAdapter extends BaseAdapter {

    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> incomeList = new ArrayList<HashMap<String, String>>();

    public WaterIncomeAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.incomeList=List;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        if (incomeList == null) {
            return 0;
        } else {
            return incomeList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (incomeList == null) {
            return null;
        } else {
            return incomeList.get(position);
        }
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
            convertView = mInflater.inflate(R.layout.item_water_income, null);
            holder.cn_content = (TextView) convertView.findViewById(R.id.id_tv_type);
            holder.cn_amount = (TextView) convertView.findViewById(R.id.id_tv_amount);
            holder.cn_time = (TextView) convertView.findViewById(R.id.id_tv_time);
            holder.cn_bucketNum=(TextView)convertView.findViewById(R.id.id_bucketNum);
            holder.cn_deliveryFlag=(TextView)convertView.findViewById(R.id.id_deliveryFlag);
            holder.cn_givefee=(TextView)convertView.findViewById(R.id.id_tv_giveFee) ;
            holder.layout_giveFee=convertView.findViewById(R.id.id_layout_givefee);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String type = incomeList.get(position).get("type");
        String amount=incomeList.get(position).get("amount");
        String payFee=incomeList.get(position).get("payFee");
        String giveFee=incomeList.get(position).get("giveFee");
        holder.cn_givefee.setText(giveFee);
        if (type.equals("1")){
            holder.cn_time.setText(incomeList.get(position).get("payTime"));
            holder.cn_content.setText(incomeList.get(position).get("payTypeName"));
            holder.cn_amount.setText("+"+payFee);
            if (!giveFee.equals("0.0")&&giveFee!=null&&(!giveFee.equals("null"))){
                holder.layout_giveFee.setVisibility(View.VISIBLE);
            }else {
                holder.layout_giveFee.setVisibility(View.GONE);
            }
        }else if (type.equals("2")){
            holder.cn_time.setText(incomeList.get(position).get("payTime"));
            String saleWaterQuantity=incomeList.get(position).get("saleWaterQuantity");
            String deliveryFlag=incomeList.get(position).get("deliveryFlag");
            holder.layout_giveFee.setVisibility(View.GONE);
            holder.cn_content.setText(saleWaterQuantity+"L纯净水");
            holder.cn_amount.setText("-" +amount);
            if (!deliveryFlag.equals("0")){
                holder.cn_bucketNum.setText("(配桶*"+incomeList.get(position).get("bucketNum")+")");
                holder.cn_bucketNum.setVisibility(View.VISIBLE);
                holder.cn_deliveryFlag.setVisibility(View.VISIBLE);
            }else {
                holder.cn_bucketNum.setVisibility(View.GONE);
                holder.cn_deliveryFlag.setVisibility(View.GONE);
            }
        }
        return convertView;
    }
    class ViewHolder {
        private TextView  cn_content;
        private TextView  cn_amount;
        private TextView  cn_time;
        private TextView  cn_deliveryFlag;
        private TextView  cn_bucketNum;
        private View      layout_giveFee;
        private TextView  cn_givefee;
    }
}
