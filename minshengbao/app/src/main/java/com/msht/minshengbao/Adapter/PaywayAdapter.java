package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.VariableUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/11/22.
 */

public class PaywayAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public OnRadioItemClickListener listener;
    public void SetOnItemClickListener(OnRadioItemClickListener listener){
        this.listener=listener;
    }
    public interface OnRadioItemClickListener{
        void ItemClick(View view, int thisPosition);
    }
    public PaywayAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
        final int thisposition=position;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_pay_way, null);
            holder.cn_balance=(TextView) convertView.findViewById(R.id.id_tv_balance);
            holder.cn_payway=(TextView)convertView.findViewById(R.id.id_pay_name);
            holder.cn_tip=(TextView)convertView.findViewById(R.id.id_tv_tip);
            holder.cn_radio=(RadioButton) convertView.findViewById(R.id.id_radio_balance);
            holder.layout_tip=convertView.findViewById(R.id.id_layout_tip);
            holder.wayimg=(ImageView)convertView.findViewById(R.id.id_pay_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String tip=mList.get(position).get("tips");
        String code=mList.get(position).get("code");
        if ((!tip.isEmpty())&&tip!=null&&(!tip.equals("null"))){
            holder.cn_tip.setText(tip);
            holder.layout_tip.setVisibility(View.VISIBLE);
        }else {
            holder.layout_tip.setVisibility(View.GONE);
        }
        holder.cn_payway.setText(mList.get(position).get("name"));
        if (code.equals("cmb_pay")){
            holder.cn_balance.setVisibility(View.GONE);
            holder.wayimg.setImageResource(R.drawable.yiwangtong_m);
        }else if (code.equals("wx_pay")){
            holder.cn_balance.setVisibility(View.GONE);
            holder.wayimg.setImageResource(R.drawable.wechat_pay_h);
        }else if (code.equals("ali_pay")){
            holder.cn_balance.setVisibility(View.GONE);
            holder.wayimg.setImageResource(R.drawable.alipay_h);
        }else if (code.equals("upacp_pay")){
            holder.cn_balance.setVisibility(View.GONE);
            holder.wayimg.setImageResource(R.drawable.yinlian_pay_h);
        }else if (code.equals("wallet_pay")){
            holder.cn_balance.setVisibility(View.VISIBLE);
            holder.wayimg.setImageResource(R.drawable.balance_pay_xh);
            holder.cn_balance.setText("( "+VariableUtil.balance+" )");
        }else if (code.equals("cash_pay")){
            holder.cn_balance.setVisibility(View.GONE);
            holder.wayimg.setImageResource(R.drawable.cash_pay_h);
        }
        if (VariableUtil.paypos!=position){
            holder.cn_radio.setChecked(false);
        }
        holder.cn_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.ItemClick(v,thisposition);
                }
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView cn_balance;
        TextView cn_payway;
        TextView cn_tip;
        View layout_tip;
        ImageView wayimg;
        RadioButton cn_radio;
    }
}
