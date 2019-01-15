package com.msht.minshengbao.androidShop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.customerview.SelectAddressDialog;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.AreaBean;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IAddAddressView;
import com.msht.minshengbao.androidShop.viewInterface.IGetAreaListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddShopAdressActivity extends ShopBaseActivity implements IAddAddressView, IGetAreaListView {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_mob)
    EditText etPhone;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    private SelectAddressDialog selectAddressDialog;
    private List<AreaBean> provinceList = new ArrayList<AreaBean>();
    private List<AreaBean> cityList = new ArrayList<AreaBean>();
    private List<AreaBean> areaList = new ArrayList<AreaBean>();
    private String selectCityId;
    private String selectProvinceId;
    private String selectAreaId;
    private String selectArea_name;
    private String selectProvince_name;
    private String selectCity_name;

    @Override
    protected void setLayout() {
        setContentView(R.layout.add_shop_address);
    }

    @Override
    protected void initImmersionBar() {
      /*  super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).navigationBarColor(R.color.black).navigationBarWithKitkatEnable(false).init();
        ImmersionBar.setTitleBar(this, mToolbar);*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.ll_city, R.id.save_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_city:
                showSelectAddressDialog();
                break;
            case R.id.save_address:
                if(TextUtils.isEmpty(etName.getText().toString())){
                    PopUtil.showComfirmDialog(this,"","请填写姓名",null,"知道了",null,null,true);
                }else if(TextUtils.isEmpty(etPhone.getText().toString())){
                    PopUtil.showComfirmDialog(this,"","请填写有效收货联系电话",null,"知道了",null,null,true);
                }else if(TextUtils.isEmpty(selectCityId)||TextUtils.isEmpty(selectAreaId)){
                    PopUtil.showComfirmDialog(this,"","请选择城市",null,"知道了",null,null,true);
                }else if(TextUtils.isEmpty(etAddress.getText().toString())){
                    PopUtil.showComfirmDialog(this,"","请填写详细收货地址",null,"知道了",null,null,true);
                }else {
                    ShopPresenter.addAddress(this);
                }
            default:
                break;
        }
    }


    private void showSelectAddressDialog() {
        if (!isFinishing() && selectAddressDialog == null) {
            selectAddressDialog = new SelectAddressDialog(this, this, provinceList, cityList, areaList);
            selectAddressDialog.show();
        } else if (!isFinishing() && !selectAddressDialog.isShowing()) {
            selectAddressDialog.show();
        }
    }

    @Override
    public void onAddAddressSuccess(String s) {
     finish();
    }

    @Override
    public String getTrue_name() {
        return etName.getText().toString();
    }

    @Override
    public String getCity_id() {
        return selectCityId;
    }


    @Override
    public String getArea_id() {
        return selectAreaId;
    }

    @Override
    public String getArea_info() {
        return tvCity.getText().toString();
    }

    @Override
    public String getAddress() {
        return etAddress.getText().toString();
    }

    @Override
    public String getTel_phone() {
        return etPhone.getText().toString();
    }

    @Override
    public String getMob_phone() {
        return etPhone.getText().toString();
    }

    @Override
    public String isDefault() {
        return "0";
    }


    @Override
    public void onGetAreaListSuccess(String s) {
        areaList.clear();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject datas = jsonObject.optJSONObject("datas");
            JSONArray area_list = datas.optJSONArray("area_list");
            for (int i = 0; i < area_list.length(); i++) {
                JSONObject area = area_list.optJSONObject(i);
                String area_Id = area.optString("area_id");
                String area_name = area.optString("area_name");
                AreaBean bean;

                if(area_list.length()<4&&i==area_list.length()-1){
                    bean = new AreaBean(area_Id, area_name, true);
                    selectAreaId = area_Id;
                    selectArea_name = area_name;
                    tvCity.setText(selectProvince_name+selectCity_name+selectArea_name);
                }else if (i == 3) {
                    bean = new AreaBean(area_Id, area_name, true);
                    selectAreaId = area_Id;
                    selectArea_name = area_name;
                    tvCity.setText(selectProvince_name+selectCity_name+selectArea_name);
                } else {
                    bean = new AreaBean(area_Id, area_name, false);
                }
                areaList.add(bean);
            }
            selectAddressDialog.notifyRcl3();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetProviceListSuccess(String s) {
        provinceList.clear();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject datas = jsonObject.optJSONObject("datas");
            JSONArray area_list = datas.optJSONArray("area_list");
            for (int i = 0; i < area_list.length(); i++) {
                JSONObject area = area_list.optJSONObject(i);
                String area_Id = area.optString("area_id");
                String area_name = area.optString("area_name");
                AreaBean bean;
                if (i==0) {
                    bean = new AreaBean(area_Id, area_name, true);

            } else {
                    bean = new AreaBean(area_Id, area_name, false);
                }
                provinceList.add(bean);
            }
            selectAddressDialog.notifyRcl1();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetCityListSuccess(String s) {
        cityList.clear();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject datas = jsonObject.optJSONObject("datas");
            JSONArray area_list = datas.optJSONArray("area_list");
            for (int i = 0; i < area_list.length(); i++) {
                JSONObject area = area_list.optJSONObject(i);
                String area_Id = area.optString("area_id");
                String area_name = area.optString("area_name");
                AreaBean bean ;
              if(area_list.length()<4&&i==area_list.length()-1){
                    bean = new AreaBean(area_Id, area_name, true);
                    selectCityId = area_Id;
                    selectCity_name = area_name;
                    tvCity.setText(selectProvince_name+selectCity_name+selectArea_name);
                    ShopPresenter.getAreaList(this, area_Id);
                }else if (i == 3) {
                    bean = new AreaBean(area_Id, area_name, true);
                    selectCityId = area_Id;
                    selectCity_name = area_name;
                    tvCity.setText(selectProvince_name+selectCity_name+selectArea_name);
                    ShopPresenter.getAreaList(this, area_Id);
                } else {
                    bean = new AreaBean(area_Id, area_name, false);
                }
                cityList.add(bean);
            }
            selectAddressDialog.notifyRcl2();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSelectedAreaItem(int position) {
        for (int i = 0; i < areaList.size(); i++) {
            if(i==position){
                areaList.get(i).setSelected(true);
                selectAreaId = areaList.get(i).getArea_id();
                selectArea_name = areaList.get(i).getArea_name();
                tvCity.setText(selectProvince_name+selectCity_name+selectArea_name);
            }else {
                areaList.get(i).setSelected(false);
            }
        }
        selectAddressDialog.notifyRcl3();
    }

    @Override
    public void onSelectedCityItem(int position) {
        for (int i = 0; i < cityList.size(); i++) {
            if(i==position){
                cityList.get(i).setSelected(true);
                selectCityId = cityList.get(i).getArea_id();
                selectCity_name = cityList.get(i).getArea_name();
                tvCity.setText(selectProvince_name+selectCity_name+selectArea_name);
                ShopPresenter.getAreaList(this,cityList.get(i).getArea_id());
            }else {
                cityList.get(i).setSelected(false);
            }
        }
        selectAddressDialog.notifyRcl2();
    }

    @Override
    public void onSelectedProviceItem(int position) {
        for (int i = 0; i < provinceList.size(); i++) {
           if(i==position){
               provinceList.get(i).setSelected(true);
               selectProvinceId = provinceList.get(i).getArea_id();
               selectProvince_name = provinceList.get(i).getArea_name();
               tvCity.setText(selectProvince_name+selectCity_name+selectArea_name);
               ShopPresenter.getCityList(this,provinceList.get(i).getArea_id());
           }else {
               provinceList.get(i).setSelected(false);
           }
        }
        selectAddressDialog.notifyRcl1();
    }
}
