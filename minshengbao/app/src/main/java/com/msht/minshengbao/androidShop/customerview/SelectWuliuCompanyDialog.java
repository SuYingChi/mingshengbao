package com.msht.minshengbao.androidShop.customerview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.CircleScrollerRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.InvContentAdapter;
import com.msht.minshengbao.androidShop.adapter.WuliuCompanyAdapter;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.InvContentItemBean;
import com.msht.minshengbao.androidShop.shopBean.WuliuCompanyItemBean;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.viewInterface.IGetInvContentView;
import com.msht.minshengbao.androidShop.viewInterface.IGetWuliuView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectWuliuCompanyDialog extends Dialog {

    private final IGetWuliuView iGetWuliuView;
    @BindView(R.id.rcl)
    RecyclerView rcl;

    private Context context;
    private WuliuCompanyAdapter adapter;

    private List<WuliuCompanyItemBean> list;


    public SelectWuliuCompanyDialog(@NonNull Context context, IGetWuliuView iGetWuliuView, List<WuliuCompanyItemBean> list) {
        super(context, R.style.ActionSheetDialogStyle);
        this.iGetWuliuView = iGetWuliuView;
        this.context = context;
        this.list = list;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (SelectWuliuCompanyDialog.this.isShowing()) {
                    SelectWuliuCompanyDialog.this.dismiss();
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
        setContentView(R.layout.select_inv_content_dialog);
        ButterKnife.bind(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = this.getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(attributes);
        rcl.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        adapter = new WuliuCompanyAdapter(context);
        adapter.setDatas(list);
        adapter.setOnItemClickListener(new CircleScrollerRecyclerAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                iGetWuliuView.onSelectedItem(position);
            }
        });
        rcl.setAdapter(adapter);
        rcl.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int adapterNowPos = l.findFirstCompletelyVisibleItemPosition();
                int nowLastPos = l.findLastCompletelyVisibleItemPosition();
                int selectPosition;
                LogUtils.e("adapterNowPos==" + adapterNowPos + "-------nowLastPos==" + nowLastPos);
                if ((nowLastPos + adapterNowPos) % 2 != 0) {
                    selectPosition = (nowLastPos + adapterNowPos) / 2 + 1;
                } else {
                    selectPosition = (nowLastPos + adapterNowPos) / 2;
                }
                if(list.size()!=0){
                    //循环滚动要取余
                    iGetWuliuView.onSelectedItem(selectPosition % list.size());
                }
            }
        });
    }

    public void notifyRcl() {
        adapter.notifyDataSetChanged();
    }
}
