package com.msht.minshengbao.FunctionActivity.fragment;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.msht.minshengbao.Adapter.MyfunctionAdapter;
import com.msht.minshengbao.FunctionActivity.GasService.GasServerOrder;
import com.msht.minshengbao.FunctionActivity.Invoice.InvoiceOpen;
import com.msht.minshengbao.FunctionActivity.MessageCenterActivity;
import com.msht.minshengbao.FunctionActivity.MyActivity.AddressManageActivity;
import com.msht.minshengbao.FunctionActivity.MyActivity.ConsultRecommend;
import com.msht.minshengbao.FunctionActivity.MyActivity.CustomerNoManage;
import com.msht.minshengbao.FunctionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.FunctionActivity.MyActivity.MoreSetting;
import com.msht.minshengbao.FunctionActivity.MyActivity.Mysetting;
import com.msht.minshengbao.FunctionActivity.MyActivity.Mywallet;
import com.msht.minshengbao.FunctionActivity.MyActivity.ShareMenuActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ACache;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.CircleImageView;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.ViewUI.widget.MyScrollview;
import com.umeng.analytics.MobclickAgent;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginMyFrag extends Fragment implements View.OnClickListener, MyScrollview.ScrollViewListener {
    private MyScrollview    myScrollview;
    private LinearLayout    layoutNavigation;
    private RelativeLayout  layoutMySetting;
    private CircleImageView circleImageView;
    private TextView tvNavigation;
    private TextView tvNickname;
    private TextView tvMessageNum;
    private String avatarUrl;
    private String nickname;
    private Bitmap myAvatar =null;
    private ACache mCache;
    private int   bgHeight;
    private Context mContext;
    private MyNoScrollGridView mGridView;
    private MyfunctionAdapter  mAdapter;
    private ArrayList<HashMap<String, Integer>> mList = new ArrayList<HashMap<String, Integer>>();
    private final String mPageName = "首页_个人中心";
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    private final GetImageHandler getImageHandler=new GetImageHandler(this);
    public LoginMyFrag() {}
    private static class GetImageHandler extends Handler{
        private WeakReference<LoginMyFrag> mWeakReference;
        public GetImageHandler(LoginMyFrag loginMyFrag) {
            mWeakReference = new WeakReference<LoginMyFrag>(loginMyFrag);
        }
        @Override
        public void handleMessage(Message msg) {
            final LoginMyFrag reference =mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null||reference.isDetached()) {
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    reference.myAvatar = (Bitmap)msg.obj;
                    if (reference.myAvatar ==null){
                        reference.circleImageView.setImageResource(R.drawable.potrait);
                    }else {
                        reference.circleImageView.setImageBitmap(reference.myAvatar);
                        reference.mCache.put("avatarimg", reference.myAvatar);
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.mContext,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_loginafter_my, container, false);
        mContext=getActivity();
        mCache=ACache.get(mContext);
        avatarUrl =SharedPreferencesUtil.getAvatarUrl(mContext,SharedPreferencesUtil.AvatarUrl,"");
        nickname=SharedPreferencesUtil.getNickName(mContext,SharedPreferencesUtil.NickName,"");
        VariableUtil.loginStatus= SharedPreferencesUtil.getLstate(mContext, SharedPreferencesUtil.Lstate, false);
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            view.findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        initView(view);
        myAvatar =mCache.getAsBitmap("avatarimg");
        if (myAvatar !=null){
            circleImageView.setImageBitmap(myAvatar);
        }else {
            if (avatarUrl !=null&&!avatarUrl.equals("")){
                onGetAvatar();
            }
        }
        initListeners();
        mAdapter=new MyfunctionAdapter(mContext,mList);
        mGridView.setAdapter(mAdapter);
        initData();
        GoActivity();
        return  view;
    }
    private void initView(View view) {
        myScrollview=(MyScrollview)view.findViewById(R.id.id_scrollview);
        mGridView=(MyNoScrollGridView)view.findViewById(R.id.id_function_view);
        layoutNavigation =(LinearLayout) view.findViewById(R.id.id_li_navigation);
        layoutMySetting =(RelativeLayout) view.findViewById(R.id.id_re_gosetting);
        layoutMySetting.setOnClickListener(this);
        tvMessageNum=(TextView)view.findViewById(R.id.id_message_num);
        view.findViewById(R.id.id_re_hotline).setOnClickListener(this);
        view.findViewById(R.id.id_re_consult).setOnClickListener(this);
        view.findViewById(R.id.id_re_setting).setOnClickListener(this);
        view.findViewById(R.id.id_right_massage).setOnClickListener(this);
        circleImageView=(CircleImageView)view.findViewById(R.id.id_potrait);
        tvNavigation =(TextView)view.findViewById(R.id.id_tv_naviga);
        tvNickname =(TextView)view.findViewById(R.id.id_nickname);
        tvNickname.setText(nickname);
        onUnReadMessage();
    }

    private void onUnReadMessage() {
        if ( VariableUtil.messageNum!=0){
            tvMessageNum.setText(String.valueOf(VariableUtil.messageNum));
            tvMessageNum.setVisibility(View.VISIBLE);
        }else {
            tvMessageNum.setVisibility(View.GONE);
        }
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
    private void GoActivity() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int code=mList.get(position).get("code");
                switch (code){
                    case 0:
                        if (VariableUtil.loginStatus){
                            Gomywallet();
                        }else {
                            goLogin();
                        }
                        break;
                    case 1:
                        if (VariableUtil.loginStatus){
                            Gogasserver();
                        }else {
                            goLogin();
                        }
                        break;
                    case 2:
                        if (VariableUtil.loginStatus){
                            Gocustoerno();
                        }else {
                            goLogin();
                        }
                        break;
                    case 3:
                        if (VariableUtil.loginStatus){
                            Goinvoice();
                        }else {
                            goLogin();
                        }
                        break;
                    case 4:
                        if (VariableUtil.loginStatus){
                            Gomanage();
                        }else {
                            goLogin();
                        }
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
    private void goLogin() {
        Intent login=new Intent(mContext, LoginActivity.class);
        startActivity(login);
    }
    private void onGetAvatar() {
        SendrequestUtil.getBitmapFromService(avatarUrl,getImageHandler);
    }
    private void initListeners() {
        ViewTreeObserver vto = layoutMySetting.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutMySetting.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                bgHeight = layoutMySetting.getHeight();
                inisetListaner();
            }
        });
    }
    private void inisetListaner() {
        myScrollview.setScrollViewListener(this);
    }
    @Override
    public void onScrollChanged(MyScrollview scrollView, int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            //设置标题的背景颜色
            layoutNavigation.setBackgroundColor(Color.argb(0, 0, 255, 0));
            tvNavigation.setTextColor(Color.argb(0, 0, 255, 0));
            //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
        } else if (t > 0 && t <= bgHeight) {
            float scale = (float) t / bgHeight;
            float alpha = (255 * scale);
            tvNavigation.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            layoutNavigation.setBackgroundColor(Color.argb((int) alpha, 249, 99, 49));
        } else {    //滑动到banner下面设置普通颜色
            layoutNavigation.setBackgroundColor(Color.argb(255, 249, 99, 49));
            tvNavigation.setTextColor(Color.argb(255, 255, 255, 255));
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_gosetting:
                if (VariableUtil.loginStatus){
                    Gosetting();
                }else {
                   goLogin();
                }
                break;
            case R.id.id_re_consult:
                if (VariableUtil.loginStatus){
                    Goconsult();
                }else {
                    goLogin();
                }
                break;
            case R.id.id_re_hotline:
                hotLine();
                break;
            case R.id.id_re_setting:
                GoMoresetting();
                break;
            case R.id.id_right_massage:
                goMessageCenter();
                break;
            default:
                break;
        }
    }

    private void goMessageCenter() {
        Intent intent=new Intent(mContext,MessageCenterActivity.class);
        startActivity(intent);
        tvMessageNum.setVisibility(View.GONE);
    }
    private void hotLine() {
        final String phone = "963666";
        new PromptDialog.Builder(mContext)
                .setTitle("服务热线")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(phone)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                    }
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
    /**
     * 动态权限
    */
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
                ToastUtil.ToastText(mContext,"未授权不可拨打电话");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void Goconsult() {
        Intent intent=new Intent(mContext, ConsultRecommend.class);
        startActivity(intent);
    }
    private void Goshare() {
        Intent intent=new Intent(mContext, ShareMenuActivity.class);
        startActivity(intent);
    }
    private void Gomanage() {
        Intent intent=new Intent(mContext, AddressManageActivity.class);
        startActivity(intent);
    }
    private void Gomywallet() {
        Intent intent=new Intent(mContext, Mywallet.class);
        startActivityForResult(intent,0x004);
    }
    private void Goinvoice() {
        Intent intent=new Intent(mContext, InvoiceOpen.class);
        startActivity(intent);
    }
    private void Gogasserver() {
        Intent intent=new Intent(mContext, GasServerOrder.class);
        startActivity(intent);
    }
    private void Gosetting() {
        Intent intent=new Intent(mContext, Mysetting.class);
        startActivityForResult(intent,1);
    }

    private void Gocustoerno() {
        Intent intent=new Intent(mContext, CustomerNoManage.class);
        startActivity(intent);
    }
    private void GoMoresetting() {
        Intent intent=new Intent(mContext, MoreSetting.class);
        startActivityForResult(intent,0x005);
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
