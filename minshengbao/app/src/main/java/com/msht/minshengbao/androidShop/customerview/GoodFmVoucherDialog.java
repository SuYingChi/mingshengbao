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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.CircleScrollerRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.GoodFmVoucherAdpter;
import com.msht.minshengbao.androidShop.adapter.HaveHeadRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.InvContentAdapter;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.InvContentItemBean;
import com.msht.minshengbao.androidShop.shopBean.VoucherBean;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.viewInterface.IGetInvContentView;
import com.msht.minshengbao.androidShop.viewInterface.IGetVoucherView;
import com.msht.minshengbao.androidShop.viewInterface.IShopGoodDetailView;
import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodFmVoucherDialog extends Dialog {

    private final IGetVoucherView iGetVoucherView;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.dismiss)
    TextView dismiss;
    private Context context;
    private GoodFmVoucherAdpter adapter;

    private List<VoucherBean> list;


    public GoodFmVoucherDialog(@NonNull Context context, IGetVoucherView iGetVoucherView, List<VoucherBean> list) {
        super(context, R.style.ActionSheetDialogStyle);
        this.iGetVoucherView = iGetVoucherView;
        this.context = context;
        this.list = list;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (GoodFmVoucherDialog.this.isShowing()) {
                    GoodFmVoucherDialog.this.dismiss();
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
        setContentView(R.layout.goodfm_voucher_dialog);
        ButterKnife.bind(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = this.getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = DimenUtil.dip2px(400);
        attributes.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(attributes);
        LinearLayoutManager llm = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(llm);
        adapter = new GoodFmVoucherAdpter(context,R.layout.item_goodfm_voucher, list);
        adapter.setOnItemClickListener(new HaveHeadRecyclerAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                iGetVoucherView.onGetVoucher(list.get(position).getVoucher_t_id());
            }
        });
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

}
