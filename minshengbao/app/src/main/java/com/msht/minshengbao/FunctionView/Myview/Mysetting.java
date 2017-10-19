package com.msht.minshengbao.FunctionView.Myview;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.MainActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ACache;
import com.msht.minshengbao.Utils.BitmapUtil;
import com.msht.minshengbao.Utils.FileUtil;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.CircleImageView;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.SelectPicPopupWindow;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import android.view.View.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.iwf.photopicker.utils.FileUtils;

public class Mysetting extends BaseActivity implements OnClickListener {
    private RelativeLayout Settingportrait;
    private RelativeLayout Settingnickname;
    private RelativeLayout Settingsex;
    private ImageView onBackprocess;
    private TextView tv_nickname,tv_sex,tv_phone,tv_naviga;
    private String fnickname;
    private String fsex;
    private Bitmap favatar;
    private String phone;
    private String userId,password;
    private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Livelihool/MsbCameraCache");
    private File mCurrentPhotoFile;// 照相机拍照得到的图片
    private File mCacheFile;
    private CircleImageView circleImageView;
    private final String mPageName ="我的设置";
    private SelectPicPopupWindow takePhotoPopWin;
    private String urlpath;			// 图片本地路径
    private Bitmap bitmap=null;
    private ACache mCache;//缓存
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private static  final int MY_PERMISSIONS_REQUEST=2;
    private static final int REQUESTCODE_PICK = 0;		// 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;		// 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;// 图片裁切标记
    private static final int REQUESTCOODE_NICK=3;//昵称标志
    private static final int REQUESTCOODE_SEX=5;//性别标志
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private static String ImagrfileName;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting);
        setCommonHeader("我的设置");
        context=getApplicationContext();
        initfindViewByid();
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        mCache = ACache.get(this);//获取缓存数据
        customDialog=new CustomDialog(this, "正在上传图片");
        initShowinfo();
        initEvent();
    }
    private void initShowinfo() {
        fnickname= SharedPreferencesUtil.getNickName(this,SharedPreferencesUtil.NickName,"");
        favatar=mCache.getAsBitmap("avatarimg");
        fsex=SharedPreferencesUtil.getSex(this,SharedPreferencesUtil.Sex,"");
        phone=SharedPreferencesUtil.getPhoneNumber(this,SharedPreferencesUtil.PhoneNumber,"");
        if (fnickname==null){
            tv_nickname.setText("");
        }else {
            tv_nickname.setText(fnickname);
        }
        tv_phone.setText(phone);
        tv_sex.setText(fsex);
        if (favatar!=null){
            circleImageView.setImageBitmap(favatar);
        }else {
            circleImageView.setImageResource(R.drawable.potrait);
        }
    }
    private void initfindViewByid() {
        circleImageView = (CircleImageView) findViewById(R.id.id_portraitview);
        Settingportrait=(RelativeLayout)findViewById(R.id.id_setting_portrait_layout);
        Settingnickname=(RelativeLayout)findViewById(R.id.id_setting_nickname_layout);
        Settingsex=(RelativeLayout)findViewById(R.id.id_setting_sex_layout);
        tv_nickname=(TextView)findViewById(R.id.id_tv_nickname);
        tv_sex=(TextView)findViewById(R.id.id_tv_sex);
        tv_phone=(TextView)findViewById(R.id.id_phone);
    }
    private void initEvent() {
        circleImageView.setOnClickListener(this);
        tv_nickname.setOnClickListener(this);
        tv_sex.setOnClickListener(this);
        Settingportrait.setOnClickListener(this);
        Settingnickname.setOnClickListener(this);
        Settingsex.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_setting_portrait_layout:
                if (Build.VERSION.SDK_INT >= 23) {     //适配ANDROID6.0
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                        AndPermission.with(this)
                                .requestCode(MY_PERMISSIONS_REQUEST)
                                .permission(Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .send();
                    }else {
                        Settingportraits();
                    }
                } else {
                    Settingportraits();
                }
                break;
            case R.id.id_portraitview:
                if (Build.VERSION.SDK_INT >= 23) {     //适配ANDROID6.0
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                        AndPermission.with(this)
                                .requestCode(MY_PERMISSIONS_REQUEST)
                                .permission(Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .send();
                    }else {
                        Settingportraits();
                    }
                } else {
                    Settingportraits();
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
            default:
                break;
        }
    }
    private void setSexInfo() {
        String sexvalue=tv_sex.getText().toString();
        Intent sex=new Intent(this,SettingSex.class);
        sex.putExtra("SEX",sexvalue);
        startActivityForResult(sex, REQUESTCOODE_SEX);
    }
    private void setNickname() {
        Intent nick=new Intent(this,SettingNickname.class);
        startActivityForResult(nick, REQUESTCOODE_NICK);
    }
    private void Settingportraits() {
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
                    TakePhoto();
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    takePhotoPopWin.dismiss();
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };
    private void TakePhoto() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            if (!PHOTO_DIR.exists()) {
                PHOTO_DIR.mkdirs();// 创建照片的存储目录
            }
            ImagrfileName= System.currentTimeMillis() + ".jpg";
            mCurrentPhotoFile = new File(PHOTO_DIR, ImagrfileName);
            Intent intentC = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri imageUri = FileProvider.getUriForFile(Mysetting.this, "com.msht.minshengbao.fileProvider", mCurrentPhotoFile);
                intentC.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentC.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            } else {
                intentC.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            }
            startActivityForResult(intentC, REQUESTCODE_TAKE);
        } else {
            Toast.makeText(context, "没有SD卡", Toast.LENGTH_LONG).show();
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
                Settingportraits();
            }
        }
        @Override
        public void onFailed(int requestCode) {
            if(requestCode==MY_PERMISSIONS_REQUEST) {
                Toast.makeText(Mysetting.this,"授权失败",Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startCrop(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Livelihool/MsbCameraCache/"+ImagrfileName);
                if (temp!=null) {
                    startCrop(Uri.fromFile(temp));
                }else {
                    Toast.makeText(context, "文件不存在", Toast.LENGTH_LONG).show();
                }
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                urlpath=mCacheFile.getAbsolutePath();
                if (urlpath!=null){
                    getImagePicture();
                }
                break;
            case REQUESTCOODE_NICK://获取昵称设置返回数据
                if(data!=null){
                    String nickname=data.getStringExtra("nickname");
                    tv_nickname.setText(nickname);
                }
                break;
            case REQUESTCOODE_SEX:
                if (data!=null){
                    String gender=data.getStringExtra("gender");
                    tv_sex.setText(gender);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void getImagePicture() {
        bitmap =BitmapUtil.decodeSampledBitmapFromFile(urlpath, 500, 500) ;
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        circleImageView.setImageDrawable(drawable);
        customDialog.show();
        requestSevice();
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
            Uri imageUri = FileProvider.getUriForFile(Mysetting.this, "com.msht.minshengbao.fileProvider", new File(url));//通过FileProvider创建一个content类型的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra("noFaceDetection", true);//去除默认的人脸识别，否则和剪裁匡重叠
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
        startActivityForResult(intent, REQUESTCODE_CUTTING);

    }
    private void requestSevice() {
        String validateURL= UrlUtil.GasmodifyInfo_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        Map<String, File> fileparams = new HashMap<String, File>();
        File file = new File(urlpath);
        textParams.put("userId",userId);
        textParams.put("password",password);
        fileparams.put("avatar", file);
        HttpUrlconnectionUtil.multipleFileParameters(validateURL, textParams, fileparams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                handler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.what = FAILURE;
                msg.obj =fail;
                handler.sendMessage(msg);
            }
        });
    }
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String results = jsonObject.optString("result");
                        String error=jsonObject.optString("error");
                        JSONObject ObjectInfo = jsonObject.optJSONObject("data");
                        String avatarurl=ObjectInfo.optString("avatar");
                        if (results.equals("success")){
                            mCache.remove("avatarimg");//清除原有数据
                            mCache.put("avatarimg", bitmap);//保存新数据
                            SharedPreferencesUtil.putAvatarUrl(context,SharedPreferencesUtil.AvatarUrl,avatarurl);
                            Retureavatar();//给Mynewfragment返回数据
                        }else {
                            faifure(error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(Mysetting.this, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;

                default:
                    break;
            }
        }

    };
    private void faifure(String error) {
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
    private void Retureavatar() {
        setResult(4);
    }
}
