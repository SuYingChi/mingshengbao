package com.msht.minshengbao.FunctionView.repairService;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.msht.minshengbao.Adapter.MasterEvaluteAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.CircleImageView;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.ViewUI.PullRefresh.LoadMoreListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MasterDetail extends BaseActivity {
    private View layout_header;
    private RadioGroup radio_evaluate;
    private RadioButton radio_all,radio_good;
    private RadioButton radio_meddle,radio_bad;
    private ImageView statusimg;
    private CircleImageView masterimg;
    private LoadMoreListView  moreListView;
    private MasterEvaluteAdapter mAdapter;
    private TextView tv_mastername;
    private TextView tv_workno,tv_serve_times,tv_experience_year;
    private TextView tv_totaluser;
    private String masterId;
    private int   requestCode=0;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private String type = "0";//默认为0全部评价
    private int pageNo=1;//当前页数
    private String size = "18";//每页加载的大小
    private JSONObject jsonbject,json;   //数据解析
    private JSONArray jsonArray;   //数据解析
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> goodList = new ArrayList<HashMap<String, String>>();
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
                            if (requestCode==0){
                                jsonbject =object.optJSONObject("data");
                                initShow();
                            }else if (requestCode==1){
                                json=object.optJSONObject("data");
                                showCount();
                            }
                        }else {
                            faifure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(MasterDetail.this, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };
    Handler evaluteHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {

                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonArray =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            if (pageNo==1){
                                goodList.clear();
                            }
                            initShowEvalute();
                            if(jsonArray.length()==0){
                                 moreListView.loadComplete(false);
                            }else {
                                moreListView.loadComplete(true);
                            }
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
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void initShowEvalute() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String avatar = jsonObject.getString("avatar");
                String username= jsonObject.getString("username");
                String eval_score = jsonObject.getString("eval_score");
                String eval_info =jsonObject.optString("eval_info");
                String time = jsonObject.getString("time");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("avatar", avatar);
                map.put("username", username);
                map.put("eval_score", eval_score);
                map.put("eval_info",eval_info);
                map.put("time",time);
                goodList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }
    private void faifure(String error) {
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
        String avatar=jsonbject.optString("avatar");
        String name=jsonbject.optString("name");
        String star=jsonbject.optString("star");
        String promise_fee=jsonbject.optString("promise_fee");
        String valid=jsonbject.optString("valid");
        String workno=jsonbject.optString("no");
        String serve_times=jsonbject.optString("serve_times");
        String experience_year=jsonbject.optString("experience_year");
        String phone=jsonbject.optString("phone");
        String skill=jsonbject.optString("skill");
        tv_mastername.setText(name);
        Glide
                .with(this)
                .load(avatar)
                .error(R.drawable.potrait)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//deactivate the disk cache for a request.
                .skipMemoryCache(true)//glide will not put image in the memory cache
                .into(masterimg);
        tv_workno.setText(workno);
        tv_serve_times.setText(serve_times+"次");
        tv_experience_year.setText(experience_year);
    }
    private void intCount() {
        requestCode=1;
        requesService();
    }
    private void showCount() {
        String total=json.optString("total");
        String total_user=json.optString("total_user");
        String per_scores=json.optString("per_score");
        float  per_score=json.optLong("per_score");
        String good=json.optString("good");
        String bad=json.optString("bad");
        String middle=json.optString("middle");
        tv_totaluser.setText(total_user+"位用户");
        radio_good.setText("好评("+good+"）");
        radio_meddle.setText("中评("+middle+")");
        radio_bad.setText("差评("+bad+")");
        if (per_score<1.5){
            statusimg.setImageResource(R.drawable.star_one_h);
        }else if (per_score>=1.5&&per_score<2.0){
            statusimg.setImageResource(R.drawable.star_onehalf_h);
        }else if (per_score>=2.0&&per_score<2.5){
            statusimg.setImageResource(R.drawable.star_two_h);
        }else if (per_score>=2.5&&per_score<3.0){
            statusimg.setImageResource(R.drawable.star_twohalf_h);
        }else if (per_score>=3.0&&per_score<3.5){
            statusimg.setImageResource(R.drawable.star_three_h);
        }else if (per_score>=3.5&&per_score<4.0){
            statusimg.setImageResource(R.drawable.star_threehalf_h);
        }else if (per_score>=4.0&&per_score<4.5){
            statusimg.setImageResource(R.drawable.star_four_h);
        }else if (per_score>=4.5&&per_score<5.0){
            statusimg.setImageResource(R.drawable.star_fourhalf_h);
        }else if (per_score==5.0){
            statusimg.setImageResource(R.drawable.star_five_h);
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
        initfindViewById();
        getmasterinfo();
        getevalution();
        initEvent();
        mAdapter=new MasterEvaluteAdapter(context,goodList);
        moreListView.setAdapter(mAdapter);
        moreListView.setLoadMoreListener(new ILoadMoreCallback() {
            @Override
            public void loadMore() {
                pageNo++;
                requestEvaluteInfo();
            }
        });
    }
    private void initfindViewById() {
        moreListView=(LoadMoreListView)findViewById(R.id.id_evalute_view);
        layout_header=getLayoutInflater().inflate(R.layout.layout_master_detail_head,null);
        masterimg=(CircleImageView)layout_header.findViewById(R.id.id_img_master);
        statusimg=(ImageView)layout_header.findViewById(R.id.id_img_evalute);
        tv_mastername=(TextView)layout_header.findViewById(R.id.id_tv_master);
        tv_workno=(TextView)layout_header.findViewById(R.id.id_workno);
        tv_serve_times=(TextView)layout_header.findViewById(R.id.id_sever_times);
        tv_experience_year=(TextView)layout_header.findViewById(R.id.id_experience_year);;
        tv_totaluser=(TextView)layout_header.findViewById(R.id.id_total_user);
        radio_evaluate = (RadioGroup)layout_header. findViewById(R.id.radio_evaluate);
        radio_all=(RadioButton)layout_header.findViewById(R.id.radio_button_all);
        radio_good=(RadioButton)layout_header.findViewById(R.id.radio_button_good);
        radio_meddle=(RadioButton)layout_header.findViewById(R.id.radio_button_middle);
        radio_bad=(RadioButton)layout_header.findViewById(R.id.radio_button_bad);
        moreListView.addHeaderView(layout_header);
    }
    private void getmasterinfo() {
        customDialog.show();
        requestCode=0;
        requesService();
    }
    private void getevalution() {
        customDialog.show();
        pageNo=1;
        requestEvaluteInfo();
    }
    private void requestEvaluteInfo() {
        String validateURL = UrlUtil.Evalute_UrL;
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("Id",masterId);
        textParams.put("type",type);
        textParams.put("size",size);
        textParams.put("page",pageNum);
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                evaluteHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                evaluteHandler.sendMessage(msg);
            }
        });
    }
    private void requesService() {
        String validateURL ="";
        if (requestCode==0){
            validateURL = UrlUtil.MasterDetail_Url;
        }else if (requestCode==1){
            validateURL = UrlUtil.RepairMater_countUrl;
        }
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("Id",masterId);
        HttpUrlconnectionUtil.executepostTwo(validateURL,textParams, new ResultListener() {
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
    private void initEvent() {
        radio_evaluate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                }
                getevalution();
            }
        });
    }
}
