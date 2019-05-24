package com.msht.minshengbao.functionActivity.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.msht.minshengbao.functionActivity.gasService.GasServerGuideActivity;
import com.msht.minshengbao.functionActivity.htmlWeb.HtmlPageActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.NetWorkUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;


/**
 * A simple {@link Fragment} subclass.
 * @author hong
 */
public class GasNeedKnowFrag extends Fragment implements View.OnClickListener {
    private RelativeLayout Rprofile;
    private RelativeLayout Rusersafety,Rtooluse;
    private RelativeLayout Rgassafety;
    private final String mPageName ="燃气介绍";
    private Context mContext;
    public GasNeedKnowFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_gas_need_know, container, false);
        mContext=getActivity();
        view.findViewById(R.id.id_layout_profile).setOnClickListener(this);
        view.findViewById(R.id.id_layout_usersafety).setOnClickListener(this);
        view.findViewById(R.id.id_layout_tooluse).setOnClickListener(this);
        view.findViewById(R.id.id_layout_safety).setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_layout_profile:
                if (NetWorkUtil.isNetWorkEnable(getActivity())){
                    gongsigaikuang();
                }else {
                    onNoNetwork();
                }
                break;
            case R.id.id_layout_usersafety:
                usersafety();
                break;
            case R.id.id_layout_tooluse:
                if (NetWorkUtil.isNetWorkEnable(getActivity())){
                    onToolUse();
                }else {
                    onNoNetwork();
                }
                break;
            case R.id.id_layout_safety:
                if (NetWorkUtil.isNetWorkEnable(mContext)){
                    gassafety();
                }else {
                    onNoNetwork();
                }
                break;
            default:
                break;
        }
    }

    private void onNoNetwork() {
        new PromptDialog.Builder(mContext)
                .setTitle("无网络连接")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("当前网络不可用")
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void  gongsigaikuang() {    //营业网点
        String navigate="公司概况";
        String url= UrlUtil.Companyprofile_Url;
        Intent yingye=new Intent(getActivity(),HtmlPageActivity.class);
        yingye.putExtra("navigate",navigate);
        yingye.putExtra("url",url);
        startActivity(yingye);
    }
    private void usersafety() {    //用户安全
        Intent safety=new Intent(getActivity(),GasServerGuideActivity.class);
        startActivity(safety);
    }
    private void onToolUse() {
        String navigate="燃气具使用";
        String url= UrlUtil.GasToolUse_Url;
        Intent use=new Intent(getActivity(),HtmlPageActivity.class);
        use.putExtra("navigate",navigate);
        use.putExtra("url",url);
        startActivity(use);
    }
    private void  gassafety() {
        String navigate="燃气安全";
        String url= UrlUtil.GasSafety_Url;
        Intent price=new Intent(getActivity(),HtmlPageActivity.class);
        price.putExtra("navigate",navigate);
        price.putExtra("url",url);
        startActivity(price);
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
