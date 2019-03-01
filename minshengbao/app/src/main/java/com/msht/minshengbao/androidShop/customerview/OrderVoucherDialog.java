package com.msht.minshengbao.androidShop.customerview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.OrderVoucherListAdpter;
import com.msht.minshengbao.androidShop.shopBean.OrderVoucherBean;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.viewInterface.ISelectedVoucherView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderVoucherDialog extends Dialog {

    private  ISelectedVoucherView iSelectedVoucherView;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.dismiss)
    TextView dismiss;
    private Context context;
    private OrderVoucherListAdpter adapter;

    private List<OrderVoucherBean> list;


    public OrderVoucherDialog(@NonNull Context context, ISelectedVoucherView iSelectedVoucherView, List<OrderVoucherBean> list) {
        super(context, R.style.ActionSheetDialogStyle);
        this.iSelectedVoucherView = iSelectedVoucherView;
        this.context = context;
        this.list = list;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (OrderVoucherDialog.this.isShowing()) {
                    OrderVoucherDialog.this.dismiss();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_voucher_dialog);
        ButterKnife.bind(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = this.getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = DimenUtil.dip2px(400);
        attributes.gravity = Gravity.BOTTOM;
        attributes.alpha=1;
        this.getWindow().setAttributes(attributes);
        LinearLayoutManager llm = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(llm);
        adapter = new OrderVoucherListAdpter(context,R.layout.item_order_voucher, list);
        adapter.setOrderVoucherListener(iSelectedVoucherView);
        adapter.setFoot_layoutId(R.layout.order_voucher_foot,null);
        rcl.setAdapter(adapter);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof Activity) {
                    if(!((Activity) context).isFinishing()){
                        dismiss();
                    }
                }
            }
        });
    }

    public List<OrderVoucherBean> getVoucherList() {
        return list;
    }

    public void refreshData(List<OrderVoucherBean> voucherList, boolean isUseVoucher) {
        list = voucherList;
        adapter.isUseVoucher(isUseVoucher);
        adapter.notifyDataSetChanged();
    }
}
