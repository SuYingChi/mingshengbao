package com.msht.minshengbao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.msht.minshengbao.Base.ListBaseAdapter;
import com.msht.minshengbao.Control.FullyGridLayoutManager;
import com.msht.minshengbao.Model.AllServiceModel;
import com.msht.minshengbao.R;

/**
 * Created by hei on 2016/12/30.
 * 技能列表的适配器
 */

public class AllServerAdapter extends ListBaseAdapter<AllServiceModel.MainCategory.ServeCategory> {
    private LayoutInflater mLayoutInflater;

    public AllServerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        AllServiceModel.MainCategory.ServeCategory serveCategory = mDataList.get(position);
        MainViewHolder viewHolder = (MainViewHolder) holder;
        viewHolder.tv_type_name.setText(serveCategory.name);
        String code=serveCategory.code;
        if (code.equals("gas_serve")){
            viewHolder.iv_type.setImageResource(R.drawable.all_gasserve_xh);
        }else if (code.equals("repair")){
            viewHolder.iv_type.setImageResource(R.drawable.all_repair_xh);
        }else if (code.equals("clean")){
            viewHolder.iv_type.setImageResource(R.drawable.all_clean_xh);
        }else if (code.equals("convenience_service")){
            viewHolder.iv_type.setImageResource(R.drawable.all_life_xh);
        }else if (code.equals("shop")){
            viewHolder.iv_type.setImageResource(R.drawable.all_shopmall_xh);
        }else {
            viewHolder.iv_type.setImageResource(R.drawable.all_gasserve_xh);
        }
        SecondAdapter secondAdapter = new SecondAdapter(mContext);
        secondAdapter.SetOnItemServeClickListener(new SecondAdapter.OnItemServeClickListener() {
            @Override
            public void ServerClick(View view, int secondPosition) {
                if(listener!=null){
                    listener.ItemClick(view,position,secondPosition);
                }
            }
        });
        viewHolder.recycler_second_type.setLayoutManager(new FullyGridLayoutManager(mContext,4));
        viewHolder.recycler_second_type.setAdapter(secondAdapter);
        secondAdapter.addAll(serveCategory.child);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(mLayoutInflater.inflate(R.layout.item_serve_main_type, parent, false));
    }
    private class MainViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_type;
        private final TextView tv_type_name;
        private final RecyclerView recycler_second_type;

        public MainViewHolder(View itemView) {
            super(itemView);
            iv_type = (ImageView) itemView.findViewById(R.id.id_serve_img);
            tv_type_name = (TextView) itemView.findViewById(R.id.tv_type_name);
            recycler_second_type = (RecyclerView) itemView.findViewById(R.id.recycler_second_type);
        }
    }

    public OnItemClickListener listener;
    public void SetOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
    public interface OnItemClickListener{
       void ItemClick(View view, int mainPosition, int secondPosition);
   }


    /**
     * 次级列表的适配器
     */
    private static class SecondAdapter extends ListBaseAdapter<AllServiceModel.MainCategory.ServeCategory.ChildCategory>{
        private LayoutInflater mLayoutInflater;
        private boolean isValid;

        /**
         *
         * @param context
         *
         */
        public SecondAdapter(Context context) {
            this.isValid=isValid;
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final AllServiceModel.MainCategory.ServeCategory.ChildCategory childCategory = mDataList.get(position);
            final SecondViewHolder viewHolder = (SecondViewHolder) holder;
            viewHolder.tv_serve.setText(childCategory.name);
            viewHolder.layout_serve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.ServerClick(v,position);
                    }
                }
            });
        }
        private OnItemServeClickListener listener;

        public void SetOnItemServeClickListener(OnItemServeClickListener listener) {
            this.listener = listener;
        }

        public interface OnItemServeClickListener{
            void ServerClick(View view, int secondPosition);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SecondViewHolder(mLayoutInflater.inflate(R.layout.item_serve_second_type,parent,false));
        }
    }
    private static class SecondViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_serve;
        private final RelativeLayout layout_serve;
        public SecondViewHolder(View itemView) {
            super(itemView);
            tv_serve = (TextView) itemView.findViewById(R.id.tv_serve_type);
            layout_serve=(RelativeLayout)itemView.findViewById(R.id.id_serve_layout);
        }
    }
}
