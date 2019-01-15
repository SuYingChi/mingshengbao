package com.msht.minshengbao.functionActivity.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.functionActivity.GasService.ServerSuccessActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;


import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * @author hong
 */
public class ConsultFragment extends Fragment {

    private EditText consult,details;
    private TextView tvNum;
    private MaterialSpinner spinner;
    private Button confirm;
    private String userId;
    private String password;
    private String mTitle,description;
    private int num=100;
    private String installType ="1";
    private final String mPageName ="咨询投诉";
    private static final String[] SEVER_TYPE = {"燃气服务","维修服务","商城"};
    private CustomDialog customDialog;
    private Context mContext;
    public ConsultFragment() {}
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<ConsultFragment> mWeakReference;
        public RequestHandler(ConsultFragment reference) {
            mWeakReference = new WeakReference<ConsultFragment>(reference);
        }

        @Override
        public void handleMessage(Message msg) {

            final ConsultFragment reference =mWeakReference.get();
            // the referenced object has been cleared
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
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            reference.onShowSuccess();
                        }else {
                            reference.onShowFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    reference.onShowFailure(msg.obj.toString());
                    break;
                default:
                    break;

            }
            super.handleMessage(msg);
        }
    }
    private void onShowFailure(String error) {
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
    private void onShowSuccess() {
        String navigation="咨询投诉";
        Intent success=new Intent(getActivity(),ServerSuccessActivity.class);
        success.putExtra("navigation",navigation);
        startActivity(success);
        getActivity().finish();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_consult, container, false);
        mContext=getActivity();
        customDialog=new CustomDialog(getActivity(), "正在加载");
        userId= SharedPreferencesUtil.getUserId(getActivity(), SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(getActivity(), SharedPreferencesUtil.Password,"");
        spinner = (MaterialSpinner)view.findViewById(R.id.spinner);
        spinner.setItems(SEVER_TYPE);
        intfindViewByid(view);
        initEvent();
        return view;
    }

    private void intfindViewByid(View view) {
        confirm =(Button)view.findViewById(R.id.id_consult_comfirm);
        consult=(EditText)view.findViewById(R.id.id_consult_question);
        details=(EditText)view.findViewById(R.id.id_consult_description);
        tvNum =(TextView)view.findViewById(R.id.id_tv_num);
        confirm.setEnabled(false);
    }
    private void initEvent() {
        consult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty( consult.getText().toString()) || TextUtils.isEmpty(details.getText().toString())
                        ) {
                    confirm.setEnabled(false);
                } else {
                    confirm.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        details.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty( consult.getText().toString()) || TextUtils.isEmpty(details.getText().toString())
                        ) {
                    confirm.setEnabled(false);
                } else {
                    confirm.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                int number=num-s.length();
                String mNum=String.valueOf(number);
                tvNum.setText(mNum);
            }
        });
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                int sType=position+1;
                installType =String.valueOf(sType);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle = consult.getText().toString().trim();
                description = details.getText().toString().trim();
                if (isMatchTitleMsg(mTitle)) {
                    customDialog.show();
                    requestService();
                }
            }
        });
    }
    private void requestService() {
        String type="7";
        String validateURL = UrlUtil.INSTALL_SERVER_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("title", mTitle);
        textParams.put("description",description);
        textParams.put("type",type);
        textParams.put("installType", installType);
        OkHttpRequestUtil.getInstance(mContext.getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private boolean isMatchTitleMsg(String title) {
        if(TextUtils.isEmpty(title))
        {
            new PromptDialog.Builder(getActivity())
                    .setTitle("民生宝")
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage("投诉问题不能为空")
                    .setButton1("确定", new PromptDialog.OnClickListener() {

                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
            return false;
        }
        return true;
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