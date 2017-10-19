package com.msht.minshengbao.FunctionView.fragmeht;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.MyWorkOrderAdapter;
import com.msht.minshengbao.Base.BaseFragment;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.repairService.MyorderworkDetail;
import com.msht.minshengbao.FunctionView.repairService.RepairEvaluate;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hong on 2017/3/14.
 */

public class OrderListFragment extends BaseFragment {
    private MyWorkOrderAdapter myWorkOrderAdapter;
    private XListView mListView;
    private View loyout_nodata;
    private int status =0;//默认未完成
    private String  userId,password;
    private int pageNo=0;
    private int pageIndex=0;
    private int refreshType;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private JSONArray jsonArray;   //数据解析
    private Activity mActivity;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    public static OrderListFragment getInstanse(int position){
        OrderListFragment orderListFragment = new OrderListFragment();
        orderListFragment.status=position;
        return orderListFragment;
    }
    @Override
    public View initView() {
        if(mRootView==null){
            mRootView= LayoutInflater.from(mContext).inflate(R.layout.fragment_orderlist,null,false);
            //view = inflater.inflate(R.layout.fragment_orderlist, container, false);
        }
        mActivity = getActivity();
        customDialog=new CustomDialog(getActivity(), "正在加载");
        userId= SharedPreferencesUtil.getUserId(getActivity(), SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(getActivity(), SharedPreferencesUtil.Password,"");
        initMyView(mRootView);
        return mRootView;
    }
    private void initMyView(View mRootView) {
        loyout_nodata=mRootView.findViewById(R.id.id_re_nodata);
        mListView=(XListView)mRootView.findViewById(R.id.id_order_view);
        mListView.setPullLoadEnable(true);
        myWorkOrderAdapter = new MyWorkOrderAdapter(getContext(),orderList);
        mListView.setAdapter(myWorkOrderAdapter);
        myWorkOrderAdapter.SetOnItemSelectListener(new MyWorkOrderAdapter.OnItemSelectListener() {
            @Override
            public void ItemSelectClick(View view, int thisposition) {
               // int position=thisposition-1;
                String orderId = orderList.get(thisposition).get("id");
                String orderNo=orderList.get(thisposition).get("orderNo");
                String type=orderList.get(thisposition).get("type");
                String title=orderList.get(thisposition).get("title");
                String finish_time=orderList.get(thisposition).get("time");
                String parent_cateogry=orderList.get(thisposition).get("parent_category_name");
                String amount=orderList.get(thisposition).get("amount");
                Intent servece = new Intent(getActivity(), RepairEvaluate.class);
                servece.putExtra("send_type","1");
                servece.putExtra("id",orderId);
                servece.putExtra("orderNo",orderNo);
                servece.putExtra("type",type);
                servece.putExtra("title",title);
                servece.putExtra("parent_category",parent_cateogry);
                servece.putExtra("finish_time",finish_time);
                servece.putExtra("real_amount",amount);
                startActivityForResult(servece, 2);

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==0x001||resultCode == 0x002 || resultCode == 0x003||resultCode==0x004||resultCode==0x005||resultCode==0x006) {
            orderList.clear();
            myWorkOrderAdapter.notifyDataSetChanged();
            loadData(1);
        }
    }
    @Override
    public void initData() {
        customDialog.show();
        orderList.clear();
        loadData(1);
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
    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String validateURL = UrlUtil.maintainservise_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        String statuses=String.valueOf(status);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("status",statuses);
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
    Handler requestHandler = new Handler() {
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
                            if (refreshType==0){
                                mListView.stopRefresh(true);
                            }else if (refreshType==1){
                                mListView.stopLoadMore();
                            }
                            if(jsonArray.length()>0){
                                if (pageNo==1){
                                    orderList.clear();
                                }
                            }
                            initShow();
                        }else {
                            mListView.stopRefresh(false);
                            faifure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    mListView.stopRefresh(false);
                    Toast.makeText(getActivity(), msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    private void faifure(String error) {
        new PromptDialog.Builder(getActivity())
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
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id=jsonObject.optString("id");
                String cid=jsonObject.optString("cid");
                String parent_category_name=jsonObject.optString("parent_category_name");
                String category_name=jsonObject.optString("category_name");
                String category_code=jsonObject.optString("category_code");
                String orderNo=jsonObject.optString("orderNo");
                String title = jsonObject.getString("title");
                String type = jsonObject.getString("type");
                String status = jsonObject.getString("status");
                String statusDesc=jsonObject.optString("statusDesc");
                String amount=jsonObject.optString("amount");
                String time = jsonObject.getString("time");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("orderNo",orderNo);
                map.put("cid",cid);
                map.put("parent_category_name",parent_category_name);
                map.put("category_name",category_name);
                map.put("category_code",category_code);
                map.put("type", type);
                map.put("title", title);
                map.put("status", status);
                map.put("statusDesc", statusDesc);
                map.put("amount",amount);
                map.put("time",time);
                orderList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (orderList.size()==0){
            loyout_nodata.setVisibility(View.VISIBLE);
        }else {
            loyout_nodata.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            myWorkOrderAdapter.notifyDataSetChanged();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int positions=position-1;
                    int pos = positions;
                    String cid=orderList.get(positions).get("cid");
                    String ids = orderList.get(positions).get("id");
                    Intent servece = new Intent(getActivity(), MyorderworkDetail.class);
                    servece.putExtra("cid",cid);
                    servece.putExtra("id", ids);
                    servece.putExtra("pos", pos);
                    startActivityForResult(servece, 1);
                }
            });
        }

    }
}
