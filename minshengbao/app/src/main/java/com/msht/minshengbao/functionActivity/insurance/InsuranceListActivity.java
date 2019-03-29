package com.msht.minshengbao.functionActivity.insurance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.Model.InsuranceBusinessModel;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.adapter.InsuranceBusinessAdapter;
import com.msht.minshengbao.functionActivity.htmlWeb.InsuranceHtmlActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/1/17 
 */
public class InsuranceListActivity extends BaseActivity {
    private XRecyclerView mXRecyclerView;
    private InsuranceBusinessAdapter mAdapter;
    private List<InsuranceBusinessModel.DataBean> dataList=new ArrayList<InsuranceBusinessModel.DataBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_list);
        context=this;
        setCommonHeader("保险业务");
        mXRecyclerView=(XRecyclerView)findViewById(R.id.id_business_list) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXRecyclerView.setLayoutManager(layoutManager);
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mXRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter=new InsuranceBusinessAdapter(dataList);
        mXRecyclerView.setAdapter(mAdapter);
        mXRecyclerView.refresh();
        mXRecyclerView.setPullRefreshEnabled(true);
        mXRecyclerView.setLoadingMoreEnabled(false);
        initInsuranceBusinessData();
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initInsuranceBusinessData();
            }
            @Override
            public void onLoadMore() { }
        });
        mAdapter.setOnItemClickListener(new InsuranceBusinessAdapter.OnItemMainClickListener() {
            @Override
            public void onItemClick(View view, int mainPosition, int secondPosition) {
                InsuranceBusinessModel.DataBean.ChildBean childBean=dataList.get(mainPosition).getChild().get(secondPosition);
                String id=String.valueOf(childBean.getId());
                String imageUrl=childBean.getImg();
                String desc=childBean.getDesc();
                String title=childBean.getTitle();
                Intent intent=new Intent(context,InsuranceHtmlActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("imageUrl",imageUrl);
                intent.putExtra("desc",desc);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
    }
    private void initInsuranceBusinessData() {
        String validateURL = UrlUtil.INSURANCE_BUSINESS_DATA;
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_GET, null, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                mXRecyclerView.refreshComplete();
                onAnalysisBusiness(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                mXRecyclerView.refreshComplete();
                ToastUtil.ToastText(context,data.toString());
            }
        });
    }
    private void onAnalysisBusiness(String s) {
        try {
            Gson gson = new Gson();
            InsuranceBusinessModel model = gson.fromJson(s, InsuranceBusinessModel.class);
            if (model.getResult().equals(SendRequestUtil.SUCCESS_VALUE)) {
                dataList.clear();
                dataList.addAll(model.getData());
                mAdapter.notifyDataSetChanged();
            }else {
                ToastUtil.ToastText(context,model.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
    }
}
