package com.msht.minshengbao.functionActivity.WaterApp;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.ShareDefaultContent;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.FileUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.QrCodeUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.QrCodeDialog;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/10/18  
 */
public class WaterFriendShareActivity extends BaseActivity implements View.OnClickListener {
    private Bitmap mBitmap;
    private String account="";
    private String  userId;
    private String  deviceToken;

    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<WaterFriendShareActivity> mWeakReference;
        public RequestHandler(WaterFriendShareActivity activity) {
            mWeakReference=new WeakReference<WaterFriendShareActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterFriendShareActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                   // ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_friend_share);
        context=this;
        mPageName="好友分享";
        setCommonHeader(mPageName);
        account= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        deviceToken=SharedPreferencesUtil.getDeviceData(this,SharedPreferencesUtil.DeviceToken,"");
        initFindViewId();
    }
    private void initFindViewId() {
        findViewById(R.id.id_weiXin_share).setOnClickListener(this);
        findViewById(R.id.id_friend_circle).setOnClickListener(this);
        findViewById(R.id.id_code_share).setOnClickListener(this);
        findViewById(R.id.id_copy_link).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_weiXin_share:
                onShareWeiXin();
                break;
            case R.id.id_friend_circle:
                onShareFriendCircle();
                break;
            case R.id.id_code_share:
                onRequestPermission();
                break;
            case R.id.id_copy_link:
                onCopyLink();
                break;
                default:
                    break;
        }
    }
    private void onRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                MPermissionUtils.requestPermissionsResult(this, ConstantUtil.MY_CAMERA_REQUEST, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int code) {
                        onCodeShare();
                    }
                    @Override
                    public void onPermissionDenied(int code) {
                        ToastUtil.ToastText(context,"没有权限您将无法进行扫描操作！");
                    }
                });

            }else {
                onCodeShare();
            }
        }else {
            onCodeShare();
        }
    }
    private void onCodeShare() {
        String shareUrl= ShareDefaultContent.waterShareUrl+"?phone="+account;
        if (mBitmap!=null&&!mBitmap.isRecycled()){
            onShowQrCodeDialog(mBitmap);
        }else {
            mBitmap=QrCodeUtil.createQRCodeBitmap(shareUrl,300);
            onShowQrCodeDialog(mBitmap);
        }
    }
    private void onShowQrCodeDialog(final Bitmap mBitmap) {
        new QrCodeDialog(context,mBitmap).builder()
                .setOnSaveButtonClickListener(new QrCodeDialog.OnSaveButtonClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FileUtil.saveImageToGallery(context,mBitmap)){
                            ToastUtil.ToastText(context,"图片已保存");
                        }

                    }
                })
                .setOnShareButtonClickListener(new QrCodeDialog.OnShareButtonClickListener() {
                    @Override
                    public void onClick(View v) {
                        UMImage image = new UMImage(WaterFriendShareActivity.this, mBitmap);
                        new ShareAction(WaterFriendShareActivity.this).withMedia(image)
                                .setPlatform(SHARE_MEDIA.WEIXIN)
                                .setCallback(umShareListener)
                                .share();
                    }
                }).show();
    }

    private void onShareFriendCircle() {
        String shareUrl= ShareDefaultContent.waterShareUrl+"?phone="+account;
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(ShareDefaultContent.waterText);
        web.setDescription(ShareDefaultContent.waterText);
        web.setThumb(new UMImage(context, R.drawable.purified_water));
        new ShareAction(WaterFriendShareActivity.this).withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(umShareListener)
                .share();
    }
    private void onShareWeiXin() {
        String shareUrl= ShareDefaultContent.waterShareUrl+"?phone="+account;
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(ShareDefaultContent.waterTitle);
        web.setDescription(ShareDefaultContent.waterText);
        web.setThumb(new UMImage(context, R.drawable.purified_water));
        new ShareAction(WaterFriendShareActivity.this).withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener)
                .share();
    }
    private void onCopyLink() {
        String shareUrl= ShareDefaultContent.waterShareUrl+"?phone="+account;
        ClipboardManager cm=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData=ClipData.newPlainText("Label",shareUrl);
        if (cm != null) {
            cm.setPrimaryClip(mClipData);
            ToastUtil.ToastText(context,"已复制到剪切板");
        }
    }
    private void onRequestShareSuccess(){
        String activityCode="water_recharge_share";
        String validateURL = UrlUtil.SUCCESS_SHARE_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("share_code",activityCode);
        textParams.put("token","");
        textParams.put("phone",account);
        textParams.put("device_token",deviceToken);
        textParams.put("relate_info","");
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA shareMedia) {}
        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtil.ToastText(context, " 分享成功啦");
            onRequestShareSuccess();
        }
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtil.ToastText(context,platform + " 分享失败啦");
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtil.ToastText(context,platform + " 分享取消了");
        }
    };
    private UMAuthListener authListener = new UMAuthListener() {


        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBitmap!=null&&!mBitmap.isRecycled()){
           mBitmap.recycle();
        }
    }
}
