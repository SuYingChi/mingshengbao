package com.msht.minshengbao.FunctionView.insurance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.umeng.analytics.MobclickAgent;


import java.util.ArrayList;
import java.util.HashMap;

public class InsuranceType extends BaseActivity {
    private ListView mListView;
    private InsuranceAdapter  mAdapter;
    private int pos=-1;
    private ArrayList<HashMap<String, String>> insuranceList = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_type);
        context=this;
        setCommonHeader("保险套餐");
        mListView=(ListView)findViewById(R.id.id_view_insurance);
        initData();
        mAdapter=new InsuranceAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                String type=insuranceList .get(position).get("type");
                String time=insuranceList .get(position).get("deadline");
                String Id=insuranceList .get(position).get("id");
                String amount=insuranceList .get(position).get("amount");
                String insurance=insuranceList .get(position).get("figure");
                mAdapter.notifyDataSetChanged();
                Intent name=new Intent();
                name.putExtra("type",type);
                name.putExtra("Id",Id);
                name.putExtra("time",time);
                name.putExtra("amount",amount);
                name.putExtra("insurance",insurance);
                setResult(1, name);
                finish();
            }
        });
    }
    private void initData() {
        for (int i=0;i<4;i++){
            HashMap<String, String> map = new HashMap<String, String>();
            if (i==0){
                map.put("id", "829073");
                map.put("type", "保险套餐A");
                map.put("amount", "300.00");
                map.put("figure", "942000.00");
                map.put("deadline","5");
                insuranceList.add(map);
            }else if (i==1){
                map.put("id", "829072");
                map.put("type", "保险套餐B");
                map.put("amount", "200.00");
                map.put("figure", "856000.00");
                map.put("deadline","3");
                insuranceList.add(map);
            }else if (i==2){
                map.put("id", "823030");
                map.put("type", "保险套餐C");
                map.put("amount", "100.00");
                map.put("figure", "1156000.00");
                map.put("deadline","1");
                insuranceList.add(map);
            }else if (i==3){
                map.put("id", "823029");
                map.put("type", "保险套餐D");
                map.put("amount", "30.00");
                map.put("figure", "303600.00");
                map.put("deadline","1");
                insuranceList.add(map);
            }
        }
    }
    private class InsuranceAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater = null;
        public InsuranceAdapter(Context context) {
            super();
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return insuranceList.size();
        }
        @Override
        public Object getItem(int position) {
            return insuranceList.get(position);
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
                convertView = mInflater.inflate(R.layout.item_insurance_type, null);
                holder.box_select=(RadioButton) convertView.findViewById(R.id.id_radio);
                holder.tv_time=(TextView) convertView.findViewById(R.id.id_deadline_time);
                holder.tv_type=(TextView) convertView.findViewById(R.id.id_text1);
                holder.tv_amount=(TextView) convertView.findViewById(R.id.id_amount);
                holder.tv_figure=(TextView)convertView.findViewById(R.id.id_insurance_amount);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String buy_amount="¥"+insuranceList.get(position).get("amount")+"元";
            String deadline=insuranceList.get(position).get("deadline")+"年";
            String insurance_amount="¥"+insuranceList.get(position).get("figure")+"元";
            holder.tv_amount.setText(buy_amount);
            holder.tv_time.setText(deadline);
            holder.tv_type.setText(insuranceList.get(position).get("type"));
            holder.tv_figure.setText(insurance_amount);
            if (pos==position){
                holder.box_select.setChecked(true);
            }
            return convertView;
        }

    }
    class ViewHolder {
        public RadioButton box_select;
        public TextView  tv_time;
        public TextView  tv_type;
        public TextView  tv_amount;
        public TextView  tv_figure;
    }
}
