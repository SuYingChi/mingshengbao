package com.msht.minshengbao.functionActivity.GasService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.RatingBar;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 工单评价
 * @author hong
 * @date 2016/9/2  
 */
public class GasEvaluateWorkOrderActivity extends BaseActivity {
    private RatingBar mRatingBar;
    private EditText tvEvaluation;
    private Button btnSendEvaluation;
    private String evalScore="3";
    private String userId,password;
    private String id;
    private CustomDialog customDialog;
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<GasEvaluateWorkOrderActivity> mWeakReference;
        public RequestHandler(GasEvaluateWorkOrderActivity activity) {
            mWeakReference=new WeakReference<GasEvaluateWorkOrderActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final GasEvaluateWorkOrderActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.initShow();
                        }else {
                            activity.failure(error);
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
        }
    }
    private void failure(String error) {
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
    private void initShow() {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("评价成功")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        setResult(0x002);
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_evaluate_workorder);
        context=this;
        mPageName="燃气服务评价";
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
        Intent idNum=getIntent();
        id=idNum.getStringExtra("id");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initView();
        initEvent();
    }
    private void initView() {
        mRatingBar=(RatingBar)findViewById(R.id.id_ratingbar);
        tvEvaluation =(EditText)findViewById(R.id.id_tv_evaluation);
        btnSendEvaluation =(Button)findViewById(R.id.id_btn_send);
    }
    private void initEvent() {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRatingBar.setStarEmptyDrawable(getResources().getDrawable(R.drawable.star_empty));
        mRatingBar.setStarFillDrawable(getResources().getDrawable(R.drawable.star_full));
        mRatingBar.setStarCount(5);
        mRatingBar.setStar(3f);
        mRatingBar.halfStar(true);
        mRatingBar.setmClickable(true);
        mRatingBar.setStarImageWidth(120f);
        mRatingBar.setStarImageHeight(60f);
        mRatingBar.setImagePadding(35);
        mRatingBar.setOnRatingChangeListener(
                new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(float RatingCount) {
                        int score=Math.round(RatingCount);
                        evalScore = Integer.toString(score);
                    }
                }
        );
        btnSendEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.show();
                requestService();
            }
        });
    }
    private void requestService() {
        String evaluation= tvEvaluation.getText().toString().trim();
        String validateURL = UrlUtil.Evalute_workUrl;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        textParams.put("evalScore",evalScore);
        textParams.put("evaluation",evaluation);
        OkHttpRequestUtil.getInstance(context).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
