package com.msht.minshengbao.FunctionView.fragmeht;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.IncomeAllAdapter;
import com.msht.minshengbao.Base.BaseFragment;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomExpenseFragment extends BaseFragment {
    private Activity mActivity;
    private View layout_nodata;
    private XListView mListView;
    private TextView mText;
    private String  userId,password;
    private IncomeAllAdapter adapter;
    private final int SUCCESS   = 1;
    private final int FAILURE   = 0;
    private JSONArray jsonArray;   //数据解析
    private int       pageNo    = 1;
    private String    type="0";
    private int pageIndex=0;
    private int refreshType;
    private CustomDialog customDialog;
    private final String mPageName = "收支明细";
    private ArrayList<HashMap<String, String>> incomeList = new ArrayList<HashMap<String, String>>();
    public static IncomExpenseFragment getinstance(int position) {
        IncomExpenseFragment incomExpense = new IncomExpenseFragment();
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
    public IncomExpenseFragment() {
        // Required empty public constructor
    }

    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonArray =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            Log.d("记录=",msg.obj.toString());
                            if (refreshType==0){
                                mListView.stopRefresh(true);
                            }else if (refreshType==1){
                                mListView.stopLoadMore();
                            }
                            if(jsonArray.length()>0){
                                if (pageNo==1){
                                    incomeList.clear();
                                }
                            }
                            initShow();
                        }else {
                            mListView.stopRefresh(false);
                            showNotify(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    mListView.stopRefresh(false);
                    Toast.makeText(getActivity(), msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void showNotify(String string) {
        new PromptDialog.Builder(getActivity())
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
    private void initShow() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id=jsonObject.optString("id");
                String type = jsonObject.getString("type");
                String content= jsonObject.getString("content");
                String amount = jsonObject.getString("amount");
                String time = jsonObject.getString("time");
                HashMap<String, String> map = new HashMap<String, String>();
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
            layout_nodata.setVisibility(View.VISIBLE);
        }else {
            mListView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public View initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_income_expense, null, false);
        userId= SharedPreferencesUtil.getUserId(getActivity(), SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(getActivity(), SharedPreferencesUtil.Password,"");
        mActivity = getActivity();
        initMyView(mRootView);
        return mRootView;
    }

    private void initMyView(View mRootView) {
        layout_nodata=mRootView.findViewById(R.id.id_nodata_view);
        mText=(TextView)mRootView.findViewById(R.id.id_tv_nodata);
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
        loadData(1);
    }
    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String validateURL = UrlUtil.MyIncome_ExpenseUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("page",pageNum);
        textParams.put("size","16");
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener() {
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
