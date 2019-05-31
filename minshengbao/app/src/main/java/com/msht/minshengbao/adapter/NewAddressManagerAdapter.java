package com.msht.minshengbao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
 * @date 2018/7/2  
 */
public class NewAddressManagerAdapter extends RecyclerView.Adapter<NewAddressManagerAdapter.MyViewHolder>{
    private Context context;
    private OnSelectDeleteListener deleteListener;
    private OnItemSelectListener listener;
    private OnItemCheckedListener checkedListener;
    private OnItemCorrelationListener correlationListener;
    private OnItemSupplementListener supplementListener;
    public void setOnItemCheckedListener(OnItemCheckedListener listener){
        this.checkedListener=listener;
    }
    public void setOnItemSelectListener(OnItemSelectListener listener){
        this.listener=listener;
    }
    public void setOnDeleteListener(OnSelectDeleteListener deleteListener){
        this.deleteListener=deleteListener;
    }
    public void setOnCorrelationListener(OnItemCorrelationListener onCorrelationListener){
        this.correlationListener=onCorrelationListener;
    }
    public void setOnSupplementListener(OnItemSupplementListener supplementListener){
        this.supplementListener=supplementListener;
    }
    public interface  OnItemSupplementListener{
        /**
         * 关补充
         * @param view
         * @param position
         */
        void onItemSupplement(View view,int position);
    }
    public interface  OnItemCorrelationListener{
        /**
         * 关联
         * @param view
         * @param position
         */
        void onItemCorrelation(View view,int position);
    }
    public interface OnItemCheckedListener{
        /**
         * 设置默认回调
         * @param view
         * @param position
         */
        void onItemCheckedClick(View view,int position);
    }
    public interface OnSelectDeleteListener{
        /**
         * 选择删除
         * @param view
         * @param position
         */
        void onItemDeleteClick(View view,int position);
    }
    public interface OnItemSelectListener{
        /**
         * 编辑
         * @param view
         * @param position
         */
        void onItemSelectClick(View view,int position);
    }
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public NewAddressManagerAdapter(Context context,ArrayList<HashMap<String, String>> list) {
        this.mList=list;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_new_address_manage_view,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final  int position=i;
        onBindDataView(myViewHolder,i);
        myViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onItemSelectClick(view,position);
                }
            }
        });
        myViewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onItemSelectClick(view,position);
                }
            }
        });
        myViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteListener!=null){
                    deleteListener.onItemDeleteClick(view,position);
                }
            }
        });
        myViewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteListener!=null){
                    deleteListener.onItemDeleteClick(view,position);
                }
            }
        });
        myViewHolder.boxSetDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedListener!=null){
                    checkedListener.onItemCheckedClick(v,position);
                }
            }
        });
        myViewHolder.btnSupplement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (supplementListener!=null){
                    supplementListener.onItemSupplement(view,position);
                }
            }
        });
        myViewHolder.btnCorrelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correlationListener!=null){
                    correlationListener.onItemCorrelation(view,position);
                }
            }
        });
    }

    private void onBindDataView(MyViewHolder myViewHolder, int i) {
        String name=mList.get(i).get("name");
        String phone=mList.get(i).get("phone");
        String customerNo=mList.get(i).get("customerNo");
        String address=mList.get(i).get("address");
        String flag=mList.get(i).get("flag");
        myViewHolder.tvAddress.setText(address);
        myViewHolder.tvPhone.setText(phone);
        if (TextUtils.isEmpty(customerNo)||customerNo.equals(ConstantUtil.NULL_VALUE)){
            myViewHolder.tvCustomerNo.setTextColor(ContextCompat.getColor(context,R.color.color_9e9e9e));
            myViewHolder.tvCustomerNo.setText("未关联燃气用户号");
            myViewHolder.btnCorrelation.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.btnCorrelation.setVisibility(View.GONE);
            myViewHolder.tvCustomerNo.setText(customerNo);
            myViewHolder.tvCustomerNo.setTextColor(ContextCompat.getColor(context,R.color.color_383838));
        }
        if (TextUtils.isEmpty(name)||name.equals(ConstantUtil.NULL_VALUE)||TextUtils.isEmpty(phone)||phone.equals(ConstantUtil.NULL_VALUE)){
            myViewHolder.tvName.setTextColor(ContextCompat.getColor(context,R.color.color_9e9e9e));
            myViewHolder.tvName.setText("没有联系人信息，无法进行其他业务哦");
            myViewHolder.layoutTip.setVisibility(View.VISIBLE);
            myViewHolder.layoutInfo.setVisibility(View.GONE);
        }else {
            myViewHolder.tvName.setTextColor(ContextCompat.getColor(context,R.color.color_383838));
            myViewHolder.tvName.setText(name);
            myViewHolder.layoutTip.setVisibility(View.GONE);
            myViewHolder.layoutInfo.setVisibility(View.VISIBLE);
        }
        if (flag.equals(ConstantUtil.VALUE_ONE)){
            myViewHolder.boxSetDefault.setText("默认地址");
            myViewHolder.boxSetDefault.setChecked(true);
            myViewHolder.boxSetDefault.setTextColor(0xfff96331);
        }else {
            myViewHolder.boxSetDefault.setTextColor(0xff555555);
            myViewHolder.boxSetDefault.setText("设为默认");
            myViewHolder.boxSetDefault.setChecked(false);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Button   btnCorrelation;
        Button   btnSupplement;
        TextView tvCustomerNo;
        TextView tvName;
        TextView tvPhone;
        TextView tvAddress;
        ImageView  imgEdit;
        ImageView  imgDelete;
        TextView   tvEdit;
        TextView   tvDelete;
        CheckBox boxSetDefault;
        View layoutInfo;
        View layoutTip;
        View itemView;
        private MyViewHolder(View view){
            super(view);
            btnCorrelation =(Button) view.findViewById(R.id.id_correlation_btn);
            btnSupplement =(Button) view.findViewById(R.id.id_supplement_btn);
            tvCustomerNo =(TextView)view.findViewById(R.id.id_tv_customerNo);
            tvName =(TextView)view.findViewById(R.id.id_tv_name);
            tvPhone=(TextView)view.findViewById(R.id.id_tv_phone);
            tvAddress =(TextView)view.findViewById(R.id.id_tv_address);
            boxSetDefault=(CheckBox)view.findViewById(R.id.id_box_setdefault);
            tvEdit=(TextView)view.findViewById(R.id.id_et_edittext);
            tvDelete=(TextView)view.findViewById(R.id.id_tv_delect);
            imgDelete=(ImageView)view.findViewById(R.id.id_delect_img);
            imgEdit=(ImageView)view.findViewById(R.id.id_edit_img);
            layoutInfo=view.findViewById(R.id.id_personal_info);
            layoutTip=view.findViewById(R.id.id_tip_name_layout);
            itemView=view.findViewById(R.id.item_layout);
        }
    }
}
