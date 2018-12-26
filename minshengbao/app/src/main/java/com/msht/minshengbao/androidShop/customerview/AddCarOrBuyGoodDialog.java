package com.msht.minshengbao.androidShop.customerview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.IShopGoodDetailView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCarOrBuyGoodDialog extends Dialog {
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
    private IShopGoodDetailView iShopGoodDetailView;
    private int num = 1;


    private GoodGuigeAdapter ad;

    public AddCarOrBuyGoodDialog(@NonNull Context context, IShopGoodDetailView iShopGoodDetailView) {
        super(context, R.style.ActionSheetDialogStyle);
        this.iShopGoodDetailView = iShopGoodDetailView;
        num = iShopGoodDetailView.getSelectedGoodNum();
        this.context= context;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (AddCarOrBuyGoodDialog.this.isShowing()) {
                    AddCarOrBuyGoodDialog.this.dismiss();
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
        setContentView(R.layout.add_car_dialog);
        ButterKnife.bind(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        tvName.setText(iShopGoodDetailView.getNameDialog());
        tvPrice.setText(StringUtil.getPriceSpannable12String(getContext(), iShopGoodDetailView.getPrice(), R.style.big_money, R.style.big_money));
        tvRemainNum.setText(String.format("库存量:%s件", iShopGoodDetailView.getRemainNum()));
        tvNum.setText(String.format("%d", num));
        GlideUtil.loadRemoteImg(context,iv,iShopGoodDetailView.getImageUrl());
        WindowManager.LayoutParams attributes = this.getWindow().getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(attributes);
        if(TextUtils.isEmpty(iShopGoodDetailView.getGuigeName())) {
            llguige.setVisibility(View.INVISIBLE);
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
                if (AddCarOrBuyGoodDialog.this.isShowing()) {
                    AddCarOrBuyGoodDialog.this.dismiss();
                }
            }
        });
    }

    @OnClick({R.id.close, R.id.plus, R.id.reduce, R.id.add_car_dialog, R.id.buy_dialog})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                if (AddCarOrBuyGoodDialog.this.isShowing()) {
                    AddCarOrBuyGoodDialog.this.dismiss();
                }
                break;
            case R.id.reduce:
                if (num > 1) {
                    num--;
                    iShopGoodDetailView.setSelectedGoodNum(num);
                    tvNum.setText(String.format("%d", num));
                }else if(num==1){
                    PopUtil.toastInBottom("最少一个商品");
                }
                break;
            case R.id.plus:
                if(num<Integer.valueOf(iShopGoodDetailView.getRemainNum())) {
                    num++;
                    iShopGoodDetailView.setSelectedGoodNum(num);
                    tvNum.setText(String.format("%d", num));
                }else {
                    PopUtil.toastInBottom("库存不够");
                }
                break;
            case R.id.add_car_dialog:
                if (AddCarOrBuyGoodDialog.this.isShowing()) {
                    AddCarOrBuyGoodDialog.this.dismiss();
                }
                iShopGoodDetailView.addCar();
                break;
            case R.id.buy_dialog:
                if (AddCarOrBuyGoodDialog.this.isShowing()) {
                    AddCarOrBuyGoodDialog.this.dismiss();
                }
                iShopGoodDetailView.buyGood();
                break;
            default:
                break;
        }
    }

    public void refreshData() {
        tvName.setText(iShopGoodDetailView.getNameDialog());
        tvPrice.setText(StringUtil.getPriceSpannable12String(getContext(), iShopGoodDetailView.getPrice(), R.style.big_money, R.style.big_money));
        tvRemainNum.setText(String.format("库存量:%s件", iShopGoodDetailView.getRemainNum()));
        tvNum.setText(String.format("%d", num));
        GlideUtil.loadRemoteImg(context,iv,iShopGoodDetailView.getImageUrl());
        if(TextUtils.isEmpty(iShopGoodDetailView.getGuigeName())) {
            llguige.setVisibility(View.INVISIBLE);
        }else {
            llguige.setVisibility(View.VISIBLE);
            tvGuigeName.setText(iShopGoodDetailView.getGuigeName());
            ad.notifyDataSetChanged();
        }
    }
}
