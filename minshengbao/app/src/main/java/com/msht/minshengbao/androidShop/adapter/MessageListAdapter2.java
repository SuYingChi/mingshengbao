package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxy.dexlibs.ComplexRecyclerViewAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.WarnBean;
import com.msht.minshengbao.androidShop.util.GlideUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MessageListAdapter2 extends ComplexRecyclerViewAdapter {


    private int type;
    private int layoutId;

    public MessageListAdapter2(Context context, List list,int type) {
        super(context, list);
        this.type = type;
        if (type == 1||type==2) {
            layoutId = R.layout.item_warn;
        } else if ( type == 3) {
            layoutId = R.layout.item_wuliu;
        }else if (  type== 4){
            layoutId = R.layout.item_youhui;
        }
    }


    @Override
    protected View onBindLayout(LayoutInflater layoutInflater, ViewGroup parent, int i) {
       return inflater.inflate(layoutId, parent, false);
    }

    @Override
    protected void onBindViewData(Object o, ComplexViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final Object dataBean = datas.get(position);
        if (dataBean instanceof WarnBean.DataBean) {
            holder.setText(R.id.time, ((WarnBean.DataBean) dataBean).getTime());
            if (type == 1||type==2) {
                holder.setText(R.id.title, ((WarnBean.DataBean) dataBean).getTitle());
                TextView tv = holder.getView(R.id.desc);
                if (((WarnBean.DataBean) dataBean).getType() == 2) {
                    tv.setEllipsize(TextUtils.TruncateAt.END);
                    tv.setSingleLine(true);
                } else if (((WarnBean.DataBean) dataBean).getType() == 1) {
                    tv.setSingleLine(false);
                }
                tv.setText(((WarnBean.DataBean) dataBean).getContent());
            } else if (type == 3) {
                try {
                    final JSONObject obj = new JSONObject(((WarnBean.DataBean) dataBean).getContent());
                    holder.setText(R.id.title, obj.optString("msg"));
                    JSONArray objs = obj.optJSONArray("goods_list");
                    if(objs!=null&&objs.length()>0){
                        GlideUtil.loadRemoteImg(mContext,holder.getImageView(R.id.iv), obj.optJSONArray("goods_list").optJSONObject(0).optString("goods_image"));
                        holder.setText(R.id.goodname, obj.optJSONArray("goods_list").optJSONObject(0).optString("goods_name"));
                    }
                   holder.setText(R.id.goodtv, "物流单号：" + obj.optString("shipping_code"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    final JSONObject obj = new JSONObject(((WarnBean.DataBean) dataBean).getContent());
                    holder.setText(R.id.title, obj.optString("log_msg"));
                    TextView tv = holder.getView(R.id.desc);
                    tv.setSingleLine(false);
                    tv.setText("点击查看详情");
                   // tv.setText(obj.optString("log_msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (((WarnBean.DataBean) dataBean).getFlag() == 0) {
                holder.getView(R.id.point).setVisibility(View.VISIBLE);
            } else {
                holder.getView(R.id.point).setVisibility(View.INVISIBLE);
            }
        }
    }
}
