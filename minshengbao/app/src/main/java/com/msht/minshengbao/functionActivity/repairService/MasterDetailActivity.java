package com.msht.minshengbao.functionActivity.repairService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.MasterEvaluateAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.CircleImageView;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.custom.PullRefresh.LoadMoreListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/7/12  
 */
public class MasterDetailActivity extends BaseActivity {
    private RadioGroup radioEvaluate;
    private RadioButton radioAll, radioGood;
    private RadioButton radioMeddle, radioBad;
    private ImageView statusImg;
    private CircleImageView masterImg;
    private LoadMoreListView  moreListView;
    private MasterEvaluateAdapter mAdapter;
    private TextView tvMasterName;
    private TextView tvWorkNo, tvServeTimes, tvExperienceYear;
    private TextView tvTotalUser;
    private String masterId;
    private int   requestCode=0;
    private String type = "0";
    private int pageNo=1;
    private JSONObject jsonObject,json;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> goodList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private final EvaluateHandler evaluateHandler=new EvaluateHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<MasterDetailActivity> mWeakReference;
        public RequestHandler(MasterDetailActivity activity) {
            mWeakReference = new WeakReference<MasterDetailActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final MasterDetailActivity activity=mWeakReference.get();
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
                            if (activity.requestCode==0){
                                activity.jsonObject =object.optJSONObject("data");
                                activity.initShow();
                            }else if (activity.requestCode==1){
                                activity.json=object.optJSONObject("data");
                                activity.onShowCount();
                            }
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
    private static class EvaluateHandler extends Handler{
        private WeakReference<MasterDetailActivity> mWeakReference;
        private EvaluateHandler(MasterDetailActivity activity) {
            mWeakReference = new WeakReference<MasterDetailActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final MasterDetailActivity activity=mWeakReference.get();
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
                        activity.jsonArray =object.optJSONArray("data");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.pageNo==1){
                                activity.goodList.clear();
                            }
                            activity.initShowEvaluate();
                            if(activity.jsonArray.length()==0){
                                activity.moreListView.loadComplete(false);
                            }else {
                                activity.moreListView.loadComplete(true);
                            }
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
    private void initShowEvaluate() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String avatar = jsonObject.getString("avatar");
                String username= jsonObject.getString("username");
                String evalScore = jsonObject.getString("eval_score");
                String evalInfo =jsonObject.optString("eval_info");
                String time = jsonObject.getString("time");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("avatar", avatar);
                map.put("username", username);
                map.put("eval_score", evalScore);
                map.put("eval_info",evalInfo);
                map.put("time",time);
                goodList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }
    private void onFailure(String error) {
        new PromptDialog.Builder(context)
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
        intCount();
        String avatar= jsonObject.optString("avatar");
        String name= jsonObject.optString("name");
        String star= jsonObject.optString("star");
        String promiseFee= jsonObject.optString("promise_fee");
        String valid= jsonObject.optString("valid");
        String workNo= jsonObject.optString("no");
        String serveTimes= jsonObject.optString("serve_times");
        String experienceYear= jsonObject.optString("experience_year");
        String phone= jsonObject.optString("phone");
        String skill= jsonObject.optString("skill");
        tvMasterName.setText(name);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.potrait);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        Glide.with(context)
                .load(avatar)
                .apply(requestOptions).into(masterImg);
        tvWorkNo.setText(workNo);
        tvServeTimes.setText(serveTimes+"次");
        tvExperienceYear.setText(experienceYear);
    }
    private void intCount() {
        requestCode=1;
        requestService();
    }
    private void onShowCount() {
        String total=json.optString("total");
        String totalUser=json.optString("total_user");
        String perScore=json.optString("per_score");
        float  perScore1=json.optLong("per_score");
        String good=json.optString("good");
        String bad=json.optString("bad");
        String middle=json.optString("middle");
        tvTotalUser.setText(totalUser+"位用户");
        radioGood.setText("好评("+good+"）");
        radioMeddle.setText("中评("+middle+")");
        radioBad.setText("差评("+bad+")");
        if (perScore1<1.5){
            statusImg.setImageResource(R.drawable.star_one_h);
        }else if (perScore1>=1.5&&perScore1<2.0){
            statusImg.setImageResource(R.drawable.star_onehalf_h);
        }else if (perScore1>=2.0&&perScore1<2.5){
            statusImg.setImageResource(R.drawable.star_two_h);
        }else if (perScore1>=2.5&&perScore1<3.0){
            statusImg.setImageResource(R.drawable.star_twohalf_h);
        }else if (perScore1>=3.0&&perScore1<3.5){
            statusImg.setImageResource(R.drawable.star_three_h);
        }else if (perScore1>=3.5&&perScore1<4.0){
            statusImg.setImageResource(R.drawable.star_threehalf_h);
        }else if (perScore1>=4.0&&perScore1<4.5){
            statusImg.setImageResource(R.drawable.star_four_h);
        }else if (perScore1>=4.5&&perScore1<5.0){
            statusImg.setImageResource(R.drawable.star_fourhalf_h);
        }else if (perScore1==5.0){
            statusImg.setImageResource(R.drawable.star_five_h);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);
        setCommonHeader("师傅详情");
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        masterId=data.getStringExtra("masterId");
        initFindViewId();
        getMasterInfo();
        onGetEvaluation();
        initEvent();
        mAdapter=new MasterEvaluateAdapter(context,goodList);
        moreListView.setAdapter(mAdapter);
        moreListView.setLoadMoreListener(new ILoadMoreCallback() {
            @Override
            public void loadMore() {
                pageNo++;
                requestEvaluateInfo();
            }
        });
    }
    private void initFindViewId() {
        moreListView=(LoadMoreListView)findViewById(R.id.id_evalute_view);
        View layoutHeader =getLayoutInflater().inflate(R.layout.layout_master_detail_head,null);
        masterImg =(CircleImageView) layoutHeader.findViewById(R.id.id_img_master);
        statusImg =(ImageView) layoutHeader.findViewById(R.id.id_img_evalute);
        tvMasterName =(TextView) layoutHeader.findViewById(R.id.id_tv_master);
        tvWorkNo =(TextView) layoutHeader.findViewById(R.id.id_workno);
        tvServeTimes =(TextView) layoutHeader.findViewById(R.id.id_sever_times);
        tvExperienceYear =(TextView) layoutHeader.findViewById(R.id.id_experience_year);;
        tvTotalUser =(TextView) layoutHeader.findViewById(R.id.id_total_user);
        radioEvaluate = (RadioGroup) layoutHeader. findViewById(R.id.radio_evaluate);
        radioAll =(RadioButton) layoutHeader.findViewById(R.id.radio_button_all);
        radioGood =(RadioButton) layoutHeader.findViewById(R.id.radio_button_good);
        radioMeddle =(RadioButton) layoutHeader.findViewById(R.id.radio_button_middle);
        radioBad =(RadioButton) layoutHeader.findViewById(R.id.radio_button_bad);
        moreListView.addHeaderView(layoutHeader);
    }
    private void getMasterInfo() {
        customDialog.show();
        requestCode=0;
        requestService();
    }
    private void onGetEvaluation() {
        customDialog.show();
        pageNo=1;
        requestEvaluateInfo();
    }
    private void requestEvaluateInfo() {
        String validateURL = UrlUtil.Evalute_UrL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("id",masterId);
        textParams.put("type",type);
        textParams.put("size","18");
        textParams.put("page",pageNum);
        SendRequestUtil.postDataFromService(validateURL, textParams,evaluateHandler);
       // OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_GET,textParams,evaluateHandler);
    }
    private void requestService() {
        String validateURL ="";
        if (requestCode==0){
            validateURL = UrlUtil.MasterDetail_Url;
        }else if (requestCode==1){
            validateURL = UrlUtil.RepairMater_countUrl;
        }
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("id",masterId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_GET,textParams,requestHandler);
    }
    private void initEvent() {
        radioEvaluate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_all:
                        type = "0";
                        break;
                    case R.id.radio_button_good:
                        type = "1";
                        break;
                    case R.id.radio_button_middle:
                        type = "2";
                        break;
                    case R.id.radio_button_bad:
                        type = "3";
                        break;
                    default:
                        break;
                }
                onGetEvaluation();
            }
        });
    }
}
