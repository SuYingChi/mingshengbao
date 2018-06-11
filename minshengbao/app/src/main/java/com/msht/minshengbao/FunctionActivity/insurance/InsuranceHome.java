package com.msht.minshengbao.FunctionActivity.insurance;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.CustomRadioGroup;
import com.msht.minshengbao.ViewUI.widget.MyRadioButton;

public class InsuranceHome extends BaseActivity implements CustomRadioGroup.IOnCheckChangedListener {
    private CustomRadioGroup radio_Group;
    private MyRadioButton    radio_one,radio_two;
    private MyRadioButton    radio_three,radio_four;
    private View     mFamilyhelp;
    private View     mDetailType;
    private Button   mButtonPay;
    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;
    private TextView mTextView5;
    private TextView mTextView6;
    private TextView msecuritys;
    private TextView mDeadlines;
    private String   vAmountNum="300.00";
    private String   vSecuritys="94.2万";
    private String   vSerialsId="1537981";
    private int   vDeadLines=5;
    private static  final int REQUEST_CALL_PHONE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_home);
        context=this;
        setCommonHeader("燃气保险");
        initView();
        initEvent();
    }
    private void initEvent() {
        radio_Group.setOnCheckedChangeListener(this);
        mDetailType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context,InsuranceDetail.class);
                detail.putExtra("id",vSerialsId);
                startActivity(detail);
            }
        });
        mButtonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context,InsurancePurchase.class);
                detail.putExtra("Amount",vAmountNum);
                detail.putExtra("insurance_Id",vSerialsId);
                detail.putExtra("vDeadLines",vDeadLines);
                detail.putExtra("vSecuritys",vSecuritys);
                startActivity(detail);
            }
        });
        findViewById(R.id.id_right_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallHotline();
            }
        });
    }
    private void initView() {
        mFamilyhelp=findViewById(R.id.id_familyHelp);
        mDetailType=findViewById(R.id.id_insurance_detail);
        mButtonPay=(Button)findViewById(R.id.id_mButtonpay);
        radio_Group = (CustomRadioGroup) findViewById(R.id.radiogroup);
        radio_one=(MyRadioButton)findViewById(R.id.radio_button_one);
        radio_two=(MyRadioButton)findViewById(R.id.radio_button_two);
        radio_three=(MyRadioButton)findViewById(R.id.radio_button_three);
        radio_four=(MyRadioButton)findViewById(R.id.radio_button_four);
        mTextView1=(TextView)findViewById(R.id.id_amount1);
        mTextView2=(TextView)findViewById(R.id.id_amount2);
        mTextView3=(TextView)findViewById(R.id.id_amount3);
        mTextView4=(TextView)findViewById(R.id.id_amount4);
        mTextView5=(TextView)findViewById(R.id.id_amount5);
        mTextView6=(TextView)findViewById(R.id.id_amount6);
        msecuritys=(TextView)findViewById(R.id.id_security);
        mDeadlines=(TextView)findViewById(R.id.id_deadline);
        DatafillWrite(vSerialsId);
    }
    private void DatafillWrite(String vSerialsId) {
        msecuritys.setText(vSecuritys+"元");
        mDeadlines.setText(vDeadLines+"年");
        if (vSerialsId.equals("1537981")){
            mTextView1.setText("250000元");
            mTextView2.setText("50000元");
            mTextView3.setText("300000元");
            mTextView4.setText("180000元");
            mTextView5.setText("12000元");
            mTextView6.setText("150000元");
        }else if (vSerialsId.equals("1534041")){
            mTextView1.setText("200000元");
            mTextView2.setText("50000元");
            mTextView3.setText("300000元");
            mTextView4.setText("200000元");
            mTextView5.setText("6000元");
            mTextView6.setText("100000元");
        }else if (vSerialsId.equals("1533994")){
            mTextView1.setText("250000元");
            mTextView2.setText("50000元");
            mTextView3.setText("400000元");
            mTextView4.setText("300000元");
            mTextView5.setText("6000元");
            mTextView6.setText("150000元");
        }else if (vSerialsId.equals("1537980")){
            mTextView1.setText("130000元");
            mTextView2.setText("20000元");
            mTextView3.setText("100000元");
            mTextView4.setText("50000元");
            mTextView5.setText("3600元");
            mTextView6.setText("0.00元");
        }
    }

    @Override
    public void onCheckedChanged(CustomRadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_button_one:
                radio_one.setBackgroundResource(R.drawable.radio_button_back_one);
                radio_two.setBackgroundResource(R.color.colorOrange_one);
                radio_three.setBackgroundResource(R.color.colorOrange_two);
                radio_four.setBackgroundResource(R.color.colorOrange_three);
                mFamilyhelp.setVisibility(View.VISIBLE);
                vAmountNum="300.00";
                vSecuritys="94.2万";
                vSerialsId="1537981";
                vDeadLines=5;
                break;
            case R.id.radio_button_two:
                radio_one.setBackgroundResource(R.color.colorOrange);
                radio_two.setBackgroundResource(R.drawable.radio_button_back_two);
                radio_three.setBackgroundResource(R.color.colorOrange_two);
                radio_four.setBackgroundResource(R.color.colorOrange_three);
                mFamilyhelp.setVisibility(View.VISIBLE);
                vAmountNum="200.00";
                vSecuritys="85.6万";
                vSerialsId="1534041";
                vDeadLines=3;
                break;
            case R.id.radio_button_three:
                radio_one.setBackgroundResource(R.color.colorOrange);
                radio_two.setBackgroundResource(R.color.colorOrange_one);
                radio_three.setBackgroundResource(R.drawable.radio_button_back_three);;
                radio_four.setBackgroundResource(R.color.colorOrange_three);
                mFamilyhelp.setVisibility(View.VISIBLE);
                vAmountNum="100.00";
                vSecuritys="115.6万元";
                vSerialsId="1533994";
                vDeadLines=1;
                break;
            case R.id.radio_button_four:
                radio_one.setBackgroundResource(R.color.colorOrange);
                radio_two.setBackgroundResource(R.color.colorOrange_one);
                radio_three.setBackgroundResource(R.color.colorOrange_two);;
                radio_four.setBackgroundResource(R.drawable.radio_button_back_four);
                mFamilyhelp.setVisibility(View.GONE);
                vAmountNum="30.00";
                vSecuritys="30.36万元";
                vSerialsId="1537980";
                vDeadLines=1;
                break;
            default:
                break;
        }
        DatafillWrite(vSerialsId);
    }

    private void CallHotline() {
        final String phone = "963666";
        new PromptDialog.Builder(this)
                .setTitle("客服电话")
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

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(InsuranceHome.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                            }
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + phone));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callIntent);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
    /*动态权限*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==REQUEST_CALL_PHONE){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                try{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "963666"));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);
                }catch (SecurityException e){
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(context,"授权失败",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
