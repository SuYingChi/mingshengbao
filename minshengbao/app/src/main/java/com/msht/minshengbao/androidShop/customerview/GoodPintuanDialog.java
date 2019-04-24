package com.msht.minshengbao.androidShop.customerview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.GoodPingTunAdpter;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.GoodPingTunBean;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.DialogInterface;
import com.msht.minshengbao.androidShop.viewInterface.IGoodPingTuanView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodPintuanDialog extends Dialog implements IGoodPingTuanView, GoodPingTunAdpter.AdapterInterface {

    private  DialogInterface dialogInterface;
    private  String goodId;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.dismiss)
    ImageView dismiss;
    @BindView(R.id.title)
    TextView tvTitle;
    private Context context;
    private GoodPingTunAdpter adapter;
    private List<GoodPingTunBean> list=new ArrayList<GoodPingTunBean>();
    private LoadingDialog centerLoadingDialog;


    public GoodPintuanDialog(@NonNull Context context, String goodId, DialogInterface  dialogInterface) {
        super(context,R.style.nomalDialog);
        this.context = context;
        this.goodId = goodId;
        this.dialogInterface = dialogInterface;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (GoodPintuanDialog.this.isShowing()) {
                    GoodPintuanDialog.this.dismiss();
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
        setContentView(R.layout.good_all_pintuan_dialog);
        ButterKnife.bind(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = this.getWindow().getAttributes();
        attributes.width=(int)(DimenUtil.getScreenWidth()*0.8);
        attributes.height = DimenUtil.dip2px(300);
        this.getWindow().setAttributes(attributes);
        LinearLayoutManager llm = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(llm);
        adapter = new GoodPingTunAdpter(context, list);
        adapter.setAdapterInterface(this);
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
        ShopPresenter.getGoodPingtuanInfo(this,goodId);
    }
    public void cancelAllTimers() {
        adapter.cancelAllTimers();
    }

    @Override
    public void onGetGoodPingtuanInfoSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject datas = obj.optJSONObject("datas");
            JSONArray pintuan_list = datas.optJSONArray("pintuan_list");
            for(int i=0;i<pintuan_list.length();i++){
               list.add(JsonUtil.toBean(pintuan_list.optJSONObject(i).toString(),GoodPingTunBean.class));
            }
            adapter.notifyChange();
            if(list.size()==0){
                tvTitle.setText("没有正在进行的拼团并且不允许开新团");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void showCenterLodaingDialog() {
        if (isShowing() && centerLoadingDialog == null) {
            centerLoadingDialog = new LoadingDialog(context);
            centerLoadingDialog.show();
        } else if (isShowing() && !centerLoadingDialog.isShowing()) {
            centerLoadingDialog.show();
        }
    }
    private void hideCenterLoadingDialog() {
        if (centerLoadingDialog != null && centerLoadingDialog.isShowing() && isShowing()) {
            centerLoadingDialog.dismiss();
        }
    }
    @Override
    public void showLoading() {
       showCenterLodaingDialog();
    }

    @Override
    public void dismissLoading() {
       hideCenterLoadingDialog();
    }

    @Override
    public void onError(String s) {
        if (!AppUtil.isNetworkAvailable()) {
            PopUtil.showComfirmDialog(context,"",context.getResources().getString(R.string.network_error),"","",null,null,true);
        } else if (TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())||"未登录".equals(s)) {
            PopUtil.toastInBottom("请登录");
        } else if(!TextUtils.isEmpty(s)){
            PopUtil.toastInCenter(s);
        }
    }

    @Override
    public String getKey() {
        return ShopSharePreferenceUtil.getInstance().getKey();
    }

    @Override
    public String getUserId() {
        return ShopSharePreferenceUtil.getInstance().getUserId();
    }

    @Override
    public String getLoginPassword() {
        return null;
    }

    @Override
    public void onClickPingTuan(String pingTuanid, String buyer_id) {
        dialogInterface.onClickPingTuan(pingTuanid, buyer_id);
    }

    public void refresh() {
        ShopPresenter.getGoodPingtuanInfo(this,goodId);
    }
}
