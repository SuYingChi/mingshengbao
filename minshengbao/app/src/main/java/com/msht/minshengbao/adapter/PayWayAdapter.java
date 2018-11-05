package com.msht.minshengbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.VariableUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hong
 * @date 2017/11/22
 */

public class PayWayAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public OnRadioItemClickListener listener;
    public void setOnItemClickListener(OnRadioItemClickListener listener){
        this.listener=listener;
    }
    public interface OnRadioItemClickListener{
        /**
         * 选择类型
         * @param view  视图
         * @param thisPosition  下标
         */
        void itemClick(View view, int thisPosition);
    }
    public PayWayAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        super();
        this.mList =list;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        final int thisPosition=position;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_pay_way, null);
            holder.cnBalance =(TextView) convertView.findViewById(R.id.id_tv_balance);
            holder.cnPayway =(TextView)convertView.findViewById(R.id.id_pay_name);
            holder.cnTip =(TextView)convertView.findViewById(R.id.id_tv_tip);
            holder.cnRadio =(RadioButton) convertView.findViewById(R.id.id_radio_balance);
            holder.layoutTip =convertView.findViewById(R.id.id_layout_tip);
            holder.wayImg =(ImageView)convertView.findViewById(R.id.id_pay_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String tip=mList.get(position).get("tips");
        String code=mList.get(position).get("code");
        if ((!tip.isEmpty())&&(!tip.equals(ConstantUtil.NULL_VALUE))){
            holder.cnTip.setText(tip);
            holder.layoutTip.setVisibility(View.VISIBLE);
        }else {
            holder.layoutTip.setVisibility(View.GONE);
        }
        holder.cnPayway.setText(mList.get(position).get("name"));
        if (code.equals("cmb_pay")){
            holder.cnBalance.setVisibility(View.GONE);
            holder.wayImg.setImageResource(R.drawable.yiwangtong_m);
        }else if (code.equals("wx_pay")){
            holder.cnBalance.setVisibility(View.GONE);
            holder.wayImg.setImageResource(R.drawable.wechat_pay_h);
        }else if (code.equals("ali_pay")){
            holder.cnBalance.setVisibility(View.GONE);
            holder.wayImg.setImageResource(R.drawable.alipay_h);
        }else if (code.equals("upacp_pay")){
            holder.cnBalance.setVisibility(View.GONE);
            holder.wayImg.setImageResource(R.drawable.yinlian_pay_h);
        }else if (code.equals("wallet_pay")){
            holder.cnBalance.setVisibility(View.VISIBLE);
            holder.wayImg.setImageResource(R.drawable.balance_pay_xh);
            holder.cnBalance.setText("( "+VariableUtil.balance+" )");
        }else if (code.equals("cash_pay")){
            holder.cnBalance.setVisibility(View.GONE);
            holder.wayImg.setImageResource(R.drawable.cash_pay_h);
        }else if (code.equals(ConstantUtil.UPACP_DIRECT)){
            holder.cnBalance.setVisibility(View.GONE);
            holder.wayImg.setImageResource(R.drawable.yinlian_pay_h);
        }else if (code.equals(ConstantUtil.BEST_PAY)){
            holder.cnBalance.setVisibility(View.GONE);
            holder.wayImg.setImageResource(R.drawable.best_pay_xh);
        }
        if (VariableUtil.payPos !=position){
            holder.cnRadio.setChecked(false);
        }else {
            holder.cnRadio.setChecked(true);
        }
        holder.cnRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.itemClick(v,thisPosition);
                }
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView cnBalance;
        TextView cnPayway;
        TextView cnTip;
        View layoutTip;
        ImageView wayImg;
        RadioButton cnRadio;
    }
}
