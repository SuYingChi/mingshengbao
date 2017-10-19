package com.msht.minshengbao.MoveSelectAddress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hong on 2017/3/8.
 */

public class PoiSearchAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>>  mList = new ArrayList<HashMap<String, String>>();
    private Context mContext;
    private final int RESOURCE = R.layout.item_app_list_poi;

    public PoiSearchAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        this.mContext=context;
        this.mList=List;
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
        ViewHolder vh = null;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(RESOURCE, parent, false);
            vh.tv_title = (TextView) convertView.findViewById(R.id.address);
            vh.tv_text = (TextView) convertView.findViewById(R.id.addressDesc);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_title.setText(mList.get(position).get("title"));
        vh.tv_text.setText(mList.get(position).get("mContent"));
        return convertView;
    }
    private class ViewHolder{
        public TextView tv_title;
        public TextView tv_text;
    }
}
