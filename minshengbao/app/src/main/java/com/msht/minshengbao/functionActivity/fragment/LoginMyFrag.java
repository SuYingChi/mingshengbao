package com.msht.minshengbao.functionActivity.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.msht.minshengbao.Bean.RepairNumBean;
import com.msht.minshengbao.androidShop.viewInterface.IRepairOrderNumView;
import com.msht.minshengbao.base.BaseHomeFragment;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.adapter.MyFunctionAdapter;
import com.msht.minshengbao.androidShop.activity.MyShopOrderActivity;
import com.msht.minshengbao.androidShop.activity.ShopCollectionActivity;
import com.msht.minshengbao.androidShop.activity.ShopFootprintActivity;
import com.msht.minshengbao.androidShop.activity.TotalMessageListActivity;
import com.msht.minshengbao.androidShop.customerview.LoadingDialog;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.ShopNumBean;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.DataStringCallback;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.IOrderNumView;
import com.msht.minshengbao.functionActivity.gasService.GasServerOrderActivity;
import com.msht.minshengbao.functionActivity.invoiceModule.InvoiceHomeActivity;
import com.msht.minshengbao.functionActivity.myActivity.AddressManageActivity;
import com.msht.minshengbao.functionActivity.myActivity.ConsultRecommendActivity;
import com.msht.minshengbao.functionActivity.myActivity.CustomerNoManageActivity;
import com.msht.minshengbao.functionActivity.myActivity.LoginActivity;
import com.msht.minshengbao.functionActivity.myActivity.MoreSettingActivity;
import com.msht.minshengbao.functionActivity.myActivity.MySettingActivity;
import com.msht.minshengbao.functionActivity.myActivity.MyWalletActivity;
import com.msht.minshengbao.functionActivity.myActivity.ShareMenuActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.CallPhoneUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.ButtonUI.MenuItemM;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.ViewUI.widget.MyScrollview;
import com.msht.minshengbao.functionActivity.repairService.RepairOrderListActivity;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author hong
 * @date 2016/7/2  
 */
public class LoginMyFrag extends BaseHomeFragment implements View.OnClickListener, MyScrollview.ScrollViewListener, IOrderNumView, IRepairOrderNumView {
    private MyScrollview myScrollview;
    private LinearLayout layoutNavigation;
    private RelativeLayout layoutMySetting;
    private TextView tvNavigation;
    private SimpleDraweeView mPortrait;
    private MenuItemM btnMessage;
    private String nickname;
    private int bgHeight;
    private Activity mActivity;
    /**
     * 最大显示消息数
     **/
    private static final int MAX_MASSAGE = 99;
    private MyNoScrollGridView mGridView;
    private MyFunctionAdapter mAdapter;
    private ArrayList<HashMap<String, Integer>> mList = new ArrayList<HashMap<String, Integer>>();
    private final String mPageName = "首页_个人中心";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private LoadingDialog centerLoadingDialog;

    private TextView tvWaitEvaluate;
    private TextView tvWaitGet;
    private TextView tvWaitPay;
    private TextView tvAllOrder;

    private TextView tvRefundOrder;
    private LinearLayout llShopOrder;
    private LinearLayout llrefund;
    private LinearLayout llwaitEveluate;
    private LinearLayout llwaitget;
    private LinearLayout llwaitPay;
    private LinearLayout llCollect;
    private LinearLayout llFootprint;
    private TextView tvCollect;
    private TextView tvFootprint;
    private TextView tvRepair;
    private TextView tvRepairUnfinish;
    private TextView tvRepairfinished;
    private TextView tvWaitEveluateRepair;
    private TextView tvRefundRepair;

    public LoginMyFrag() {
    }

    @Override
    public void showLoading() {

        if (this.getActivity() != null && !this.getActivity().isFinishing() && centerLoadingDialog == null) {
            centerLoadingDialog = new LoadingDialog(getContext());
            centerLoadingDialog.show();
        } else if (!isDetached() && !centerLoadingDialog.isShowing()) {
            centerLoadingDialog.show();
        }

    }

