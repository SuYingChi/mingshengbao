package com.msht.minshengbao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/9/19  
 */
public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.MyViewHolder> {
    private Context mContext;
    private int mPos;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }
    public void setItemRadioClickBack(ItemRadioClickBack itemRadioClickBack){
        this.itemRadioClickBack=itemRadioClickBack;
    }
    public interface ItemRadioClickBack{
        /**
         * item回调
         * @param pos 下标
         */
        void onItemRadioBack(int pos);
    }
    public interface ItemClickCallBack{
        /**
         * item回调
         * @param pos 下标
         */
        void onItemClick(int pos);
    }
    private ItemClickCallBack clickCallBack;
    private ItemRadioClickBack itemRadioClickBack;
    public SelectAdapter(Context context, ArrayList<HashMap<String, String>> list, int pos) {
        this.mContext=context;
        this.mPos=pos;
        this.mList=list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final  int thisPosition=position;
        String moduleType=mList.get(position).get("moduleType");
        holder.repairType.setText(mList.get(position).get("name"));
        switch (moduleType){
            case ConstantUtil.VALUE_ONE:
                holder.leftImg.setImageResource(R.drawable.installtype);
                break;
            case ConstantUtil.VALUE_TWO:
                holder.leftImg.setImageResource(R.drawable.gastable);
                break;
            case ConstantUtil.VALUE_THREE:
                holder.leftImg.setImageResource(R.drawable.timeimg);
                break;
            default:
                holder.leftImg.setImageResource(R.drawable.installtype);
                break;
        }
        if (mPos==position){
            holder.radio.setChecked(true);
        }else {
            holder.radio.setChecked(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickCallBack != null){
                    clickCallBack.onItemClick(thisPosition);
                }
            }
        });
        holder.radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemRadioClickBack != null){
                    itemRadioClickBack.onItemRadioBack(thisPosition);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        if (mList!=null){
            return mList.size();
        }else {
            return 0;
        }
    }
    /**自定义的ViewHolder，持有每个Item的的所有界面元素 */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton radio;
        ImageView leftImg;
        TextView repairType;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            radio=(RadioButton)view.findViewById(R.id.id_radio);
            leftImg = (ImageView) view.findViewById(R.id.id_table_img);
            repairType = (TextView) view.findViewById(R.id.id_tv_table);
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
