package com.msht.minshengbao.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


import com.msht.minshengbao.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class SelectDateAdapter extends RecyclerView.Adapter<SelectDateAdapter.DateViewHolder>{
    private ArrayList<HashMap<String ,String>> mList=new ArrayList<HashMap<String ,String>>();
    private int lastPosition=0;
    private UpdateListener updateListener;
    public void setUpdateListener(UpdateListener updateListener){
        this.updateListener = updateListener;
    }
    /**数据更新回调接口*/
    public interface UpdateListener{
        /**
         * 回调接口
         * @param position
         * @param list
         */
        void onUpdateClick(int position, String list);
    }
    public void setSelection(int position)
    {
        lastPosition=position;
    }
    /**
     *
     * @param list
     * @param
     */
    public SelectDateAdapter(ArrayList<HashMap<String ,String>> list) {
        this.mList=list;
    }
    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_radiobutton,parent,false);
        return new DateViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String mDateString=mList.get(position).get("dateMonth");
        holder.radioDate.setText(mDateString);
        holder.radioDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateListener!=null){
                    updateListener.onUpdateClick(position,mList.get(position).get("dateMonth"));
                }
            }
        });
        if (lastPosition==position){
            holder.radioDate.setChecked(true);
        }else {
            holder.radioDate.setChecked(false);
        }
    }
    @Override
    public int getItemCount() {
        if (mList!=null){
            return mList.size();
        }else {
            return 0;
        }
    }
    public static class DateViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioDate;
        private DateViewHolder(View itemView) {
            super(itemView);
            radioDate=(RadioButton)itemView.findViewById(R.id.id_radio_date) ;
        }
    }
}
