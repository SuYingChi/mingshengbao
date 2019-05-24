package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.custom.ButtonUI.ButtonM;
import com.msht.minshengbao.adapter.RedPacketAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterRedPacketDialog {
    private XRecyclerView    mRecyclerView;
    private RedPacketAdapter mAdapter;
    private View   layoutNoData;
    private String amount;
    private int type=0;
    private int pageIndex=0;
    private Context context;
    private Dialog dialog;
    private Display display;
    private OnSheetButtonOneClickListener itemClickOneListener;
    public interface OnSheetButtonOneClickListener {
        /**
         * 回调
         * @param code
         * @param amount
         */
        void onSelectClick(String code,String amount);
    }
    public  WaterRedPacketDialog setOnSheetButtonOneClickListener(OnSheetButtonOneClickListener listener){
        this.itemClickOneListener=listener;
        return this;
    }
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public WaterRedPacketDialog(Context context,String amount) {
        this.context = context;
        this.amount=amount;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }

    public WaterRedPacketDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_water_red_packet, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow!=null){
            dialogWindow.setGravity(Gravity.START|Gravity.BOTTOM);
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            dialogWindow.setAttributes(lp);
        }
        layoutNoData=view.findViewById(R.id.id_noData_layout);
        RadioGroup radioGroup=(RadioGroup)view.findViewById(R.id.id_group) ;
        ButtonM textCancel = (ButtonM) view.findViewById(R.id.id_cancel_use);
        mRecyclerView=(XRecyclerView)view.findViewById(R.id.id_redPacket_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView=(XRecyclerView)view.findViewById(R.id.id_redPacket_data);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter=new RedPacketAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(true);
        getRedPacketData(0,1);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() { }
            @Override
            public void onLoadMore() {
                getRedPacketData(type,pageIndex+1);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.id_valid:
                        getRedPacketData(0,1);
                        break;
                    case R.id.id_invalid:
                        getRedPacketData(1,1);
                        break;
                    default:
                        getRedPacketData(0,1);
                        break;
                }
            }
        });
        mAdapter.setClickCallBack(new RedPacketAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                if (type==0){
                    String code=mList.get(pos).get("code");
                    String discountAmount=mList.get(pos).get("discountAmount");
                    if (itemClickOneListener!=null){
                        VariableUtil.mPos=pos;
                        itemClickOneListener.onSelectClick(code,discountAmount);
                    }
                    dialog.dismiss();
                }
            }
        });
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickOneListener!=null){
                    VariableUtil.mPos=-1;
                    itemClickOneListener.onSelectClick("","0");
                }
                dialog.dismiss();
            }
        });
        return this;
    }
    private void getRedPacketData(int i, int page) {
        pageIndex =page;
        type=i;
        String validateURL = UrlUtil.WATER_GET_COUPON;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("account",VariableUtil.waterAccount);
        textParams.put("type",String.valueOf(i));
        textParams.put("amount",amount);
        textParams.put("pageNo",String.valueOf(pageIndex));
        textParams.put("pageSize","16");
        OkHttpRequestManager.getInstance(context.getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                mRecyclerView.loadMoreComplete();
                onAnalysisData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                mRecyclerView.loadMoreComplete();
                ToastUtil.ToastText(context,data.toString());
            }
        });
    }
    private void onAnalysisData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("message");
            JSONObject jsonObject =object.optJSONObject("data");
            boolean lastPage=jsonObject.optBoolean("lastPage");
            JSONArray jsonArray=jsonObject.optJSONArray("list");
            if (lastPage){
                mRecyclerView.setLoadingMoreEnabled(false);
            }else {
                mRecyclerView.setLoadingMoreEnabled(true);
            }
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                if (pageIndex==1){
                    mList.clear();
                }
                onReceiveData(jsonArray);
            }else {
                ToastUtil.ToastText(context,error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onReceiveData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String code = json.getString("code");
                String name=json.optString("name");
                String status= json.getString("status");
                String scope = json.getString("scope");
                String conditionType =json.optString("conditionType");
                String conditionAmount=json.optString("conditionAmount");
                String discountAmount=json.optString("discountAmount");
                String description=json.optString("description");
                String days=json.optString("days");
                HashMap<String, String> map = new HashMap<String, String>(12);
                map.put("code", code);
                map.put("name", name);
                map.put("status",status);
                map.put("scope",scope);
                map.put("conditionType",conditionType);
                map.put("conditionAmount",conditionAmount);
                map.put("discountAmount",discountAmount);
                map.put("description",description);
                map.put("days",days);
                map.put("isVisible","0");
                map.put("type",String.valueOf(type));
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mList!=null&&mList.size()>0){
            layoutNoData.setVisibility(View.GONE);
        }else {
            layoutNoData.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }
    public WaterRedPacketDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public WaterRedPacketDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
