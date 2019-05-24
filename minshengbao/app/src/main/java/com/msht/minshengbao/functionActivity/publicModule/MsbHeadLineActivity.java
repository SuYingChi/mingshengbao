package com.msht.minshengbao.functionActivity.publicModule;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.adapter.HeadLineAdapter;
import com.msht.minshengbao.functionActivity.htmlWeb.RichTextActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/11/14  
 */
public class MsbHeadLineActivity extends BaseActivity {

    private XRecyclerView mRecyclerView;
    private HeadLineAdapter mAdapter;
    private View layoutNoData;
    private int       pageNo    = 1;
    private int pageIndex=0;
    private int refreshType;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> headLineList = new ArrayList<HashMap<String, String>>();
    private final GetMsbHeadLineHandler headLineHandler=new GetMsbHeadLineHandler(this);
    private static class GetMsbHeadLineHandler extends Handler {
        private WeakReference<MsbHeadLineActivity> mWeakReference;
        public GetMsbHeadLineHandler(MsbHeadLineActivity activity) {
            mWeakReference = new WeakReference<MsbHeadLineActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final MsbHeadLineActivity activity =mWeakReference.get();
            // the referenced object has been cleared
            if (activity == null||activity.isFinishing()) {
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            if (activity.refreshType==0){
                activity.mRecyclerView.refreshComplete();
            }else if (activity.refreshType==1){
                activity.mRecyclerView.loadMoreComplete();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String code=object.optString("code");
                        JSONObject jsonObject=object.getJSONObject("data");
                        if (code.equals(ConstantUtil.VALUE_ZERO)){
                            boolean isEndPage=jsonObject.optBoolean("isEndPage");
                            JSONArray jsonArray=jsonObject.optJSONArray("data");
                            if(jsonArray.length()>0){
                                if (activity.pageNo==1){
                                    activity.headLineList .clear();
                                }
                            }
                            if (isEndPage){
                                activity.mRecyclerView.setLoadingMoreEnabled(false);
                            }else {
                                activity.mRecyclerView.setLoadingMoreEnabled(true);
                            }
                            activity.onReceiveHeadLineData(jsonArray);
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
    private void onReceiveHeadLineData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String title=jsonObject.optString("title");
                String url=jsonObject.optString("url");
                String source=jsonObject.optString("source");
                String publicTime=jsonObject.optString("publishTime");
                String pic=jsonObject.optString("pic");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("title",title);
                map.put("url",url);
                map.put("source",source);
                map.put("publishTime",publicTime);
                map.put("pic",pic);
                headLineList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        if (headLineList.size()>0){
            layoutNoData.setVisibility(View.GONE);
        }else {
            layoutNoData.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msb_head_line);
        context=this;
        mPageName="民生头条";
        customDialog=new CustomDialog(this, "正在加载");
        setCommonHeader(mPageName);
        mRecyclerView=(XRecyclerView)findViewById(R.id.id_headLine_list);
        layoutNoData=findViewById(R.id.id_noData_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter=new HeadLineAdapter(headLineList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        initGetHeadLineData();
        mAdapter.setClickCallBack(new HeadLineAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int position) {
                String id=headLineList.get(position).get("id");
                String title=headLineList.get(position).get("title");
                String pic=headLineList.get(position).get("pic");
                Intent intent=new Intent(context,RichTextActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("title",title);
                intent.putExtra("pic",pic);
                startActivity(intent);
            }
        });
    }
    private void initGetHeadLineData() {
        customDialog.show();
        loadData(1);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshType=0;
                loadData(1);
            }
            @Override
            public void onLoadMore() {
                refreshType=1;
                loadData(pageIndex + 1);
            }
        });
    }
    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String pageNum=String.valueOf(pageNo);
        String requestUrl=UrlUtil.MSB_APP_HEADLINE_LIST;
        try {
            requestUrl =requestUrl +"?pageNum="+ URLEncoder.encode(pageNum, "UTF-8")+"&pageSize="+URLEncoder.encode("38", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_GET,null,headLineHandler);
    }
}
