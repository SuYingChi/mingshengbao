package com.msht.minshengbao.functionActivity.LPGActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.adapter.MyBottleAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Bean.MenuItem;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.CallPhoneUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.ButtonUI.MenuItemM;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.ViewUI.PullRefresh.LoadMoreListView;
import com.msht.minshengbao.ViewUI.widget.TopRightMenu;
import com.msht.minshengbao.functionActivity.Public.QRCodeScanActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Demo class
 * 我的账户
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class LpgMyAccountActivity extends BaseActivity  {
    private TextView tvPhone;
    private TextView tvAddress;
    private String   userName;
    private String   siteId;
    private int pageIndex=0;
    private LoadMoreListView moreListView;
    private ImageView rightImage;
    private MyBottleAdapter myBottleAdapter;
    private TextView tvBottleCount;
    private static final String PAGE_NAME="我的账户(lpg)";
    private  ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private static final int LIST_SIZE=3;
    private static final int BINDING_SUCCESS_CODE=1;
    private static final int SWITCH_SUCCESS_CODE=2;
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    private CustomDialog customDialog;

    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<LpgMyAccountActivity> mWeakReference;
        public RequestHandler(LpgMyAccountActivity activity) {
            mWeakReference = new WeakReference<LpgMyAccountActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgMyAccountActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("msg");
                        JSONObject dataObject=object.optJSONObject("data");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            activity.onReceiveData(dataObject);
                        }else {
                            boolean isRegister=object.optBoolean("isRegister");
                            if (!isRegister){
                                activity.moreListView.setVisibility(View.VISIBLE);
                                activity.goBindingAccount();
                            }else {
                                activity.moreListView.setVisibility(View.GONE);
                                activity.onFailure(error);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    activity.moreListView.setVisibility(View.GONE);
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onReceiveData(JSONObject object) {
        boolean isRegister=object.optBoolean("isRegister");
        if (isRegister){
            moreListView.setVisibility(View.VISIBLE);
            String userId =object.optString("userId");
            String mobile =object.optString("moblie");
            String addressName =object.optString("addressName");
            String userName =object .optString("userName");
            String sex=object.optString("sex");
            String bottleTotal="("+object.optString("total")+" 瓶)";
            siteId=object.optString("siteId");
            boolean isEndPage=object.optBoolean("isEndPage");
            boolean isStartPage=object.optBoolean("isStartPage");
            SharedPreferencesUtil.putStringData(context,SharedPreferencesUtil.LPG_SEX,sex);
            SharedPreferencesUtil.putStringData(context,SharedPreferencesUtil.LPG_USER_ID,userId);
            SharedPreferencesUtil.putStringData(context,SharedPreferencesUtil.LPG_USER_NAME,userName);
            SharedPreferencesUtil.putStringData(context,SharedPreferencesUtil.LPG_MOBILE,mobile);
            JSONArray jsonArray=object.optJSONArray("bottleList");
            if (isEndPage){
                moreListView.loadComplete(false);
            }else {
                moreListView.loadComplete(true);
            }
            if (jsonArray.length()>0&&isStartPage){
                mList.clear();
            }
            onFillData(jsonArray);
            tvBottleCount.setText(bottleTotal);
            tvPhone.setText(mobile);
            tvAddress.setText(addressName);
        }else {
            moreListView.setVisibility(View.GONE);
            goBindingAccount();
        }
    }
    private void onFillData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id=jsonObject.optString("id");
                String bottleWeight=jsonObject.optString("bottleWeight");
                String usedDate=jsonObject.optString("usedDate");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("bottleWeight", bottleWeight);
                map.put("usedDate", usedDate);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        myBottleAdapter.notifyDataSetChanged();
    }
    private void goBindingAccount() {
        Intent intent=new Intent(context,BindingAccountActivity.class);
        intent.putExtra("flag",0);
        startActivityForResult(intent,BINDING_SUCCESS_CODE);
        finish();
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_my_account);
        context=this;
        userName = SharedPreferencesUtil.getUserName(context, SharedPreferencesUtil.UserName, "");
        setCommonHeader("我的账户");
        customDialog=new CustomDialog(this, "正在加载");
        initFindViewId();
        myBottleAdapter=new MyBottleAdapter(context,mList);
        moreListView.setAdapter(myBottleAdapter);
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    int pos=position-1;
                    String bottleId=mList.get(pos).get("id");
                    goBottleInfo(bottleId);
                }
            }
        });
        moreListView.setLoadMoreListener(new ILoadMoreCallback() {
            @Override
            public void loadMore() {
                loadData(pageIndex + 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case BINDING_SUCCESS_CODE:
                initListData();
                break;
            case SWITCH_SUCCESS_CODE:
                mList.clear();
                myBottleAdapter.notifyDataSetChanged();
                initListData();
                break;
                default:
                    break;
        }
    }
    private void initFindViewId() {
        rightImage=(ImageView)findViewById(R.id.id_right_img);
        rightImage.setVisibility(View.VISIBLE);
        View layoutHeader =findViewById(R.id.id_re_layout);
        layoutHeader.setBackgroundResource(R.color.colorOrange);
        View layoutAccountHeader=getLayoutInflater().inflate(R.layout.layout_lpg_account_header,null);
        moreListView=(LoadMoreListView)findViewById(R.id.id_bottle_list);
        tvAddress=(TextView)layoutAccountHeader.findViewById(R.id.id_tv_address);
        tvPhone=(TextView)layoutAccountHeader.findViewById(R.id.id_tv_phone);
        tvBottleCount=(TextView)layoutAccountHeader.findViewById(R.id.id_bottle_num);
        MenuItemM mimOrderGas=(MenuItemM)layoutAccountHeader.findViewById(R.id.id_order_gas_page);
        MenuItemM mimOrderList=(MenuItemM)layoutAccountHeader.findViewById(R.id.id_order_list_page);
        MenuItemM mimCashPledge=(MenuItemM)layoutAccountHeader.findViewById(R.id.id_cash_pledge_page);
        moreListView.addHeaderView(layoutAccountHeader);
        initListData();
        mimOrderGas.setOnClickListener(new MenuItemM.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPlaceOrder();
            }
        });
        mimOrderList.setOnClickListener(new MenuItemM.OnClickListener() {
            @Override
            public void onClick(View v) {

                goOrderList();
            }
        });
        mimCashPledge.setOnClickListener(new MenuItemM.OnClickListener() {
            @Override
            public void onClick(View v) {

                goReturnBottle();
            }
        });
        layoutAccountHeader.findViewById(R.id.id_connection_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone ="963666";
                new PromptDialog.Builder(context)
                        .setTitle("服务热线")
                        .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                        .setMessage(phone)
                        .setButton1("取消", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) { dialog.dismiss(); }})
                        .setButton2("呼叫", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                requestLimit(phone);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTopRightMenuView();
            }
        });
    }

    private void onTopRightMenuView() {
        TopRightMenu mTopRightMenu = new TopRightMenu(LpgMyAccountActivity.this);
        //添加菜单项
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.mipmap.switch_account_h, "切换账号"));
        menuItems.add(new MenuItem(R.mipmap.lpg_user_h, "开户申请"));
        menuItems.add(new MenuItem(R.mipmap.lpg_search_h, "钢瓶查询"));
        mTopRightMenu
                //显示菜单图标，默认为true
                .showIcon(true)
                //背景变暗，默认为true
                .dimBackground(true)
                //显示动画，默认为true
                .needAnimationStyle(true)
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        if (position==0){
                            startSwitchUser();
                        }else if (position==1){
                            goNewUserActivity();
                        }else {
                            goScanCode();
                        }
                    }

                })
                //带偏移量
                .menuShowAsDropDown(rightImage, -90, 0);
    }

    private void goScanCode() {
        Intent intent =new Intent(context, QRCodeScanActivity.class);
        startActivity(intent);
    }
    private void startSwitchUser() {
        Intent intent=new Intent(context,LpgSwitchAccountActivity.class);
        startActivityForResult(intent,SWITCH_SUCCESS_CODE);
    }
    private void goNewUserActivity() {
        Intent intent=new Intent(context,LpgNewUserActivity.class);
        startActivity(intent);
    }
    private void goBottleInfo(String bottleId) {
        Intent intent=new Intent(context,LpgSteelBottleQueryActivity.class);
        intent.putExtra("bottleId",bottleId);
        startActivity(intent);
    }
    private void goReturnBottle() {

        Intent intent=new Intent(context,LpgDepositOrderActvity.class);
        startActivity(intent);
    }
    private void goOrderList() {
        Intent intent=new Intent(context,LpgMyOrderListActivity.class);
        startActivity(intent);
    }
    private void goPlaceOrder() {
        Intent intent=new Intent(context,LpgPlaceOrderActivity.class);
        intent.putExtra("siteId",siteId);
        startActivity(intent);
    }
    private void requestLimit(final String phone) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                CallPhoneUtil.callPhone(context,phone);
            } else {
                MPermissionUtils.requestPermissionsResult(this, MY_PERMISSIONS_REQUEST_CALL_PHONE, new String[]{Manifest.permission.CALL_PHONE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int code) {
                        if (code==MY_PERMISSIONS_REQUEST_CALL_PHONE){
                            CallPhoneUtil.callPhone(context,phone);
                        }
                    }
                    @Override
                    public void onPermissionDenied(int code) {
                        ToastUtil.ToastText(context,"没有权限您将无法进行相关操作！");
                    }
                });
            }
        }else {
            CallPhoneUtil.callPhone(context,phone);
        }
    }
    private void initListData() {
        customDialog.show();
        loadData(1);
    }
    private void loadData(int i) {
        pageIndex =i;
        String pageNum=String.valueOf(i);
        String pageSize="16";
        String requestUrl= UrlUtil.LPG_MSB_USER_INFO;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("msbMobile",userName);
        textParams.put("pageNum",pageNum);
        textParams.put("pageSize",pageSize);
        OkHttpRequestManager.getInstance(context).requestAsyn(requestUrl,OkHttpRequestManager.TYPE_GET,textParams,requestHandler);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
