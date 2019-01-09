package com.msht.minshengbao.androidShop.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.ShopComfirmOrdersActivity;
import com.msht.minshengbao.androidShop.adapter.CarListAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.CarGoodItemBean;
import com.msht.minshengbao.androidShop.shopBean.ComfirmShopGoodBean;
import com.msht.minshengbao.androidShop.shopBean.ShopStoreBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.ICarListView;
import com.msht.minshengbao.androidShop.viewInterface.IDeleteCarItemView;
import com.msht.minshengbao.androidShop.viewInterface.IModifyCarGoodNumView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import com.msht.minshengbao.events.CarNumEvent;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class ShopCarFragment extends ShopBaseLazyFragment implements ICarListView, IModifyCarGoodNumView, OnRefreshListener, IDeleteCarItemView {
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
    private CarListAdapter adapter;
    private List<JSONObject> carList = new ArrayList<JSONObject>();
    private String sum;
    private String cart_count;
    private List<JSONObject> guessList;
    private boolean isNotifyAdapter = true;
    private List<ShopStoreBean> selectedStoreList = new ArrayList<ShopStoreBean>();
    private List<CarGoodItemBean> selectedGoodList = new ArrayList<CarGoodItemBean>();
    private List<ShopStoreBean> storeList = new ArrayList<ShopStoreBean>();
    private String cart_id;
    private String goods_num;
    private Handler handler = new Handler();
    private boolean isViewCreated = false;
    private List<JSONObject> gotoBuyList = new ArrayList<JSONObject>();
    private CarParentListener carParentListener;
    private boolean initUnselectState = false;
    private int carnum;
    private boolean isModifyGoodNum = false;


    @Override
    protected int setLayoutId() {
        return R.layout.shop_car_list;
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        carParentListener = (CarParentListener) getParentFragment();
    }

    @Override
    protected void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(linearLayoutManager);
       // rcl.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new CarListAdapter(getContext());
        adapter.setDatas(carList);
        adapter.setCarListListener(new CarListAdapter.CarListListener() {
            @Override
            public void onUncheckItem(final JSONObject goodObject, final ShopStoreBean storebject,final int childPosition, final int position) {
                isNotifyAdapter = false;
                String cart_id = goodObject.optString("cart_id");
                String goods_id = goodObject.optString("goods_id");
                String goods_num = goodObject.optString("goods_num");
                String goods_price = goodObject.optString("goods_price");
                CarGoodItemBean bean = new CarGoodItemBean(cart_id, goods_id, goods_num, goods_price);
                int position2 = storeList.indexOf(storebject);
                LogUtils.e("---position2----"+position2+"------childPosition-----"+childPosition);
                try {
                    carList.get(position2).optJSONArray("goods").optJSONObject(childPosition).put("goodcheck",false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (selectedGoodList.contains(bean)) {
                    gotoBuyList.remove(selectedGoodList.indexOf(bean));
                    selectedGoodList.remove(bean);
                    updateAmount();
                }
                if (cbSelectAll.isChecked()) {
                    cbSelectAll.setChecked(false);
                }
            }

            //此时店铺下的商品全部也未选中
            @Override
            public void onUncheckStoreItem(final ShopStoreBean storeObject) {
                LogUtils.e("------onUncheckStoreItem===" + storeObject);
                isNotifyAdapter = false;
                if (selectedStoreList.contains(storeObject)) {
                    selectedStoreList.remove(storeObject);
                }
                int position = storeList.indexOf(storeObject);
                JSONObject item = carList.get(position);
                JSONArray goods = item.optJSONArray("goods");
                try {
                    carList.get(position).put("storecheck",false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < goods.length(); i++) {
                    JSONObject goodObject = goods.optJSONObject(i);
                    String cart_id = goodObject.optString("cart_id");
                    String goods_id = goodObject.optString("goods_id");
                    String goods_num = goodObject.optString("goods_num");
                    String goods_price = goodObject.optString("goods_price");
                    CarGoodItemBean bean = new CarGoodItemBean(cart_id, goods_id, goods_num, goods_price);
                    if (selectedGoodList.contains(bean)) {
                        gotoBuyList.remove(selectedGoodList.indexOf(bean));
                        selectedGoodList.remove(bean);
                        updateAmount();
                    }
                }
                if (cbSelectAll.isChecked()) {
                    cbSelectAll.setChecked(false);
                }
            }

            @Override
            public void onCheckStoreItem(final ShopStoreBean storeObject) {
                LogUtils.e("-----onCheckStoreItem===" + storeObject);
                isNotifyAdapter = false;
                if (!selectedStoreList.contains(storeObject)) {
                    selectedStoreList.add(storeObject);
                }
                int position = storeList.indexOf(storeObject);
                JSONObject item = carList.get(position);
                JSONArray goods = item.optJSONArray("goods");
                try {
                    carList.get(position).put("storecheck",true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < goods.length(); i++) {
                    JSONObject goodObject = goods.optJSONObject(i);
                    String cart_id = goodObject.optString("cart_id");
                    String goods_id = goodObject.optString("goods_id");
                    String goods_num = goodObject.optString("goods_num");
                    String goods_price = goodObject.optString("goods_price");
                    CarGoodItemBean bean = new CarGoodItemBean(cart_id, goods_id, goods_num, goods_price);
                  /*  if (!goodObject.optBoolean("storage_state")) {
                    } else */
                    if (!selectedGoodList.contains(bean)) {
                        selectedGoodList.add(bean);
                        gotoBuyList.add(goodObject);
                        updateAmount();
                    }
                }
                if (!cbSelectAll.isChecked() && isAllSelected()) {
                    cbSelectAll.setChecked(true);
                }
            }

            @Override
            public void onCheckGoodItem(final JSONObject goodObject,final  ShopStoreBean storebject,final int childPosition,final int position) {
                String cart_id = goodObject.optString("cart_id");
                String goods_id = goodObject.optString("goods_id");
                String goods_num = goodObject.optString("goods_num");
                String goods_price = goodObject.optString("goods_price");
                CarGoodItemBean bean = new CarGoodItemBean(cart_id, goods_id, goods_num, goods_price);
                //刷新过列表后，外层recycleview的position传不对，只穿1，不知道为啥
                int position2 = storeList.indexOf(storebject);
                LogUtils.e("---position2----"+position2+"------childPosition-----"+childPosition);
                try {
                    carList.get(position2).optJSONArray("goods").optJSONObject(childPosition).put("goodcheck",true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!selectedGoodList.contains(bean)) {
                    selectedGoodList.add(bean);
                    updateAmount();
                    gotoBuyList.add(goodObject);
                } else {
                    int selectedGood = selectedGoodList.indexOf(bean);
                    if (!selectedGoodList.get(selectedGood).equals(goods_num)) {
                        selectedGoodList.remove(selectedGood);
                        selectedGoodList.add(selectedGood, bean);
                        updateAmount();
                        gotoBuyList.remove(selectedGood);
                        gotoBuyList.add(selectedGood, goodObject);
                    }
                }

            }

            //此时该店铺下还有商品选中
            @Override
            public void onUnCheckGoodAndUnCheckStoreItem(final ShopStoreBean storeBean) {
                isNotifyAdapter = false;
                if (selectedStoreList.contains(storeBean)) {
                    selectedStoreList.remove(storeBean);
                }
                if (cbSelectAll.isChecked()) {
                    cbSelectAll.setChecked(false);
                }
            }

            @Override
            public void onModifyGoodNum(final JSONObject goodObject) {
                cart_id = goodObject.optString("cart_id");
                goods_num = goodObject.optString("goods_num");
                ShopPresenter.modifyGoodNum(ShopCarFragment.this, ShopCarFragment.this);
            }

            @Override
            public void onGoodDetail(final String goodid) {
              /*  Map<String, String> map = new HashMap<String, String>();
                map.put("type", "goods");
                map.put("data", goodid);
                doNotAdClick(map);*/
                doShopItemViewClick("goods", goodid);
            }
        });
        rcl.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        cbEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbEdit.setText("完成");
                    ll_finish_bottom.setVisibility(View.VISIBLE);
                    ll_edit_bottom.setVisibility(View.INVISIBLE);
                    adapter.finishStatus();
                } else {
                    cbEdit.setText("编辑");
                    ll_finish_bottom.setVisibility(View.INVISIBLE);
                    ll_edit_bottom.setVisibility(View.VISIBLE);
                    adapter.editStatus();
                }
            }
        });
        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //当未全选时，点击了全选按钮，此时需要notify，设为全选
                if (!isAllSelected() && isChecked) {
                    isNotifyAdapter = true;
                }
                //点击全选按钮时，此时未全部选中，
                if (isNotifyAdapter) {
                    if (isChecked) {
                        for (JSONObject object : carList) {
                            ShopStoreBean bean = new ShopStoreBean(object.optString("store_name"), object.optString("store_id"));
                            JSONArray goods = object.optJSONArray("goods");
                            for (int i = 0; i < goods.length(); i++) {
                                JSONObject obj = goods.optJSONObject(i);
                                String cart_id = obj.optString("cart_id");
                                String goods_id = obj.optString("goods_id");
                                String goods_num = obj.optString("goods_num");
                                String goods_price = obj.optString("goods_price");
                                CarGoodItemBean goodBean = new CarGoodItemBean(cart_id, goods_id, goods_num, goods_price);
                                if (!selectedGoodList.contains(goodBean)) {
                                    selectedGoodList.add(goodBean);
                                    updateAmount();
                                    gotoBuyList.add(obj);
                                }
                            }
                            if (!selectedStoreList.contains(bean)) {
                                selectedStoreList.add(bean);
                            }
                        }

                    } else {
                        selectedStoreList.clear();
                        selectedGoodList.clear();
                        updateAmount();
                        gotoBuyList.clear();
                    }
                    initUnselectState = false;
                    adapter.isSelectAllAndNotify(isChecked, isNotifyAdapter, initUnselectState);
                } else if (!initUnselectState) {
                    //当取消选择列表某项时，此时全选按钮为未选择，但是不触发列表的刷新，只是恢复标志位为默认的true
                    isNotifyAdapter = true;
                } else {
                    initUnselectState = false;
                }
            }
        });
       /* if (!getKey().equals("")) {
            initCarliststate();
            ShopPresenter.getCarList(this, true);
        }*/
    }

    private boolean isAllSelected() {
        if (selectedStoreList.size() == carList.size()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), mToolbar);
        isViewCreated = true;
    }

    @Override
    protected void initData() {
        if (!getKey().equals("")) {
            ShopPresenter.getCarList(this, true);
        }
    }

    private void initCarliststate() {
        initUnselectState = true;
        isNotifyAdapter = false;
        adapter.isSelectAllAndNotify(false, isNotifyAdapter, initUnselectState);
        cbSelectAll.setChecked(false);
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (isViewCreated) {
            if (!getKey().equals("")) {
                ShopPresenter.getCarList(ShopCarFragment.this, true);
            }
        }
    }

    //适配首页的add hide show架构，如果显示的同时做更新操作需要外放接口供父容器调用
    public void refreshCarList() {
        ShopPresenter.getCarList(ShopCarFragment.this, true);
    }


    public interface CarParentListener {
        void changeCarEmpty();
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
            carnum = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray good = jsonArray.optJSONObject(i).optJSONArray("goods");
              /*  for (int ii = 0; ii < good.length(); ii++) {
                    carnum += Integer.valueOf(good.optJSONObject(ii).optString("goods_num"));
                }*/
                carnum += good.length();
            }
            EventBus.getDefault().postSticky(new CarNumEvent(carnum));
            carList.clear();
            storeList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.optJSONObject(i);
                obj.put("storecheck",false);
                obj.put("goodcheck",false);
                carList.add(obj);
                String storeName = obj.optString("store_name");
                String storeId = obj.optString("store_id");
                ShopStoreBean storeBean = new ShopStoreBean(storeName, storeId);
                storeList.add(storeBean);
            }
            if (carList.size() == 0) {
                carParentListener.changeCarEmpty();
            }
            if (!isModifyGoodNum) {
                selectedStoreList.clear();
                selectedGoodList.clear();
                gotoBuyList.clear();
                updateAmount();
                initCarliststate();
                adapter.notifyDataSetChanged();
                cart_count = datas.optString("cart_count");
                JSONArray guess_like_list = datas.optJSONArray("guess_like_list");
                guessList = JsonUtil.jsonArrayToList(guess_like_list.toString());
            } else {
                isModifyGoodNum = false;
                int selectedGoodposition = -1;
                for (CarGoodItemBean bean : selectedGoodList) {
                    if (bean.getCarId().equals(cart_id)) {
                        selectedGoodposition = selectedGoodList.indexOf(bean);
                        break;
                    }
                }
                if (selectedGoodposition != -1 && !selectedGoodList.get(selectedGoodposition).getQuantity().equals(goods_num)) {
                    selectedGoodList.get(selectedGoodposition).setQuantity(goods_num);
                    gotoBuyList.get(selectedGoodposition).put("goods_num", goods_num);
                    updateAmount();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        if (!getKey().equals("")) {
            ShopPresenter.getCarList(ShopCarFragment.this, false);
        }
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
        if (selectedGoodList.size() == 0) {
            PopUtil.showComfirmDialog(getContext(), null, "请至少选择一件商品", null, "好的", null, null, true);
        } else {
            ShopPresenter.deleteCarItems(this);
        }

    }

    private void goToBuy() {
        if (gotoBuyList.size() == 0) {
            PopUtil.showComfirmDialog(getContext(), null, "请选择至少一件商品", null, "好的", null, null, true);
        } else {
            Map<String, ComfirmShopGoodBean> map = new HashMap<String, ComfirmShopGoodBean>();
            for (JSONObject jsonObject : gotoBuyList) {
                String store_id = jsonObject.optString("store_id");
                String store_name = jsonObject.optString("store_name");
                if(!jsonObject.optBoolean("storage_state")){
                    PopUtil.toastInBottom("已为您取消购买已下架或库存不足的商品");
                }else if("1".equals(jsonObject.optString("pickup_self"))){
                    PopUtil.toastInBottom("暂不支持购买自提商品，已为您取消购买所选自提商品");
                }else if (jsonObject.optBoolean("storage_state")&&"0".equals(jsonObject.optString("pickup_self"))) {
                    if (!map.containsKey(store_id)) {
                        List<ComfirmShopGoodBean.GoodsBean> list = new ArrayList<ComfirmShopGoodBean.GoodsBean>();
                        ComfirmShopGoodBean.GoodsBean bean = JsonUtil.toBean(jsonObject.toString(), ComfirmShopGoodBean.GoodsBean.class);
                        list.add(bean);
                        ComfirmShopGoodBean comfirmShopGoodBean = new ComfirmShopGoodBean();
                        comfirmShopGoodBean.setStore_id(store_id);
                        comfirmShopGoodBean.setStore_name(store_name);
                        comfirmShopGoodBean.setGoods(list);
                        map.put(store_id, comfirmShopGoodBean);
                    } else {
                        ComfirmShopGoodBean bean = map.get(store_id);
                        List<ComfirmShopGoodBean.GoodsBean> list = bean.getGoods();
                        if (jsonObject.optBoolean("storage_state")) {
                            list.add(JsonUtil.toBean(jsonObject.toString(), ComfirmShopGoodBean.GoodsBean.class));
                        }
                        bean.setGoods(list);
                    }
                }
            }
            List<ComfirmShopGoodBean> list = new ArrayList<ComfirmShopGoodBean>();
            for (Map.Entry<String, ComfirmShopGoodBean> entry : map.entrySet()) {
                LogUtils.e("key= " + entry.getKey() + " and value= "
                        + entry.getValue());
                list.add(entry.getValue());
            }
       /*     String isPickup_self = "";
            out:
            for (int ii = 0; ii < list.size(); ii++) {
                List<ComfirmShopGoodBean.GoodsBean> goods = list.get(ii).getGoods();
                for (int i = 0; i < goods.size(); i++) {
                    if (ii == 0 && i == 0) {
                        isPickup_self = goods.get(i).getPickup_self();
                    } else if (!goods.get(i).getPickup_self().equals(isPickup_self)) {
                        isPickup_self = "1";
                        break out;
                    }
                }
            }*/
           /* if (!TextUtils.isEmpty(isPickup_self)) {
                if (TextUtils.equals(isPickup_self, "0")) {*/
                    Intent intent = new Intent(getActivity(), ShopComfirmOrdersActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ifCar", "1");
                    bundle.putString("isPickup_self", "0");
                    bundle.putSerializable("data", (Serializable) list);
                    intent.putExtras(bundle);
                    startActivity(intent);
             /*   } else {
                    PopUtil.toastInBottom("暂不支持自提商品购买");
                }
            } else {
                PopUtil.toastInBottom("请取消选择已下架或不支持购买的商品");
            }*/
        }
    }

    @Override
    public String getCarId() {
        return cart_id;
    }

    @Override
    public String getCarItemNum() {
        return goods_num;
    }

    @Override
    public void onModifyGoodNumSuccess(String s) {
        PopUtil.showAutoDissHookDialog(getContext(), "购物车数量修改成功", 100);
        isModifyGoodNum = true;
        ShopPresenter.getCarList(this, false);
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public String getSelectCartList() {
        StringBuilder sf = new StringBuilder();
        for (int i = 0; i < selectedGoodList.size(); i++) {
            if (i == 0) {
                sf.append(selectedGoodList.get(i).getCarId());
            } else {
                sf.append("|").append(selectedGoodList.get(i).getCarId());
            }
        }
        return sf.toString();
    }

    @Override
    public void onDeleteCarItemsSuccess(String s) {
        PopUtil.showAutoDissHookDialog(getContext(), "购物车删除成功", 0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!getKey().equals("")) {
                    ShopPresenter.getCarList(ShopCarFragment.this, false);
                }
            }
        }, 1600);
    }

    private void updateAmount() {
        double total = 0.00;
        for (CarGoodItemBean bean : selectedGoodList) {
            total += Double.valueOf(bean.getGoodPrice()) * Integer.valueOf(bean.getQuantity());
        }
        tvToatl.setText("合计：" + StringUtil.getPriceSpannable12String(getContext(), total + "", R.style.big_money, R.style.big_money));
    }
}
