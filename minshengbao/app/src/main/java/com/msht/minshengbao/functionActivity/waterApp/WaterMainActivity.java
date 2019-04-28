package com.msht.minshengbao.functionActivity.waterApp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.ViewUI.widget.CustomToast;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.Bean.WaterRedPacketShareBean;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ShareDefaultContent;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.GsonImpl;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.WaterShareRedPacketDialog;
import com.msht.minshengbao.functionActivity.htmlWeb.HtmlPageActivity;
import com.msht.minshengbao.functionActivity.publicModule.QrCodeScanActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/10/9  
 */
public class WaterMainActivity extends BaseActivity implements View.OnClickListener {
    private TextView  tvAccount;
    private TextView  tvBalance;
    private String    account="";
    private String    waterAccount;
    private String couponCode;
    private String orderNo;
    private int       requestType=0;
    private CustomDialog customDialog;
    private final BalanceHandler balanceHandler=new BalanceHandler(this);
    private static class BalanceHandler extends Handler {
        private WeakReference<WaterMainActivity> mWeakReference;
        public BalanceHandler(WaterMainActivity activity) {
            mWeakReference=new WeakReference<WaterMainActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterMainActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }else {
                if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                    activity.customDialog.dismiss();
                }
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String message = object.optString("message");
                        String code=object.optString("code");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestType==0){
                                JSONObject json =object.optJSONObject("data");
                                activity.onReceiveAccountData(json);
                            }else if (activity.requestType==1){
                                activity.initData();
                            }
                        }else {
                            if (activity.requestType==0&&code.equals(ConstantUtil.VALUE_CODE_0009)){
                                VariableUtil.waterAccount=activity.account;
                                activity.waterAccount=activity.account;
                                activity.onSetAccountData();
                                activity.onRegisterWaterAccount();
                            }else {
                                CustomToast.showWarningLong(message);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onSetAccountData() {
        String accountText="ID:"+waterAccount;
        if (RegularExpressionUtil.isPhone(waterAccount)){
            accountText="ID:"+waterAccount.substring(0,3)+"****"+waterAccount.substring(7,waterAccount.length());
        }
        String balanceText="¥0.0";
        tvBalance.setText(balanceText);
        tvAccount.setText(accountText);
    }
    private void onReceiveAccountData(JSONObject json) {
        String id=json.optString("id");
        String type=json.optString("type");
        waterAccount=json.optString("account");
        if (!TextUtils.isEmpty(waterAccount)){
            VariableUtil.waterAccount=waterAccount;
        }else {
            VariableUtil.waterAccount=account;
            waterAccount=account;
        }
        String payAmount=json.optString("payBalance");
        String giveAmount=json.optString("giveBalance");
        String accountText="ID:"+waterAccount;
        if (RegularExpressionUtil.isPhone(waterAccount)){
            accountText="ID:"+waterAccount.substring(0,3)+"****"+waterAccount.substring(7,waterAccount.length());
        }
        double payBalance=json.optDouble("payBalance");
        double giveBalance=json.optDouble("giveBalance");
        double totalBalance=payBalance+giveBalance;
        totalBalance= VariableUtil.twoDecinmal2(totalBalance);
        String balanceText="¥"+String.valueOf(totalBalance);
        tvBalance.setText(balanceText);
        tvAccount.setText(accountText);
        initValidShare();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_main);
        context=this;
        mPageName="民生水宝";
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
        account= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        initFindViewId();
        initData();
    }
    private void initValidShare() {
        String validateURL= UrlUtil.WATER_VALID_SHARE_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("account",VariableUtil.waterAccount);
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                onAnalysisData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                ToastUtil.ToastText(context,data.toString());
            }
        });
    }
    private void onAnalysisData(String s) {
        final WaterRedPacketShareBean bean=GsonImpl.get().toObject(s,WaterRedPacketShareBean.class);
        if (bean.getResult().equals(ConstantUtil.SUCCESS_VALUE)){
            if (bean.getData().getIsShare()==1){
                couponCode=bean.getData().getCouponCode();
                orderNo=bean.getData().getOrderNo();
                onShowRedPacketDialog();
            }
        }else {
            CustomToast.showWarningLong(bean.getMessage());
        }
    }
    private void onShareRedPacket(final String shareCode) {
       new ShareAction(WaterMainActivity.this).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA shareMedia) {
                        String shareUrl=UrlUtil.WATER_RED_PACKET_SHARE_LINK;
                        String linkUrl=UrlUtil.WATER_RED_PACKET_LINK+"shareCode="+shareCode;
                        try {
                            shareUrl=shareUrl+"&redirect_uri="+URLEncoder.encode(linkUrl, "UTF-8")+UrlUtil.WATER_RED_PACKET_VALUE;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        UMWeb web = new UMWeb(shareUrl);
                        web.setTitle(ShareDefaultContent.WATER_RED_PACKET_SHARE_TITLE);
                        web.setDescription(ShareDefaultContent.WATER_RED_PACKET_SHARE_TEXT);
                        web.setThumb(new UMImage(context, R.drawable.water_red_packet_ad));
                        new ShareAction(WaterMainActivity.this).withMedia(web)
                                .setPlatform(shareMedia)
                                .setCallback(umShareListener)
                                .share();
                    }
                }).open();

    }
    private void onShowRedPacketDialog() {
        WaterMainActivity activity=new WeakReference<WaterMainActivity>(this).get();
        if (activity!=null&&!activity.isFinishing()&&context!=null){
            new WaterShareRedPacketDialog(context).builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setOnAdvertisingClickListener(new WaterShareRedPacketDialog.OnAdvertisingClickListener() {
                        @Override
                        public void onClick(View view) {
                            onRequestShareSuccess();
                        }
                    })
                    .setOnCancelClickListener(new WaterShareRedPacketDialog.OnCancelClickListener() {
                        @Override
                        public void onCancelClick(View view) {
                            onCancelShare();
                        }
                    }).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantUtil.VALUE3:
                if(resultCode==ConstantUtil.VALUE1){
                    initData();
                }
                break;
            case ConstantUtil.VALUE4:
                if(resultCode==ConstantUtil.VALUE1){
                    initData();
                }
                break;
            case ConstantUtil.VALUE2:
                if(resultCode==ConstantUtil.VALUE1){
                    initData();
                }
                break;
            case ConstantUtil.VALUE1:
                initData();
                break;
                default:
                    break;
        }
    }
    private void initData() {
        requestType=0;
        customDialog.show();
        String validateURL= UrlUtil.WATER_ACCOUNT_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("type","1");
        textParams.put("account",account);
        SendRequestUtil.postDataFromService(validateURL,textParams,balanceHandler);
    }
    private void onRegisterWaterAccount() {
        requestType=1;
        customDialog.show();
        String validateURL= UrlUtil.WATER_INNER_REGISTER;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("phone",account);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL,OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,balanceHandler);
    }
    private void initFindViewId() {
        findViewById(R.id.id_tip_layout).setOnClickListener(this);
        findViewById(R.id.id_scan_layout).setOnClickListener(this);
        findViewById(R.id.id_balance_layout).setOnClickListener(this);
        findViewById(R.id.id_nearby_layout).setOnClickListener(this);
        findViewById(R.id.id_malfunction_layout).setOnClickListener(this);
        findViewById(R.id.id_service_layout).setOnClickListener(this);
        findViewById(R.id.id_redPacket_layout).setOnClickListener(this);
        findViewById(R.id.id_replace_account).setOnClickListener(this);
        findViewById(R.id.id_ID_layout).setOnClickListener(this);
        findViewById(R.id.id_share_layout).setOnClickListener(this);
        tvAccount=(TextView)findViewById(R.id.id_ID);
        tvBalance=(TextView)findViewById(R.id.id_balance);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_scan_layout:
                requestPermission();
                break;
            case R.id.id_balance_layout:
                balanceInformation();
                break;
            case R.id.id_nearby_layout:
                onNearbyMachine();
                break;
            case R.id.id_malfunction_layout:
                onMalfunction();
                break;
            case R.id.id_redPacket_layout:
                onRedPacketCard();
                break;
            case R.id.id_service_layout:
                onServiceCenter();
                break;
            case R.id.id_replace_account:
                switchAccount();
                break;
            case R.id.id_ID_layout:
                switchAccount();
                break;
            case R.id.id_share_layout:
                friendShare();
                break;
            case R.id.id_tip_layout:
                rechargeAmount();
                break;
                default:
                    break;
        }
    }
    private void onCancelShare() {
        String validateURL= UrlUtil.WATER_RED_PACKET_CANCEL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("account",VariableUtil.waterAccount);
        textParams.put("orderNo",orderNo);
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {}
            @Override
            public void responseReqFailed(Object data) {
                ToastUtil.ToastText(context,data.toString());
            }
        });
    }
    private void onRequestShareSuccess() {
        String validateURL= UrlUtil.WATER_RED_PACKET_CREATE;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("account",VariableUtil.waterAccount);
        textParams.put("orderNo",orderNo);
        textParams.put("couponCode",couponCode);
        textParams.put("linkUrl",UrlUtil.WATER_RED_PACKET_LINK);
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                onCreateShareCode(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                CustomToast.showErrorDialog(data.toString());
            }
        });
    }
    private void onCreateShareCode(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String message = object.optString("message");
            String code=object.optString("code");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                JSONObject json =object.optJSONObject("data");
                String shareCode=json.optString("shareCode");
                onShareRedPacket(shareCode);
            }else {
                CustomToast.showWarningLong(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onRedPacketCard() {
        Intent intent=new Intent(context,RedPacketCardActivity.class);
        intent.putExtra("account",waterAccount);
        startActivityForResult(intent,1);
    }
    private void onServiceCenter() {
        String url=UrlUtil.WATER_SERVICE_CENTER;
        Intent intent=new Intent(context, HtmlPageActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","客服中心");
        startActivity(intent);
    }
    private void friendShare() {
        Intent intent=new Intent(context,WaterFriendShareActivity.class);
        intent.putExtra("account",waterAccount);
        startActivity(intent);
    }
    private void onMalfunction() {
        Intent intent=new Intent(context,WaterMalfunctionActivity.class);
        startActivity(intent);
    }
    private void balanceInformation() {
        Intent intent=new Intent(context,WaterBalanceActivity.class);
        startActivityForResult(intent,4);
    }
    private void switchAccount() {
        Intent intent=new Intent(context,WaterSwitchAccountActivity.class);
        startActivityForResult(intent,3);
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .runtime()
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                scanQrCode();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                CustomToast.showWarningLong("没有权限您将无法进行扫描操作！");
                            }
                        }).start();
            }else {
                scanQrCode();
            }
        }else {
            scanQrCode();
        }
    }
    private void scanQrCode() {
        Intent intent =new Intent(context, QrCodeScanActivity.class);
        startActivityForResult(intent,1);
    }
    private void onNearbyMachine() {
        Intent intent=new Intent(context,WaterEquipmentMapActivity.class);
        startActivityForResult(intent,1);
    }
    private void rechargeAmount() {
        Intent intent=new Intent(context,WaterBalanceActivity.class);
        startActivityForResult(intent,4);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA shareMedia) {}
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if(!platform.name().equals(ConstantUtil.WEI_XIN_PLATFORM)){
                CustomToast.showSuccessDialog("分享成功啦");
            }else{
                CustomToast.showSuccessDialog("收藏成功啦");
            }
        }
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            CustomToast.showErrorDialog("分享失败啦");
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            CustomToast.showWarningLong("分享取消了");
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();

        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
    }
}
