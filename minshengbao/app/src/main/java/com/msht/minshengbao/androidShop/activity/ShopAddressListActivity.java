package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.AddressListAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.ShopAddressListBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IDeleteAddressView;
import com.msht.minshengbao.androidShop.viewInterface.IGetAddressListView;
import com.msht.minshengbao.androidShop.viewInterface.IEditAddressView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopAddressListActivity extends ShopBaseActivity implements IEditAddressView, IGetAddressListView, AddressListAdapter.OnAddressItemClickListner, MyHaveHeadViewRecyclerAdapter.OnItemClickListener, IDeleteAddressView {
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    private List<ShopAddressListBean.DatasBean.AddressListBean> list = new ArrayList<ShopAddressListBean.DatasBean.AddressListBean>();
    private AddressListAdapter addressListAdapter;
    private String isDefault;
    private String mob_phone;
    private String tel_phone;
    private String address;
    private String area_info;
    private String area_id;
    private String address_id;
    private String true_name;
    private String city_id;
    private String deleteAddress_id;
    private int deletePosition=0;

    @Override
    protected void setLayout() {
        setContentView(R.layout.select_address);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).navigationBarColor(R.color.black).navigationBarWithKitkatEnable(false).init();
        ImmersionBar.setTitleBar(this, mToolbar);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcl.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        addressListAdapter = new AddressListAdapter(this, this);
        addressListAdapter.setDatas(list);
        addressListAdapter.setOnItemClickListener(this);
        rcl.setAdapter(addressListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShopPresenter.getAddressList(this,true);
    }

    @OnClick({R.id.back,R.id.add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add_address:
                Intent intent = new Intent(this, AddShopAdressActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onEditAddressSuccess() {
        ShopPresenter.getAddressList(this,false);
        PopUtil.showAutoDissHookDialog(this, "设置默认地址成功", 500);
    }

    @Override
    public String getAddress_id() {
        return address_id;
    }

    @Override
    public String getTrue_name() {
        return true_name;
    }

    @Override
    public String getCity_id() {
        return city_id;
    }

    @Override
    public String getArea_id() {
        return area_id;
    }

    @Override
    public String getArea_info() {
        return area_info;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getTel_phone() {
        if (TextUtils.isEmpty(tel_phone)) {
            tel_phone = mob_phone;
        }
        return tel_phone;
    }

    @Override
    public String getMob_phone() {
        return mob_phone;
    }

    @Override
    public String isDefault() {
        return isDefault;
    }

    @Override
    public void onGetAddressListSuccess(String s) {
        ShopAddressListBean shopAddressListBean = JsonUtil.toBean(s, ShopAddressListBean.class);
        list.clear();
        list.addAll(shopAddressListBean.getDatas().getAddress_list());
        addressListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemCheckedChange(int position, boolean isChecked) {
        ShopAddressListBean.DatasBean.AddressListBean bean = list.get(position);
        //不是默认地址，选中该地址，发起请求设置默认地址
        if (!bean.getIs_default().equals("1") && isChecked) {
            address_id = bean.getAddress_id();
            true_name = bean.getTrue_name();
            city_id = bean.getCity_id();
            area_id = bean.getArea_id();
            area_info = bean.getArea_info();
            address = bean.getAddress();
            tel_phone = bean.getTel_phone();
            mob_phone = bean.getMob_phone();
            isDefault = "1";
            for (int i = 0; i < list.size(); i++) {
                ShopAddressListBean.DatasBean.AddressListBean bean2 = list.get(i);
                if (bean2.getIs_default().equals("1") && i != position) {
                    bean2.setIs_default("0");
                    list.set(i, bean2);
                    break;
                }
            }
            addressListAdapter.notifyDataSetChanged();
            ShopPresenter.editAddress(this);
        }
    }

    @Override
    public void onItemEdit(int position) {
       Intent intent =  new Intent(this,ShopEditAddressActivity.class);
        ShopAddressListBean.DatasBean.AddressListBean bean = list.get(position);
        intent.putExtra("data",bean);
        startActivity(intent);
    }

    @Override
    public void onItemDeleted(int position) {
        deleteAddress_id = list.get(position).getAddress_id();
        deletePosition = position;
         ShopPresenter.deleteAddress(this);
    }

    @Override
    public void onItemClick(int position) {
        ShopAddressListBean.DatasBean.AddressListBean bean = list.get(position);
        Intent intent = new Intent();
        intent.putExtra("data",bean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ShopPresenter.getAddressList(this,true);
    }

    @Override
    public String getDeleteAddressId() {
        return deleteAddress_id;
    }

    @Override
    public void onDeleteAddressSuccess(String s) {
        PopUtil.showAutoDissHookDialog(this,"成功删除收货地址",0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.remove(deletePosition);
                addressListAdapter.notifyDataSetChanged();
            }
        },1500);

    }
}
