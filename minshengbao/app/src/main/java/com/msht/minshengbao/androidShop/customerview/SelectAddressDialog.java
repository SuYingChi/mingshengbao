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
import android.widget.LinearLayout;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.AddressSelectAdapter;
import com.msht.minshengbao.androidShop.adapter.CircleScrollerRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.AreaBean;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.viewInterface.IAddAddressView;
import com.msht.minshengbao.androidShop.viewInterface.IGetAreaListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectAddressDialog extends Dialog {

    private final IGetAreaListView iGetAreaListView;
    @BindView(R.id.province)
    RecyclerView rcl1;
    @BindView(R.id.city)
    RecyclerView rcl2;
    @BindView(R.id.area)
    RecyclerView rcl3;

    private Context context;
    private AddressSelectAdapter rcl1Adapter;
    private AddressSelectAdapter rcl2Adapter;
    private AddressSelectAdapter rcl3Adapter;
    private List<AreaBean> provinceList;
    private List<AreaBean> cityList;
    private List<AreaBean> areaList;

    public SelectAddressDialog(@NonNull Context context, IGetAreaListView iGetAreaListView,List<AreaBean> provinceList,List<AreaBean> cityList,List<AreaBean> areaList) {
        super(context, R.style.ActionSheetDialogStyle);
        this.iGetAreaListView = iGetAreaListView;
        this.context = context;
        this.provinceList = provinceList;
        this.cityList = cityList;
        this.areaList = areaList;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (SelectAddressDialog.this.isShowing()) {
                    SelectAddressDialog.this.dismiss();
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
        setContentView(R.layout.select_address_dialog);
        ButterKnife.bind(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = this.getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(attributes);
        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) rcl1.getLayoutParams();
        lp1.width= DimenUtil.getScreenWidth()/3;
        rcl1.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) rcl2.getLayoutParams();
        lp2.width= DimenUtil.getScreenWidth()/3;
        rcl2.setLayoutParams(lp2);
        LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) rcl3.getLayoutParams();
        lp3.width= DimenUtil.getScreenWidth()/3;
        rcl3.setLayoutParams(lp3);
        rcl1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        rcl1Adapter = new AddressSelectAdapter(context);
        rcl1Adapter.setDatas(provinceList);
       rcl1Adapter.setOnItemClickListener(new CircleScrollerRecyclerAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(int position) {
               iGetAreaListView.onSelectedProviceItem(position);
           }
       });
        rcl1.setAdapter(rcl1Adapter);
        rcl1.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx,int dy){
                super.onScrolled(recyclerView,dx,dy);
                LinearLayoutManager l = (LinearLayoutManager)recyclerView.getLayoutManager();
                int adapterNowPos = l.findFirstCompletelyVisibleItemPosition();
                int nowLastPos=l.findLastCompletelyVisibleItemPosition();
                int selectPosition;
                LogUtils.e("adapterNowPos=="+adapterNowPos+"-------nowLastPos=="+nowLastPos);
                if(adapterNowPos==0){
                    selectPosition =0;
                } else if((nowLastPos+adapterNowPos)%2!=0){
                    selectPosition = (nowLastPos + adapterNowPos )/ 2 + 1;
                }else {
                    selectPosition = (nowLastPos + adapterNowPos) / 2;
                }
                if(provinceList.size()!=0){
                    //循环滚动要取余
                    iGetAreaListView.onSelectedProviceItem(selectPosition % provinceList.size());
                }
            }
        });

        rcl2.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        rcl2Adapter = new AddressSelectAdapter(context);
        rcl2Adapter.setOnItemClickListener(new CircleScrollerRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                iGetAreaListView.onSelectedCityItem(position);
            }
        });
        rcl2Adapter.setDatas(cityList);
        rcl2.setAdapter(rcl2Adapter);
        rcl2.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx,int dy){
                super.onScrolled(recyclerView,dx,dy);
                LinearLayoutManager l = (LinearLayoutManager)recyclerView.getLayoutManager();
                int adapterNowPos = l.findFirstCompletelyVisibleItemPosition();
                int nowLastPos=l.findLastCompletelyVisibleItemPosition();
                int selectPosition;
                if(adapterNowPos==0){
                    selectPosition = 0;
                }
               else  if((nowLastPos+adapterNowPos)%2!=0){
                    selectPosition = (nowLastPos + adapterNowPos )/ 2 + 1;
                }else {
                    selectPosition = (nowLastPos + adapterNowPos) / 2;
                }
                if(cityList.size()!=0){
                    //循环滚动要取余
                    iGetAreaListView.onSelectedCityItem(selectPosition % cityList.size());
                }
            }
        });
        rcl3.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        rcl3Adapter = new AddressSelectAdapter(context);
        rcl3Adapter.setOnItemClickListener(new CircleScrollerRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                iGetAreaListView.onSelectedAreaItem(position);
            }
        });
        rcl3Adapter.setDatas(areaList);
        rcl3.setAdapter(rcl3Adapter);
        rcl3.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx,int dy) {
                super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int adapterNowPos = l.findFirstCompletelyVisibleItemPosition();
                    int nowLastPos = l.findLastCompletelyVisibleItemPosition();
                    int selectPosition;
                    if ((nowLastPos + adapterNowPos) % 2 != 0) {
                        selectPosition = (nowLastPos + adapterNowPos) / 2 + 1;
                    } else {
                        selectPosition = (nowLastPos + adapterNowPos) / 2;
                    }
                    if (areaList.size() != 0) {
                        //循环滚动要取余
                        iGetAreaListView.onSelectedAreaItem(selectPosition % areaList.size());
                    }
            }
        });
        ShopPresenter.getProvinceAreaList(iGetAreaListView);
    }

    @OnClick({R.id.finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                if (SelectAddressDialog.this.isShowing()) {
                    SelectAddressDialog.this.dismiss();
                }
                break;
            default:
                break;
        }
    }


    public void notifyRcl1() {
        rcl1Adapter.notifyDataSetChanged();

    }

    public void notifyRcl2() {
        rcl2Adapter.notifyDataSetChanged();

    }

    public void notifyRcl3() {
        rcl3Adapter.notifyDataSetChanged();
    }
}
