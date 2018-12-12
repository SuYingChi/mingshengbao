package com.msht.minshengbao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hong
 * @date 2017/3/20
 */

public class GetRecordAdapter extends  RecyclerView.Adapter<GetRecordAdapter.MyViewHolder> {
    private ArrayList<HashMap<String, String>> writeList = new ArrayList<HashMap<String, String>>();
    public GetRecordAdapter(Context context, ArrayList<HashMap<String, String>> mList) {
        super();
        this.writeList=mList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_write_table,viewGroup,false);
        return new GetRecordAdapter.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.cnMeter.setText(writeList.get(i).get("meter")+"NM³");
        myViewHolder.cnAddress.setText(writeList.get(i).get("address"));
        myViewHolder.cnCustomer.setText(writeList.get(i).get("customerNo"));
        myViewHolder.cnTime.setText(writeList.get(i).get("time"));


    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        if (writeList!=null){
            return writeList.size();
        }else {
            return 0;
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
         TextView cnMeter;
         TextView cnAddress;
         TextView cnCustomer;
         TextView cnTime;
         View itemView;
        private MyViewHolder(View view){
            super(view);
            cnMeter =(TextView) view.findViewById(R.id.id_table_data);
            cnCustomer =(TextView)view.findViewById(R.id.id_customerNo);
            cnAddress =(TextView) view.findViewById(R.id.id_address_text);
            cnTime =(TextView) view.findViewById(R.id.id_tv_time);
            itemView=view.findViewById(R.id.item_left);
        }
    }

}
