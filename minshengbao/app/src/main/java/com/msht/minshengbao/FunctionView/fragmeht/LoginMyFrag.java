package com.msht.minshengbao.FunctionView.fragmeht;
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
import android.support.annotation.Nullable;
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
import com.msht.minshengbao.Callback.ResultImgListenner;
import com.msht.minshengbao.FunctionView.GasService.GasServerOrder;
import com.msht.minshengbao.FunctionView.GasService.Gasqianxian;
import com.msht.minshengbao.FunctionView.Invoice.InvoiceOpen;
import com.msht.minshengbao.FunctionView.Myview.AboutMine;
import com.msht.minshengbao.FunctionView.Myview.AddressManage;
import com.msht.minshengbao.FunctionView.Myview.ConsultRecommend;
import com.msht.minshengbao.FunctionView.Myview.CustomerNoManage;
import com.msht.minshengbao.FunctionView.Myview.MoreSetting;
import com.msht.minshengbao.FunctionView.Myview.Mysetting;
import com.msht.minshengbao.FunctionView.Myview.Mywallet;
import com.msht.minshengbao.FunctionView.Myview.ShareMenuActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ACache;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.CircleImageView;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.ViewUI.widget.MyScrollview;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginMyFrag extends Fragment implements View.OnClickListener, MyScrollview.ScrollViewListener {
    private MyScrollview    myScrollview;
    private LinearLayout    Lnavigation;
    private RelativeLayout  Rmysetting;
    private CircleImageView circleImageView;
    private TextView tv_naviga;
    private TextView tv_nickname;
    private String userId;
    private String password;
    private String avatarurl;
    private String nickname;
    private Bitmap myavatar=null;
    private ACache mCache;
    private int bgHeight;// 上半身的高度
    private static final int SUCCESS=1;
    private static final int FAILURE=2;
    private Context mContext;
    private   boolean lstate=false;
    private MyNoScrollGridView mGridView;
    private MyfunctionAdapter  mAdapter;
    private ArrayList<HashMap<String, Integer>> mList = new ArrayList<HashMap<String, Integer>>();
    private final String mPageName = "首页_个人中心";
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    public LoginMyFrag() {}
    Handler getimgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    myavatar = (Bitmap)msg.obj;
                    if (myavatar==null){   //未设置头像时
                        circleImageView.setImageResource(R.drawable.potrait);
                    }else {
                        circleImageView.setImageBitmap(myavatar);
                        mCache.put("avatarimg", myavatar);
                    }
                    break;
                case FAILURE:
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_loginafter_my, container, false);
        mContext=getActivity();
        mCache=ACache.get(mContext);
        userId= SharedPreferencesUtil.getUserId(mContext, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(mContext, SharedPreferencesUtil.Password,"");
        avatarurl=SharedPreferencesUtil.getAvatarUrl(mContext,SharedPreferencesUtil.AvatarUrl,"");
        nickname=SharedPreferencesUtil.getNickName(mContext,SharedPreferencesUtil.NickName,"");
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            view.findViewById(R.id.id_view).setVisibility(View.GONE);
        }
        initView(view);
        myavatar=mCache.getAsBitmap("avatarimg");
        if (myavatar!=null){
            circleImageView.setImageBitmap(myavatar);
        }else {
            if (avatarurl!=null&&!avatarurl.equals("")){
                initgetavatar();
            }
        }
        initListeners();
        mAdapter=new MyfunctionAdapter(mContext,mList);
        mGridView.setAdapter(mAdapter);
        initData();
        GoActivity();
        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==4){
                    avatarurl=SharedPreferencesUtil.getAvatarUrl(mContext,SharedPreferencesUtil.AvatarUrl,"");
                    myavatar=mCache.getAsBitmap("avatarimg");
                    if (myavatar!=null){
                        circleImageView.setImageBitmap(myavatar);
                    }else {
                        if (avatarurl!=null&&!avatarurl.equals("")){
                            initgetavatar();
                        }
                    }
                }
                break;
        }
    }
    private void initView(View view) {
        myScrollview=(MyScrollview)view.findViewById(R.id.id_scrollview);
        mGridView=(MyNoScrollGridView)view.findViewById(R.id.id_function_view);
        Lnavigation=(LinearLayout) view.findViewById(R.id.id_li_navigation);
        Rmysetting=(RelativeLayout) view.findViewById(R.id.id_re_gosetting);
        Rmysetting.setOnClickListener(this);
        view.findViewById(R.id.id_re_hotline).setOnClickListener(this);
        view.findViewById(R.id.id_re_consult).setOnClickListener(this);
        view.findViewById(R.id.id_re_setting).setOnClickListener(this);
        circleImageView=(CircleImageView)view.findViewById(R.id.id_potrait);
        tv_naviga=(TextView)view.findViewById(R.id.id_tv_naviga);
        tv_nickname=(TextView)view.findViewById(R.id.id_nickname);
        tv_nickname.setText(nickname);
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
                        Gomywallet();
                        break;
                    case 1:
                        Gogasserver();
                        break;
                    case 2:
                        Gocustoerno();
                        break;
                    case 3:
                        Goinvoice();
                        break;
                    case 4:
                        Gomanage();
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
    private void initgetavatar() {
        HttpUrlconnectionUtil.BitmapGet(avatarurl, new ResultImgListenner() {
            @Override
            public void Success(Bitmap bitmap) {
                Message msg = new Message();
                msg.obj = bitmap;
                msg.what = SUCCESS;
                getimgHandler.sendMessage(msg);
            }
            @Override
            public void Failure(String string) {
                Message msg = new Message();
                msg.obj =string;
                msg.what =FAILURE;
                getimgHandler.sendMessage(msg);
            }
        });
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
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_re_gosetting:
                Gosetting();
                break;
            case R.id.id_re_consult:
                Goconsult();
                break;
            case R.id.id_re_hotline:
                hotline();
                break;
            case R.id.id_re_setting:
                GoMoresetting();
                break;
            default:
                break;
        }
    }
    private void hotline() {
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
                Toast.makeText(mContext,"未授权不可拨打电话",Toast.LENGTH_SHORT).show();
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
        Intent intent=new Intent(mContext, AddressManage.class);
        startActivity(intent);
    }
    private void Gomywallet() {
        Intent intent=new Intent(mContext, Mywallet.class);
        startActivity(intent);
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
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
       // MobclickAgent.onPageStart(mPageName);

    };
    @Override
    public void onPause() {
        super.onPause();
       // MobclickAgent.onPageEnd(mPageName);
    }
}
