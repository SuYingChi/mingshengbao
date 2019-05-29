package com.msht.minshengbao.functionActivity.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.adapter.CouponAdapter;
import com.msht.minshengbao.androidShop.activity.GetRedPacketActivity;
import com.msht.minshengbao.androidShop.activity.ShopStoreMainActivity;
import com.msht.minshengbao.androidShop.activity.ShopVouchActivity;
import com.msht.minshengbao.base.BaseFragment;
import com.msht.minshengbao.androidShop.ShopConstants;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.PullRefresh.XListView;
import com.msht.minshengbao.functionActivity.gasService.GasPayFeeHomeActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author hong
 */
public class CouponFragment extends BaseFragment {
    private String userId;
    private String password;
    private String status = "1";
    private ImageView layoutNoData;
    private Button btnShare;
    private XListView xListView;
    private CouponAdapter mAdapter;
    //  private int pageNo=1;
    private int pageIndex = 0;
    private int refreshType;
    private JSONArray jsonArray;
    private final String mPageName = "优惠券";
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> couponList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler = new RequestHandler(this);
    private int position = 0;
    private int pageTotal;
    private int voucherPageIndex = 1;
    private int redPacketpageTotal;
    private int redPacketpageIndex = 1;


    public CouponFragment() {
    }

    public static CouponFragment getinstance(int position) {
        CouponFragment couponFragment = new CouponFragment();
        couponFragment.position = position;
        if (position == 0) {
            couponFragment.status = "1";
        } else if (position == 2) {
            couponFragment.status = "";
        } else if (position == 1) {
            couponFragment.status = "";
        }
        return couponFragment;
    }

    private static class RequestHandler extends Handler {
        private WeakReference<CouponFragment> mWeakReference;


