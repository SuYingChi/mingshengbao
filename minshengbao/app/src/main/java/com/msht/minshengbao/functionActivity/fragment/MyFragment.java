package com.msht.minshengbao.functionActivity.fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.adapter.MyFunctionAdapter;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.functionActivity.MyActivity.MoreSetting;
import com.msht.minshengbao.functionActivity.MyActivity.ShareMenuActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.ViewUI.widget.MyScrollview;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment implements View.OnClickListener, MyScrollview.ScrollViewListener {
    private MyScrollview   myScrollview;
    private LinearLayout   Lnavigation;
    private RelativeLayout Rmysetting;
    private TextView tv_naviga;
    private int bgHeight;// 上半身的高度
    private Context mContext;
    private final String mPageName = "首页_我的（未登录）";
    private boolean lstate=false;
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    private MyNoScrollGridView mGridView;
    private MyFunctionAdapter mAdapter;
    private ArrayList<HashMap<String, Integer>> mList = new ArrayList<HashMap<String, Integer>>();
    public  MyFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my, container, false);
        mContext=getActivity();
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            view.findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        initView(view);
        initListeners();
        mAdapter=new MyFunctionAdapter(mContext,mList);
        mGridView.setAdapter(mAdapter);
        initData();
        GoActivity();
        return view;
    }
    private void initListeners() {
        ViewTreeObserver vto = Rmysetting.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rmysetting.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                bgHeight = Rmysetting.getHeight();
                inisetListaner();
            }
        });
    }

    private void inisetListaner() {
        myScrollview.setScrollViewListener(this);
    }

    private void GoActivity() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int code=mList.get(position).get("code");
                switch (code){
                    case 0:
                        gologin();
                        break;
                    case 1:
                        gologin();
                        break;
                    case 2:
                        gologin();
                        break;
                    case 3:
                        gologin();
                        break;
                    case 4:
                        gologin();
                        break;
                    case 5:
                        Goshare();
                        break;
                    default:
                        break;
                }

            }
        });
    }
    private void initData() {
        for (int i = 0; i <6; i++) {
            if (VariableUtil.BoolCode){
                if (i!=1&&i!=2){
                    HashMap<String, Integer> map = new HashMap<String, Integer>();
                    map.put("code", i);
                    mList.add(map);
                }
            }else {
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("code", i);
                mList.add(map);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
    private void initView(View view) {
        mGridView=(MyNoScrollGridView)view.findViewById(R.id.id_function_view);
        myScrollview=(MyScrollview)view.findViewById(R.id.id_scrollview);
        Lnavigation=(LinearLayout) view.findViewById(R.id.id_li_navigation);
        Rmysetting=(RelativeLayout) view.findViewById(R.id.id_re_gologin);
        tv_naviga=(TextView)view.findViewById(R.id.id_tv_naviga);
        Rmysetting.setOnClickListener(this);
        view.findViewById(R.id.id_re_hotline).setOnClickListener(this);
        view.findViewById(R.id.id_re_consult).setOnClickListener(this);
        view.findViewById(R.id.id_re_moreset).setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_gologin:
                gologin();
                break;
            case R.id.id_re_hotline:
                Hotline();
                break;
            case R.id.id_re_consult:
                gologin();
                break;
            case R.id.id_re_moreset:
                GoMoreseting();
                break;
            default:
                break;
        }
    }

    private void GoMoreseting() {
        Intent intent=new Intent(mContext, MoreSetting.class);
        startActivity(intent);
    }
    private void Hotline() {
        final String phone = "963666";
        new PromptDialog.Builder(mContext)
                .setTitle("服务热线")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(phone)
                .setButton1("取消", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();}
                })
                .setButton2("呼叫", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        //String phone = "963666";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (mContext.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            }
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + phone));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callIntent);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
    /*动态权限*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==MY_PERMISSIONS_REQUEST_CALL_PHONE){
            String phone = "963666";
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }else {
                Toast.makeText(mContext,"授权失败",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void Goshare() {
        Intent intent=new Intent(mContext, ShareMenuActivity.class);
        startActivity(intent);
    }
    private void gologin() {
        Intent login=new Intent(mContext, LoginActivity.class);
        startActivity(login);
    }
    @Override
    public void onScrollChanged(MyScrollview scrollView, int l, int t, int oldl, int oldt) {
        if (t <= 0) {   //设置标题的背景颜色
            Lnavigation.setBackgroundColor(Color.argb(0, 0, 255, 0));
            tv_naviga.setTextColor(Color.argb(0, 0, 255, 0));
        } else if (t > 0 && t <= bgHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) t / bgHeight;
            float alpha = (255 * scale);
            tv_naviga.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            Lnavigation.setBackgroundColor(Color.argb((int) alpha, 249, 99, 49));
        } else {    //滑动到banner下面设置普通颜色
            Lnavigation.setBackgroundColor(Color.argb(255, 249, 99, 49));
            tv_naviga.setTextColor(Color.argb(255, 255, 255, 255));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);

    };
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
}
