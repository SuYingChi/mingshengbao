package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.InvItemBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvListAdapter extends RecyclerView.Adapter {
    private final Context context;
    private final LayoutInflater inflate;
    private final InvListListener invListListener;
    private List<InvItemBean> list;

    public InvListAdapter(Context context, List<InvItemBean> list, InvListListener invListListener) {
        this.list = list;
        this.context = context;
        inflate = LayoutInflater.from(context);
        this.invListListener = invListListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = inflate
                    .inflate(R.layout.item_inv, parent, false);
            InvViewHolder vh = new InvViewHolder(v);
            return vh;
        } else {
            View v = inflate
                    .inflate(R.layout.item_inv_foot, parent, false);
            InvFootViewHolder vh = new InvFootViewHolder(v);
            return vh;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof InvViewHolder) {
            //放在外边会报空指针，可能数据为空，已经生产了viewholder才说明list该位置有数据
            InvItemBean bean = list.get(position);
            final InvViewHolder holders = (InvViewHolder) holder;
            holders.cb.setText(new StringBuilder().append(bean.getInv_title()).append(" ").append(TextUtils.isEmpty(bean.getInv_code())||TextUtils.equals("null",bean.getInv_code())?"":bean.getInv_code()).append(" ").append(bean.getInv_content()).toString());
            holders.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //选中后不可更改
                        holders.cb.setClickable(false);
                    } else {
                        //取消选中后，可以更改
                        holders.cb.setClickable(true);
                    }
                    invListListener.onCheckChange(isChecked, position);
                }
            });
            holders.cb.setChecked(bean.isCheck());
            holders.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invListListener.onDeleteItem(position);
                }
            });
        } else if (holder instanceof InvFootViewHolder) {
            InvFootViewHolder holders = (InvFootViewHolder) holder;
            holders.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invListListener.onAddNewInv();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return 100;
        } else return 1;
    }

     class InvViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb)
        CheckBox cb;
        @BindView(R.id.delete)
        ImageView iv;

        public InvViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

     class InvFootViewHolder extends RecyclerView.ViewHolder {
        public InvFootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface InvListListener {
        void onDeleteItem(int position);

        void onCheckChange(boolean isChecked, int position);

        void onAddNewInv();
    }
}
