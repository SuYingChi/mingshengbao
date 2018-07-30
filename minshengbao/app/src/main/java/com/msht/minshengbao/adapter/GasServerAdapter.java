package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/3/28.
 */

public class GasServerAdapter  extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, String>> seviceList = new ArrayList<HashMap<String, String>>();
    private LayoutInflater mInflater = null;
    public GasServerAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.seviceList=List;
        this.mContext=context;
    }
    @Override
    public int getCount() {
        return seviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return seviceList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_my_servirce, null);
            holder.service_img=(ImageView) convertView.findViewById(R.id.id_img_serveTYPE);
            holder.bn_status=(Button) convertView.findViewById(R.id.id_btn_dealwith);
            holder.cn_title=(TextView) convertView.findViewById(R.id.id_tv_serveTYPE);
            holder.cn_time=(TextView) convertView.findViewById(R.id.id_tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String type=seviceList.get(position).get("type");
        String status=seviceList.get(position).get("status");
        if (type.equals("6")||type.equals("7")) {
            holder.service_img.setImageResource(R.drawable.homepage_gas_tousu3x);
        }else if (type.equals("5")){
            holder.service_img.setImageResource(R.drawable.homepage_gas_baoxiu3x);
        }else if (type.equals("3")){
            holder.service_img.setImageResource(R.drawable.homepage_gas_dianhuo3x);
        }
        if (status.equals("1")) {
            holder.bn_status.setText("待受理");
            holder.bn_status.setTextColor(Color.parseColor("#fff96331"));
            holder.bn_status.setBackgroundResource(R.drawable.shape_orange_corner_boder_inwithte);
        }else if (status.equals("2")){
            holder.bn_status.setText("已取消");
            holder.bn_status.setTextColor(Color.parseColor("#fff96331"));
            holder.bn_status.setBackgroundResource(R.drawable.shape_orange_corner_boder_inwithte);

        }else if (status.equals("3")){
            holder.bn_status.setText("已驳回");
            holder.bn_status.setTextColor(Color.parseColor("#fff96331"));
            holder.bn_status.setBackgroundResource(R.drawable.shape_orange_corner_boder_inwithte);
        }else if (status.equals("4")){
            holder.bn_status.setText("已受理");
            holder.bn_status.setTextColor(Color.parseColor("#fff96331"));
            holder.bn_status.setBackgroundResource(R.drawable.shape_orange_corner_boder_inwithte);
        }else if (status.equals("5")){
            holder.bn_status.setText("待支付");
            holder.bn_status.setTextColor(Color.parseColor("#fff96331"));
            holder.bn_status.setBackgroundResource(R.drawable.shape_orange_corner_boder_inwithte);
        }else if (status.equals("6")){
            holder.bn_status.setText("待评价");
            holder.bn_status.setTextColor(Color.parseColor("#fff96331"));
            holder.bn_status.setBackgroundResource(R.drawable.shape_orange_corner_boder_inwithte);
        }else if (status.equals("7")){
            holder.bn_status.setTextColor(Color.parseColor("#ffbfbfbf"));
            holder.bn_status.setText("已完成");
            holder.bn_status.setBackgroundResource(R.drawable.shape_gray_corner_boder_inwhite);
        }else if (status.equals("8")){
            holder.bn_status.setText("待生成账单");
            holder.bn_status.setTextColor(Color.parseColor("#fff96331"));
            holder.bn_status.setBackgroundResource(R.drawable.shape_orange_corner_boder_inwithte);
        }
        holder.cn_title.setText(seviceList.get(position).get("title"));
        holder.cn_time.setText(seviceList.get(position).get("time"));
        return convertView;
    }
    class ViewHolder {
        public ImageView service_img;
        public Button bn_status;
        public TextView cn_title;
        public TextView  cn_time;
    }
}
