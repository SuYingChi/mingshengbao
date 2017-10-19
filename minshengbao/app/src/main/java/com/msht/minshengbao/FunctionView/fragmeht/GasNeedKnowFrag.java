package com.msht.minshengbao.FunctionView.fragmeht;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.msht.minshengbao.FunctionView.GasService.GasServerGuide;
import com.msht.minshengbao.FunctionView.HtmlWeb.HtmlPage;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.NetWorkUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;


/**
 * A simple {@link Fragment} subclass.
 */
public class GasNeedKnowFrag extends Fragment implements View.OnClickListener {
    private RelativeLayout Rprofile;
    private RelativeLayout Rusersafety,Rtooluse;
    private RelativeLayout Rgassafety;
    private final String mPageName ="燃气介绍";

    public GasNeedKnowFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_gas_need_know, container, false);
        Rprofile=(RelativeLayout)view.findViewById(R.id.id_layout_profile);
        Rusersafety=(RelativeLayout)view.findViewById(R.id.id_layout_usersafety);
        Rtooluse=(RelativeLayout)view.findViewById(R.id.id_layout_tooluse);
        Rgassafety=(RelativeLayout)view.findViewById(R.id.id_layout_safety);
        initEvent();
        return view;
    }

    private void initEvent() {
        Rprofile.setOnClickListener(this);
        Rusersafety.setOnClickListener(this);
        Rtooluse.setOnClickListener(this);
        Rgassafety.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_layout_profile:
                if (NetWorkUtil.IsNetWorkEnable(getActivity())){
                    gongsigaikuang();
                }else {
                    Nonetwork();
                }
                break;
            case R.id.id_layout_usersafety:
                usersafety();
                break;
            case R.id.id_layout_tooluse:
                if (NetWorkUtil.IsNetWorkEnable(getActivity())){
                    ToolUse();
                }else {
                    Nonetwork();
                }
                break;
            case R.id.id_layout_safety:
                if (NetWorkUtil.IsNetWorkEnable(getActivity())){
                    gassafety();
                }else {
                    Nonetwork();
                }
                break;
            default:
                break;
        }
    }

    private void Nonetwork() {
        new PromptDialog.Builder(getActivity())
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
        Intent yingye=new Intent(getActivity(),HtmlPage.class);
        yingye.putExtra("navigate",navigate);
        yingye.putExtra("url",url);
        startActivity(yingye);
    }
    private void usersafety() {    //用户安全
        Intent safety=new Intent(getActivity(),GasServerGuide.class);
        startActivity(safety);
    }
    private void ToolUse() {
        String navigate="燃气具使用";
        String url= UrlUtil.GasToolUse_Url;
        Intent use=new Intent(getActivity(),HtmlPage.class);
        use.putExtra("navigate",navigate);
        use.putExtra("url",url);
        startActivity(use);
    }
    private void  gassafety() {
        String navigate="燃气安全";
        String url= UrlUtil.GasSafety_Url;
        Intent price=new Intent(getActivity(),HtmlPage.class);
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
