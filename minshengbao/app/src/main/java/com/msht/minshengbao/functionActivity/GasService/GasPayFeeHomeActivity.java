package com.msht.minshengbao.functionActivity.GasService;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Bean.AdvertisingInfo;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.AndroidWorkaround;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.SelectDialog;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.functionActivity.MyActivity.AutomatePayActivity;
import com.msht.minshengbao.functionActivity.MyActivity.CustomerNoManageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/1/7 
 */
public class GasPayFeeHomeActivity extends BaseActivity implements View.OnClickListener {
    private BGABanner mCubeBanner;
    private Button btnQuery;
    private EditText etCustomerNo;
    private int requestType=0;
    private int requestCode=0;
    private String   userId;
    private String   password;
    private String   meterNo;
    private ArrayList<HashMap<String, String>> tableList = new ArrayList<HashMap<String, String>>();
    private ArrayList<AdvertisingInfo> adInformation = new ArrayList<AdvertisingInfo>();
    private List<String> advertisingList = new ArrayList<>();
    private CustomDialog customDialog;
    private final SearchHandler searchHandler=new SearchHandler(this);
    private final GetAdvertisingHandler requestHandler=new GetAdvertisingHandler(this);
    private static class GetAdvertisingHandler extends Handler {
        private WeakReference<GasPayFeeHomeActivity> mWeakReference;
        public GetAdvertisingHandler(GasPayFeeHomeActivity activity) {
            mWeakReference = new WeakReference<GasPayFeeHomeActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final GasPayFeeHomeActivity activity =mWeakReference.get();
            // the referenced object has been cleared
            if (activity == null||activity.isFinishing()) {
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error=object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONArray array =object.optJSONArray("data");
                            if (activity.requestType==0){
                                activity.initAdvertisingData(array);
                            }else {
                                activity.onTableData(array);
                            }
                        }else {
                            ToastUtil.ToastText(activity.context,error);
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
    private static class SearchHandler extends Handler{
        private WeakReference<GasPayFeeHomeActivity> mWeakReference;
        public SearchHandler(GasPayFeeHomeActivity activity) {
            mWeakReference = new WeakReference<GasPayFeeHomeActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final GasPayFeeHomeActivity activity =mWeakReference.get();
            // the referenced object has been cleared
            if (activity == null||activity.isFinishing()) {
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error=object.optString("error");
                        JSONObject jsonObject =object.optJSONObject("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0){
                                activity.onSearchTableData(jsonObject);
                            }else {
                                activity.onGasExpenseData(jsonObject);
                            }

                        }else {
                            ToastUtil.ToastText(activity.context,error);
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
    private void initAdvertisingData(JSONArray array) {
        adInformation.clear();
        advertisingList.clear();
        if (array!=null&&array.length()!=0){
            try {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    AdvertisingInfo info = new AdvertisingInfo();
                    info.setImages(jsonObject.getString("image"));
                    info.setUrl(jsonObject.getString("url"));
                    info.setDesc(jsonObject.optString("desc"));
                    info.setTitle(jsonObject.optString("title"));
                    info.setShare(jsonObject.optString("share"));
                    adInformation.add(info);
                    advertisingList.add(jsonObject.optString("image"));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            if (adInformation!=null&&adInformation.size()!=0){
                mCubeBanner.setVisibility(View.VISIBLE);
            }else {
                mCubeBanner.setVisibility(View.GONE);
            }
            mCubeBanner.setAutoPlayAble(advertisingList.size() > 1);
            mCubeBanner.setAdapter(new MyImageAdapter());
            mCubeBanner.setDelegate(new MyImageAdapter());
            mCubeBanner.setData(advertisingList, null);
        }else {
            mCubeBanner.setVisibility(View.GONE);
        }
    }
    private void onTableData(JSONArray array) {
        tableList.clear();
        if (array.length()>0) {
            for (int i = 0; i <array.length(); i++) {
                JSONObject jsonObject =array.optJSONObject(i);
                String id = jsonObject.optString("id");
                String address = jsonObject.optString("address");
                String bh = jsonObject.optString("bh");
                String moduleType="2";
                String lastNum = jsonObject.optString("lastNum");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("moduleType",moduleType);
                map.put("name", address);
                map.put("bh", bh);
                map.put("lastNum",lastNum);
                tableList.add(map);
            }
            JSONObject json = array.optJSONObject(0);
            int  tableType = json.optInt("lx");
            switch (tableType){
                case 11:
                    onOrdinaryTableExpense();
                    break;
                case 12:
                    onIcCardTable();
                    break;
                case 17:
                    onInternetTable();
                    break;
                default:
                    break;
            }
        }else {
            ToastUtil.ToastText(context,"您输入燃气用户号异常！");
        }
    }
    private void onSearchTableData(JSONObject jsonObject) {
        String address=jsonObject.optString("address");
        String name=jsonObject.optString("name");
        String lastRechargeAmount=jsonObject.optString("lastRechargeAmount");
        String customerNo=jsonObject.optString("customerNo");
        String lastRechargeTime=jsonObject.optString("lastRechargeTime");
        Intent intent=new Intent(context,GasInternetTableRechargeActivity.class);
        intent.putExtra("address",address);
        intent.putExtra("customerNo",customerNo);
        intent.putExtra("lastRechargeAmount",lastRechargeAmount);
        intent.putExtra("lastRechargeTime",lastRechargeTime);
        intent.putExtra("meterNo",meterNo);
        startActivity(intent);
    }

    private void onGasExpenseData(JSONObject jsonObject) {
        String name=jsonObject.optString("name");
        String customerNum=jsonObject.optString("customerNo");
        String allBalance=jsonObject.optString("balance");
        String debts=jsonObject.optString("debts");
        String totalNum=jsonObject.optString("total_num");
        String gasFee=jsonObject.optString("gas_fee");
        String discountFees=jsonObject.optString("discount_fee");
        String lateFee=jsonObject.optString("late_fee");
        JSONArray json=jsonObject.optJSONArray("detail_list");
        VariableUtil.detailList.clear();
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject object = json.getJSONObject(i);
                String date = object.getString("date");
                String num = object.getString("num");
                String gasFees = object.getString("gas_fee");
                String amounts=object.getString("amount");
                String balance=object.getString("balance");
                String discountFee=object.getString("discount_fee");
                String lateFees=object.getString("late_fee");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("date", date);
                map.put("num", num);
                map.put("gas_fees", gasFees);
                map.put("amounts",amounts);
                map.put("balance",balance);
                map.put("discount_fee", discountFee);
                map.put("late_fee", lateFees);
                VariableUtil.detailList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        Intent payFees=new Intent(context, GasExpenseQueryActivity.class);
        payFees.putExtra("name", name);
        payFees.putExtra("CustomerNo", customerNum);
        payFees.putExtra("allBalance",allBalance);
        payFees.putExtra("debts", debts);
        payFees.putExtra("totalNum", totalNum);
        payFees.putExtra("discountFees", discountFees);
        payFees.putExtra("gasFee",gasFee);
        payFees.putExtra("lateFee", lateFee);
        startActivity(payFees);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_pay_fee_home);
        //适配华为手机虚拟键遮挡tab的问题
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
        context=this;
        setCommonHeader("燃气缴费");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initFindViewId();
        initBannerData();
    }

    private void initFindViewId() {

        mCubeBanner = findViewById(R.id.banner);
        findViewById(R.id.id_ordinary_layout).setOnClickListener(this);
        findViewById(R.id.id_ic_layout).setOnClickListener(this);
        findViewById(R.id.id_internet_layout).setOnClickListener(this);
        findViewById(R.id.id_question_text).setOnClickListener(this);
        findViewById(R.id.id_automate_pay).setOnClickListener(this);
        findViewById(R.id.id_customerNo_manager).setOnClickListener(this);
        findViewById(R.id.id_near_machine).setOnClickListener(this);
        btnQuery=(Button)findViewById(R.id.id_btn_query);
        btnQuery.setEnabled(false);
        btnQuery.setOnClickListener(this);
        etCustomerNo=(EditText)findViewById(R.id.id_et_customerNo);
        etCustomerNo.addTextChangedListener(new MyTextWatcher());
    }

    private void initBannerData() {
        requestType=0;
        String requestUrl=UrlUtil.ADVERTISING_URL;
        try {
            requestUrl =requestUrl +"?city_id="+ URLEncoder.encode(VariableUtil.cityId, "UTF-8")+"&code="+URLEncoder.encode("gas_pay_start_activity", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_GET,null,requestHandler);
    }
    private class MyImageAdapter implements BGABanner.Adapter<ImageView, String>,BGABanner.Delegate<ImageView, String> {

        @Override
        public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
            Glide.with(itemView.getContext())
                    .load(model)
                    .apply(new RequestOptions().placeholder(R.drawable.icon_stub).error(R.drawable.icon_error).dontAnimate().centerCrop())
                    .into(itemView);
        }

        @Override
        public void onBannerItemClick(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
            AdvertisingInfo info=adInformation.get(position);
            String myUrl=info.getUrl();
            String title=info.getTitle();
            String share=info.getShare();
            String desc=info.getDesc();
            AppActivityUtil.onAppActivityType(context,myUrl,title,share,desc,"gas_start_activity","");
        }
    }
    private void onQueryTableType() {
        requestType=1;
        String customerNo=etCustomerNo.getText().toString().trim();
        customDialog.show();
        String validateURL=UrlUtil.GET_TABLE_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("CustomerNo",customerNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onOrdinaryTableExpense() {
        requestCode=1;
        customDialog.show();
        String customerNo=etCustomerNo.getText().toString().trim();
        String validateURL=UrlUtil.SEARCH_BILL_GAS_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("category","1");
        textParams.put("CustomerNo",customerNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,searchHandler);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_ordinary_layout:
                onOrdinaryTable();
                break;
            case R.id.id_ic_layout:
                onIcCardTable();
                break;
            case R.id.id_internet_layout:
                onInternetTableHome();
                break;
            case R.id.id_question_text:
                onQuestionText();
                break;
            case R.id.id_automate_pay:
                onAutomatePay();
                break;
            case R.id.id_customerNo_manager:
                onManagerCustomerNo();
                break;
            case R.id.id_near_machine:
                onNearMachine();
                break;
            case R.id.id_btn_query:
                onQueryTableType();
            default:
                break;
        }
    }
    private void onOrdinaryTable() {
        Intent intent=new Intent(context,GasPayFeeActivity.class);
        startActivity(intent);
    }
    private void onIcCardTable() {
        Intent card=new Intent(context,GasIcCardActivity.class);
        startActivity(card);
    }
    private void onInternetTable() {
        if (context!=null&&tableList!=null){
            new SelectDialog(context,tableList,-1).builder()
                    .setTitleText("选择表具")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .setOnSheetItemClickListener(new SelectDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            meterNo = tableList.get(position).get("bh");
                            onSearchRecord(meterNo);

                        }
                    })
                    .show();
        }
    }

    private void onInternetTableHome(){
        Intent intent=new Intent(context,GasInternetTableActivity.class);
        startActivity(intent);
    }
    private void onSearchRecord(String meterNo) {
        requestCode=0;
        customDialog.show();
        String validateURL=UrlUtil.INTERNET_TABLE_LAST_DATA;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("meterNo",meterNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,searchHandler);
    }

    private void onQuestionText() {
        String url= UrlUtil.GET_CUSTOMER_NO_URL;
        Intent intent=new Intent(context,GetGasCustomerNoActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","获取方式");
        startActivity(intent);
    }
    private void onAutomatePay() {
        Intent agree=new Intent(context, AutomatePayActivity.class);
        startActivity(agree);
    }
    private void onManagerCustomerNo() {
        Intent intent=new Intent(context, CustomerNoManageActivity.class);
        startActivity(intent);
    }
    private void onNearMachine() {
        Intent intent =new Intent(context, GasIcCardMachineMap.class);
        startActivity(intent);
    }
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(etCustomerNo.getText().toString())){
                btnQuery.setEnabled(true);
            }else {
                btnQuery.setEnabled(false);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }
    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();;
        }
        super.onDestroy();
    }
}
