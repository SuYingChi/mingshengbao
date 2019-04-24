package com.msht.minshengbao.androidShop.customerview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.GoodGuigeAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.util.DateUtils;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.IShopGoodDetailView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PingTuanBuyGoodDialog extends Dialog {
    private  Context context;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.dialog_name)
    TextView tvName;
    @BindView(R.id.dialog_price)
    TextView tvPrice;
    @BindView(R.id.dialog_remain_num)
    TextView tvRemainNum;
    @BindView(R.id.number)
    TextView tvNum;
    @BindView(R.id.guigercl)
    RecyclerView rcl;
    @BindView(R.id.guigeName)
    TextView tvGuigeName;
    @BindView(R.id.llguige)
    LinearLayout llguige;
    @BindView(R.id.close)
    ImageView ivClose;
    @BindView(R.id.day)
    TextView tvDay;
    @BindView(R.id.hour)
    TextView tvHour;
    @BindView(R.id.minute)
    TextView tvMinute;
    @BindView(R.id.second)
    TextView tvSecond;
    @BindView(R.id.buy_dialog)
    TextView tvBuy;
   /* @BindView(R.id.addcar)
    TextView tvAddCar;*/
    private IShopGoodDetailView iShopGoodDetailView;
    private int num = 1;


    private GoodGuigeAdapter ad;
    private CountDownTimer countDownTimer;

    public PingTuanBuyGoodDialog(@NonNull Context context, IShopGoodDetailView iShopGoodDetailView) {
        super(context, R.style.ActionSheetDialogStyle);
        this.iShopGoodDetailView = iShopGoodDetailView;
        num = iShopGoodDetailView.getSelectedGoodNum();
        this.context= context;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (PingTuanBuyGoodDialog.this.isShowing()) {
                    PingTuanBuyGoodDialog.this.dismiss();
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
        setContentView(R.layout.pingtuanbuy_dialog);
        ButterKnife.bind(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        tvName.setText(iShopGoodDetailView.getNameDialog());
        tvPrice.setText(StringUtil.getPriceSpannable12String(getContext(), iShopGoodDetailView.getPrice(), R.style.big_money, R.style.big_money));
     /*   tvAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PingTuanBuyGoodDialog.this.isShowing()) {
                    PingTuanBuyGoodDialog.this.dismiss();
                }
                iShopGoodDetailView.addCar();
            }
        });*/
        countDownTimer = new CountDownTimer(iShopGoodDetailView.getleftTime(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                List<String> list = DateUtils.secondFormatToLeftDay(millisUntilFinished / 1000);
                tvDay.setText(list.get(0));
                tvHour.setText(list.get(1));
                tvMinute.setText(list.get(2));
                tvSecond.setText(list.get(3));
                tvBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (PingTuanBuyGoodDialog.this.isShowing()) {
                            PingTuanBuyGoodDialog.this.dismiss();
                        }
                        iShopGoodDetailView.buyGood(true);
                    }
                });
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                if (PingTuanBuyGoodDialog.this.isShowing()) {
                    PingTuanBuyGoodDialog.this.dismiss();
                }
                iShopGoodDetailView.showAddCarDialog();
            }
        }.start();
        tvRemainNum.setText(String.format("库存量:%s件", iShopGoodDetailView.getRemainNum()));
        tvNum.setText(String.format("%d", num));
        GlideUtil.loadRemoteImg(context,iv,iShopGoodDetailView.getImageUrl());
        WindowManager.LayoutParams attributes = this.getWindow().getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(attributes);
        if(TextUtils.isEmpty(iShopGoodDetailView.getGuigeName())) {
            llguige.setVisibility(View.GONE);
        }else {
            llguige.setVisibility(View.VISIBLE);
            tvGuigeName.setText(iShopGoodDetailView.getGuigeName());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            rcl.setLayoutManager(gridLayoutManager);
            gridLayoutManager.setAutoMeasureEnabled(true);
            rcl.setNestedScrollingEnabled(false);
            ad = new GoodGuigeAdapter(context);
            ad.setDatas(iShopGoodDetailView.getGuigeList());
            ad.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int childposition) {
                    iShopGoodDetailView.onSelectGoodId(childposition);
                }
            });
            rcl.setAdapter(ad);
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PingTuanBuyGoodDialog.this.isShowing()) {
                    PingTuanBuyGoodDialog.this.dismiss();
                }
            }
        });
    }

    @OnClick({R.id.close, R.id.ll_plus, R.id.ll_reduce})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                if (PingTuanBuyGoodDialog.this.isShowing()) {
                    PingTuanBuyGoodDialog.this.dismiss();
                }
                break;
            case R.id.ll_reduce:
                if (num > 1) {
                    num--;
                    iShopGoodDetailView.setSelectedGoodNum(num);
                    tvNum.setText(String.format("%d", num));
                }else if(num==1){
                    PopUtil.toastInBottom("最少一个商品");
                }
                break;
            case R.id.ll_plus:
                if(num<Integer.valueOf(iShopGoodDetailView.getRemainNum())) {
                    num++;
                    iShopGoodDetailView.setSelectedGoodNum(num);
                    tvNum.setText(String.format("%d", num));
                }else {
                    PopUtil.toastInBottom("库存不够");
                }
                break;
            default:
                break;
        }
    }

    public void refreshData() {
        tvName.setText(iShopGoodDetailView.getNameDialog());
        tvPrice.setText(StringUtil.getPriceSpannable12String(getContext(), iShopGoodDetailView.getPrice(), R.style.big_money, R.style.big_money));
        tvRemainNum.setText(String.format("库存量:%s件", iShopGoodDetailView.getRemainNum()));
        tvNum.setText(String.format("%d", iShopGoodDetailView.getSelectedGoodNum()));
        GlideUtil.loadRemoteImg(context,iv,iShopGoodDetailView.getImageUrl());
        if(TextUtils.isEmpty(iShopGoodDetailView.getGuigeName())) {
            llguige.setVisibility(View.INVISIBLE);
        }else {
            llguige.setVisibility(View.VISIBLE);
            tvGuigeName.setText(iShopGoodDetailView.getGuigeName());
            ad.notifyDataSetChanged();
        }
    }

    public void cancelCounmDown() {
        if(countDownTimer!=null) {
            countDownTimer.cancel();
        }
    }

   /* public void setIsAllowAddCarVisible(boolean isAddCarVisible) {
       if(isAddCarVisible&&tvAddCar!=null&&tvAddCar.getVisibility()==View.GONE) {
           tvAddCar.setVisibility(View.VISIBLE);
       }else if(!isAddCarVisible&&tvAddCar!=null&&tvAddCar.getVisibility()==View.VISIBLE){
           tvAddCar.setVisibility(View.GONE);
       }
    }*/
}
