package com.msht.minshengbao.androidShop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.customerview.SelectWuliuCompanyDialog;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.InvContentItemBean;
import com.msht.minshengbao.androidShop.shopBean.WuliuCompanyItemBean;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IGetWuliuView;
import com.msht.minshengbao.androidShop.viewInterface.IReturnGoodSendGoodView;
import com.msht.minshengbao.androidShop.viewInterface.OnDissmissLisenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ReturnGoodSendGoodActivity extends ShopBaseActivity implements IGetWuliuView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    @BindView(R.id.ll_danhao)
    LinearLayout lldanhao;
    @BindView(R.id.ll_seclect)
    LinearLayout llseclect;
    @BindView(R.id.wuliu)
    TextView tvwuliu;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.post)
    TextView tvPost;
    private String returnId;
    private List<WuliuCompanyItemBean> companyList = new ArrayList<WuliuCompanyItemBean>();
    private SelectWuliuCompanyDialog selectWuliuCompanyDialog;
    private String expressId;

    @Override
    protected void setLayout() {
        setContentView(R.layout.return_good_send);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(this),0,0);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        returnId = getIntent().getStringExtra("data");
        ShopPresenter.getRetrunGoodSendInit(this);
        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et.getText().toString())) {
                    PopUtil.showComfirmDialog(ReturnGoodSendGoodActivity.this, "", "请填写物流单号", "", "知道了", null, null, true);
                } else {
                    ShopPresenter.retrunGoodSendPost(ReturnGoodSendGoodActivity.this);
                }

            }
        });
    }

    @Override
    public void onSelectedItem(int position) {
        for (int i = 0; i < companyList.size(); i++) {
            if (i == position) {
                companyList.get(i).setSelected(true);
                String invContent = companyList.get(i).getContent();
                expressId = companyList.get(i).getExpressId();
                tvwuliu.setText(invContent);
            } else {
                companyList.get(i).setSelected(false);
            }
        }
        selectWuliuCompanyDialog.notifyRcl();
    }

    @Override
    public String getReturnId() {
        return returnId;
    }

    @Override
    public void onGetReturnGoodInitSuccess(String s) {
        try {
            WuliuCompanyItemBean wuliuCompanyItemBean;
            JSONObject obj = new JSONObject(s);
            JSONObject data = obj.optJSONObject("datas");
            JSONArray array = data.optJSONArray("express_list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject objj = array.optJSONObject(i);
                String express_id = objj.optString("express_id");
                String express_name = objj.optString("express_name");
                if (i == 3) {
                    wuliuCompanyItemBean = new WuliuCompanyItemBean(express_name, express_id, true);
                    expressId = express_id;
                    tvwuliu.setText(express_name);
                } else {
                    wuliuCompanyItemBean = new WuliuCompanyItemBean(express_name, express_id, false);
                }
                companyList.add(wuliuCompanyItemBean);
            }
            llseclect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInvDialog();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getExpress_id() {
        return expressId;
    }

    @Override
    public String invoice_no() {
        return et.getText().toString();
    }

    @Override
    public void onPostReturnGoodSuccess(String s) {
        PopUtil.showAutoDissHookDialog(this, "提交发货信息成功", 0, new OnDissmissLisenter() {
            @Override
            public void onDissmiss() {
                finish();
            }
        });
    }

    private void showInvDialog() {
        if (!isFinishing() && selectWuliuCompanyDialog == null) {
            selectWuliuCompanyDialog = new SelectWuliuCompanyDialog(this, this, companyList);
            selectWuliuCompanyDialog.show();
        } else if (!isFinishing() && !selectWuliuCompanyDialog.isShowing()) {
            selectWuliuCompanyDialog.show();
        }
    }
}
