package com.msht.minshengbao.androidShop.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.activity.ShopComfirmOrdersActivity;
import com.msht.minshengbao.androidShop.activity.ShopGoodDetailActivity;
import com.msht.minshengbao.androidShop.adapter.NewCarListAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.ComfirmShopGoodBean;
import com.msht.minshengbao.androidShop.shopBean.ShopCarBean;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.ICarListView;
import com.msht.minshengbao.androidShop.viewInterface.IDeleteCarItemView;
import com.msht.minshengbao.androidShop.viewInterface.IModifyCarGoodNumView;
import com.msht.minshengbao.androidShop.viewInterface.OnDissmissLisenter;
import com.msht.minshengbao.events.CarNumEvent;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class NewShopCarFragment extends ShopBaseLazyFragment implements NewCarListAdapter.CarListListener, OnRefreshListener, ICarListView, IModifyCarGoodNumView, IDeleteCarItemView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.total_money)
    TextView tvToatl;
    @BindView(R.id.buy)
    TextView tvBuy;
    @BindView(R.id.ll_edit_bottom)
    LinearLayout ll_edit_bottom;
    @BindView(R.id.ll_finish_bottom)
    LinearLayout ll_finish_bottom;
    @BindView(R.id.edit)
    CheckBox cbEdit;
    @BindView(R.id.select_all)
    CheckBox cbSelectAll;
    private CarParentListener carParentListener;
    private List<ShopCarBean> dataList = new ArrayList<ShopCarBean>();
    private NewCarListAdapter adapter;
    private boolean isViewCreated;
    private boolean isEdited = false;
    private String cart_id;
    private int goods_num;
    private int modifyPosition;
    private int modifyChilposition;

    public interface CarParentListener {
        void changeEmpty();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.shop_car_list;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        carParentListener = (CarParentListener) getParentFragment();
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mToolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(getContext()), 0, 0);
        rcl.setLayoutManager(linearLayoutManager);
        adapter = new NewCarListAdapter(getContext(), R.layout.item_car_list, dataList);
        adapter.setCarListListener(this);
        rcl.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        cbEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEdited = isChecked;
                if (isChecked) {
                    cbEdit.setText("完成");
                    ll_finish_bottom.setVisibility(View.VISIBLE);
                    ll_edit_bottom.setVisibility(View.INVISIBLE);
                } else {
                    cbEdit.setText("编辑");
                    ll_finish_bottom.setVisibility(View.INVISIBLE);
                    ll_edit_bottom.setVisibility(View.VISIBLE);
                }
                for (ShopCarBean bean : dataList) {
                    bean.setEdit(isChecked);
                    for (ShopCarBean.DatasBean.goodBean goodbean : bean.getDatasBean().getGoodBeanList()) {
                        goodbean.setEdite(isEdited);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isAllStoreChecked() && isChecked) {
                    for (ShopCarBean bean : dataList) {
                        bean.setCheckStore(true);
                        for (ShopCarBean.DatasBean.goodBean goodbean : bean.getDatasBean().getGoodBeanList()) {
                            goodbean.setSelected(true);
                        }
                    }
                    updateAmount();
                    adapter.notifyDataSetChanged();
                } else if (isAllStoreChecked() && !isChecked) {
                    for (ShopCarBean bean : dataList) {
                        bean.setCheckStore(false);
                        for (ShopCarBean.DatasBean.goodBean goodbean : bean.getDatasBean().getGoodBeanList()) {
                            goodbean.setSelected(false);
                        }
                    }
                    updateAmount();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
    }

    @Override
    protected void initData() {
        if (!getKey().equals("")) {
            ShopPresenter.getCarList(this, true);
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (isViewCreated) {
            if (!getKey().equals("")) {
                ShopPresenter.getCarList(this, true);
            }
        }
    }

    //适配首页的add hide show架构，如果显示的同时做更新操作需要外放接口供父容器调用
    public void refreshCarList() {
        ShopPresenter.getCarList(this, true);
    }

    @Override
    public void onGetCarListSuccess(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.finishLoadMoreWithNoMoreData();
        refreshLayout.setNoMoreData(true);
        try {

            JSONObject jsonObject = new JSONObject(s);
            JSONObject datas = jsonObject.optJSONObject("datas");
            JSONArray jsonArray = datas.optJSONArray("cart_list");
            int carnum = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray good = jsonArray.optJSONObject(i).optJSONArray("goods");
                carnum += good.length();
            }

            EventBus.getDefault().postSticky(new CarNumEvent(carnum));
            dataList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                ShopCarBean shopCarbean = new ShopCarBean();
                JSONObject obj = jsonArray.optJSONObject(i);
                shopCarbean.setStoreId(obj.optString("store_id"));
                shopCarbean.setStoreName(obj.optString("store_name"));
                JSONArray goodsArray = obj.optJSONArray("goods");
                List<ShopCarBean.DatasBean.goodBean> goodBeanList = new ArrayList<ShopCarBean.DatasBean.goodBean>();
                for (int ii = 0; ii < goodsArray.length(); ii++) {
                    JSONObject goodobj = goodsArray.optJSONObject(ii);
                    ShopCarBean.DatasBean.goodBean goodbean = new ShopCarBean.DatasBean.goodBean(obj.optString("store_name"), obj.optString("store_id"), false, isEdited);
                    goodbean.setSelected(false);
                    goodbean.setGoodGuige(goodobj.optString("goods_spec"));
                    goodbean.setGoodImage(goodobj.optString("goods_image_url"));
                    goodbean.setGoodId(goodobj.optString("goods_id"));
                    goodbean.setGoodName(goodobj.optString("goods_name"));
                    goodbean.setGoodPrice(goodobj.optString("goods_price"));
                    goodbean.setGoodNum(Integer.valueOf(goodobj.optString("goods_num")));
                    goodbean.setGoodStorageNum(goodobj.optString("goods_storage"));
                    goodbean.setIsPickupSelf(goodobj.optString("pickup_self"));
                    goodbean.setCarId(goodobj.optString("cart_id"));
                    goodbean.setEdite(false);
                    goodbean.setStorageSate(goodobj.optBoolean("storage_state"));
                    goodBeanList.add(goodbean);
                }
                shopCarbean.setDatasBean(new ShopCarBean.DatasBean(goodBeanList));
                dataList.add(shopCarbean);
            }
            if (dataList.size() == 0) {
                carParentListener.changeEmpty();
            }
            updateAmount();
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateAmount() {
        double total = 0;
        for (ShopCarBean shopCarBean : dataList) {
            for (ShopCarBean.DatasBean.goodBean goodbean : shopCarBean.getDatasBean().getGoodBeanList()) {
                if (goodbean.isSelected()) {
                    total += Double.valueOf(goodbean.getGoodPrice()) * goodbean.getGoodNum();
                }
            }
        }
        tvToatl.setText(String.format("合计：%s", StringUtil.getPriceSpannable12String(getContext(), total + "", R.style.big_money, R.style.big_money)));
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
    }

    @OnClick({R.id.buy2, R.id.delete, R.id.buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buy:
            case R.id.buy2:
                goToBuy();
                break;
            case R.id.delete:
                deleteCarItem();
            default:
                break;
        }
    }

    private void deleteCarItem() {
        if (isHasGoodChecked()) {
            ShopPresenter.deleteCarItems(this);
        } else {
            PopUtil.showComfirmDialog(getContext(), null, "请至少选择一件商品", null, "好的", null, null, true);
        }
    }

    private void goToBuy() {
        if (isHasGoodChecked()) {
            List<ComfirmShopGoodBean> buyList = new ArrayList<ComfirmShopGoodBean>();
            for (ShopCarBean carbean : dataList) {
                if (isStoreHasGoodChecked(carbean.getDatasBean().getGoodBeanList())) {
                    ComfirmShopGoodBean combean = new ComfirmShopGoodBean();
                    combean.setStore_name(carbean.getStoreId());
                    combean.setStore_id(carbean.getStoreName());
                    List<ComfirmShopGoodBean.GoodsBean> goodsBean = new ArrayList<ComfirmShopGoodBean.GoodsBean>();
                    for (ShopCarBean.DatasBean.goodBean goodBean : carbean.getDatasBean().getGoodBeanList()) {
                        if (goodBean.isSelected() && goodBean.getStorageSate()) {
                            ComfirmShopGoodBean.GoodsBean bb = new ComfirmShopGoodBean.GoodsBean(goodBean.getStoreName(), goodBean.getStoreId(), goodBean.getGoodImage(), goodBean.getGoodName(), goodBean.getGoodNum() + "", goodBean.getGoodPrice(), goodBean.getGoodId());
                            bb.setPickup_self(goodBean.getIsPickupSelf());
                            bb.setCart_id(goodBean.getCarId());
                            bb.setGoods_spec(goodBean.getGoodGuige());
                            goodsBean.add(bb);
                        } else if (goodBean.isSelected() && !goodBean.getStorageSate()) {
                            PopUtil.toastInBottom(goodBean.getGoodName() + "库存不足或已经下架");
                        }
                    }
                    if (goodsBean.size() != 0) {
                        combean.setGoods(goodsBean);
                        buyList.add(combean);
                    }
                }
            }
            if (buyList.size() == 0) {
                PopUtil.showComfirmDialog(getContext(), null, "请选择至少一件有库存和未下架的商品", null, "好的", null, null, true);
            } else if (isSingleType(buyList)) {
                Intent intent = new Intent(getActivity(), ShopComfirmOrdersActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ifCar", "1");
                bundle.putString("isPickup_self", buyList.get(0).getGoods().get(0).getPickup_self());
                bundle.putSerializable("data", (Serializable) buyList);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                PopUtil.showComfirmDialog(getContext(), null, "不可同时购买自提商品和普通商品哦亲", null, "好的", null, null, true);
            }
        } else {
            PopUtil.showComfirmDialog(getContext(), null, "请选择至少一件商品", null, "好的", null, null, true);
        }

    }

    private boolean isSingleType(List<ComfirmShopGoodBean> buyList) {
        String pickUpSelf = buyList.get(0).getGoods().get(0).getPickup_self();
        for (ComfirmShopGoodBean comfirmShopGoodBean : buyList) {
            for (ComfirmShopGoodBean.GoodsBean bean : comfirmShopGoodBean.getGoods()) {
                if (!TextUtils.equals(pickUpSelf, bean.getPickup_self())) {
                    return false;
                }
            }

        }
        return true;
    }

    @Override
    public String getCarId() {
        return cart_id;
    }

    @Override
    public String getCarItemNum() {
        return goods_num + "";
    }

    @Override
    public void onModifyGoodNumSuccess(String s) {
        PopUtil.showAutoDissHookDialog(getContext(), "购物车数量修改成功", 100, new OnDissmissLisenter() {
            @Override
            public void onDissmiss() {
                dataList.get(modifyPosition).getDatasBean().getGoodBeanList().get(modifyChilposition).setGoodNum(goods_num);
                updateAmount();
            }
        });
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    List<ShopCarBean> selectedGoodlist = new ArrayList<ShopCarBean>();

    @Override
    public String getSelectCartList() {
        selectedGoodlist.clear();
        StringBuilder sf = new StringBuilder();
        if (isHasGoodChecked()) {
            for (ShopCarBean shopCarBean : dataList) {
                if (isStoreHasGoodChecked(shopCarBean.getDatasBean().getGoodBeanList())) {
                    selectedGoodlist.add(shopCarBean);
                    for (ShopCarBean.DatasBean.goodBean goodBean : shopCarBean.getDatasBean().getGoodBeanList()) {
                        if (goodBean.isSelected()) {
                            if (TextUtils.isEmpty(sf.toString())) {
                                sf.append(goodBean.getCarId());
                            } else {
                                sf.append("|").append(goodBean.getCarId());
                            }
                        }
                    }

                }
            }
        } else {
            PopUtil.toastInCenter("没有可购买购物车商品");
        }
        return sf.toString();
    }

    @Override
    public void onDeleteCarItemsSuccess(String s) {
        PopUtil.showAutoDissHookDialog(getContext(), "购物车删除成功", 0, new OnDissmissLisenter() {
            @Override
            public void onDissmiss() {
                for (ShopCarBean shopCarBean : selectedGoodlist) {
                    if (shopCarBean.isCheckStore()) {
                        dataList.remove(shopCarBean);
                    } else {
                        int index = dataList.indexOf(shopCarBean);
                        //当list 数据量大的时候for each里面使用remove add 会出现ConcurrentModificationException异常
                        List<ShopCarBean.DatasBean.goodBean> goodlist = shopCarBean.getDatasBean().getGoodBeanList();
                        for (int i=0;i<goodlist.size();i++) {
                            ShopCarBean.DatasBean.goodBean goodBean = goodlist.get(i);
                            if (goodBean.isSelected()) {
                                dataList.get(index).getDatasBean().getGoodBeanList().remove(goodBean);
                            }
                        }
                      /*  //解决方法
                        for (ShopCarBean bean : dataList) {
                            if (bean.equals(shopCarBean)) {
                                List<ShopCarBean.DatasBean.goodBean> goodlist = bean.getDatasBean().getGoodBeanList();
                                Iterator<ShopCarBean.DatasBean.goodBean> iterator2 = goodlist.iterator();
                                while (iterator2.hasNext()) {
                                    ShopCarBean.DatasBean.goodBean good = iterator2.next();
                                    if (good.isSelected()) {
                                        iterator2.remove();
                                    }
                                }
                                break;
                            }
                        }*/
                    }
                }
                updateAmount();
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onGoodItemCheckChange(int chilposition, int position, boolean isCheck) {
        if (!dataList.get(position).getDatasBean().getGoodBeanList().get(chilposition).isSelected() && isCheck) {
            dataList.get(position).getDatasBean().getGoodBeanList().get(chilposition).setSelected(true);
            if (isStoreAllGoodChecked(dataList.get(position).getDatasBean().getGoodBeanList())) {
                dataList.get(position).setCheckStore(true);
                adapter.notifyDataSetChanged();
                if (isAllStoreChecked()) {
                    cbSelectAll.setChecked(true);
                }
            }
            updateAmount();
        } else if (dataList.get(position).getDatasBean().getGoodBeanList().get(chilposition).isSelected() && !isCheck) {
            dataList.get(position).getDatasBean().getGoodBeanList().get(chilposition).setSelected(false);
            if (dataList.get(position).isCheckStore()) {
                dataList.get(position).setCheckStore(false);
                adapter.notifyDataSetChanged();
            }
            updateAmount();
            cbSelectAll.setChecked(false);
        }
    }

    private boolean isStoreAllGoodChecked(List<ShopCarBean.DatasBean.goodBean> goodBeanList) {
        for (ShopCarBean.DatasBean.goodBean goodbean : goodBeanList) {
            if (!goodbean.isSelected()) {
                return false;
            }
        }
        return true;
    }

    private boolean isStoreHasGoodChecked(List<ShopCarBean.DatasBean.goodBean> goodBeanList) {
        for (ShopCarBean.DatasBean.goodBean goodbean : goodBeanList) {
            if (goodbean.isSelected()) {
                return true;
            }
        }
        return false;
    }

    private boolean isHasGoodChecked() {
        for (ShopCarBean bean : dataList) {
            for (ShopCarBean.DatasBean.goodBean goodbean : bean.getDatasBean().getGoodBeanList()) {
                if (goodbean.isSelected()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAllStoreChecked() {
        for (ShopCarBean bean : dataList) {
            if (!bean.isCheckStore()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onStoreCheckChange(int position, boolean isCheck) {
        if (!dataList.get(position).isCheckStore() && isCheck) {
            dataList.get(position).setCheckStore(true);
            for (ShopCarBean.DatasBean.goodBean goodbean : dataList.get(position).getDatasBean().getGoodBeanList()) {
                if (!goodbean.isSelected()) {
                    goodbean.setSelected(true);
                }
            }
            updateAmount();
            if (isAllStoreChecked()) {
                cbSelectAll.setChecked(true);
            }
        } else if (dataList.get(position).isCheckStore() && !isCheck) {
            dataList.get(position).setCheckStore(false);
            for (ShopCarBean.DatasBean.goodBean goodbean : dataList.get(position).getDatasBean().getGoodBeanList()) {
                if (goodbean.isSelected()) {
                    goodbean.setSelected(false);
                }
            }
            updateAmount();
            cbSelectAll.setChecked(false);
        }
    }

    @Override
    public void onModifyItemNum(int chilposition, int position, int num) {
        modifyPosition = position;
        modifyChilposition = chilposition;
        cart_id = dataList.get(position).getDatasBean().getGoodBeanList().get(chilposition).getCarId();
        goods_num = num;
        ShopPresenter.modifyGoodNum(this, this);
    }

    @Override
    public void onGotoGoodDetail(String goodid) {
        Intent intent = new Intent(getActivity(), ShopGoodDetailActivity.class);
        startActivity(intent.putExtra("goodsid", goodid));
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        if (!getKey().equals("")) {
            ShopPresenter.getCarList(this, false);
        }
    }
}
