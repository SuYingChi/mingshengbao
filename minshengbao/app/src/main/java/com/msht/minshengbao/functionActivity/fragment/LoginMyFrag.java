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
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
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
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.IOrderNumView;
import com.msht.minshengbao.custom.widget.CustomToast;
import com.msht.minshengbao.functionActivity.gasService.GasServerOrderActivity;
import com.msht.minshengbao.functionActivity.invoiceModule.InvoiceHomeActivity;
import com.msht.minshengbao.functionActivity.myActivity.AddressManageActivity;
import com.msht.minshengbao.functionActivity.myActivity.ConsultRecommendActivity;
import com.msht.minshengbao.functionActivity.myActivity.CustomerNoManageActivity;
import com.msht.minshengbao.functionActivity.myActivity.MoreSettingActivity;
import com.msht.minshengbao.functionActivity.myActivity.MySettingActivity;
import com.msht.minshengbao.functionActivity.myActivity.MyWalletActivity;
import com.msht.minshengbao.functionActivity.myActivity.NewAddressManagerActivity;
import com.msht.minshengbao.functionActivity.myActivity.ShareMenuActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.CallPhoneUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.custom.ButtonUI.MenuItemM;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.widget.MyNoScrollGridView;
import com.msht.minshengbao.custom.widget.MyScrollview;
import com.msht.minshengbao.functionActivity.repairService.RepairOrderListActivity;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    private ArrayList<HashMap<String, Integer>> mList = new ArrayList<HashMap<String, Integer>>();
    private final String mPageName = "首页_个人中心";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private LoadingDialog centerLoadingDialog;

    private TextView tvWaitEvaluate;
    private TextView tvWaitGet;
    private TextView tvWaitPay;
    private TextView tvBalance;

    private TextView tvRefundOrder;
    private View llShopOrder;
    private LinearLayout llrefund;
    private LinearLayout llwaitEveluate;
    private LinearLayout llwaitget;
    private LinearLayout llwaitPay;
    private View llCollect;
    private View llFootprint;
    private TextView tvCollect;
    private TextView tvFootprint;
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
            PopUtil.toastInBottom("请登录");
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
        /*if("0".equals(bean.getData().getUndoneCount())){
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
        tvWaitEveluateRepair.setText(bean.getData().getUnevalCount());*/
    }

    private void onGetNumSuccess(ShopNumBean bean) {
        int footprintNum = bean.getDatas().getMember_info().getBrowses_goods();
        tvFootprint.setVisibility(View.VISIBLE);
        tvFootprint.setText(String.format(Locale.CHINA,"%d",footprintNum));
        llFootprint.setClickable(true);
        int collectNum = bean.getDatas().getMember_info().getFavorites_goods();
        tvCollect.setVisibility(View.VISIBLE);
        tvCollect.setText(String.format(Locale.CHINA,"%d", collectNum));
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
        initEvent(mRootView);
        getOrdersNum();
        getBalance();
        return mRootView;
    }

    private void getBalance() {
        String userId= SharedPreferencesUtil.getUserId(mActivity, SharedPreferencesUtil.UserId,"");
        String password=SharedPreferencesUtil.getPassword(mActivity, SharedPreferencesUtil.Password,"");
        String validateURL = UrlUtil.Mywallet_balanceUrl;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestManager.getInstance(mActivity.getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                onAnalysisData(data.toString());
            }

            @Override
            public void responseReqFailed(Object data) {
                CustomToast.showWarningLong(data.toString());
            }
        });
    }

    private void onAnalysisData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("error");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                JSONObject data=object.getJSONObject("data");
                String balance=data.optString("balance");
                tvBalance.setText(balance);
            }else {
                CustomToast.showErrorLong(error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void initEvent(View view) {
        view.findViewById(R.id.id_repair_order_layout).setOnClickListener(this);
        view.findViewById(R.id.id_gas_order_layout).setOnClickListener(this);
        view.findViewById(R.id.id_invoice_layout).setOnClickListener(this);
        view.findViewById(R.id.id_address_manage).setOnClickListener(this);
        view.findViewById(R.id.id_share_button).setOnClickListener(this);
        view.findViewById(R.id.id_hot_line_img).setOnClickListener(this);
        view.findViewById(R.id.id_re_consult).setOnClickListener(this);
        view.findViewById(R.id.id_setting_img).setOnClickListener(this);
        view.findViewById(R.id.id_right_massage).setOnClickListener(this);
        view.findViewById(R.id.id_wallet_layout).setOnClickListener(this);
        mPortrait.setOnClickListener(this);

    }

    private void initView(View view) {
        myScrollview = (MyScrollview) view.findViewById(R.id.id_scrollview);
        layoutMySetting = (RelativeLayout) view.findViewById(R.id.id_my_setting);
        btnMessage = (MenuItemM) view.findViewById(R.id.id_mim_message);
        mPortrait = (SimpleDraweeView) view.findViewById(R.id.id_portrait_view);
        tvNavigation = (TextView) view.findViewById(R.id.id_tv_naviga);
        TextView tvNickname = (TextView) view.findViewById(R.id.id_tv_nickname);
        tvBalance=(TextView)view.findViewById(R.id.id_wallet_value) ;
        tvNickname.setOnClickListener(this);
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
        llCollect =view.findViewById(R.id.id_collect_layout);
        tvCollect = (TextView) view.findViewById(R.id.id_collect_value);
        tvFootprint = (TextView) view.findViewById(R.id.id_footprint_num);
        llCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShopCollectionActivity.class));
            }
        });
        llFootprint =view.findViewById(R.id.id_footprint_layout);
        llFootprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, ShopFootprintActivity.class));
            }
        });
        llShopOrder=view.findViewById(R.id.id_mall_order_layout);
        llShopOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, MyShopOrderActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("indexChild", 0);
                mActivity.startActivity(intent);
            }
        });
        llwaitPay = (LinearLayout) view.findViewById(R.id.my_wait_pay);
        llwaitPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MyShopOrderActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("indexChild", 1);
                startActivity(intent);
            }
        });
        llwaitget = (LinearLayout) view.findViewById(R.id.my_wait_get);
        llwaitget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MyShopOrderActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("indexChild", 3);
                startActivity(intent);
            }
        });
        llwaitEveluate = (LinearLayout) view.findViewById(R.id.my_wait_eveluate);
        llwaitEveluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MyShopOrderActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("indexChild", 4);
                startActivity(intent);
            }
        });
        llrefund = (LinearLayout) view.findViewById(R.id.my_refund);
        llrefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MyShopOrderActivity.class);
                intent.putExtra("index", 1);
                intent.putExtra("indexChild", 0);
                startActivity(intent);
            }
        });
        tvWaitEvaluate = (TextView) view.findViewById(R.id.my_wait_eveluate_order_num);
        tvWaitGet = (TextView) view.findViewById(R.id.wait_get_order_num);
        tvWaitPay = (TextView) view.findViewById(R.id.wait_pay_order_num);
        tvRefundOrder = (TextView) view.findViewById(R.id.my_refund_order_num);

    }

    private void onUnReadMessage() {
        if (VariableUtil.messageNum >= MAX_MASSAGE) {
            btnMessage.setUnReadCount(MAX_MASSAGE);
        } else {
            btnMessage.setUnReadCount(VariableUtil.messageNum);
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_portrait_view:
                if (isLoginState(mContext)) {
                    goSetting();
                } else {
                    AppActivityUtil.onLoginActivity(mContext,"");
                }
                break;
            case R.id.id_tv_nickname:
                if (isLoginState(mContext)) {
                    goSetting();
                } else {
                    AppActivityUtil.onLoginActivity(mContext,"");
                }
                break;
            case R.id.id_re_consult:
                if (isLoginState(mContext)) {
                    goConsult();
                } else {
                    AppActivityUtil.onLoginActivity(mContext,"");
                }
                break;
            case R.id.id_hot_line_img:
                hotLine();
                break;
            case R.id.id_setting_img:
                goMoreSetting();
                break;
            case R.id.id_right_massage:
                goMessageCenter();
                break;
            case R.id.id_wallet_layout:
                goMyWallet();
                break;
            case R.id.id_repair_order_layout:
                onRepairOrder(0);
                break;
            case R.id.id_gas_order_layout:
                goGasServer();
                break;
            case R.id.id_invoice_layout:
                goInvoice();
                break;
            case R.id.id_address_manage:
                goManage();
                break;
            case R.id.id_share_button:
                goShare();
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
        Intent intent = new Intent(mContext, NewAddressManagerActivity.class);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
        OkHttpRequestManager.getInstance(getContext()).requestCancel(this);
    }

}
