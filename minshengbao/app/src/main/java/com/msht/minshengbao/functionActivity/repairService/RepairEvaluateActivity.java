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

import com.msht.minshengbao.ViewUI.Dialog.PublicNoticeDialog;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.RatingBar;
import com.msht.minshengbao.events.UpdateDataEvent;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/7/2  
 */
public class RepairEvaluateActivity extends BaseActivity {
    private RatingBar mRatingBar;
    private EditText  etEvaluation;
    private Button btnSendEva;
    private String orderNo;
    private String evalScore="3";
    private String userId,password;
    private String id, parentCategory;
    private String categoryDesc;
    private String type, finishTime;
    private String title, realAmount;
    private String sendType="0";
    private CustomDialog customDialog;
    private Context context;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<RepairEvaluateActivity> mWeakReference;
        public RequestHandler(RepairEvaluateActivity activity) {
            mWeakReference = new WeakReference<RepairEvaluateActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final RepairEvaluateActivity activity =mWeakReference.get();
            if (activity == null||activity.isFinishing()) {
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            activity.btnSendEva.setEnabled(true);
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.setResult(0x002);
                            EventBus.getDefault().post(new UpdateDataEvent(true));
                            activity.onEvaluateSuccess();    //提示评价成功
                        }else {
                            activity.onFailure(error);
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
    private void onEvaluateSuccess() {
        new PublicNoticeDialog(context).builder()
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .setLineViewVisibility(false)
                .setCloseImageVisibility(false)
                .setTitleText("民生宝")
                .setMessageContentText("您的评价已提交")
                .setButtonText("我知道了")
                .setOnPositiveClickListener(new PublicNoticeDialog.OnPositiveClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_evaluate);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        sendType =data.getStringExtra("sendType");
        id=data.getStringExtra("id");
        orderNo=data.getStringExtra("orderNo");
        type= data.getStringExtra("type");
        title=data.getStringExtra("title");
        categoryDesc=data.getStringExtra("categoryDesc");
        finishTime =data.getStringExtra("finishTime");
        parentCategory =data.getStringExtra("parentCategory");
        realAmount =data.getStringExtra("realAmount");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initFindViewId();
        initEvent();
    }
    private void initFindViewId() {
        ImageView typeImg =(ImageView)findViewById(R.id.id_img_type);
        TextView tvType =(TextView)findViewById(R.id.id_tv_type);
        TextView tvAmount =(TextView)findViewById(R.id.id_tv_amount);
        TextView tvTime =(TextView)findViewById(R.id.id_create_time);
        TextView tvTitle =(TextView)findViewById(R.id.id_tv_title);
        TextView tvOrderNo =(TextView)findViewById(R.id.id_orderNo);
        mRatingBar=(RatingBar)findViewById(R.id.id_ratingbar);
        etEvaluation =(EditText)findViewById(R.id.id_tv_evaluation);
        btnSendEva =(Button)findViewById(R.id.id_btn_send);
        if (sendType.equals(ConstantUtil.VALUE_THREE)){
            findViewById(R.id.id_layout_star).setVisibility(View.GONE);
            setCommonHeader("追加评价");
            etEvaluation.setHint("您当前还没有填写评论哦！");
        }else {
            setCommonHeader("维修评价");
            findViewById(R.id.id_layout_star).setVisibility(View.VISIBLE);
        }
        switch (type){
            case ConstantUtil.VALUE_ONE:
                typeImg.setImageResource(R.drawable.home_sanitary_xh);
                break;
            case ConstantUtil.VALUE_TWO:
                typeImg.setImageResource(R.drawable.home_appliance_fix_xh);
                break;
            case ConstantUtil.VALUE_THREE:
                typeImg.setImageResource(R.drawable.home_lanterns_xh);
                break;
            case ConstantUtil.VALUE_FOUR:
                typeImg.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.VALUE_FORTY_EIGHT:
                typeImg.setImageResource(R.drawable.home_appliance_clean_xh);
                break;
            default:
                typeImg.setImageResource(R.drawable.home_appliance_clean_xh);
                break;
        }
        tvOrderNo.setText(orderNo);
        tvTitle.setText(categoryDesc);
        tvType.setText(parentCategory);
        tvAmount.setText(realAmount);
        tvTime.setText(finishTime);
    }
    private void initEvent() {
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
                    public void onRatingChange(float ratingCount) {
                        int score=Math.round(ratingCount);
                        evalScore = Integer.toString(score);
                    }
                }
        );
        btnSendEva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                btnSendEva.setEnabled(false);
                requestService();
            }
        });
    }
    private void requestService() {
        String evalInfo= etEvaluation.getText().toString().trim();
        String  validateURL = UrlUtil.RepairOrder_EvalUrl;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password", password);
        textParams.put("id",id);
        textParams.put("type", sendType);
        textParams.put("evalScore",evalScore);
        textParams.put("evalInfo",evalInfo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
