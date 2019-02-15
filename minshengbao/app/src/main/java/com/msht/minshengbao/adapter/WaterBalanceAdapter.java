package com.msht.minshengbao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterBalanceAdapter extends BaseAdapter {

    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> incomeList = new ArrayList<HashMap<String, String>>();

    public WaterBalanceAdapter(Context context, ArrayList<HashMap<String, String>> List) {
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
            convertView = mInflater.inflate(R.layout.item_water_balance_detail, null);
            holder.cnContent = (TextView) convertView.findViewById(R.id.id_tv_type);
            holder.cnAmount = (TextView) convertView.findViewById(R.id.id_tv_amount);
            holder.cnTime = (TextView) convertView.findViewById(R.id.id_create_time);
            holder.cnPayType=(TextView)convertView.findViewById(R.id.id_payType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String type = incomeList.get(position).get("type");
        String payTypeName=incomeList.get(position).get("payTypeName");
        String createTime=incomeList.get(position).get("createTime");
        String childType=incomeList.get(position).get("childType");
        String amountText=incomeList.get(position).get("amount");
        holder.cnTime.setText(createTime);
        holder.cnPayType.setText(payTypeName);
        switch (type){
            case ConstantUtil.VALUE_ONE:
                holder.cnContent.setText("余额充值");
                amountText="+"+amountText;
                holder.cnAmount.setTextColor(Color.parseColor("#FF30A949"));
                break;
            case ConstantUtil.VALUE_TWO:
                holder.cnContent.setText("扫码购水");
                amountText="-"+amountText;
                holder.cnAmount.setTextColor(Color.parseColor("#FFF96331"));
                break;
            case ConstantUtil.VALUE_THREE:
                holder.cnContent.setText("余额退款");
                amountText="+"+amountText;
                holder.cnAmount.setTextColor(Color.parseColor("#FF30A949"));
                break;
            case ConstantUtil.VALUE_FOUR:
                holder.cnContent.setText("活动赠送");
                amountText="+"+amountText;
                holder.cnAmount.setTextColor(Color.parseColor("#FF30A949"));
                break;
            case ConstantUtil.VALUE_FIVE:
                if (childType.equals(ConstantUtil.VALUE_TWO)){
                    holder.cnContent.setText("充值退款");
                    amountText="-"+amountText;
                    holder.cnAmount.setTextColor(Color.parseColor("#FF30A949"));
                }else {
                    holder.cnContent.setText("消费退款");
                    amountText="+"+amountText;
                    holder.cnAmount.setTextColor(Color.parseColor("#FFF96331"));
                }
                break;
            case ConstantUtil.VALUE_SIX:
                amountText="+"+amountText;
                holder.cnContent.setText("抽奖赠送");
                holder.cnAmount.setTextColor(Color.parseColor("#FF30A949"));
                break;
            case ConstantUtil.VALUE_SEVER:
                amountText="+"+amountText;
                holder.cnContent.setText("活动赠送");
                holder.cnAmount.setTextColor(Color.parseColor("#FF30A949"));

                break;
            case ConstantUtil.VALUE_EIGHT:
                amountText="+"+amountText;
                holder.cnContent.setText("体验充值");
                holder.cnAmount.setTextColor(Color.parseColor("#FF30A949"));
                break;
                default:
                    holder.cnContent.setText("送水到家");
                    holder.cnAmount.setTextColor(Color.parseColor("#FF30A949"));
                    break;
        }
        holder.cnAmount.setText(amountText);
        return convertView;
    }
    class ViewHolder {
        private TextView  cnContent;
        private TextView  cnAmount;
        private TextView  cnTime;
        private TextView  cnPayType;
    }
}