    @Override
    public void dismissLoading() {
        if (centerLoadingDialog != null && centerLoadingDialog.isShowing() && getActivity() != null && !getActivity().isFinishing()) {
            centerLoadingDialog.dismiss();
        }
    }

    @Override
    public void onError(String s) {
        if (!AppUtil.isNetworkAvailable()) {
            PopUtil.showComfirmDialog(getContext(), "", getResources().getString(R.string.network_error), "", "", null, null, true);

        } else if (TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey()) || "未登录".equals(s)) {
            PopUtil.toastInBottom("请登录商城");
            LogUtils.e(Log.getStackTraceString(new Throwable()));
        } else {
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
        return ShopSharePreferenceUtil.getInstance().getPassword();
    }


    public void getOrdersNum() {
        ShopPresenter.getOrderNum(this, new DataStringCallback(this) {
            @Override
            public void onResponse(String s, int i) {
                super.onResponse(s, i);
                if (isResponseSuccess) {
                    ShopNumBean bean = JsonUtil.toBean(s, ShopNumBean.class);
                    if (bean != null) {
                        onGetNumSuccess(bean);
                    } else {
                        LoginMyFrag.this.onError(s);
                    }
                }
            }
        });
        ShopPresenter.getRepairOrderNum(this,SharedPreferencesUtil.getUserId(getContext(),SharedPreferencesUtil.UserId,""),SharedPreferencesUtil.getPassword(getContext(),SharedPreferencesUtil.Password,""), new DataStringCallback(this) {
            @Override
            public void onResponse(String s, int i) {
                    try {
                        JSONObject object = new JSONObject(s);
                        if(TextUtils.equals(object.optString("result"),"error")){
                          PopUtil.toastInCenter(object.optString("error"));
                        }else if(TextUtils.equals(object.optString("result"),"success")){
                            onGetRepairNumSuccess(s);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        });
    }

    private void onGetRepairNumSuccess(String s) {
        RepairNumBean bean = JsonUtil.toBean(s, RepairNumBean.class);
        if("0".equals(bean.getData().getUndoneCount())){
            tvRepairUnfinish.setVisibility(View.GONE);
        }else {
            tvRepairUnfinish.setVisibility(View.VISIBLE);
        }
        tvRepairUnfinish.setText(bean.getData().getUndoneCount());
        if("0".equals(bean.getData().getUnevalCount())){
            tvWaitEveluateRepair.setVisibility(View.GONE);
        }else {
            tvWaitEveluateRepair.setVisibility(View.VISIBLE);
        }
        tvWaitEveluateRepair.setText(bean.getData().getUnevalCount());
    }

    private void onGetNumSuccess(ShopNumBean bean) {
        int footprintNum = bean.getDatas().getMember_info().getBrowses_goods();
        tvFootprint.setVisibility(View.VISIBLE);
        tvFootprint.setText(String.format("%d", footprintNum));
        llFootprint.setClickable(true);
        int collectNum = bean.getDatas().getMember_info().getFavorites_goods();
        tvCollect.setVisibility(View.VISIBLE);
        tvCollect.setText(String.format("%d", collectNum));
        llCollect.setClickable(true);

        String refundOrderNum = bean.getDatas().getMember_info().getReturnX();
        if ("0".equals(refundOrderNum)) {
            tvRefundOrder.setVisibility(View.GONE);
        } else {
            tvRefundOrder.setVisibility(View.VISIBLE);
            tvRefundOrder.setText(refundOrderNum);
        }
        llrefund.setClickable(true);
        String waitEveluateOrdersNum = bean.getDatas().getMember_info().getOrder_noeval_count();
        if ("0".equals(waitEveluateOrdersNum)) {
            tvWaitEvaluate.setVisibility(View.GONE);
        } else {
            tvWaitEvaluate.setVisibility(View.VISIBLE);
            tvWaitEvaluate.setText(waitEveluateOrdersNum);
        }
        llwaitEveluate.setClickable(true);
        String waitPayOrdersNum = bean.getDatas().getMember_info().getOrder_nopay_count();
        if (waitPayOrdersNum.equals("0")) {
            tvWaitPay.setVisibility(View.GONE);
        } else {
            tvWaitPay.setVisibility(View.VISIBLE);
            tvWaitPay.setText(waitPayOrdersNum);
        }
        llwaitPay.setClickable(true);
        String waitGetOrdersNum = bean.getDatas().getMember_info().getOrder_noreceipt_count();
        if (waitGetOrdersNum.equals("0")) {
            tvWaitGet.setVisibility(View.GONE);
        } else {
            tvWaitGet.setVisibility(View.VISIBLE);
            tvWaitGet.setText(waitGetOrdersNum);
        }
        llwaitget.setClickable(true);
        llShopOrder.setClickable(true);
    }

    @Override
    public View initFindView() {
        mActivity = getActivity();
        if (mRootView == null) {
            mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_loginafter_my, null, false);
        }
        String avatarUrl = SharedPreferencesUtil.getAvatarUrl(mContext, SharedPreferencesUtil.AvatarUrl, "");
        nickname = SharedPreferencesUtil.getNickName(mContext, SharedPreferencesUtil.NickName, "");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mRootView.findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        initView(mRootView);
        Uri uri = Uri.parse(avatarUrl);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                //. 其他设置（如果有的话）
                .build();
        mPortrait.setController(controller);
        initListeners();
        mAdapter = new MyFunctionAdapter(mContext, mList);
        mGridView.setAdapter(mAdapter);
        initData();
        initEvent(mRootView);
        goActivity();
        getOrdersNum();
        return mRootView;
    }

    private void initEvent(View view) {
        view.findViewById(R.id.repair_order).setOnClickListener(this);
        view.findViewById(R.id.my_repair_wait_pay).setOnClickListener(this);
        view.findViewById(R.id.repair_finished).setOnClickListener(this);
        view.findViewById(R.id.my_wait_repair_eveluate).setOnClickListener(this);
        view.findViewById(R.id.my_repair_refund).setOnClickListener(this);

    }

    private void initView(View view) {
        myScrollview = (MyScrollview) view.findViewById(R.id.id_scrollview);
        mGridView = (MyNoScrollGridView) view.findViewById(R.id.id_function_view);
        layoutNavigation = (LinearLayout) view.findViewById(R.id.id_li_navigation);
        layoutMySetting = (RelativeLayout) view.findViewById(R.id.id_re_gosetting);
        layoutMySetting.setOnClickListener(this);
        btnMessage = (MenuItemM) view.findViewById(R.id.id_mim_message);
        view.findViewById(R.id.id_re_hotline).setOnClickListener(this);
        view.findViewById(R.id.id_re_consult).setOnClickListener(this);
        view.findViewById(R.id.id_re_setting).setOnClickListener(this);
        view.findViewById(R.id.id_right_massage).setOnClickListener(this);
        mPortrait = (SimpleDraweeView) view.findViewById(R.id.id_portrait);
        tvNavigation = (TextView) view.findViewById(R.id.id_tv_naviga);
        TextView tvNickname = (TextView) view.findViewById(R.id.id_nickname);
        if (!TextUtils.isEmpty(nickname)) {
            tvNickname.setText(nickname);
        } else {
            String userName = SharedPreferencesUtil.getNickName(mActivity, SharedPreferencesUtil.UserName, "");
            if (RegularExpressionUtil.isPhone(userName)) {
                userName = userName.substring(0, 3) + "****" + userName.substring(7, userName.length());
            }
            tvNickname.setText(userName);

        }
        onUnReadMessage();
        btnMessage.setOnClickListener(new MenuItemM.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMessageCenter();
            }
        });
        llCollect = (LinearLayout) view.findViewById(R.id.ll_collect);
        tvCollect = (TextView) view.findViewById(R.id.collected_num);
        tvFootprint = (TextView) view.findViewById(R.id.footprint_num);
        llCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShopCollectionActivity.class));
            }
        });
        llFootprint = (LinearLayout) view.findViewById(R.id.ll_footprint);
        llFootprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShopFootprintActivity.class));
            }
        });
        llShopOrder = (LinearLayout) view.findViewById(R.id.shop_order);
        llShopOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyShopOrderActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("indexChild", 0);
                getActivity().startActivity(intent);
            }
        });
        llwaitPay = (LinearLayout) view.findViewById(R.id.my_wait_pay);
        llwaitPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyShopOrderActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("indexChild", 1);
                getActivity().startActivity(intent);
            }
        });
        llwaitget = (LinearLayout) view.findViewById(R.id.my_wait_get);
        llwaitget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyShopOrderActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("indexChild", 3);
                getActivity().startActivity(intent);
            }
        });
        llwaitEveluate = (LinearLayout) view.findViewById(R.id.my_wait_eveluate);
        llwaitEveluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyShopOrderActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("indexChild", 4);
                getActivity().startActivity(intent);
            }
        });
        llrefund = (LinearLayout) view.findViewById(R.id.my_refund);
        llrefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyShopOrderActivity.class);
                intent.putExtra("index", 1);
                intent.putExtra("indexChild", 0);
                getActivity().startActivity(intent);
            }
        });
        tvWaitEvaluate = (TextView) view.findViewById(R.id.my_wait_eveluate_order_num);
        tvWaitGet = (TextView) view.findViewById(R.id.wait_get_order_num);
        tvWaitPay = (TextView) view.findViewById(R.id.wait_pay_order_num);
        tvAllOrder = (TextView) view.findViewById(R.id.shop_order_num);
        tvRefundOrder = (TextView) view.findViewById(R.id.my_refund_order_num);
        tvRepair = (TextView)view.findViewById(R.id.repair_order_num);
        tvRepairUnfinish = (TextView)view.findViewById(R.id.repair_unfinished_order_num);
        tvRepairfinished = (TextView)view.findViewById(R.id.repair_finished_order_num);
        tvWaitEveluateRepair = (TextView)view.findViewById(R.id.my_wait_eveluate_repair_order_num);
        tvRefundRepair =(TextView)view.findViewById(R.id.my_refund_repair_order_num);

    }

    private void onUnReadMessage() {
        if (VariableUtil.messageNum >= MAX_MASSAGE) {
            btnMessage.setUnReadCount(MAX_MASSAGE);
        } else {
            btnMessage.setUnReadCount(VariableUtil.messageNum);
        }
    }

    private void initData() {
        for (int i = 0; i < 6; i++) {
            if (VariableUtil.BoolCode) {
                if (i != 1 && i != 2) {
                    HashMap<String, Integer> map = new HashMap<String, Integer>();
                    map.put("code", i);
                    mList.add(map);
                }
            } else {
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("code", i);
                mList.add(map);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void goActivity() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int code = mList.get(position).get("code");
                switch (code) {
                    case 0:
                        if (isLoginState(mContext)) {
                            goMyWallet();
                        } else {
                            AppActivityUtil.onStartLoginActivity(mContext, "");
                        }
                        break;
                    case 1:
                        if (isLoginState(mContext)) {
                            goGasServer();
                        } else {
                            AppActivityUtil.onStartLoginActivity(mContext, "");
                        }
                        break;
                    case 2:
                        if (isLoginState(mContext)) {
                            goCustomerNo();
                        } else {
                            AppActivityUtil.onStartLoginActivity(mContext, "");
                        }
                        break;
                    case 3:
                        if (isLoginState(mContext)) {
                            goInvoice();
                        } else {
                            AppActivityUtil.onStartLoginActivity(mContext, "");
                        }
                        break;
                    case 4:
                        if (isLoginState(mContext)) {
                            goManage();
                        } else {
                            AppActivityUtil.onStartLoginActivity(mContext, "");
                        }
                        break;
                    case 5:
                        goShare();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initListeners() {
        ViewTreeObserver vto = layoutMySetting.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutMySetting.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                bgHeight = layoutMySetting.getHeight();
                initSetListener();
            }
        });
    }

    private void initSetListener() {
        myScrollview.setScrollViewListener(this);
    }

    @Override
    public void onScrollChanged(MyScrollview scrollView, int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            //设置标题的背景颜色
            layoutNavigation.setBackgroundColor(Color.argb(0, 0, 255, 0));
            tvNavigation.setTextColor(Color.argb(0, 0, 255, 0));
            //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
        } else if (t > 0 && t <= bgHeight) {
            float scale = (float) t / bgHeight;
            float alpha = (255 * scale);
            tvNavigation.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            layoutNavigation.setBackgroundColor(Color.argb((int) alpha, 249, 99, 49));
        } else {    //滑动到banner下面设置普通颜色
            layoutNavigation.setBackgroundColor(Color.argb(255, 249, 99, 49));
            tvNavigation.setTextColor(Color.argb(255, 255, 255, 255));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_re_gosetting:
                if (isLoginState(mContext)) {
                    goSetting();
                } else {
                    AppActivityUtil.onStartLoginActivity(mContext, "");
                }
                break;
            case R.id.id_re_consult:
                if (isLoginState(mContext)) {
                    goConsult();
                } else {
                    AppActivityUtil.onStartLoginActivity(mContext, "");
                }
                break;
            case R.id.id_re_hotline:
                hotLine();
                break;
            case R.id.id_re_setting:
                goMoreSetting();
                break;
            case R.id.id_right_massage:
                goMessageCenter();
                break;
            case R.id.repair_order:
                onRepairOrder(0);
                break;
            case R.id.my_repair_wait_pay:
                onRepairOrder(1);
                break;
            case R.id.repair_finished:
                onRepairOrder(2);
                break;
            case R.id.my_wait_repair_eveluate:
                onRepairOrder(3);
                break;
            case R.id.my_repair_refund:
                onRepairOrder(4);
                break;
            default:
                break;
        }
    }

    private void onRepairOrder(int i) {
        Intent intent = new Intent(mContext, RepairOrderListActivity.class);
        intent.putExtra("status", i);
        startActivity(intent);
    }

    private void goMessageCenter() {
        // Intent intent = new Intent(mContext, MessageCenterActivity.class);
        Intent intent = new Intent(mContext, TotalMessageListActivity.class);
        startActivity(intent);
        btnMessage.setUnReadCount(0);
    }

    private void hotLine() {
        final String phone = "963666";
        new PromptDialog.Builder(mContext)
                .setTitle("服务热线")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(phone)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("呼叫", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        requestLimit(phone);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void goConsult() {
        Intent intent = new Intent(mContext, ConsultRecommendActivity.class);
        startActivity(intent);
    }

    private void goShare() {
        Intent intent = new Intent(mContext, ShareMenuActivity.class);
        startActivity(intent);
    }

    private void goManage() {
        Intent intent = new Intent(mContext, AddressManageActivity.class);
        startActivity(intent);
    }

    private void goMyWallet() {
        Intent intent = new Intent(mContext, MyWalletActivity.class);
        startActivityForResult(intent, 0x004);
    }

    private void goInvoice() {
        Intent intent = new Intent(mContext, InvoiceHomeActivity.class);
        startActivity(intent);
    }

    private void goGasServer() {
        Intent intent = new Intent(mContext, GasServerOrderActivity.class);
        startActivity(intent);
    }

    private void goSetting() {
        Intent intent = new Intent(mContext, MySettingActivity.class);
        startActivityForResult(intent, 1);
    }

    private void goCustomerNo() {
        Intent intent = new Intent(mContext, CustomerNoManageActivity.class);
        startActivity(intent);
    }

    private void goMoreSetting() {
        Intent intent = new Intent(mContext, MoreSettingActivity.class);
        startActivityForResult(intent, 0x005);
    }

    private void requestLimit(final String phone) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                CallPhoneUtil.callPhone(mContext, phone);
            } else {
                MPermissionUtils.requestPermissionsResult(this, MY_PERMISSIONS_REQUEST_CALL_PHONE, new String[]{Manifest.permission.CALL_PHONE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int code) {
                        if (code == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
                            CallPhoneUtil.callPhone(mContext, phone);
                        }
                    }

                    @Override
                    public void onPermissionDenied(int code) {
                        ToastUtil.ToastText(mContext, "没有权限您将无法进行相关操作！");
                    }
                });
            }
        } else {
            CallPhoneUtil.callPhone(mContext, phone);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
}
