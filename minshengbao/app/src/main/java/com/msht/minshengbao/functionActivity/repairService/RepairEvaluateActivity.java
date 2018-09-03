package com.msht.minshengbao.functionActivity.repairService;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.RatingBar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RepairEvaluate extends BaseActivity {
    private ImageView  typeimg;
    private RatingBar  mRatingBar;
    private EditText   et_evaluation;
    private Button     btn_sendeva;
    private TextView   tv_type,tv_amount,tv_tmie;
    private TextView   tv_title,tv_orderNo;
    private String     orderNo;
    private String     evalScore="0";   
    private String     userId,password;
    private String     id,parent_category;
    private String     type,finish_time;
    private String     title,real_amount;
    private String     send_type;
    private final int  SUCCESS = 1;
    private final int  FAILURE = 0;
    private CustomDialog customDialog;
    private Context context;
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
                            initShow();    //提示评价成功
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
    private void initShow() {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("评价成功")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        setResult(2);
                        dialog.dismiss();
                        finish();
                    }
                }).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_evaluate);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        send_type=data.getStringExtra("send_type");
        id=data.getStringExtra("id");
        orderNo=data.getStringExtra("orderNo");
        type= data.getStringExtra("type");
        title=data.getStringExtra("title");
        finish_time=data.getStringExtra("finish_time");
        parent_category=data.getStringExtra("parent_category");
        real_amount=data.getStringExtra("real_amount");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initfindViewById();
        initEvent();
    }
    private void initfindViewById() {
        typeimg=(ImageView)findViewById(R.id.id_img_type);
        tv_type=(TextView)findViewById(R.id.id_tv_type);
        tv_amount=(TextView)findViewById(R.id.id_tv_amount);
        tv_tmie=(TextView)findViewById(R.id.id_create_time);
        tv_title=(TextView)findViewById(R.id.id_tv_title);
        tv_orderNo=(TextView)findViewById(R.id.id_orderNo);
        mRatingBar=(RatingBar)findViewById(R.id.id_ratingbar);
        et_evaluation=(EditText)findViewById(R.id.id_tv_evaluation);
        btn_sendeva=(Button)findViewById(R.id.id_btn_send);
        if (send_type.equals("3")){
            findViewById(R.id.id_layout_star).setVisibility(View.GONE);
            setCommonHeader("追加评价");
            et_evaluation.setHint("您当前还没有填写评论哦！");
        }else {
            setCommonHeader("维修评价");
            findViewById(R.id.id_layout_star).setVisibility(View.VISIBLE);
        }
        if (type.equals("1")){
            typeimg.setImageResource(R.drawable.home_sanitary_xh);
        }else if (type.equals("2")){
            typeimg.setImageResource(R.drawable.home_appliance_fix_xh);
        }else if (type.equals("3")){
            typeimg.setImageResource(R.drawable.home_lanterns_xh);
        }else if (type.equals("4")){
            typeimg.setImageResource(R.drawable.home_otherfix_xh);
        }else if (type.equals("36")){
            typeimg.setImageResource(R.drawable.home_appliance_clean_xh);
        }
        tv_orderNo.setText(orderNo);
        tv_title.setText(title);
        tv_type.setText(parent_category);
        tv_amount.setText(real_amount);
        tv_tmie.setText(finish_time);
    }
    private void initEvent() {
        mRatingBar.setStarEmptyDrawable(getResources().getDrawable(R.drawable.star_empty));
        mRatingBar.setStarFillDrawable(getResources().getDrawable(R.drawable.star_full));
        mRatingBar.setStarCount(5);
        mRatingBar.setStar(0f);
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
                customDialog.dismiss();
                requestSevice();
            }
        });
    }
    private void requestSevice() {
        String evalInfo=et_evaluation.getText().toString().trim();
        String  validateURL = UrlUtil.RepairOrder_EvalUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password", password);
        textParams.put("id",id);
        textParams.put("type",send_type);
        textParams.put("evalScore",evalScore);
        textParams.put("evalInfo",evalInfo);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener(){
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
