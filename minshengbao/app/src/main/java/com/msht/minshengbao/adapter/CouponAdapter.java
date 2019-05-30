package com.msht.minshengbao.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.androidShop.activity.ShopSuccessActivity;
import com.msht.minshengbao.androidShop.customerview.RotateTextView;
import com.msht.minshengbao.functionActivity.MainActivity;

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

        void onGoShopHome();

        void onClikshowDesc(int position);

        void onUseServiceCoupon(String direct_url);
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
            holder.layoutBack =(LinearLayout)convertView.findViewById(R.id.id_layout_back);
            holder.cnName =(TextView)convertView.findViewById(R.id.id_title_name);
            holder.cnScope =(TextView) convertView.findViewById(R.id.id_scope);
            holder.cnAmount =(TextView) convertView.findViewById(R.id.id_amount);
            holder.remain_time =(TextView)convertView.findViewById(R.id.remain_time);
            holder.cnEndDate =(TextView) convertView.findViewById(R.id.id_end_date);
            holder.show_use_desc =(TextView) convertView.findViewById(R.id.show_use_desc);
            holder.use_desc =(TextView) convertView.findViewById(R.id.use_desc);
            holder.updown =(ImageView) convertView.findViewById(R.id.updown);
            holder.tvuse =(TextView)convertView.findViewById(R.id.use);
            holder.tvBelowAmount =(TextView)convertView.findViewById(R.id.below_amount);
            holder.id_effective_text =(TextView)convertView.findViewById(R.id.id_effective_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(tab==0) {
            holder.tvBelowAmount.setVisibility(View.GONE);
            final String type = haveuseList.get(position).get("type");
            String remainderDay = haveuseList.get(position).get("remainder_days");
            if (type.equals(VariableUtil.VALUE_ONE)) {
                holder.cnAmount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.white));
                holder.cnName.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.black));
                holder.layoutBack.setBackgroundResource(R.drawable.left_kaqun);
                holder.tvuse.setText("点击使用");
                holder.tvuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     listener.onUseServiceCoupon(haveuseList.get(position).get("direct_url"));
                    }
                });
                holder.tvuse.setClickable(true);
                holder.tvuse.setTextColor(Color.WHITE);
                holder.tvuse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                holder.tvuse.setBackgroundDrawable(MyApplication.getInstance().getResources().getDrawable(R.drawable.btn_yellow));
                if ((!TextUtils.isEmpty(remainderDay)) && (!remainderDay.equals(ConstantUtil.VALUE_ZERO))) {
                    String mDayText = "仅剩" + remainderDay + "天";
                    holder.remain_time.setVisibility(View.VISIBLE);
                    holder.remain_time.setText(mDayText);
                } else {
                    holder.remain_time.setVisibility(View.GONE);
                }
            } else if (type.equals(VariableUtil.VALUE_TWO)) {
                holder.cnAmount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.white));
                holder.cnName.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                holder.layoutBack.setBackgroundResource(R.drawable.left_used_kaqun);
                holder.tvuse.setText("已过期");
                holder.tvuse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                holder.tvuse.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                holder.tvuse.setClickable(false);
                holder.tvuse.setBackgroundResource(R.drawable.btn_cancle);
                holder.remain_time.setVisibility(View.GONE);
            } else if (type.equals(VariableUtil.VALUE_THREE)) {
                holder.cnAmount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.white));
                holder.cnName.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                holder.layoutBack.setBackgroundResource(R.drawable.left_used_kaqun);
                holder.tvuse.setText("已过期");
                holder.tvuse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                holder.tvuse.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                holder.tvuse.setClickable(false);
                holder.remain_time.setVisibility(View.GONE);
                holder.tvuse.setBackgroundResource(R.drawable.btn_cancle);
            }
            if("1".equals(haveuseList.get(position).get("show"))){
                holder.use_desc.setVisibility(View.VISIBLE);
                holder.updown.setImageDrawable(MyApplication.getMsbApplicationContext().getResources().getDrawable(R.drawable.shop_up_triangle));
            }else if("0".equals(haveuseList.get(position).get("show"))){
                holder.updown.setImageDrawable(MyApplication.getMsbApplicationContext().getResources().getDrawable(R.drawable.shop_down_triangle));
                holder.use_desc.setVisibility(View.GONE);
            }
            holder.show_use_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClikshowDesc(position);
                }
            });
            holder.updown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClikshowDesc(position);
                }
            });
            holder.cnName.setText(haveuseList.get(position).get("name"));
            holder.cnScope.setText(haveuseList.get(position).get("scope"));
            holder.cnAmount.setText("¥" + haveuseList.get(position).get("amount"));
            holder.cnEndDate.setText(haveuseList.get(position).get("end_date"));
            holder.id_effective_text.setText(haveuseList.get(position).get("start_date")+"  ~");
            holder.use_desc.setText(haveuseList.get(position).get("desc"));
        }else if(tab==1){
            String rpacket_state = haveuseList.get(position).get("rpacket_state");
            holder.cnScope.setText("商城通用");
            holder.tvBelowAmount.setVisibility(View.VISIBLE);
            switch (rpacket_state){
                case "1":
                    holder.cnAmount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.white));
                    holder.cnName.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.black));
                    holder.layoutBack.setBackgroundResource(R.drawable.left_kaqun);
                    holder.tvuse.setText("点击使用");
                    holder.tvuse.setClickable(true);
                    holder.tvuse.setTextColor(Color.WHITE);
                    holder.tvuse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    holder.tvuse.setBackgroundDrawable(MyApplication.getInstance().getResources().getDrawable(R.drawable.btn_yellow));
                    holder.tvuse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           listener.onGoShopHome();
                        }
                    });
                    holder.updown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onClikshowDesc(position);
                        }
                    });
                    if ((!TextUtils.isEmpty(haveuseList.get(position).get("left_days"))) && !"0".equals(haveuseList.get(position).get("left_days"))) {
                        String mDayText = "仅剩" + haveuseList.get(position).get("left_days") + "天";
                        holder.remain_time.setVisibility(View.VISIBLE);
                        holder.remain_time.setText(mDayText);
                    } else {
                        holder.remain_time.setVisibility(View.GONE);
                    }
                    break;
                case "2":
                    holder.cnAmount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.white));
                    holder.cnName.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                    holder.layoutBack.setBackgroundResource(R.drawable.left_used_kaqun);
                    holder.tvuse.setText("已过期");
                    holder.tvuse.setClickable(false);
                    holder.tvuse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    holder.tvuse.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                    holder.tvuse.setBackgroundResource(R.drawable.btn_cancle);
                    holder.remain_time.setVisibility(View.GONE);
                    break;
                case "3":
                    holder.cnAmount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.white));
                    holder.cnName.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                    holder.layoutBack.setBackgroundResource(R.drawable.left_used_kaqun);
                    holder.tvuse.setText("已过期");
                    holder.tvuse.setClickable(false);
                    holder.tvuse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    holder.tvuse.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                    holder.tvuse.setBackgroundResource(R.drawable.btn_cancle);
                    holder.remain_time.setVisibility(View.GONE);
                    break;
                default:break;
            }
            holder.cnName.setText(haveuseList.get(position).get("rpacket_title"));
            holder.cnEndDate.setText(haveuseList.get(position).get("rpacket_end_date_text"));
            holder.cnAmount.setText("¥" + haveuseList.get(position).get("rpacket_price"));
            holder.tvBelowAmount.setText("满"+ haveuseList.get(position).get("rpacket_limit")+"¥可用");
            if("1".equals(haveuseList.get(position).get("show"))){
                holder.use_desc.setVisibility(View.VISIBLE);
                holder.updown.setImageDrawable(MyApplication.getMsbApplicationContext().getResources().getDrawable(R.drawable.shop_up_triangle));
                  }else if("0".equals(haveuseList.get(position).get("show"))){
                holder.updown.setImageDrawable(MyApplication.getMsbApplicationContext().getResources().getDrawable(R.drawable.shop_down_triangle));
                holder.use_desc.setVisibility(View.GONE);
            }
            holder.show_use_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClikshowDesc(position);
                }
            });
            holder.updown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClikshowDesc(position);
                }
            });
            holder.id_effective_text.setText(haveuseList.get(position).get("rpacket_start_date_text")+"  ~");
            holder.use_desc.setText(haveuseList.get(position).get("rpacket_desc"));
        } else if(tab==2){
            String voucher_state = haveuseList.get(position).get("voucher_state");
            switch (voucher_state){
                case "1":
                    holder.cnAmount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.white));
                    holder.cnName.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.black));
                    holder.layoutBack.setBackgroundResource(R.drawable.left_kaqun);
                    holder.tvuse.setClickable(true);
                    holder.tvuse.setText("点击使用");
                    holder.tvuse.setTextColor(Color.WHITE);
                    holder.tvuse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    holder.tvuse.setBackgroundDrawable(MyApplication.getInstance().getResources().getDrawable(R.drawable.btn_yellow));
                    holder.tvuse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         listener.onClickVoucher(haveuseList.get(position).get("store_id"));
                        }
                    });
                    if ((!TextUtils.isEmpty(haveuseList.get(position).get("left_days"))) && !"0".equals(haveuseList.get(position).get("left_days"))) {
                        String mDayText = "仅剩" + haveuseList.get(position).get("left_days") + "天";
                        holder.remain_time.setVisibility(View.VISIBLE);
                        holder.remain_time.setText(mDayText);
                    } else {
                        holder.remain_time.setVisibility(View.GONE);
                    }
                    break;
                case "2":
                    holder.cnAmount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.white));
                    holder.cnName.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                    holder.layoutBack.setBackgroundResource(R.drawable.left_used_kaqun);
                    holder.tvuse.setText("已过期");
                    holder.tvuse.setClickable(false);
                    holder.tvuse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    holder.tvuse.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                    holder.tvuse.setBackgroundResource(R.drawable.btn_cancle);
                    holder.remain_time.setVisibility(View.GONE);
                    break;
                case "3":
                    holder.cnAmount.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.white));
                    holder.cnName.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                    holder.layoutBack.setBackgroundResource(R.drawable.left_used_kaqun);
                    holder.tvuse.setText("已过期");
                    holder.tvuse.setClickable(false);
                    holder.tvuse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    holder.tvuse.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.shop_grey));
                    holder.tvuse.setBackgroundResource(R.drawable.btn_cancle);
                    holder.remain_time.setVisibility(View.GONE);
                    break;
                    default:break;
            }
            if("1".equals(haveuseList.get(position).get("show"))){
                holder.use_desc.setVisibility(View.VISIBLE);
                holder.updown.setImageDrawable(MyApplication.getMsbApplicationContext().getResources().getDrawable(R.drawable.shop_up_triangle));
            }else if("0".equals(haveuseList.get(position).get("show"))){
                holder.updown.setImageDrawable(MyApplication.getMsbApplicationContext().getResources().getDrawable(R.drawable.shop_down_triangle));
                holder.use_desc.setVisibility(View.GONE);
            }
            holder.show_use_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClikshowDesc(position);
                }
            });
            holder.updown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClikshowDesc(position);
                }
            });
            holder.cnScope.setText("买满"+haveuseList.get(position).get("voucher_limit")+"可用");
            holder.tvBelowAmount.setText("买满"+haveuseList.get(position).get("voucher_limit")+"可用");
            holder.cnName.setText(haveuseList.get(position).get("store_name"));
            holder.cnEndDate.setText(haveuseList.get(position).get("voucher_end_date_text"));
            holder.cnAmount.setText("¥" + haveuseList.get(position).get("voucher_price"));
            holder.id_effective_text.setText(haveuseList.get(position).get("voucher_start_date_text")+"  ~");
            holder.use_desc.setText(haveuseList.get(position).get("voucher_des"));
        }
            return convertView;
    }
    class ViewHolder {
        LinearLayout layoutBack;
        TextView cnName;
        TextView cnScope;
        TextView cnAmount;
        TextView remain_time;
        TextView cnEndDate;
        TextView use_desc;
        TextView show_use_desc;
        ImageView updown;
        TextView tvuse;
        TextView tvBelowAmount;
        TextView id_effective_text;

    }


}
