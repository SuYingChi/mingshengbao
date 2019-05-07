package com.msht.minshengbao.androidShop.customerview;

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
import com.msht.minshengbao.androidShop.adapter.CircleScrollerRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.InvContentAdapter;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.InvContentItemBean;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.viewInterface.IGetInvContentView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectSiteAreaDialog extends Dialog {

    private final IGetInvContentView iGetInvContentView;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.area)
    TextView area;
    @BindView(R.id.selected)
    TextView selected;
    private Context context;
    private InvContentAdapter adapter;

    private List<InvContentItemBean> list;
    private SelectSiteInterface selectSiteInterface;


    public SelectSiteAreaDialog(@NonNull Context context, IGetInvContentView iGetInvContentView, List<InvContentItemBean> list) {
        super(context, R.style.ActionSheetDialogStyle);
        this.iGetInvContentView = iGetInvContentView;
        this.context = context;
        this.list = list;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (SelectSiteAreaDialog.this.isShowing()) {
                    SelectSiteAreaDialog.this.dismiss();
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
        setContentView(R.layout.select_site_dialog);
        ButterKnife.bind(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = this.getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(attributes);
        rcl.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        adapter = new InvContentAdapter(context);
        adapter.setDatas(list);
        adapter.setOnItemClickListener(new CircleScrollerRecyclerAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                iGetInvContentView.onSelectedInvContentItem(position);
            }
        });
        rcl.setAdapter(adapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSiteInterface.onCanceled();
            }
        });
        selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSiteInterface.onSelected();
            }
        });
    }

    public void notifyRcl() {
        adapter.notifyDataSetChanged();
    }
    public void  setiSelectSiteInterface(SelectSiteInterface selectSiteInterface){
        this.selectSiteInterface =selectSiteInterface;
    }

    public void setAreaTitle(String invContent) {
        area.setText(invContent);
    }

    public  interface SelectSiteInterface{
      void   onCanceled();
      void onSelected();
    }
}
