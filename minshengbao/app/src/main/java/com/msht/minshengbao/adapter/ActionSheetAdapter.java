package com.msht.minshengbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/10  
 */
public class ActionSheetAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private String[] mList;
    private ArrayList<HashMap<String, Integer>> integerList = new ArrayList<HashMap<String,Integer>>();
    public ActionSheetAdapter(Context context, String[] list) {
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mList=list;
    }
    @Override
    public int getCount() {
        if (mList!=null){
            return mList.length;
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (mList!=null){
            return mList[position];
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
            convertView = mInflater.inflate(R.layout.item_my_actionsheet, null);
            holder.btnText=(TextView) convertView.findViewById(R.id.id_btn_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String code=mList[position];
        holder.btnText.setText(code);
        return convertView;
    }
    class ViewHolder {
        TextView btnText;
    }
}
