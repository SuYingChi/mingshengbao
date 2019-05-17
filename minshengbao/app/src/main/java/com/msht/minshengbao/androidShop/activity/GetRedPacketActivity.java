package com.msht.minshengbao.androidShop.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.ShopConstants;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IGetCodekeyView;
import com.msht.minshengbao.androidShop.viewInterface.IGetRedPacketView;
import com.msht.minshengbao.androidShop.viewInterface.IGetYanzhengCodeView;


import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class GetRedPacketActivity extends ShopBaseActivity implements IGetCodekeyView, IGetYanzhengCodeView, IGetRedPacketView {
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.kami)
    EditText etKami;
    @BindView(R.id.etcode)
    EditText etCode;
    private String codekey;
    @BindView(R.id.ivcode)
     ImageView ivcode;
    @BindView(R.id.regetcode)
    TextView regetcode;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.get_redpacket)
    TextView get_redpacket;

    @Override
    protected void setLayout() {
        setContentView(R.layout.get_red_packet);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(this),0,0);
       //默认弹出字母数字键盘
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etKami.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] ac = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                return ac;
            }
        });
        etCode.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] ac = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                return ac;
            }
        });
       ShopPresenter.getCodekey(this);
        regetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopPresenter.getCodekey(GetRedPacketActivity.this);
            }
        });
        get_redpacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etKami.getText().toString())){
                    PopUtil.toastInBottom("请输入卡密");
                }else if(TextUtils.isEmpty(etCode.getText().toString())){
                    PopUtil.toastInBottom("请输入右侧验证码");
                }else {
                    ShopPresenter.getRedPacket(GetRedPacketActivity.this);
                }
            }
        });
    }

    @Override
    public void onGetCodeKey(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            codekey  = obj.optJSONObject("datas").optString("codekey");
            GlideUtil.loadByHeightFitWidth(this,ivcode, ShopConstants.CODE_BITMAP+"&k="+codekey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetYanzhengCode(Bitmap s) {
        ivcode.setImageBitmap(s);
    }

    @Override
    public String getCodeKey() {
        return codekey;
    }


    @Override
    public String getPwdCode() {
        return etKami.getText().toString();
    }

    @Override
    public void onGetRedPacket(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            if("1".equals(obj.optString("datas"))){
                PopUtil.showAutoDissHookDialog(this,"领取红包成功",0);
            }else {
                PopUtil.toastInBottom("领取红包失败");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCodekey() {
        return codekey;
    }

    @Override
    public String getCaptcha() {
        return etCode.getText().toString();
    }
}
