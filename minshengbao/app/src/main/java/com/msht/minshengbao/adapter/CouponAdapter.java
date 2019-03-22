package com.msht.minshengbao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.VariableUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/4/1.
 */

public class CouponAdapter extends BaseAdapter {

    private int tab;
    private ArrayList<HashMap<String, String>> haveuseList = new ArrayList<HashMap<String, String>>();
    private LayoutInflater mInflater = null;
    public OnClickVoucherListener listener;

    public CouponAdapter(Context context, ArrayList<HashMap<String, String>> list, int position) {
        super();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.haveuseList=list;
        this.tab = position;
    }

    public void setOnClickVoucherListener(OnClickVoucherListener listener){
        this.listener=listener;
    }
    public interface OnClickVoucherListener{
        /**
         *
         * @param storeId
         */
        void onClickVoucher(String storeId);
    }
    @Override
    public int getCount() {
        return haveuseList.size();
    }
    @Override
    public Object getItem(int position) {
        return haveuseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_dicount_coupon, null);
            holder.layoutBack =(RelativeLayout)convertView.findViewById(R.id.id_layout_back);
            holder.cnName =(TextView)convertView.findViewById(R.id.id_title_name);
            holder.cnScope =(TextView) convertView.findViewById(R.id.id_scope);
            holder.cnAmount =(TextView) convertView.findViewById(R.id.id_amount);
            holder.cnUseLimit =(TextView) convertView.findViewById(R.id.id_use_limit);
            holder.cnTime =(TextView)convertView.findViewById(R.id.id_time);
            holder.cnEndDate =(TextView) convertView.findViewById(R.id.id_end_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(tab==0) {
            final String type = haveuseList.get(position).get("type");
            String remainderDay = haveuseList.get(position).get("remainder_days");
            if (type.equals(VariableUtil.VALUE_ONE)) {
                holder.cnAmount.setTextColor(Color.parseColor("#F5BC33"));
                holder.cnName.setTextColor(Color.parseColor("#F5BC33"));
                holder.layoutBack.setBackgroundResource(R.drawable.dicount_coupon_2xh);
            } else if (type.equals(VariableUtil.VALUE_TWO)) {
                holder.cnAmount.setTextColor(Color.parseColor("#FF383838"));
                holder.cnName.setTextColor(Color.parseColor("#FF383838"));
                holder.layoutBack.setBackgroundResource(R.mipmap.coupon_haveused_2xh);
            } else if (type.equals(VariableUtil.VALUE_THREE)) {
                holder.cnAmount.setTextColor(Color.parseColor("#FF383838"));
                holder.cnName.setTextColor(Color.parseColor("#FF383838"));
                holder.layoutBack.setBackgroundResource(R.mipmap.coupon_exceed_2xh);
            }
            if ((!TextUtils.isEmpty(remainderDay)) && (!remainderDay.equals(ConstantUtil.VALUE_ZERO))) {
                String mDayText = "剩" + remainderDay + "天";
                holder.cnTime.setVisibility(View.VISIBLE);
                holder.cnTime.setText(mDayText);
            } else {
                holder.cnTime.setVisibility(View.GONE);
            }
            String limitUse = "买满" + haveuseList.get(position).get("use_limit") + "元可用";
            holder.cnName.setText(haveuseList.get(position).get("name"));
            holder.cnScope.setText(haveuseList.get(position).get("scope"));
            holder.cnAmount.setText("¥" + haveuseList.get(position).get("amount"));
            holder.cnUseLimit.setText(limitUse);
            holder.cnEndDate.setText(haveuseList.get(position).get("end_date"));
        }else if(tab==1){
            String voucher_state = haveuseList.get(position).get("voucher_state");
            holder.cnUseLimit.setVisibility(View.GONE);
            switch (voucher_state){
                case "1":
                    holder.cnAmount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.msb_color));
                    holder.cnName.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.msb_color));
                    holder.layoutBack.setBackgroundResource(R.drawable.dicount_coupon_2xh);
                    holder.cnTime.setVisibility(View.VISIBLE);
                    holder.cnTime.setText("点击使用");
                    holder.cnTime.setTextColor(Color.WHITE);
                    holder.cnTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    holder.cnTime.setBackgroundDrawable(MyApplication.getInstance().getResources().getDrawable(R.drawable.btn_red));
                    holder.cnTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         listener.onClickVoucher(haveuseList.get(position).get("store_id"));
                        }
                    });
                    break;
                case "2":
                    holder.cnAmount.setTextColor(Color.parseColor("#FF383838"));
                    holder.cnName.setTextColor(Color.parseColor("#FF383838"));
                    holder.layoutBack.setBackgroundResource(R.mipmap.coupon_haveused_2xh);
                    holder.cnTime.setVisibility(View.GONE);
                    break;
                case "3":
                    holder.cnAmount.setTextColor(Color.parseColor("#FF383838"));
                    holder.cnName.setTextColor(Color.parseColor("#FF383838"));
                    holder.layoutBack.setBackgroundResource(R.mipmap.coupon_exceed_2xh);
                    holder.cnTime.setVisibility(View.GONE);
                    break;
                    default:break;
            }
            holder.cnName.setText(haveuseList.get(position).get("store_name"));
            holder.cnEndDate.setText(haveuseList.get(position).get("voucher_end_date_text"));
            String limitUse = "买满" + haveuseList.get(position).get("voucher_limit") + "元可用";
            holder.cnScope.setText(limitUse);
            holder.cnAmount.setText("¥" + haveuseList.get(position).get("voucher_price"));
        }
            return convertView;
    }
    class ViewHolder {
        RelativeLayout layoutBack;
        TextView cnName;
        TextView cnScope;
        TextView cnAmount;
        TextView cnUseLimit;
        TextView cnTime;
        TextView cnEndDate;
    }


}
