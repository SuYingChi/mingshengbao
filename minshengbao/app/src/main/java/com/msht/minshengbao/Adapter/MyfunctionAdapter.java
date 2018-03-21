package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/5/15.
 */

public class MyfunctionAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, Integer>> mList = new ArrayList<HashMap<String, Integer>>();
    public MyfunctionAdapter(Context context, ArrayList<HashMap<String, Integer>> List) {
        super();
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext=context;
        this.mList=List;
    }
    @Override
    public int getCount() {
        if (mList!=null){
            return mList.size();
        }else {
            return 0;
        }
    }
    @Override
    public Object getItem(int position) {
        if (mList!=null){
            return mList.get(position);
        }else {
            return null;
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
            holder =new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_myfunction_view, null);
            holder.img_function=(ImageView)convertView.findViewById(R.id.id_function_img);
            holder.tv_name=(TextView) convertView.findViewById(R.id.id_function_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int integer=mList.get(position).get("code");
        if (integer==0){
            holder.img_function.setImageResource(R.drawable.mywallet_xh);
            holder.tv_name.setText("我的账号");
        }else if (integer==1){
            holder.img_function.setImageResource(R.drawable.gas_server_xh);
            holder.tv_name.setText("燃气工单");
        }else if (integer==2){
            holder.img_function.setImageResource(R.drawable.gas_customerno_xh);
            holder.tv_name.setText("燃气用户号");
        }else if (integer==3){
            holder.img_function.setImageResource(R.drawable.invoice_xh);
            holder.tv_name.setText("发票申请");
        }else if (integer==4){
            holder.img_function.setImageResource(R.drawable.address_manage_xh);
            holder.tv_name.setText("地址管理");
        }else if (integer==5){
            holder.img_function.setImageResource(R.drawable.share_xh);
            holder.tv_name.setText("分享应用");
        }else if (integer==6){
            holder.img_function.setImageResource(R.drawable.share_xh);
        }
        return convertView;
    }
    class ViewHolder {
        public ImageView img_function;
        public TextView  tv_name;
    }
}