        public RequestHandler(CouponFragment couponFragment) {
            mWeakReference = new WeakReference<CouponFragment>(couponFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final CouponFragment reference = mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null || reference.isDetached()) {
                return;
            }
            if (reference.customDialog != null && reference.customDialog.isShowing()) {
                reference.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        if (reference.position == 0) {
                            JSONObject object = new JSONObject(msg.obj.toString());
                            String result = object.optString("result");
                            String error = object.optString("error");
                            reference.jsonArray = object.optJSONArray("data");
                            if (result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                                if (reference.refreshType == 0) {
                                    reference.xListView.stopRefresh(true);
                                } else if (reference.refreshType == 1) {
                                    reference.xListView.stopLoadMore();
                                }
                                if (reference.jsonArray.length() > 0) {
                                    if (reference.pageIndex == 1 && "1".equals(reference.status)) {
                                        reference.couponList.clear();
                                    }
                                }
                                reference.onGetCouponData();
                                switch (reference.status) {
                                    case "1":
                                        reference.loadData(reference.pageIndex, "2");
                                        break;
                                    case "2":
                                        reference.loadData(reference.pageIndex, "3");
                                        break;
                                    case "3":
                                        reference.status = "1";
                                        if (reference.couponList.size() == 0) {
                                            //  if (reference.status.equals(VariableUtil.VALUE_ONE)){
                                            reference.layoutNoData.setImageDrawable(MyApplication.getMsbApplicationContext().getResources().getDrawable(R.drawable.no_get_voucher));
                                            reference.layoutNoData.setVisibility(View.VISIBLE);
                                       /* }else {
                                            reference.layoutNoData.setVisibility(View.GONE);
                                        }*/
                                        } else {
                                            reference.layoutNoData.setVisibility(View.GONE);
                                          //  reference.arrangeList();
                                            reference.mAdapter.notifyDataSetChanged();
                                        }
                                        break;
                                    default:
                                        break;
                                }

                            } else {
                                reference.onFailure(error);
                            }
                        } else if (reference.position == 2) {
                            JSONObject object = new JSONObject(msg.obj.toString());
                            int resultCode = object.optInt("code");
                            if (resultCode == 200) {
                                reference.pageTotal = object.optInt("page_total");
                                reference.jsonArray = object.optJSONObject("datas").optJSONObject("group_voucher_list").optJSONArray(unused);
                                JSONArray usedArray = object.optJSONObject("datas").optJSONObject("group_voucher_list").optJSONArray(used);
                                JSONArray expireArray = object.optJSONObject("datas").optJSONObject("group_voucher_list").optJSONArray(expire);
                                if(reference.jsonArray==null){
                                    reference.jsonArray = new JSONArray();
                                }
                                if (usedArray != null) {
                                    for (int i = 0; i < usedArray.length(); i++) {
                                        reference.jsonArray.put(usedArray.optJSONObject(i));
                                    }
                                }
                                if (expireArray != null) {
                                    for (int i = 0; i < expireArray.length(); i++) {
                                        reference.jsonArray.put(expireArray.optJSONObject(i));
                                    }
                                }
                                if (reference.refreshType == 0) {
                                    reference.xListView.stopRefresh(true);
                                } else if (reference.refreshType == 1) {
                                    reference.xListView.stopLoadMore();
                                }
                                if (reference.jsonArray.length() > 0) {
                                    if (reference.voucherPageIndex == 1) {
                                        reference.couponList.clear();
                                    }
                                }
                                reference.onGetCouponData();
                                if (reference.couponList.size() == 0) {
                                    reference.layoutNoData.setImageDrawable(MyApplication.getMsbApplicationContext().getResources().getDrawable(R.drawable.no_voucher));
                                    reference.layoutNoData.setVisibility(View.VISIBLE);
                                } else {
                                    reference.layoutNoData.setVisibility(View.GONE);
                                    reference.arrangeList();
                                    reference.mAdapter.notifyDataSetChanged();
                                }
                            } else {
                                reference.onFailure(object.optJSONObject("datas").optString("error"));
                            }
                        } else if (reference.position == 1) {
                            JSONObject object = new JSONObject(msg.obj.toString());
                            int resultCode = object.optInt("code");
                            if (resultCode == 200) {
                                reference.redPacketpageTotal = object.optInt("page_total");
                                reference.jsonArray = object.optJSONObject("datas").optJSONObject("redpacket_list").optJSONArray(unused);
                                JSONArray usedArray = object.optJSONObject("datas").optJSONObject("redpacket_list").optJSONArray(used);
                                JSONArray expireArray = object.optJSONObject("datas").optJSONObject("redpacket_list").optJSONArray(expire);
                                if(reference.jsonArray==null){
                                    reference.jsonArray = new JSONArray();
                                }
                                if (usedArray != null) {
                                    for (int i = 0; i < usedArray.length(); i++) {
                                        reference.jsonArray.put(usedArray.optJSONObject(i));
                                    }
                                }
                                if (expireArray != null) {
                                    for (int i = 0; i < expireArray.length(); i++) {
                                        reference.jsonArray.put(expireArray.optJSONObject(i));
                                    }
                                }
                                if (reference.refreshType == 0) {
                                    reference.xListView.stopRefresh(true);
                                } else if (reference.refreshType == 1) {
                                    reference.xListView.stopLoadMore();
                                }
                                if (reference.jsonArray.length() > 0) {
                                    if (reference.redPacketpageIndex == 1) {
                                        reference.couponList.clear();
                                    }
                                }
                                reference.onGetCouponData();
                                if (reference.couponList.size() == 0) {
                                    reference.layoutNoData.setImageDrawable(MyApplication.getMsbApplicationContext().getResources().getDrawable(R.drawable.no_voucher));
                                    reference.layoutNoData.setVisibility(View.VISIBLE);
                                } else {
                                    reference.layoutNoData.setVisibility(View.GONE);
                                    reference.arrangeList();
                                    reference.mAdapter.notifyDataSetChanged();
                                }

                            } else {
                                reference.onFailure(object.optJSONObject("datas").optString("error"));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.mContext, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    }

    private void arrangeList() {
        ArrayList<HashMap<String, String>> templist = new ArrayList<HashMap<String, String>>();
        for(HashMap<String,String> map:couponList){
            if(position==0) {
                if ("1".equals(map.get("type"))) {
                    templist.add(map);
                }
            }else if(position==1){
                if ("1".equals(map.get("rpacket_state"))) {
                    templist.add(map);
                }
            }else if(position==2){
                if ("1".equals(map.get("voucher_state"))) {
                    templist.add(map);
                }
            }
        }
        for(HashMap<String,String> map:couponList){
            if(position==0) {
                if ("2".equals(map.get("type"))) {
                    templist.add(map);
                }
            } else if(position==1){
                if ("2".equals(map.get("rpacket_state"))) {
                    templist.add(map);
                }
            }else if(position==2){
                if ("2".equals(map.get("voucher_state"))) {
                    templist.add(map);
                }
            }
        }
        for(HashMap<String,String> map:couponList){
            if(position==0) {
                if ("3".equals(map.get("type"))) {
                    templist.add(map);
                }
            }else if(position==1){
                if ("3".equals(map.get("rpacket_state"))) {
                    templist.add(map);
                }
            }else if(position==2){
                if ("3".equals(map.get("voucher_state"))) {
                    templist.add(map);
                }
            }
        }
        couponList.clear();
        couponList.addAll(templist);
    }

    private void onGetCouponData() {
        try {
            if (position == 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.optString("id");
                    String name = jsonObject.optString("name");
                    String scope = jsonObject.getString("scope");
                    String amount = jsonObject.getString("amount");
                    String useLimit = jsonObject.getString("use_limit");
                    String startDate = jsonObject.getString("start_date");
                    String endDate = jsonObject.getString("end_date");
                    String desc = jsonObject.getString("desc");
                    String remainderDays = "";
                    if (jsonObject.has("remainder_days")) {
                        remainderDays = jsonObject.optString("remainder_days");
                    }
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", id);
                    map.put("name", name);
                    map.put("scope", scope);
                    map.put("amount", amount);
                    map.put("use_limit", useLimit);
                    map.put("start_date", startDate);
                    map.put("end_date", endDate);
                    map.put("remainder_days", remainderDays);
                    map.put("type", status);
                    map.put("desc",desc);
                    map.put("show", "0");
                    map.put("direct_url",jsonObject.getString("direct_url"));
                    couponList.add(map);
                }
            } else if (position == 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String rpacket_limit = jsonObject.optString("rpacket_limit");
                    String rpacket_price = jsonObject.optString("rpacket_price");
                    String rpacket_start_date_text = jsonObject.getString("rpacket_start_date_text");
                    String rpacket_end_date_text = jsonObject.getString("rpacket_end_date_text");
                    String rpacket_title = jsonObject.getString("rpacket_title");
                    String rpacket_state = jsonObject.getString("rpacket_state");
                    String rpacket_desc = jsonObject.getString("rpacket_desc");
                    String left_days = jsonObject.getString("left_days");
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("rpacket_limit", rpacket_limit);
                    map.put("rpacket_price", rpacket_price);
                    map.put("rpacket_start_date_text", rpacket_start_date_text);
                    map.put("rpacket_end_date_text", rpacket_end_date_text);
                    map.put("rpacket_title", rpacket_title);
                    map.put("rpacket_state", rpacket_state);
                    map.put("rpacket_desc",rpacket_desc);
                    map.put("left_days", left_days);
                    map.put("show", "0");
                    couponList.add(map);
                }
            } else if (position == 2) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    map.put("store_name", jsonObject.optString("store_name"));
                    map.put("voucher_end_date_text", jsonObject.optString("voucher_end_date_text"));
                    map.put("voucher_start_date_text", jsonObject.optString("voucher_start_date_text"));
                    map.put("voucher_limit", jsonObject.optString("voucher_limit"));
                    map.put("voucher_price", jsonObject.optString("voucher_price"));
                    map.put("voucher_state", jsonObject.optString("voucher_state"));
                    map.put("voucher_desc", jsonObject.optString("voucher_desc"));
                    map.put("voucher_state_text", jsonObject.optString("voucher_state_text"));
                    map.put("voucher_id", jsonObject.optString("voucher_id"));
                    map.put("store_id", jsonObject.optString("store_id"));
                    String left_days = jsonObject.getString("left_days");
                    map.put("left_days", left_days);
                    map.put("show", "0");
                    couponList.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onFailure(String error) {
        new PromptDialog.Builder(getActivity())
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    initData();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public View initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_coupon, null, false);
        customDialog = new CustomDialog(getActivity(), "正在加载");
        userId = SharedPreferencesUtil.getUserId(getActivity(), SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(getActivity(), SharedPreferencesUtil.Password, "");
        initMyView(mRootView);
        return mRootView;
    }

    private void initMyView(View mRootView) {
        layoutNoData = (ImageView) mRootView.findViewById(R.id.nodata);
        xListView = (XListView) mRootView.findViewById(R.id.id_dicount_mlistview);
        TextView get_redpacket = (TextView) mRootView.findViewById(R.id.get_redpacket);
        if (position == 1) {
            get_redpacket.setText("领取购物红包");
            get_redpacket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), GetRedPacketActivity.class));
                }
            });
            get_redpacket.setVisibility(View.VISIBLE);
        }else if(position == 2){
            get_redpacket.setText("领更多商家好券");
            get_redpacket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), ShopVouchActivity.class));
                }
            });
            get_redpacket.setVisibility(View.VISIBLE);
        }
        else {
            get_redpacket.setVisibility(View.GONE);
        }
        xListView.setPullLoadEnable(true);
        mAdapter = new CouponAdapter(getContext(), couponList, position);
        mAdapter.setOnClickVoucherListener(new CouponAdapter.OnClickVoucherListener() {
            @Override
            public void onClickVoucher(String storeId) {
                Intent intent = new Intent(getActivity(), ShopStoreMainActivity.class);
                intent.putExtra("id", storeId);
                startActivity(intent);
            }

            @Override
            public void onGoShopHome() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("index", 1);
                startActivity(intent);
            }

            @Override
            public void onClikshowDesc(int position) {
                String show = couponList.get(position).get("show");
                if("1".equals(show)){
                    couponList.get(position).put("show","0");
                }else if("0".equals(show)){
                    couponList.get(position).put("show","1");
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUseServiceCoupon(String direct_url) {
                Set<String> name = Uri.parse(direct_url).getQueryParameterNames();
                if(name.contains("code")) {
                    String code = Uri.parse(direct_url).getQueryParameter("code");
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        if ("gas_pay".equals(code)) {
                            startActivity(new Intent(getActivity(), GasPayFeeHomeActivity.class));
                        } else if ("homepage".equals(code)) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("index", 0);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
        xListView.setAdapter(mAdapter);
        xListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                refreshType = 0;
                if (position == 0) {
                    loadData(1, "1");
                } else if (position == 2) {
                    loadShopVoucherData(1);
                } else if (position == 1) {
                    loadShopRedpacketData(1);
                }
            }

            @Override
            public void onLoadMore() {
                refreshType = 1;
                if (position == 0) {
                    loadData(pageIndex + 1, "1");
                } else if (position == 2) {
                    if (voucherPageIndex < pageTotal) {
                        loadShopVoucherData(voucherPageIndex + 1);
                    } else {
                        xListView.stopLoadMore("无更多数据了哦");
                    }
                } else if (position == 1) {
                    if (redPacketpageIndex < redPacketpageTotal) {
                        loadShopRedpacketData(redPacketpageIndex + 1);
                    } else {
                        xListView.stopLoadMore("无更多数据了哦");
                    }
                }
            }
        });

    }

    @Override
    public void initData() {
        if (position == 0) {
            customDialog.show();
            loadData(1, "1");
        } else if (position == 2) {
            customDialog.show();
            loadShopVoucherData(1);
        } else if (position == 1) {
            customDialog.show();
            loadShopRedpacketData(1);
        }
    }

    private void loadShopVoucherData(int pageIndex) {
        this.voucherPageIndex = pageIndex;
        String validateURL = ShopConstants.VOUCHER_LIST;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("key", ShopSharePreferenceUtil.getInstance().getKey());
        textParams.put("curpage", pageIndex + "");
        textParams.put("page", "10");
        OkHttpRequestUtil.getInstance(MyApplication.getMsbApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART, textParams, requestHandler);
    }

    private static final String unused = "unused";
    private static final String used = "used";
    private static final String expire = "expire";

    private void loadShopRedpacketData(int redPacketpageIndex) {
        this.redPacketpageIndex = redPacketpageIndex;
        String validateURL = ShopConstants.GET_RED_PACKET_LIST;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("key", ShopSharePreferenceUtil.getInstance().getKey());
        textParams.put("curpage", redPacketpageIndex + "");
        textParams.put("page", "10");
        OkHttpRequestUtil.getInstance(MyApplication.getMsbApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART, textParams, requestHandler);
    }

    private void loadData(int pageIndex, String status) {
        this.pageIndex = pageIndex;
        this.status = status;
        //   pageNo=i;
        String validateURL = UrlUtil.Counpon_Url;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId", userId);
        textParams.put("password", password);
        textParams.put("status", status);
        textParams.put("page", pageIndex + "");
        OkHttpRequestUtil.getInstance(mContext.getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART, textParams, requestHandler);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);

    }

    ;

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onDestroy() {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
