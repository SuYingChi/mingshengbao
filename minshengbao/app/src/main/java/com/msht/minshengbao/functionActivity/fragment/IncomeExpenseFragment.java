package com.msht.minshengbao.functionActivity.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.adapter.IncomeAllAdapter;
import com.msht.minshengbao.Base.BaseFragment;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * @author hong
 */
public class IncomeExpenseFragment extends BaseFragment {
    private Activity mActivity;
    private View layoutNoData;
    private XListView mListView;
    private String  userId,password;
    private IncomeAllAdapter adapter;
    private JSONArray jsonArray;
    private int       pageNo    = 1;
    private String    type="0";
    private int pageIndex=0;
    private int refreshType;
    private CustomDialog customDialog;
    private final String mPageName = "收支明细";
    private final RequestHandler requestHandler=new RequestHandler(this);
    private ArrayList<HashMap<String, String>> incomeList = new ArrayList<HashMap<String, String>>();
    public static IncomeExpenseFragment getinstance(int position) {
        IncomeExpenseFragment incomExpense = new IncomeExpenseFragment();
        switch (position){
            case 0:
                incomExpense.type="0";
                break;
            case 1:
                incomExpense.type="1";
                break;
            case 2:
                incomExpense.type="2";
            default:
                break;
        }
        return incomExpense ;
    }
    public IncomeExpenseFragment() {/* Required empty public constructor*/}
 private static class RequestHandler extends Handler{
     private WeakReference<IncomeExpenseFragment> mWeakReference;
     public RequestHandler(IncomeExpenseFragment reference) {
         mWeakReference = new WeakReference<IncomeExpenseFragment>(reference);
     }
     @Override
     public void handleMessage(Message msg) {
         final IncomeExpenseFragment reference =mWeakReference.get();
         if (reference == null||reference.isDetached()) {
             return;
         }
         if (reference.customDialog!=null&&reference.customDialog.isShowing()){
             reference.customDialog.dismiss();
         }
         switch (msg.what) {
             case SendRequestUtil.SUCCESS:
                 try {
                     JSONObject object = new JSONObject(msg.obj.toString());
                     String result=object.optString("result");
                     String error = object.optString("error");
                     reference.jsonArray =object.optJSONArray("data");
                     if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                         if (reference.refreshType==0){
                             reference.mListView.stopRefresh(true);
                         }else if (reference.refreshType==1){
                             reference.mListView.stopLoadMore();
                         }
                         if(reference.jsonArray.length()>0){
                             if (reference.pageNo==1){
                                 reference.incomeList.clear();
                             }
                         }
                         reference.onReceiveData();
                     }else {
                         reference.mListView.stopRefresh(false);
                         reference.onShowNotify(error);
                     }
                 }catch (Exception e){
                     e.printStackTrace();
                 }
                 break;
             case SendRequestUtil.FAILURE:
                 reference.mListView.stopRefresh(false);
                 ToastUtil.ToastText(reference.mActivity,msg.obj.toString());
                 break;
             default:
                 break;
         }
         super.handleMessage(msg);
     }
 }
 private void onShowNotify(String string) {
        new PromptDialog.Builder(mActivity)
                .setTitle(R.string.my_dialog_title)
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(string)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }
    private void onReceiveData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id=jsonObject.optString("id");
                String type = jsonObject.getString("type");
                String content= jsonObject.getString("content");
                String amount = jsonObject.getString("amount");
                String time = jsonObject.getString("time");
                HashMap<String, String> map = new HashMap<String, String>(6);
                map.put("id", id);
                map.put("type", type);
                map.put("content", content);
                map.put("amount", amount);
                map.put("time",time);
                incomeList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (incomeList.size()==0){
            layoutNoData.setVisibility(View.VISIBLE);
        }else {
            mListView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public View initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_income_expense, null, false);
        customDialog=new CustomDialog(getActivity(), "正在加载");
        userId= SharedPreferencesUtil.getUserId(getActivity(), SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(getActivity(), SharedPreferencesUtil.Password,"");
        mActivity = getActivity();
        initMyView(mRootView);
        return mRootView;
    }

    private void initMyView(View mRootView) {
        layoutNoData =mRootView.findViewById(R.id.id_nodata_view);
        TextView mText=(TextView)mRootView.findViewById(R.id.id_tv_nodata);
        mText.setText("亲，您还没有收支数据");
        mListView=(XListView)mRootView.findViewById(R.id.id_income_view);
        mListView.setPullLoadEnable(true);
        adapter = new IncomeAllAdapter(getActivity(),incomeList);
        mListView.setAdapter(adapter);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
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
    @Override
    public void initData() {
        customDialog.show();
        loadData(1);
    }
    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String validateURL = UrlUtil.MyIncome_ExpenseUrl;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("page",pageNum);
        textParams.put("size","16");
        OkHttpRequestUtil.getInstance(mActivity.getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
}
