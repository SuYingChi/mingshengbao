package com.msht.minshengbao.FunctionActivity.MyActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ACache;
import com.msht.minshengbao.Utils.BitmapUtil;
import com.msht.minshengbao.Utils.FileUtil;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.CircleImageView;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.SelectPicPopupWindow;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import android.view.View.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 *
 * @author hong
 * @date 2016/10/31
 */
public class Mysetting extends BaseActivity implements OnClickListener {
    private RelativeLayout settingPortrait;
    private RelativeLayout settingNickname;
    private RelativeLayout layoutSettingSex;
    private TextView tvNickname, tvSex, tvPhone;
    private String phone;
    private String avatarUrl;
    private String userId,password;
    private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Livelihool/MsbCameraCache");
    /**
     * mCurrentPhotoFile 照相机拍照得到的图片
     */
    private File mCurrentPhotoFile;
    private File mCacheFile;
    private CircleImageView circleImageView;
    private final String mPageName ="我的设置";
    private SelectPicPopupWindow takePhotoPopWin;
    /** 
     * urlPath 图片本地路径
     */
    private String urlPath;
    private Bitmap bitmap=null;
    private ACache mCache;
    private static  final int MY_PERMISSIONS_REQUEST=2;
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
    private final GetImageHandler getImageHandler=new GetImageHandler(this);
    private static class GetImageHandler extends Handler{
        private WeakReference<Mysetting> mWeakReference;
        public GetImageHandler(Mysetting activity) {
            mWeakReference = new WeakReference<Mysetting>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final Mysetting reference =mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null||reference.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    Bitmap bitmap  = (Bitmap)msg.obj;
                    if (bitmap==null||bitmap.isRecycled()){
                        reference.circleImageView.setImageResource(R.drawable.potrait);
                    }else {
                        reference.circleImageView.setImageBitmap(bitmap);
                        reference.mCache.put("avatarimg", bitmap);
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.context,msg.obj.toString());
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
        setContentView(R.layout.activity_mysetting);
        setCommonHeader("我的设置");
        context=getApplicationContext();
        initfindViewByid();
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        avatarUrl =SharedPreferencesUtil.getAvatarUrl(context,SharedPreferencesUtil.AvatarUrl,"");
        mCache = ACache.get(this);
        customDialog=new CustomDialog(this, "正在上传图片");
        initShowinfo();
        initEvent();
    }
    private void initShowinfo() {
        String mNickname = SharedPreferencesUtil.getNickName(this,SharedPreferencesUtil.NickName,"");
        Bitmap mAvatar =mCache.getAsBitmap("avatarimg");
        String mSexText =SharedPreferencesUtil.getSex(this,SharedPreferencesUtil.Sex,"");
        phone=SharedPreferencesUtil.getPhoneNumber(this,SharedPreferencesUtil.PhoneNumber,"");
        if (mNickname ==null){
            tvNickname.setText("");
        }else {
            tvNickname.setText(mNickname);
        }
        tvPhone.setText(phone);
        tvSex.setText(mSexText);
        if (mAvatar !=null){
            circleImageView.setImageBitmap(mAvatar);
        }else {
            SendrequestUtil.getBitmapFromService(avatarUrl,getImageHandler);
        }
    }
    private void initfindViewByid() {
        circleImageView = (CircleImageView) findViewById(R.id.id_portraitview);
        settingPortrait =(RelativeLayout)findViewById(R.id.id_setting_portrait_layout);
        settingNickname =(RelativeLayout)findViewById(R.id.id_setting_nickname_layout);
        layoutSettingSex =(RelativeLayout)findViewById(R.id.id_setting_sex_layout);
        tvNickname =(TextView)findViewById(R.id.id_tv_nickname);
        tvSex =(TextView)findViewById(R.id.id_tv_sex);
        tvPhone =(TextView)findViewById(R.id.id_phone);
    }
    private void initEvent() {
        findViewById(R.id.id_layout_setpwd).setOnClickListener(this);
        circleImageView.setOnClickListener(this);
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
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                        AndPermission.with(this)
                                .requestCode(MY_PERMISSIONS_REQUEST)
                                .permission(Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .send();
                    }else {
                        onSettingPortraits();
                    }
                } else {
                    onSettingPortraits();
                }
                break;
            case R.id.id_portraitview:
                //适配ANDROID6.0
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                        AndPermission.with(this)
                                .requestCode(MY_PERMISSIONS_REQUEST)
                                .permission(Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .send();
                    }else {
                        onSettingPortraits();
                    }
                } else {
                    onSettingPortraits();
                }
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
            default:
                break;
        }
    }

    private void setPassword() {
        Intent pwd=new Intent(this,PasswordActivity.class);
        startActivityForResult(pwd, RESET_CODE);
    }
    private void setSexInfo() {
        String sexvalue= tvSex.getText().toString();
        Intent sex=new Intent(this,SettingSex.class);
        sex.putExtra("SEX",sexvalue);
        startActivityForResult(sex, REQUEST_CODE_SEX);
    }
    private void setNickname() {
        Intent nick=new Intent(this,SettingNickname.class);
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
                Uri imageUri = FileProvider.getUriForFile(Mysetting.this, "com.msht.minshengbao.fileProvider", mCurrentPhotoFile);
                intentC.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentC.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            } else {
                intentC.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            }
            startActivityForResult(intentC, REQUEST_CODE_TAKE);
        } else {
            ToastUtil.ToastText(context,"没有SD卡");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if(requestCode==MY_PERMISSIONS_REQUEST) {
                onSettingPortraits();
            }
        }
        @Override
        public void onFailed(int requestCode) {
            if(requestCode==MY_PERMISSIONS_REQUEST) {
                ToastUtil.ToastText(context,"授权失败");
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 直接从相册获取
            case REQUEST_CODE_PICK:
                try {
                    startCrop(data.getData());
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
                    ToastUtil.ToastText(context,"文件不存在");
                }
                break;
            // 取得裁剪后的图片
            case REQUEST_CODE_CUTTING:
                urlPath=mCacheFile.getAbsolutePath();
                if (urlPath!=null){
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
        bitmap =BitmapUtil.decodeSampledBitmapFromFile(urlPath, 500, 500) ;
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        circleImageView.setImageDrawable(drawable);
        customDialog.show();
        requestService();
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
        //sdk>=24
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //通过FileProvider创建一个content类型的Uri
            Uri imageUri = FileProvider.getUriForFile(Mysetting.this, "com.msht.minshengbao.fileProvider", new File(url));
            //去除默认的人脸识别，否则和剪裁匡重叠
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra("noFaceDetection", true);
            intent.setDataAndType(imageUri, "image/*");
            //19=<sdk<24
        }else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT&&android.os.Build.VERSION.SDK_INT<Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            //sdk<19
        }else {
            intent.setDataAndType(uri, "image/*");
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
        SendrequestUtil.postFileToServer(textParams,stringFileHashMap,validateURL,myHandler);
    }
    private static class MyHandler extends Handler{
        private WeakReference<Mysetting> mWeakReference;
        public MyHandler(Mysetting activity) {
            mWeakReference = new WeakReference<Mysetting>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final Mysetting activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String results = jsonObject.optString("result");
                        String error=jsonObject.optString("error");
                        JSONObject objectInfo = jsonObject.optJSONObject("data");
                        String avatarUrl=objectInfo.optString("avatar");
                        if (results.equals(SendrequestUtil.SUCCESS_VALUE)){
                            //清除原有数据
                            activity.mCache.remove("avatarimg");
                            activity.mCache.put("avatarimg", activity.bitmap);
                            SharedPreferencesUtil.putAvatarUrl(activity.context,SharedPreferencesUtil.AvatarUrl,avatarUrl);
                            activity.onResetAvatar();//给Mynewfragment返回数据
                        }else {
                            activity.onFailure(error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onFailure(String error) {
        new PromptDialog.Builder(this)
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
    private void onResetAvatar() {
        setResult(4);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
