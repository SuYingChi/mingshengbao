package com.msht.minshengbao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.R;
import com.msht.minshengbao.custom.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/5/31.
 */

public class MasterEvaluateAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> goodList = new ArrayList<HashMap<String, String>>();
    public MasterEvaluateAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.context = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.goodList=List;
    }
    @Override
    public int getCount() {
        return goodList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Bitmap avatarimg=null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_evalute_info, null);
            holder.portrait=(CircleImageView) convertView.findViewById(R.id.id_portrait);
            holder.img_status=(ImageView) convertView.findViewById(R.id.id_img_status);
            holder.cn_username=(TextView) convertView.findViewById(R.id.id_tv_username);
            holder.cn_eval_info=(TextView) convertView.findViewById(R.id.id_evalute_info);
            holder.cn_time=(TextView) convertView.findViewById(R.id.id_evalute_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String eval_score=goodList.get(position).get("eval_score");
        String avatarurl=goodList.get(position).get("avatar");
        if (eval_score.equals("1")) {
            holder.img_status.setImageResource(R.drawable.star_one_h);
        }else if (eval_score.equals("2")){
            holder.img_status.setImageResource(R.drawable.star_two_h);
        }else if (eval_score.equals("3")){
            holder.img_status.setImageResource(R.drawable.star_three_h);
        }else if (eval_score.equals("4")){
            holder.img_status.setImageResource(R.drawable.star_four_h);
        }else if (eval_score.equals("5")){
            holder.img_status.setImageResource(R.drawable.star_five_h);
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.portrait_xh);
        Glide.with(context).load(avatarurl).apply(requestOptions)
                .into(holder.portrait);
       /* Glide
                .with(context)
                .load(avatarurl)
                .error(R.drawable.potrait)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//deactivate the disk cache for a request.
                .skipMemoryCache(true)//glide will not put image in the memory cache
                .into(holder.portrait);*/

        holder.cn_username.setText(goodList.get(position).get("username"));
        holder.cn_eval_info.setText(goodList.get(position).get("eval_info"));
        holder.cn_time.setText(goodList.get(position).get("time"));
        return convertView;
    }
    class ViewHolder {
        public CircleImageView portrait;
        public ImageView img_status;
        public TextView cn_username;
        public TextView        cn_time;
        public TextView        cn_eval_info;

    }
}
