package com.msht.minshengbao.functionActivity.fragment;
import android.Manifest;
import android.app.Activity;
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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.msht.minshengbao.Base.BaseHomeFragment;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.adapter.MyFunctionAdapter;
import com.msht.minshengbao.functionActivity.GasService.GasServerOrderActivity;
import com.msht.minshengbao.functionActivity.Invoice.InvoiceHomeActivity;
import com.msht.minshengbao.functionActivity.MessageCenterActivity;
import com.msht.minshengbao.functionActivity.MyActivity.AddressManageActivity;
import com.msht.minshengbao.functionActivity.MyActivity.ConsultRecommendActivity;
import com.msht.minshengbao.functionActivity.MyActivity.CustomerNoManageActivity;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.functionActivity.MyActivity.MoreSettingActivity;
import com.msht.minshengbao.functionActivity.MyActivity.MySettingActivity;
import com.msht.minshengbao.functionActivity.MyActivity.MyWalletActivity;
import com.msht.minshengbao.functionActivity.MyActivity.ShareMenuActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ACache;
import com.msht.minshengbao.Utils.CallPhoneUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.ButtonUI.MenuItemM;
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
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/7/2  
 */
public class LoginMyFrag extends BaseHomeFragment implements View.OnClickListener, MyScrollview.ScrollViewListener {
    private MyScrollview    myScrollview;
    private LinearLayout    layoutNavigation;
    private RelativeLayout  layoutMySetting;
    private TextView tvNavigation;
    private SimpleDraweeView mPortrait;
    private MenuItemM btnMessage;
    private String nickname;
    private int   bgHeight;
    private Activity mActivity;
    /**最大显示消息数 **/
    private static final int MAX_MASSAGE=99;
    private MyNoScrollGridView mGridView;
    private MyFunctionAdapter mAdapter;
    private ArrayList<HashMap<String, Integer>> mList = new ArrayList<HashMap<String, Integer>>();
    private final String mPageName = "首页_个人中心";
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    public LoginMyFrag() {}
    @Override
    public View initFindView() {
        if(mRootView==null){
            mRootView= LayoutInflater.from(mContext).inflate(R.layout.fragment_loginafter_my,null,false);
        }
        mActivity=getActivity();
        String avatarUrl =SharedPreferencesUtil.getAvatarUrl(mContext,SharedPreferencesUtil.AvatarUrl,"");
        nickname=SharedPreferencesUtil.getNickName(mContext,SharedPreferencesUtil.NickName,"");
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            mRootView.findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        initView(mRootView);
        Uri uri = Uri.parse(avatarUrl);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                //. 其他设置（如果有的话）
                .build();
        mPortrait.setController(controller);
        initListeners();
        mAdapter=new MyFunctionAdapter(mContext,mList);
        mGridView.setAdapter(mAdapter);
        initData();
        goActivity();
        return mRootView;
    }
    private void initView(View view) {
        myScrollview=(MyScrollview)view.findViewById(R.id.id_scrollview);
        mGridView=(MyNoScrollGridView)view.findViewById(R.id.id_function_view);
        layoutNavigation =(LinearLayout) view.findViewById(R.id.id_li_navigation);
        layoutMySetting =(RelativeLayout) view.findViewById(R.id.id_re_gosetting);
        layoutMySetting.setOnClickListener(this);
        btnMessage=(MenuItemM)view.findViewById(R.id.id_mim_message);
        view.findViewById(R.id.id_re_hotline).setOnClickListener(this);
        view.findViewById(R.id.id_re_consult).setOnClickListener(this);
        view.findViewById(R.id.id_re_setting).setOnClickListener(this);
        view.findViewById(R.id.id_right_massage).setOnClickListener(this);
        mPortrait=(SimpleDraweeView)view.findViewById(R.id.id_portrait);
        tvNavigation =(TextView)view.findViewById(R.id.id_tv_naviga);
        TextView tvNickname =(TextView)view.findViewById(R.id.id_nickname);
        if (!TextUtils.isEmpty(nickname)){
            tvNickname.setText(nickname);
        }else {
            String userName=SharedPreferencesUtil.getNickName(mActivity,SharedPreferencesUtil.UserName,"");
            if (RegularExpressionUtil.isPhone(userName)){
                userName=userName.substring(0,3)+"****"+userName.substring(7,userName.length());
            }
            tvNickname.setText(userName);

        }
        onUnReadMessage();
        btnMessage.setOnClickListener(new MenuItemM.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMessageCenter();
            }
        });
    }
    private void onUnReadMessage() {
        if (VariableUtil.messageNum>=MAX_MASSAGE){
            btnMessage.setUnReadCount(MAX_MASSAGE);
        }else {
            btnMessage.setUnReadCount(VariableUtil.messageNum);
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
    private void goActivity() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int code=mList.get(position).get("code");
                switch (code){
                    case 0:
                        if (isLoginState(mContext)){
                            goMyWallet();
                        }else {
                            AppActivityUtil.onStartLoginActivity(mContext,"");
                        }
                        break;
                    case 1:
                        if (isLoginState(mContext)){
                            goGasServer();
                        }else {
                            AppActivityUtil.onStartLoginActivity(mContext,"");
                        }
                        break;
                    case 2:
                        if (isLoginState(mContext)){
                            goCustomerNo();
                        }else {
                            AppActivityUtil.onStartLoginActivity(mContext,"");
                        }
                        break;
                    case 3:
                        if (isLoginState(mContext)){
                            goInvoice();
                        }else {
                            AppActivityUtil.onStartLoginActivity(mContext,"");
                        }
                        break;
                    case 4:
                        if (isLoginState(mContext)){
                            goManage();
                        }else {
                            AppActivityUtil.onStartLoginActivity(mContext,"");
                        }
                        break;
                    case 5:
                        goShare();
                        break;
                    default:
                        break;
                }
            }
        });
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
        } else if (t >0 && t <= bgHeight) {
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
                if (isLoginState(mContext)){
                    goSetting();
                }else {
                    AppActivityUtil.onStartLoginActivity(mContext,"");
                }
                break;
            case R.id.id_re_consult:
                if (isLoginState(mContext)){
                    goConsult();
                }else {
                    AppActivityUtil.onStartLoginActivity(mContext,"");
                }
                break;
            case R.id.id_re_hotline:
                hotLine();
                break;
            case R.id.id_re_setting:
                goMoreSetting();
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
        btnMessage.setUnReadCount(0);
    }
    private void hotLine() {
        final String phone = "963666";
        new PromptDialog.Builder(mContext)
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
    private void goConsult() {
        Intent intent=new Intent(mContext, ConsultRecommendActivity.class);
        startActivity(intent);
    }
    private void goShare() {
        Intent intent=new Intent(mContext, ShareMenuActivity.class);
        startActivity(intent);
    }
    private void goManage() {
        Intent intent=new Intent(mContext, AddressManageActivity.class);
        startActivity(intent);
    }
    private void goMyWallet() {
        Intent intent=new Intent(mContext, MyWalletActivity.class);
        startActivityForResult(intent,0x004);
    }
    private void goInvoice() {
        Intent intent=new Intent(mContext, InvoiceHomeActivity.class);
        startActivity(intent);
    }
    private void goGasServer() {
        Intent intent=new Intent(mContext, GasServerOrderActivity.class);
        startActivity(intent);
    }
    private void goSetting() {
        Intent intent=new Intent(mContext, MySettingActivity.class);
        startActivityForResult(intent,1);
    }
    private void goCustomerNo() {
        Intent intent=new Intent(mContext, CustomerNoManageActivity.class);
        startActivity(intent);
    }
    private void goMoreSetting() {
        Intent intent=new Intent(mContext, MoreSettingActivity.class);
        startActivityForResult(intent,0x005);
    }
    private void requestLimit(final String phone) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                CallPhoneUtil.callPhone(mContext,phone);
            } else {
                MPermissionUtils.requestPermissionsResult(this, MY_PERMISSIONS_REQUEST_CALL_PHONE, new String[]{Manifest.permission.CALL_PHONE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int code) {
                        if (code==MY_PERMISSIONS_REQUEST_CALL_PHONE){
                            CallPhoneUtil.callPhone(mContext,phone);
                        }
                    }
                    @Override
                    public void onPermissionDenied(int code) {
                        ToastUtil.ToastText(mContext,"没有权限您将无法进行相关操作！");
                    }
                });
            }
        }else {
            CallPhoneUtil.callPhone(mContext,phone);
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
