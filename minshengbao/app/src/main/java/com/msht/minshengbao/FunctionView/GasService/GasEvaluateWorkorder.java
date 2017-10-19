package com.msht.minshengbao.FunctionView.GasService;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.RatingBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GasEvaluateWorkorder extends BaseActivity {
    private RatingBar mRatingBar;
    private EditText  tv_evaluation;
    private Button    btn_sendeva;
    private String evalScore="3";
    private String evaluation;
    private String userId,password;
    private String id;
    private final int  SUCCESS = 1;
    private final int  FAILURE = 0;
    private CustomDialog customDialog;
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            initShow();
                        }else {
                            failure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };
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
        setCommonHeader("燃气服务评价");
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
        tv_evaluation=(EditText)findViewById(R.id.id_tv_evaluation);
        btn_sendeva=(Button)findViewById(R.id.id_btn_send);
    }
    private void initEvent() {
        backimg.setOnClickListener(new View.OnClickListener() {
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
                        int Score=Math.round(RatingCount);
                        evalScore = Integer.toString(Score);
                    }
                }
        );
        btn_sendeva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.show();
                requestSevice();
            }
        });
    }
    private void requestSevice() {
        evaluation=tv_evaluation.getText().toString().trim();
        String validateURL = UrlUtil.Evalute_workUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        textParams.put("evalScore",evalScore);
        textParams.put("evaluation",evaluation);
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener(){
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                requestHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                requestHandler.sendMessage(msg);
            }
        });
    }
}
