package com.msht.minshengbao.functionActivity.myActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.ViewUI.widget.CustomToast;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.FileUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.SelectPicPopupWindow;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;


import android.view.View.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo class
 *
 * @author hong
 * @date 2016/10/31
 */
public class MySettingActivity extends BaseActivity implements OnClickListener {
    private RelativeLayout settingPortrait;
    private RelativeLayout settingNickname;
    private RelativeLayout layoutSettingSex;
    private TextView tvNickname, tvSex, tvPhone;
    private TextView tvBindStatus;
    private SimpleDraweeView simpleDraweeView;
    private String avatarUrl;
    private String userId,password;
    private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory().getPath() + "/Livelihool/MsbCameraCache");
    private File mCacheFile;
    private SelectPicPopupWindow takePhotoPopWin;
    /** 
     * urlPath 图片本地路径
     */
    private String urlPath;

    /**
     * // 相册选图标记
     */
    private static final int REQUEST_CODE_PICK = 0;
    /**
     *相机拍照标记
     */
    private static final int REQUEST_CODE_TAKE = 1;
    /**
     * 图片裁切标记
     */
    private static final int REQUEST_CODE_CUTTING = 2;
    /**
     *昵称标志
     */
    private static final int REQUEST_CODE_NICK =3;
    /**
     * 性别标志
     */
    private static final int REQUEST_CODE_SEX =5;
    /**
     * 设置密码
     */
    private static final int RESET_CODE=4;
    private static final int RESULT_CODE=1;
    /**
     * IMAGE_FILE_NAME  头像文件名称
     */
    private static String imageFileName;
    private CustomDialog customDialog;
    private final MyHandler myHandler=new MyHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting);
        mPageName ="我的设置";
        setCommonHeader(mPageName);
        context=getApplicationContext();
        initFindViewById();
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        avatarUrl =SharedPreferencesUtil.getAvatarUrl(context,SharedPreferencesUtil.AvatarUrl,"");
        customDialog=new CustomDialog(this, "正在上传图片");
        initShowInfo();
        initPersonalInfo();
        initEvent();
    }

    private void initPersonalInfo() {
        String validateURL = UrlUtil.USER_INFO_GAS_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,""));
        textParams.put("password", SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,""));
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
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
            String results = object.optString("result");
            String error = object.optString("error");
            JSONObject objectJson = object.getJSONObject("data");
            if (results.equals(SendRequestUtil.SUCCESS_VALUE)) {
               onPersonalInformation(objectJson);
            } else {
                CustomToast.showWarningLong(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void onPersonalInformation(JSONObject objectJson) {
        String sex = objectJson.optString("sex");
        String phoneNo = objectJson.optString("phone");
        String isWeChatBind=objectJson.optString("isWeChatBind");
        String mNickname=objectJson.optString("nickname");
        String avatar=objectJson.optString("avatar");
        SharedPreferencesUtil.putAvatarUrl(this,SharedPreferencesUtil.AvatarUrl,avatar);
        SharedPreferencesUtil.putStringData(this,SharedPreferencesUtil.IS_WEI_CHAT_BIND,isWeChatBind);
        SharedPreferencesUtil.putSex(this, SharedPreferencesUtil.Sex, sex);
        SharedPreferencesUtil.putPhoneNumber(this, SharedPreferencesUtil.PhoneNumber, phoneNo);
        if (TextUtils.isEmpty(mNickname)){
            tvNickname.setText("");
        }else {
            tvNickname.setText(mNickname);
        }
        if (isWeChatBind.equals(ConstantUtil.VALUE_ONE)){
            tvBindStatus.setText("已绑定");
        }else {
            tvBindStatus.setText("未绑定");
        }
        tvPhone.setText(phoneNo);
        tvSex.setText(sex);
        onSetPortraitImage(avatar);
    }

    private void initShowInfo() {
        String mNickname = SharedPreferencesUtil.getNickName(this,SharedPreferencesUtil.NickName,"");
        String mSexText =SharedPreferencesUtil.getSex(this,SharedPreferencesUtil.Sex,"");
        String phone=SharedPreferencesUtil.getPhoneNumber(this,SharedPreferencesUtil.PhoneNumber,"");
        if (mNickname ==null){
            tvNickname.setText("");
        }else {
            tvNickname.setText(mNickname);
        }
        tvPhone.setText(phone);
        tvSex.setText(mSexText);
        onSetPortraitImage(avatarUrl);
    }

    private void onSetPortraitImage(String url) {
        Uri uri = Uri.parse(url);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                //. 其他设置（如果有的话）
                .build();
        simpleDraweeView.setController(controller);
    }

    private void initFindViewById() {
        simpleDraweeView=(SimpleDraweeView)findViewById(R.id.id_portrait);
       // circleImageView = (CircleImageView) findViewById(R.id.id_portraitview);
        settingPortrait =(RelativeLayout)findViewById(R.id.id_setting_portrait_layout);
        settingNickname =(RelativeLayout)findViewById(R.id.id_setting_nickname_layout);
        layoutSettingSex =(RelativeLayout)findViewById(R.id.id_setting_sex_layout);
        tvNickname =(TextView)findViewById(R.id.id_tv_nickname);
        tvSex =(TextView)findViewById(R.id.id_tv_sex);
        tvPhone =(TextView)findViewById(R.id.id_phone);
        tvBindStatus=(TextView)findViewById(R.id.id_weiChat_status);
        String isWeChatBind=SharedPreferencesUtil.getStringData(this,SharedPreferencesUtil.IS_WEI_CHAT_BIND,"0");
        if (isWeChatBind.equals(ConstantUtil.VALUE_ONE)){
            tvBindStatus.setText("已绑定");
        }else {
            tvBindStatus.setText("未绑定");
        }

    }
    private void initEvent() {
        findViewById(R.id.id_layout_setpwd).setOnClickListener(this);
        findViewById(R.id.id_layout_weiChat).setOnClickListener(this);
        simpleDraweeView.setOnClickListener(this);
        tvNickname.setOnClickListener(this);
        tvSex.setOnClickListener(this);
        settingPortrait.setOnClickListener(this);
        settingNickname.setOnClickListener(this);
        layoutSettingSex.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_setting_portrait_layout:
                //适配ANDROID6.0
                onSettingPermission();
                break;
            case R.id.id_portrait:
                onSettingPermission();
                break;
            case R.id.id_setting_nickname_layout:
                setNickname();
                break;
            case R.id.id_tv_nickname:
                setNickname();
                break;
            case R.id.id_setting_sex_layout:
                setSexInfo();
                break;
            case R.id.id_tv_sex:
                setSexInfo();
                break;
            case R.id.id_layout_setpwd:
                setPassword();
                break;
            case R.id.id_layout_weiChat:
               onBindWeiChat();
                break;
            default:
                break;
        }
    }

    private void onBindWeiChat() {
        String isWeChatBind=SharedPreferencesUtil.getStringData(this,SharedPreferencesUtil.IS_WEI_CHAT_BIND,"0");
        if (!isWeChatBind.equals(ConstantUtil.VALUE_ONE)){
            UMShareConfig config = new UMShareConfig();
            config.isNeedAuthOnGetUserInfo(true);
            UMShareAPI.get(this).setShareConfig(config);
            UMShareAPI.get(this).getPlatformInfo(MySettingActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
        }else {
            CustomToast.showWarningDialog("您的账户已绑定");
        }
    }

    private void onSettingPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .runtime()
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                onSettingPortraits();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                CustomToast.showErrorDialog("授权失败");
                            }
                        }).start();
            }else {
                onSettingPortraits();
            }
        } else {
            onSettingPortraits();
        }
    }

    private void setPassword() {
        Intent pwd=new Intent(this,PasswordActivity.class);
        startActivityForResult(pwd, RESET_CODE);
    }
    private void setSexInfo() {
        String sexValue= tvSex.getText().toString();
        Intent sex=new Intent(this,SettingSexActivity.class);
        sex.putExtra("SEX",sexValue);
        startActivityForResult(sex, REQUEST_CODE_SEX);
    }
    private void setNickname() {
        Intent nick=new Intent(this,SettingNicknameActivity.class);
        startActivityForResult(nick, REQUEST_CODE_NICK);
    }
    private void onSettingPortraits() {
        takePhotoPopWin = new SelectPicPopupWindow(this, onClickListener);
        takePhotoPopWin.showAtLocation(findViewById(R.id.activity_mysetting), Gravity.CENTER, 0, 0);
    }
    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    takePhotoPopWin.dismiss();
                    takePhoto();
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    takePhotoPopWin.dismiss();
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUEST_CODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };
    private void takePhoto() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            if (!PHOTO_DIR.exists()) {
                PHOTO_DIR.mkdirs();// 创建照片的存储目录
            }
            imageFileName = System.currentTimeMillis() + ".jpg";
            File mCurrentPhotoFile = new File(PHOTO_DIR, imageFileName);
            Intent intentC = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri imageUri = FileProvider.getUriForFile(MySettingActivity.this, "com.msht.minshengbao.fileProvider", mCurrentPhotoFile);
                intentC.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentC.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            } else {
                intentC.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            }
            startActivityForResult(intentC, REQUEST_CODE_TAKE);
        } else {
            CustomToast.showWarningDialog("没有SD卡");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 直接从相册获取
            case REQUEST_CODE_PICK:
                try {
                    if (data!=null){
                        startCrop(data.getData());
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            // 调用相机拍照
            case REQUEST_CODE_TAKE:
                File temp = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Livelihool/MsbCameraCache/"+ imageFileName);
                if (temp.exists()) {
                    startCrop(Uri.fromFile(temp));
                }else {
                    CustomToast.showWarningDialog("文件不存在");
                }
                break;
            // 取得裁剪后的图片
            case REQUEST_CODE_CUTTING:
                urlPath=mCacheFile.getAbsolutePath();
                if (!TextUtils.isEmpty(urlPath)){
                    getImagePicture();
                }
                break;
            //获取昵称设置返回数据
            case REQUEST_CODE_NICK:
                if(data!=null){
                    String nickname=data.getStringExtra("nickname");
                    tvNickname.setText(nickname);
                }
                break;
            case REQUEST_CODE_SEX:
                if (data!=null){
                    String gender=data.getStringExtra("gender");
                    tvSex.setText(gender);
                }
                break;
            case RESET_CODE:
                //修改密码成功返回
                if (resultCode==RESULT_CODE){
                    setResult(0x005);
                    finish();
                }
                break;
            default:
                    break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void getImagePicture() {
        if (!TextUtils.isEmpty(urlPath)){
            customDialog.show();
            requestService();
        }
    }
    private void startCrop(Uri uri) {
        String fileName = System.currentTimeMillis() + ".jpg";
        if (!PHOTO_DIR.exists()) {
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
        }
        mCacheFile = new File(PHOTO_DIR, fileName);
        Uri   outputUri = Uri.fromFile(new File(mCacheFile.getPath()));
        String url = FileUtil.getPath(context, uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (!TextUtils.isEmpty(url)){
            //sdk>=24
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                //通过FileProvider创建一个content类型的Uri
                Uri imageUri = FileProvider.getUriForFile(context, "com.msht.minshengbao.fileProvider", new File(url));
                //去除默认的人脸识别，否则和剪裁匡重叠
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra("noFaceDetection", true);
                intent.setDataAndType(imageUri, "image/*");
                //19=<sdk<24
            }else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
                //sdk<19
            }else {
                intent.setDataAndType(uri, "image/*");
            }
        }
        intent.putExtra("crop", true);
        //比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("output", outputUri);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_CODE_CUTTING);
    }
    private void requestService() {
        String validateURL= UrlUtil.GasmodifyInfo_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        Map<String, File> stringFileHashMap = new HashMap<String, File>();
        File file = new File(urlPath);
        textParams.put("userId",userId);
        textParams.put("password",password);
        stringFileHashMap.put("avatar", file);
        SendRequestUtil.postFileToServer(textParams,stringFileHashMap,validateURL,myHandler);
    }
    private static class MyHandler extends Handler{
        private WeakReference<MySettingActivity> mWeakReference;
        public MyHandler(MySettingActivity activity) {
            mWeakReference = new WeakReference<MySettingActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final MySettingActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String results = jsonObject.optString("result");
                        String error=jsonObject.optString("error");
                        JSONObject objectInfo = jsonObject.optJSONObject("data");
                        String avatarUrl=objectInfo.optString("avatar");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)){
                            SharedPreferencesUtil.putAvatarUrl(activity.context,SharedPreferencesUtil.AvatarUrl,avatarUrl);
                            activity.onSetPortraitImage(avatarUrl);
                            activity.onResetAvatar();//给Mynewfragment返回数据
                        }else {
                            CustomToast.showWarningLong(error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onResetAvatar() {
        setResult(4);
    }
    UMAuthListener umAuthListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {}
        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            onGetWeiChatData(data);
        }
        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            CustomToast.showSuccessLong("失败：" + t.getMessage());
        }
        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            CustomToast.showSuccessLong("取消了");
        }
    };

    private void onGetWeiChatData(final Map<String, String> weiChatData) {
        String unionId=weiChatData.get("unionid");
        String requestUrl=UrlUtil.BING_WEI_MY_PAGE_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("unionid",unionId);
        textParams.put("userId",userId);
        customDialog.show();
        if (customDialog!=null){
            customDialog.setDialogContent("正在加载...");
            customDialog.show();
        }
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(requestUrl, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                onAnalysisData(weiChatData,data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                CustomToast.showErrorLong(data.toString());
            }
        });
    }
    private void onAnalysisData(Map<String, String> weiChatData, String s) {
        try {
            JSONObject object = new JSONObject(s);
            String result=object.optString("result");
            String error = object.optString("error");
            if (result.equals(SendRequestUtil.SUCCESS_VALUE)){
                JSONObject objectInfo = object.optJSONObject("data");
                String code=objectInfo.optString("code");
                if (!TextUtils.isEmpty(code)){
                    switch (code){
                        case ConstantUtil.WC_USER_EXIST:
                            tvBindStatus.setText("已绑定");
                            String unionId=weiChatData.get("unionid");
                            onShowResultDialog(unionId);
                            break;
                        case ConstantUtil.BIND_WEI_CHAT_SUCCESS:
                            CustomToast.showSuccessDialog("绑定成功");
                            tvBindStatus.setText("已绑定");
                            break;
                            default:
                                CustomToast.showErrorDialog("绑定异常");
                                break;
                    }
                }else {
                    CustomToast.showErrorDialog("绑定异常");
                }
            }else {
                CustomToast.showErrorLong(error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onShowResultDialog(final String unionId) {
        String message="该微信已经绑定民生宝账户，若继/n续绑定的话，会与原账户解绑";
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(message)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        onChangeBindWeiChat( unionId);
                    }
                })
                .show();
    }

    private void onChangeBindWeiChat(String unionId) {
        String requestUrl=UrlUtil.CHANGE_BIND_WEI_CHAT_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("unionid",unionId);
        textParams.put("userId",userId);
        customDialog.show();
        if (customDialog!=null){
            customDialog.setDialogContent("正在加载...");
            customDialog.show();
        }
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(requestUrl, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                onChangeBindData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                CustomToast.showErrorLong(data.toString());
            }
        });
    }
    private void onChangeBindData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String result=object.optString("result");
            String error = object.optString("error");
            if (result.equals(SendRequestUtil.SUCCESS_VALUE)){
                JSONObject objectInfo = object.optJSONObject("data");
                int isWeChatBind=objectInfo.optInt("isWeChatBind");
                if (isWeChatBind==1){
                    CustomToast.showSuccessDialog("绑定成功");
                    tvBindStatus.setText("已绑定");
                    SharedPreferencesUtil.putStringData(this,SharedPreferencesUtil.IS_WEI_CHAT_BIND,String.valueOf(isWeChatBind));
                }else {
                    tvBindStatus.setText("未绑定");
                    CustomToast.showSuccessDialog("绑定失败");
                }
            }else {
                CustomToast.showErrorLong(error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
        UMShareAPI.get(this).release();
    }
}
