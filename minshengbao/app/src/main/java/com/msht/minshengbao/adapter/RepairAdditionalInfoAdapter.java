package com.msht.minshengbao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.GsonImpl;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/2/25  
 */
public class RepairAdditionalInfoAdapter extends RecyclerView.Adapter<RepairAdditionalInfoAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public  RepairAdditionalInfoAdapter(Context context, ArrayList<HashMap<String, String>> List) {
        super();
        this.mList=List;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_repair_order_addtional_info,viewGroup,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String name=mList.get(i).get("name");
        String code=mList.get(i).get("code");
        String value=mList.get(i).get("value");
        myViewHolder.tvCategoryName.setText(name);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myViewHolder.mRecyclerView.setLayoutManager(layoutManager);
        ChildAdditionalInfoAdapter mAdditionalAdapter=new ChildAdditionalInfoAdapter(context,GsonImpl.getAdditionalList(value));
        myViewHolder.mRecyclerView.setAdapter(mAdditionalAdapter);
    }
    @Override
    public int getItemCount() {
        if (mList!=null){
            return mList.size();
        }else {
            return 0;
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        RecyclerView mRecyclerView;
       // TextView tvCount;
        private MyViewHolder(View view){
            super(view);
            tvCategoryName =(TextView)view.findViewById(R.id.id_category_name) ;
            mRecyclerView=(RecyclerView)view.findViewById(R.id.id_child_info);
            //tvCount =(TextView)view.findViewById(R.id.id_count);
        }
    }

    private class ChildAdditionalInfoAdapter extends RecyclerView.Adapter<ChildAdditionalInfoAdapter.MyChildViewHolder>{
        private ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();

        public  ChildAdditionalInfoAdapter(Context context, ArrayList<HashMap<String, String>> List) {
            super();
            this.childList=List;
        }
        @NonNull
        @Override
        public MyChildViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_repair_child_additional_info,viewGroup,false);
            return new MyChildViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyChildViewHolder myChildViewHolder, int i) {

            String name=childList.get(i).get("name");
            String code=childList.get(i).get("code");
            String value="x"+childList.get(i).get("value");
            myChildViewHolder.tvCategoryName.setText(name);
            myChildViewHolder.tvCount.setText(value);
        }

        @Override
        public int getItemCount() {
            if (childList!=null){
                return childList.size();
            }else {
                return 0;
            }
        }
        class MyChildViewHolder extends RecyclerView.ViewHolder {
            TextView tvCategoryName;
            TextView tvCount;
            private MyChildViewHolder(View view){
                super(view);
                tvCategoryName =(TextView)view.findViewById(R.id.id_category_name) ;
                tvCount =(TextView)view.findViewById(R.id.id_count);
            }
        }
    }
}
