package com.msht.minshengbao.FunctionView.Invoice;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.FunctionView.HtmlWeb.AgreeTreayt;
import com.msht.minshengbao.FunctionView.Public.SelectAddress;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.BitmapUtil;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StreamTools;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ApplyInvoice extends BaseActivity implements View.OnClickListener {
    private ImageView licenseimg;
    private LinearLayout Li_view;
    private LinearLayout Li_taxpayer;
    private LinearLayout Li_company;
    private LinearLayout Li_camara;
    private Button  btn_personal,btn_company;
    private Button btn_commen,btn_zengzhi,btn_send;
    private TextView  Eaddress;
    private EditText  et_name;
    private EditText  et_recipients,et_phone;
    private EditText  et_taxpayer_num,et_bank,et_bankcard;
    private EditText  et_company_tel,et_company_addr;
    private TextView  tv_title,tv_amount;
    private TextView  tv_rightText;
    private RelativeLayout Rdistrict;
    private String  userId,password,type="1";
    private String  name,content,amount;
    private String  recipients,phonenum,district;
    private String  address,identyfyNo,bank;
    private String  bankcard,company_tel,company_addr;
    private String  relate_order;
    private File certe_file=null;
    private boolean   BooleanType=false;
    private boolean   BooleaninVoice=false;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;
    private static  final int REQUEST_CALL_CAMERE=1;
    private CustomDialog customDialog;
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result_code=object.optString("result_code");
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if (Results.equals("success")) {
                            success();
                        } else {
                            noticeDialog(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(ApplyInvoice.this,"网络请求出错!",
                            Toast.LENGTH_SHORT)
                            .show();
                    break;
                case ERRORCODE:
                    customDialog.dismiss();
                    Toast.makeText(ApplyInvoice.this, "请求出错!",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void success() {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("您的发票已申请成功！")
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        setResult(1);
                        dialog.dismiss();
                        finish();

                    }
                }).show();
    }
    private void noticeDialog(String s) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_invoice);
        context=this;
        setCommonHeader("发票申请");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        Intent data=getIntent();
        relate_order=data.getStringExtra("idinvoice");
        amount=data.getStringExtra("total_amount");
        initHeader();
        initView();
        initEvent();
    }
    private void initHeader() {
        findViewById(R.id.id_status_view).setVisibility(View.GONE);//状态栏View
        tv_rightText=(TextView) findViewById(R.id.id_tv_rightText);
        tv_rightText.setVisibility(View.VISIBLE);
        tv_rightText.setText("说明");
    }
    private void initView() {
        licenseimg=(ImageView)findViewById(R.id.id_license_img);
        btn_commen=(Button)findViewById(R.id.id_btn_commen);
        btn_zengzhi=(Button)findViewById(R.id.id_btn_zengzhi);
        btn_send=(Button)findViewById(R.id.id_btn_send);
        btn_personal=(Button)findViewById(R.id.id_btn_personal);
        btn_company=(Button)findViewById(R.id.id_btn_company);
        Rdistrict=(RelativeLayout)findViewById(R.id.id_re_district);
        Li_view=(LinearLayout)findViewById(R.id.id_li_view);
        Li_taxpayer=(LinearLayout)findViewById(R.id.id_taxpayer_layout);
        Li_company=(LinearLayout)findViewById(R.id.id_company_layout);;
        Li_camara=(LinearLayout)findViewById(R.id.id_camara_layout);;
        Eaddress=(TextView) findViewById(R.id.id_tv_address);
        tv_title=(TextView) findViewById(R.id.id_tv_title);
        et_name=(EditText)findViewById(R.id.id_et_name);
        et_recipients=(EditText)findViewById(R.id.id_et_recipient);
        et_phone=(EditText)findViewById(R.id.id_et_phone);
        et_taxpayer_num=(EditText)findViewById(R.id.id_et_taxpayer_num);
        et_bank=(EditText)findViewById(R.id.id_et_bank);
        et_bankcard=(EditText)findViewById(R.id.id_et_bankcard);
        et_company_tel=(EditText)findViewById(R.id.id_et_company_tel);
        et_company_addr=(EditText)findViewById(R.id.id_et_company_addr);
        tv_amount=(TextView)findViewById(R.id.id_amount);
      //  Eaddress.setInputType(InputType.TYPE_NULL);
        tv_amount.setText(amount);
        btn_send.setEnabled(false);
    }
    private void initEvent() {
        tv_rightText.setOnClickListener(this);
        btn_commen.setOnClickListener(this);
        btn_zengzhi.setOnClickListener(this);
        btn_company.setOnClickListener(this);
        btn_personal.setOnClickListener(this);
        licenseimg.setOnClickListener(this);
        Rdistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SelectAddress.class);
                startActivityForResult(intent,1);
            }
        });
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(et_name.getText().toString())||TextUtils.isEmpty(et_recipients.getText().toString())
                        ||TextUtils.isEmpty(et_phone.getText().toString())||TextUtils.isEmpty(Eaddress.getText().toString())){
                    btn_send.setEnabled(false);
                }else {
                    btn_send.setEnabled(true);
                }

            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        et_recipients.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(et_name.getText().toString())||TextUtils.isEmpty(et_recipients.getText().toString())
                        ||TextUtils.isEmpty(et_phone.getText().toString())||TextUtils.isEmpty(Eaddress.getText().toString())
                        ){
                    btn_send.setEnabled(false);
                }else {
                    btn_send.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(et_name.getText().toString())||TextUtils.isEmpty(et_recipients.getText().toString())
                        ||TextUtils.isEmpty(et_phone.getText().toString())||TextUtils.isEmpty(Eaddress.getText().toString())
                        ){
                    btn_send.setEnabled(false);
                }else {
                    btn_send.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        Eaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(et_name.getText().toString())||TextUtils.isEmpty(et_recipients.getText().toString())
                        ||TextUtils.isEmpty(et_phone.getText().toString())||TextUtils.isEmpty(Eaddress.getText().toString())){
                    btn_send.setEnabled(false);
                }else {
                    btn_send.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        btn_send.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_goback:
                finish();
                break;
            case R.id.id_btn_commen:
                BooleaninVoice=false;
                btn_commen.setBackgroundResource(R.drawable.shape_orange_corner_button);
                btn_zengzhi.setBackgroundResource(R.drawable.shape_back_gray_corner_button);
                if (BooleanType){
                    type="3";
                    Li_view.setVisibility(View.VISIBLE);
                    Li_taxpayer.setVisibility(View.VISIBLE);
                    Li_company.setVisibility(View.GONE);
                    Li_camara.setVisibility(View.GONE);
                    et_bank.setHint("请输入您的开户银行（选填）");
                    et_bankcard.setHint("请输入您的开户账号（选填）");
                }else {
                    type="1";
                    Li_view.setVisibility(View.GONE);
                    Li_camara.setVisibility(View.GONE);
                    Li_taxpayer.setVisibility(View.GONE);
                }
                break;
            case R.id.id_btn_zengzhi:
                BooleaninVoice=true;
                type="2";
                btn_commen.setBackgroundResource(R.drawable.shape_back_gray_corner_button);
                btn_zengzhi.setBackgroundResource(R.drawable.shape_orange_corner_button);
                Li_view.setVisibility(View.VISIBLE);
                Li_camara.setVisibility(View.VISIBLE);
                Li_taxpayer.setVisibility(View.VISIBLE);
                Li_company.setVisibility(View.VISIBLE);
                Li_view.setVisibility(View.VISIBLE);
                et_bank.setHint("请输入您的开户银行（必填）");
                et_bankcard.setHint("请输入您的开户账号（必填）");
                break;
            case R.id.id_btn_personal:
                BooleanType=false;
                BooleaninVoice=false;
                type="1";
                btn_commen.setBackgroundResource(R.drawable.shape_orange_corner_button);
                btn_personal.setBackgroundResource(R.drawable.shape_orange_corner_button);
                btn_company.setBackgroundResource(R.drawable.shape_back_gray_corner_button);
                btn_zengzhi.setVisibility(View.GONE);
                Li_view.setVisibility(View.GONE);
                Li_camara.setVisibility(View.GONE);
                Li_taxpayer.setVisibility(View.GONE);
                break;
            case R.id.id_btn_company:
                BooleanType=true;
                if (BooleaninVoice){
                    type="2";
                    Li_view.setVisibility(View.VISIBLE);
                    Li_camara.setVisibility(View.VISIBLE);
                    Li_taxpayer.setVisibility(View.VISIBLE);
                    Li_company.setVisibility(View.VISIBLE);
                    Li_view.setVisibility(View.VISIBLE);
                    btn_zengzhi.setBackgroundResource(R.drawable.shape_orange_corner_button);
                    et_bank.setHint("请输入您的开户银行（必填）");
                    et_bankcard.setHint("请输入您的开户账号（必填）");
                }else {
                    type="3";
                    Li_view.setVisibility(View.VISIBLE);
                    Li_taxpayer.setVisibility(View.VISIBLE);
                    Li_company.setVisibility(View.GONE);
                    Li_camara.setVisibility(View.GONE);
                    btn_zengzhi.setBackgroundResource(R.drawable.shape_back_gray_corner_button);
                    et_bank.setHint("请输入您的开户银行（选填）");
                    et_bankcard.setHint("请输入您的开户账号（选填）");
                }
                btn_zengzhi.setVisibility(View.VISIBLE);
                btn_company.setBackgroundResource(R.drawable.shape_orange_corner_button);
                btn_personal.setBackgroundResource(R.drawable.shape_back_gray_corner_button);
                break;
            case R.id.id_license_img:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(ApplyInvoice.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ApplyInvoice.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CALL_CAMERE);
                    }else {
                        receivephoto();
                    }
                } else {
                    receivephoto();
                }
                break;
            case R.id.id_btn_send:
                applyData();
                break;
            case R.id.id_tv_rightText:
                String idNo="4";
                Intent intent=new Intent(this, AgreeTreayt.class);
                intent.putExtra("idNo",idNo);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    /*动态权限*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==REQUEST_CALL_CAMERE){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                receivephoto();
            }else {
                Toast.makeText(ApplyInvoice.this,"授权失败",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void receivephoto() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(true)
                .start(ApplyInvoice.this, PhotoPicker.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE)){
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                String filepath = photos.get(0);
                certe_file = new File(filepath);
                if (certe_file.exists()&&certe_file.canRead()) {
                    licenseimg.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(filepath, 500, 500));
                }
                compressImg(certe_file);
            }
        }else if (requestCode==1){
            if (resultCode==1){
                String mAddress=data.getStringExtra("mAddress");
                Eaddress.setText(mAddress);
            }
        }
    }
    private void compressImg(File files) {
        Luban.with(this)
                .load(files)                     //传人要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }
                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        certe_file=file;
                    }
                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过去出现问题时调用
                        Toast.makeText(ApplyInvoice.this,"图片压缩失败!",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }).launch();    //启动压缩
    }

    private void applyData() {
        name      =et_name.getText().toString().trim();
        content   =tv_title.getText().toString().trim();
        recipients=et_recipients.getText().toString().trim();
        phonenum  =et_phone.getText().toString().trim();
        district  =Eaddress.getText().toString().trim();
        address   =district;
        identyfyNo=et_taxpayer_num.getText().toString().trim();
        bank      =et_bank.getText().toString().trim();
        bankcard  =et_bankcard.getText().toString().trim();
        company_tel=et_company_tel.getText().toString().trim();
        company_addr=et_company_addr.getText().toString().trim();
        double double_amount=Double.valueOf(amount).doubleValue();
        if (type.equals("1")){
            requestServer(double_amount);
        }else if (type.equals("2")){
            if (TextUtils.isEmpty(et_taxpayer_num.getText().toString())){
                noticeDialog("请输入您的纳税人识别号");
            }else if (TextUtils.isEmpty(et_bank.getText().toString())){
                noticeDialog("请输入您的开户银行");
            }else if (TextUtils.isEmpty(et_bankcard.getText().toString())){
                noticeDialog("请输入您的开户账号");
            }else if (TextUtils.isEmpty(et_company_tel.getText().toString())){
                noticeDialog("请输入您的企业电话");
            }else if (TextUtils.isEmpty(et_company_addr.getText().toString())){
                noticeDialog("请输入您的企业地址");
            }else {
                requestServer(double_amount);
            }
        }else if (type.equals("3")){
            if (TextUtils.isEmpty(et_taxpayer_num.getText().toString())){
                noticeDialog("请输入您的纳税人识别号");
            }else {
                requestServer(double_amount);
            }
        }
    }

    private void requestServer(double double_amount) {
        if (double_amount<400){
            new PromptDialog.Builder(this)
                    .setTitle("民生宝")
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage("由于您的发票金额还未满400元，我们" +
                            "会通过顺丰到付的方式给您邮寄发票，" +
                            "签收时请将邮费会给快递小哥哦")
                    .setButton1("我再想想", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setButton2("我很确定", new PromptDialog.OnClickListener() {

                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                            customDialog.show();
                            Thread ApplyThread=new Thread(new ApplyInvoiceHandler());
                            ApplyThread.start();
                        }
                    })
                    .show();
        }else {
            customDialog.show();
            Thread ApplyThread=new Thread(new ApplyInvoiceHandler());
            ApplyThread.start();
        }
    }
    private class ApplyInvoiceHandler implements Runnable {
        @Override
        public void run() {
            String validateURL= UrlUtil.Invoice_applyUrl;
            HttpURLConnection conn = null;
            DataInputStream dis = null;
            Map<String, String> textParams = new HashMap<String, String>();
            Map<String,File> fileParams=new HashMap<String,File>();
            try {
                URL url = new URL(validateURL);
                textParams.put("userId", userId);
                textParams.put("password",password);
                textParams.put("type",type);
                textParams.put("name",name);
                textParams.put("title",content);
                textParams.put("amount",amount);
                textParams.put("recipient",recipients);
                textParams.put("phone",phonenum);
                textParams.put("address",address);
                textParams.put("taxpayer_num",identyfyNo);
                textParams.put("bank",bank);
                textParams.put("bankcard",bankcard);
                textParams.put("company_tel",company_tel);
                textParams.put("company_address",company_addr);
                textParams.put("relate_order",relate_order);
                if (certe_file!=null){
                    fileParams.put("business_license_img",certe_file);
                }
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setDoInput(true); // 发送POST请求必须设置允许输入
                conn.setDoOutput(true); // 发送POST请求必须设置允许输出
                conn.setUseCaches(false);//新加
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Charset", "UTF-8");//设置编码
                conn.setRequestProperty("ser-Agent", "Fiddler");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
                OutputStream os = conn.getOutputStream();
                DataOutputStream ds = new DataOutputStream(os);
                NetUtil.writeStringParams(textParams, ds);
                if (certe_file!=null){
                    NetUtil.writeFileParams(fileParams,ds);
                }
                NetUtil.paramsEnd(ds);
                os.flush();
                os.close();
                conn.connect();
                int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
                if (code==200) {
                    InputStream is=conn.getInputStream();
                    String result= StreamTools.readInputStream(is);
                    Message msg = new Message();
                    msg.obj = result;
                    msg.what = SUCCESS;
                    requestHandler.sendMessage(msg);
                }else {
                    Message msg = new Message();
                    msg.what = ERRORCODE;
                    requestHandler.sendMessage(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
                Message msg = new Message();
                msg.what = FAILURE;
                requestHandler.sendMessage(msg);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }
}
