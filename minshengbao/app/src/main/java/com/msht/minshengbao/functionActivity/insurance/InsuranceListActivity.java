package com.msht.minshengbao.functionActivity.insurance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Model.AllServiceModel;
import com.msht.minshengbao.Model.InsuranceBusinessModel;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.adapter.InsuranceBusinessAdapter;
import com.msht.minshengbao.adapter.InvoiceHistoryAdapter;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeAdvBean;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.functionActivity.HtmlWeb.InsuranceHtmlActivity;

import java.util.ArrayList;
import java.util.HashMap;
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
                ToastUtil.ToastText(context,data.toString());
            }
        });
    }
    private void onAnalysisBusiness(String s) {
       // String M="{\"result_code\": 0, \"error\": null, \"result\": \"success\", \"data\": [{ \"id\": 1, \"s_index\": 1, \"type\": \"燃气险\", \"child\": [{ \"img\": \"https://msb.oss-cn-shenzhen.aliyuncs.com/msb_image/test/adImg/float_activity/20190103/null/58b2973f3aa94fb9ba9b1d0391bb1ee5.png\", \"amount\": 30.0, \"company\": \"太平洋洋保险\", \"id\": 3, \"title\": \"商业燃气险\", \"desc\": \"燃气安全无小事，燃气保险保平安\" },{ \"img\": \"https://msb.oss-cn-shenzhen.aliyuncs.com/msb_image/test/adImg/float_activity/20190103/null/58b2973f3aa94fb9ba9b1d0391bb1ee5.png\", \"amount\": 100.0, \"company\": \"北冰洋保险\", \"id\": 4, \"title\": \"商业燃气险\", \"desc\": \"燃气安全无小事，燃气保险保平安\" }]},{ \"s_index\": 2, \"id\": 2, \"type\": \"意外险\", \"child\": [{ \"img\": \"http://msb.oss-cn-shenzhen.aliyuncs.com/msb_image/test/adImg/float_activity/20181129/199/d7c89a2723fb4af2b12f3e74032912c9.jpg\", \"amount\": 0.0, \"company\": \"人寿保险\", \"id\": 5, \"title\": \"居民燃气险(意外险)\", \"desc\": \"燃气安全无小事，燃气保险保平安\" }]}]}";
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
