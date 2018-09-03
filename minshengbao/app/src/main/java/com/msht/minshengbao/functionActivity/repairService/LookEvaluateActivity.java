package com.msht.minshengbao.functionActivity.repairService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
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
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/7/4  
 */
public class LookEvaluateActivity extends BaseActivity {
    private RatingBar mRatingBar;
    private Button btnAdd;
    private View layoutAdd, layoutMaster;
    private TextView  tvEvaluation, tvMasterEva, userAddEva;
    private String    userId,password,orderId;
    private String    type, finishTime,orderNo;
    private String    title, realAmount, parentCategory;
    private String evaluateScore, evaluateInfo;
    private CustomDialog customDialog;
    private final RequestHandler requestHandle=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<LookEvaluateActivity> mWeakReference;
        private RequestHandler(LookEvaluateActivity activity) {
            mWeakReference = new WeakReference<LookEvaluateActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LookEvaluateActivity activity=mWeakReference.get();
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
                        JSONObject jsonObject=object.getJSONObject("data");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onReceiveData(jsonObject);
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
    private void onReceiveData(JSONObject jsonObject) {
        try{
            JSONObject objectUser=jsonObject.getJSONObject("user_eval");
            evaluateInfo =objectUser.optString("evaluateInfo");
            evaluateScore =objectUser.optString("evaluateScore");
            if (!TextUtils.isEmpty(evaluateInfo)){
                tvEvaluation.setText(evaluateInfo);
            }else {
                tvEvaluation.setHint("您当前没有评语");
            }
            switch (evaluateScore){
                case ConstantUtil.VALUE_ONE:
                    mRatingBar.setStar(1f);
                    break;
                case ConstantUtil.VALUE_TWO:
                    mRatingBar.setStar(2f);
                    break;
                case ConstantUtil.VALUE_THREE:
                    mRatingBar.setStar(3f);
                    break;
                case ConstantUtil.VALUE_FOUR:
                    mRatingBar.setStar(4f);
                    break;
                case ConstantUtil.VALUE_FIVE:
                     mRatingBar.setStar(5f);
                    break;
                    default:
                        break;
            }
            if (jsonObject.has("repairman_eval")){
                layoutMaster.setVisibility(View.VISIBLE);
                JSONObject objectMaster=jsonObject.getJSONObject("repairman_eval");
                String evaluateInfo=objectMaster.optString("evaluateInfo");
                tvMasterEva.setText(evaluateInfo);
            }else {
                layoutMaster.setVisibility(View.GONE);
            }
            if (jsonObject.has("user_add_eval")){
                btnAdd.setVisibility(View.GONE);
                layoutAdd.setVisibility(View.VISIBLE);
                JSONObject userAddEval=jsonObject.getJSONObject("user_add_eval");
                String userAddInfo=userAddEval.optString("evaluateInfo");
                userAddEva.setText(userAddInfo);
            }else {
                btnAdd.setVisibility(View.VISIBLE);
                layoutAdd.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onFailure(String error) {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_evaluate);
        setCommonHeader("查看评价");
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        orderNo=data.getStringExtra("orderNo");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        orderId=data.getStringExtra("id");
        type= data.getStringExtra("type");
        parentCategory =data.getStringExtra("parentCategory");
        title=data.getStringExtra("title");
        finishTime =data.getStringExtra("finishTime");
        realAmount =data.getStringExtra("realAmount");
        evaluateScore =data.getStringExtra("evaluateScore");
        evaluateInfo =data.getStringExtra("evaluateInfo");
        initfindViewById();
        initData();
    }
    private void initfindViewById() {
        ((TextView)findViewById(R.id.id_orderNo)).setText(orderNo);
        ((TextView)findViewById(R.id.id_tv_type)).setText(parentCategory);
        ((TextView)findViewById(R.id.id_tv_amount)).setText(realAmount);
        ((TextView)findViewById(R.id.id_create_time)).setText(finishTime);
        ((TextView)findViewById(R.id.id_tv_title)).setText(title);
        tvMasterEva =(TextView)findViewById(R.id.id_master_massage);
        userAddEva =(TextView)findViewById(R.id.id_again_massage);
        ImageView typeImg =(ImageView)findViewById(R.id.id_img_type);
        mRatingBar=(RatingBar)findViewById(R.id.id_ratingbar);
        tvEvaluation =(TextView) findViewById(R.id.id_tv_evaluation);
        btnAdd =(Button)findViewById(R.id.id_btn_add);
        layoutAdd =findViewById(R.id.id_li_add);
        layoutMaster =findViewById(R.id.id_li_master);
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
            case ConstantUtil.VALUE_THIRTY_SIX:
                typeImg.setImageResource(R.drawable.home_appliance_clean_xh);
                break;
            default:
                break;
        }
        mRatingBar.setStarEmptyDrawable(getResources().getDrawable(R.drawable.star_empty));
        mRatingBar.setStarFillDrawable(getResources().getDrawable(R.drawable.star_full));
        mRatingBar.setStarCount(5);
        mRatingBar.halfStar(true);
        mRatingBar.setStarImageWidth(120f);
        mRatingBar.setStarImageHeight(60f);
        mRatingBar.setImagePadding(35);
        mRatingBar.setmClickable(false);
        mRatingBar.setEnabled(false);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,RepairEvaluateActivity.class);
                intent.putExtra("send_type","3");
                intent.putExtra("id",orderId);
                intent.putExtra("orderNo",orderNo);
                intent.putExtra("type",type);
                intent.putExtra("title",title);
                intent.putExtra("parentCategory", parentCategory);
                intent.putExtra("finishTime", finishTime);
                intent.putExtra("realAmount", realAmount);
                startActivityForResult(intent,0x003);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0x003:
                if (resultCode==0x002){
                    initData();
                }
                break;
            default:
                break;
        }
    }
    private void initData() {
        customDialog.show();
        String validateURL =UrlUtil.LookEvalute_Url;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id", orderId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_GET,textParams,requestHandle);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
