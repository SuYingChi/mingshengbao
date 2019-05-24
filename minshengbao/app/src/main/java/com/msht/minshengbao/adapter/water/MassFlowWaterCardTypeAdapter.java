package com.msht.minshengbao.adapter.water;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.TypeConvertUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.adapter.InvoiceHistoryAdapter;
import com.msht.minshengbao.adapter.NewAddressManagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/5/24 
 */
public class MassFlowWaterCardTypeAdapter extends RecyclerView.Adapter<MassFlowWaterCardTypeAdapter.MyViewHolder>{

    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }
    public interface ItemClickCallBack{
        /**
         * item回调
         * @param pos 下标
         */
        void onItemClick(int pos);
    }
    private ItemClickCallBack clickCallBack;
    public MassFlowWaterCardTypeAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.mList=list;
       // this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mass_flow_type_view,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        String title="民生水宝"+mList.get(i).get("title");
        String amount=mList.get(i).get("amount");
        String giveFee=mList.get(i).get("giveFee");
        String waterQuantity=mList.get(i).get("waterQuantity")+"L";
        String originAmount=TypeConvertUtil.getStringAddToDouble(amount,giveFee)+"元";
        String amountText=amount+"元";
        myViewHolder.tvName.setText(title);
        myViewHolder.tvAmount.setText(amountText);
        myViewHolder.tvOriginAmount.setText(originAmount);
        myViewHolder.tvVolume.setText(waterQuantity);
        myViewHolder.tvOriginAmount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (VariableUtil.mPos==i){
            myViewHolder.itemView.setBackgroundResource(R.drawable.mass_flow_card_type1);
        }else {
            myViewHolder.itemView.setBackgroundResource(R.drawable.mass_flow_card_type2);
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickCallBack!=null){
                    clickCallBack.onItemClick(i);
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
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvVolume;
        TextView tvAmount;
        TextView tvOriginAmount;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            tvOriginAmount =(TextView)view.findViewById(R.id.id_origin_amount);
            tvName =(TextView)view.findViewById(R.id.id_name);
            tvVolume=(TextView)view.findViewById(R.id.id_volume);
            tvAmount =(TextView)view.findViewById(R.id.id_amount);
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
