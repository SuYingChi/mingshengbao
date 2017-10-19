package com.msht.minshengbao.FunctionView.repairService;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
public class LookEvaluate extends BaseActivity {
    private ImageView typeimg;
    private RatingBar mRatingBar;
    private Button    btn_add;
    private View layout_add,layout_master;
    private TextView  tv_evaluation,tv_master_eva,user_add_eva;
    private TextView  tv_naviga;
    private String    userId,password,orderId;
    private String    type,finish_time,orderNo;
    private String    title,real_amount,parent_category;
    private String    evaluate_score,evaluate_info;
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
                        JSONObject jsonObject=object.getJSONObject("data");
                        if(Results.equals("success")) {
                            initShow(jsonObject);
                        }else {
                            faifure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
    private void initShow(JSONObject jsonObject) {
        try{
            JSONObject object_user=jsonObject.getJSONObject("user_eval");
            evaluate_info=object_user.optString("evaluate_info");
            evaluate_score=object_user.optString("evaluate_score");
            if (!evaluate_info.equals("")){
                tv_evaluation.setText(evaluate_info);
            }else {
                tv_evaluation.setHint("您当前没有评语");
            }
            if (evaluate_score.equals("1")){
                mRatingBar.setStar(1f);
            }else if (evaluate_score.equals("2")){
                mRatingBar.setStar(2f);
            }else if (evaluate_score.equals("3")){
                mRatingBar.setStar(3f);
            }else if (evaluate_score.equals("4")){
                mRatingBar.setStar(4f);
            }else if (evaluate_score.equals("5")){
                mRatingBar.setStar(5f);
            }
            if (jsonObject.has("repairman_eval")){
                layout_master.setVisibility(View.VISIBLE);
                JSONObject object_master=jsonObject.getJSONObject("repairman_eval");
                String master_info=object_master.optString("evaluate_info");
                tv_master_eva.setText(master_info);
            }else {
                layout_master.setVisibility(View.GONE);
            }
            if (jsonObject.has("user_add_eval")){
                btn_add.setVisibility(View.GONE);
                layout_add.setVisibility(View.VISIBLE);
                JSONObject object_add=jsonObject.getJSONObject("user_add_eval");
                String user_add_info=object_add.optString("evaluate_info");
                user_add_eva.setText(user_add_info);
            }else {
                btn_add.setVisibility(View.VISIBLE);
                layout_add.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void faifure(String error) {
        new PromptDialog.Builder(this)
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
        parent_category=data.getStringExtra("parent_category");
        title=data.getStringExtra("title");
        finish_time=data.getStringExtra("finish_time");
        real_amount=data.getStringExtra("real_amount");
        evaluate_score=data.getStringExtra("evaluate_score");
        evaluate_info=data.getStringExtra("evaluate_info");
        initfindViewById();
        initData();
    }
    private void initfindViewById() {
        ((TextView)findViewById(R.id.id_orderNo)).setText(orderNo);
        ((TextView)findViewById(R.id.id_tv_type)).setText(parent_category);
        ((TextView)findViewById(R.id.id_tv_amount)).setText(real_amount);
        ((TextView)findViewById(R.id.id_create_time)).setText(finish_time);
        ((TextView)findViewById(R.id.id_tv_title)).setText(title);
        tv_master_eva=(TextView)findViewById(R.id.id_master_massage);
        user_add_eva=(TextView)findViewById(R.id.id_again_massage);
        typeimg=(ImageView)findViewById(R.id.id_img_type);
        mRatingBar=(RatingBar)findViewById(R.id.id_ratingbar);
        tv_evaluation=(TextView) findViewById(R.id.id_tv_evaluation);
        btn_add=(Button)findViewById(R.id.id_btn_add);
        layout_add=findViewById(R.id.id_li_add);
        layout_master=findViewById(R.id.id_li_master);
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
        mRatingBar.setStarEmptyDrawable(getResources().getDrawable(R.drawable.star_empty));
        mRatingBar.setStarFillDrawable(getResources().getDrawable(R.drawable.star_full));
        mRatingBar.setStarCount(5);
        mRatingBar.halfStar(true);
        mRatingBar.setStarImageWidth(120f);
        mRatingBar.setStarImageHeight(60f);
        mRatingBar.setImagePadding(35);
        mRatingBar.setmClickable(false);
        mRatingBar.setEnabled(false);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,RepairEvaluate.class);
                intent.putExtra("send_type","3");
                intent.putExtra("id",orderId);
                intent.putExtra("orderNo",orderNo);
                intent.putExtra("type",type);
                intent.putExtra("title",title);
                intent.putExtra("parent_category",parent_category);
                intent.putExtra("finish_time",finish_time);
                intent.putExtra("real_amount",real_amount);
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
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id", orderId);
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
