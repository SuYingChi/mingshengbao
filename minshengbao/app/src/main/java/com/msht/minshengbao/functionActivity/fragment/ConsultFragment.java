package com.msht.minshengbao.functionActivity.fragment;


import android.app.Dialog;
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
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.functionActivity.GasService.ServerSuccess;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultFragment extends Fragment {

    private EditText consult,details;
    private TextView tv_num;
    private MaterialSpinner spinner;
    private Button comfirm;
    private String userId;
    private String password;
    private String Title,description;
    private int num=100;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private String installtype="1";
    private final String mPageName ="咨询投诉";
    private static final String[] severType = {"燃气服务","维修服务","商城"};
    private CustomDialog customDialog;
    public ConsultFragment() {}
    Handler repairHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            showsuccess();
                        }else {
                            showfaiture(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    showfaiture(msg.obj.toString());
                    break;
                default:
                    break;

            }
        }
    };
    private void showfaiture(String error) {
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
    private void showsuccess() {
        String navigation="咨询投诉";
        Intent success=new Intent(getActivity(),ServerSuccess.class);
        success.putExtra("navigation",navigation);
        startActivity(success);
        getActivity().finish();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_consult, container, false);
        customDialog=new CustomDialog(getActivity(), "正在加载");
        userId= SharedPreferencesUtil.getUserId(getActivity(), SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(getActivity(), SharedPreferencesUtil.Password,"");
        spinner = (MaterialSpinner)view.findViewById(R.id.spinner);
        spinner.setItems(severType);
        intfindViewByid(view);
        initEvent();
        return view;
    }

    private void intfindViewByid(View view) {
        comfirm=(Button)view.findViewById(R.id.id_consult_comfirm);
        consult=(EditText)view.findViewById(R.id.id_consult_question);
        details=(EditText)view.findViewById(R.id.id_consult_description);
        tv_num=(TextView)view.findViewById(R.id.id_tv_num);
        comfirm.setEnabled(false);
    }
    private void initEvent() {
        consult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty( consult.getText().toString()) || TextUtils.isEmpty(details.getText().toString())
                        ) {
                    comfirm.setEnabled(false);
                } else {
                    comfirm.setEnabled(true);
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
                    comfirm.setEnabled(false);
                } else {
                    comfirm.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                int number=num-s.length();
                String Wnum=String.valueOf(number);
                tv_num.setText(Wnum);
            }
        });
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                int Stype=position+1;
                installtype=String.valueOf(Stype);
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Title = consult.getText().toString().trim();
                description = details.getText().toString().trim();
                if (matchtitleMsg(Title)) {
                    customDialog.show();
                    requestService();
                }
            }
        });
    }
    private void requestService() {
        String type="7";
        String validateURL = UrlUtil.INSTALL_SERVER_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("title",Title);
        textParams.put("description",description);
        textParams.put("type",type);
        textParams.put("installType",installtype);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                repairHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                repairHandler.sendMessage(msg);
            }
        });
    }
    private boolean matchtitleMsg(String title) {
        if(title.equals(""))
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
