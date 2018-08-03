package com.msht.minshengbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.ButtonUI.ButtonM;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * lpg订单列表
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/12 
 */
public class LpgOrderListAdapter extends BaseAdapter {

    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    public OnItemSelectListener listener;

    public interface OnItemSelectListener{
        /**
         * 选择付款
         * @param view
         * @param thisPosition
         */
        void  onSelectItemClick(View view,int thisPosition);
    }
    public void setOnItemSelectListener(OnItemSelectListener listener){
        this.listener=listener;
    }
    public LpgOrderListAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.orderList=list;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (orderList == null) {
            return 0;
        } else {
            return orderList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (orderList == null) {
            return null;
        } else {
            return orderList.get(position);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  int thisPosition=position;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_lpg_order, null);
            holder.tvOrderNo = (TextView) convertView.findViewById(R.id.id_tv_orderNo);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.id_tv_status);
            holder.tvFiveWeightText = (TextView) convertView.findViewById(R.id.id_five_weight_text);
            holder.tvFifteenWeightText=(TextView)convertView.findViewById(R.id.id_fifteen_weight_text);
            holder.tvFiftyWeightText=(TextView)convertView.findViewById(R.id.id_fifty_weight_text);
            holder.tvFiveWeightNum=(TextView)convertView.findViewById(R.id.id_five_weight_num);
            holder.tvFifteenWeightNum=(TextView)convertView.findViewById(R.id.id_fifteen_weight_num);
            holder.tvFiftyWeightNum=(TextView)convertView.findViewById(R.id.id_fifty_weight_num);
            holder.btnPay=(ButtonM) convertView.findViewById(R.id.id_btn_pay);
            holder.tvTotalAmount=(TextView) convertView.findViewById(R.id.id_total_amount);
            holder.layoutFive=convertView.findViewById(R.id.id_layout_five);
            holder.layoutFifteen=convertView.findViewById(R.id.id_layout_fifteen);
            holder.layoutFifty=convertView.findViewById(R.id.id_layout_fifty);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String orderId=orderList.get(position).get("orderId");
        String fiveBottleCount=orderList.get(position).get("fiveBottleCount");
        String fifteenBottleCount=orderList.get(position).get("fifteenBottleCount");
        String fiftyBottleCount=orderList.get(position).get("fiftyBottleCount");
        String realAmount="¥"+orderList.get(position).get("realAmount");
        String orderStatus=orderList.get(position).get("orderStatus");
        holder.tvTotalAmount.setText(realAmount);
        holder.tvOrderNo.setText(orderId);
        if (fiveBottleCount.equals(VariableUtil.VALUE_ZERO)){
            holder.layoutFive.setVisibility(View.GONE);
        }else {
            holder.layoutFive.setVisibility(View.VISIBLE);
            String fiveNumText="x"+fiveBottleCount;
            holder.tvFiveWeightNum.setText(fiveNumText);
        }
        if (fifteenBottleCount.equals(VariableUtil.VALUE_ZERO)){
            holder.layoutFifteen.setVisibility(View.GONE);
        }else {
            holder.layoutFifteen.setVisibility(View.VISIBLE);
            String fifteenNumText="x"+fifteenBottleCount;
            holder.tvFifteenWeightNum.setText(fifteenNumText);
        }
        if (fiftyBottleCount.equals(VariableUtil.VALUE_ZERO)){
            holder.layoutFifty.setVisibility(View.GONE);
        }else {
            holder.layoutFifty.setVisibility(View.VISIBLE);
            String fiftyNumText="x"+fiftyBottleCount;
            holder.tvFiftyWeightNum.setText(fiftyNumText);
        }
        holder.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onSelectItemClick(v,thisPosition);
                }
            }
        });
        switch (orderStatus){
            case VariableUtil.VALUE_ZERO:
                holder.tvStatus.setText("待支付");
                holder.btnPay.setVisibility(View.VISIBLE);
                break;
            case VariableUtil.VALUE_ONE:
                holder.tvStatus.setText("待发货");
                holder.btnPay.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_TWO:
                holder.tvStatus.setText("已发货");
                holder.btnPay.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_THREE:
                holder.tvStatus.setText("已完成");
                holder.btnPay.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_FOUR:
                holder.tvStatus.setText("已退款");
                holder.btnPay.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_FIVE:
                holder.tvStatus.setText("已取消");
                holder.btnPay.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_SIX:
                holder.tvStatus.setText("待收货");
                holder.btnPay.setVisibility(View.GONE);
                break;
            case VariableUtil.VALUE_SEVER:
                holder.tvStatus.setText("已收货");
                holder.btnPay.setVisibility(View.GONE);
                break;
                default:
                    break;
        }
        return convertView;
    }
    class ViewHolder {
        TextView  tvOrderNo;
        TextView  tvStatus;
        TextView  tvFiveWeightText;
        TextView  tvFifteenWeightText;
        TextView  tvFiftyWeightText;
        TextView  tvFiveWeightNum;
        TextView  tvFifteenWeightNum;
        TextView  tvFiftyWeightNum;
        TextView  tvTotalAmount;
        View      layoutFive;
        View      layoutFifteen;
        View      layoutFifty;
        ButtonM   btnPay;
    }
}
