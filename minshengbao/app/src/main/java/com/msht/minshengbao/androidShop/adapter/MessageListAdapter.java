package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.ShopKeywordListActivity;
import com.msht.minshengbao.androidShop.shopBean.WarnBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageListAdapter extends MyHaveHeadViewRecyclerAdapter<WarnBean.DataBean> {


    private int type;

    public MessageListAdapter(Context context, int layoutId) {
        super(context, layoutId);
        if (layoutId == R.layout.item_warn) {
            type = 1;
        } else if (layoutId == R.layout.item_wuliu) {
            type = 2;
        }else {
            type= 3;
        }
    }

    @Override
    public void convert(RecyclerHolder holder, final WarnBean.DataBean dataBean, final int position) {
        holder.setIsRecyclable(false);
        holder.setText(R.id.time, dataBean.getTime());
        if (type == 1) {
            holder.setText(R.id.title, dataBean.getTitle());
            TextView tv = holder.getView(R.id.desc);
            if (dataBean.getType() == 2) {
                tv.setEllipsize(TextUtils.TruncateAt.END);
                tv.setSingleLine(true);
            } else if (dataBean.getType() == 1) {
                tv.setSingleLine(false);
            }
            tv.setText(dataBean.getContent());
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        onItemClickListener.onItemClick(position);
                }
            });
        }else if(type == 2){
            try {
                final JSONObject obj = new JSONObject(dataBean.getContent());
                holder.setText(R.id.title,obj.optString("msg"));
                holder.setImage(R.id.iv,obj.optJSONArray("goods_list").optJSONObject(0).optString("goods_image"));
                holder.setText(R.id.goodname,obj.optJSONArray("goods_list").optJSONObject(0).optString("goods_name"));
                holder.setText(R.id.goodtv,"物流单号："+obj.optString("shipping_code"));
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(Integer.valueOf(obj.optString("order_id")));
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            try {
                final JSONObject obj = new JSONObject(dataBean.getContent());
                holder.setText(R.id.title, obj.optString("log_msg"));
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      Intent intent =  new Intent(context, ShopKeywordListActivity.class);
                      intent.putExtra("keyword",obj.optString("log_type_v"));
                       context.startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.setText(R.id.desc,dataBean.getContent());
        }
        if(dataBean.getFlag()==0) {
            holder.getView(R.id.point).setVisibility(View.VISIBLE);
        }else {
            holder.getView(R.id.point).setVisibility(View.INVISIBLE);
        }
    }
}
