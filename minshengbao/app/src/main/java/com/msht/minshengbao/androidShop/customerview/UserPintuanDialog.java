package com.msht.minshengbao.androidShop.customerview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.msht.minshengbao.androidShop.adapter.UserPingTunAdpter;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.GoodPingTunBean;
import com.msht.minshengbao.androidShop.shopBean.UserPinTunBean;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.DateUtils;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.DialogInterface;
import com.msht.minshengbao.androidShop.viewInterface.IGoodPingTuanView;
import com.msht.minshengbao.androidShop.viewInterface.IUserPingTuanView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserPintuanDialog extends Dialog implements IUserPingTuanView {

    private  DialogInterface dialogInterface;
    private  String pingtuanid;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.dismiss)
    ImageView dismiss;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.leftnum)
    TextView tvLeftNum;
    private Context context;
    private UserPingTunAdpter adapter;
    private List<UserPinTunBean> list=new ArrayList<UserPinTunBean>();
    private LoadingDialog centerLoadingDialog;
    private CountDownTimer countDownTimer;
    @BindView(R.id.hour)
    TextView hour;
    @BindView(R.id.minute)
     TextView minute;
    @BindView(R.id.second)
     TextView second;
    @BindView(R.id.cantuan)
    TextView cantuan;
    private String buyer_id;

    public String getPingtuanid() {
        return pingtuanid;
    }

    public UserPintuanDialog(@NonNull Context context, String pingtuanid, DialogInterface dialogInterface) {
        super(context,R.style.nomalDialog);
        this.context = context;
        this.pingtuanid = pingtuanid;
        this.dialogInterface = dialogInterface;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (UserPintuanDialog.this.isShowing()) {
                    UserPintuanDialog.this.dismiss();
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
        setContentView(R.layout.user_pintuan_dialog);
        ButterKnife.bind(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = this.getWindow().getAttributes();
        attributes.width=(int)(DimenUtil.getScreenWidth()*0.8);
        this.getWindow().setAttributes(attributes);
        LinearLayoutManager llm = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        llm.setAutoMeasureEnabled(true);
        rcl.setLayoutManager(llm);
        adapter = new UserPingTunAdpter(context, list);;
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
        ShopPresenter.getUserPingtuanInfo(this,pingtuanid);
    }
    public void cancelAllTimers() {
       if(countDownTimer!=null) {
           countDownTimer.cancel();
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
    public void onUserPingtuanInfoSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject datas = obj.optJSONObject("datas");
            JSONObject pintuan_info = datas.optJSONObject("pintuan_info");
            long end_time_left = pintuan_info.optLong("end_time_left")*1000;
            JSONArray pintuan_list = pintuan_info.optJSONArray("pintuan_list");
            tvName.setText(pintuan_info.optString("buyer_name"));
            buyer_id = pintuan_info.optString("buyer_id");
            tvLeftNum.setText("还差"+pintuan_info.optString("num")+"人拼成");
            for(int i=0;i<pintuan_list.length();i++){
                list.add(JsonUtil.toBean(pintuan_list.optJSONObject(i).toString(),UserPinTunBean.class));
            }
            adapter.notifyDataSetChanged();
            if(end_time_left>0) {
                countDownTimer = new CountDownTimer(end_time_left, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        List<String> list = DateUtils.secondFormatToLeftDay(millisUntilFinished / 1000);
                        hour.setText(list.get(1));
                        minute.setText(list.get(2));
                        second.setText(list.get(3));
                        cantuan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                             dialogInterface.onClickPingTuan(pingtuanid,buyer_id);
                            }
                        });
                    }

                    @Override
                    public void onFinish() {
                      cantuan.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              PopUtil.toastInCenter("拼团已经结束");
                          }
                      });
                    }
                }.start();
            }else {
                hour.setText("00");
                minute.setText("00");
                second.setText("00");
                cantuan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopUtil.toastInCenter("拼团已经结束");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
