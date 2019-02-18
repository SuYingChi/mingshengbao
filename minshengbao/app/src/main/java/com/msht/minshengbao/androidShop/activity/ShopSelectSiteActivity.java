package com.msht.minshengbao.androidShop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lxy.dexlibs.ComplexRecyclerViewAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.adapter.HaveHeadRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.MsgUserListAdapter;
import com.msht.minshengbao.androidShop.adapter.SiteListAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.customerview.SelectSiteAreaDialog;
import com.msht.minshengbao.androidShop.customerview.SelectWuliuCompanyDialog;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.InvContentItemBean;
import com.msht.minshengbao.androidShop.shopBean.InvItemBean;
import com.msht.minshengbao.androidShop.shopBean.SiteBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.IGetInvContentView;
import com.msht.minshengbao.androidShop.viewInterface.ISiteListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopSelectSiteActivity extends ShopBaseActivity implements ISiteListView, IGetInvContentView {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.site_area)
    TextView tvSiteArea;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rlt)
    RelativeLayout rlt;
    private List<SiteBean.DatasBean.AddrListBean> dataList= new ArrayList<SiteBean.DatasBean.AddrListBean>();
    private List<SiteBean.DatasBean.AddrListBean> allSiteList= new ArrayList<SiteBean.DatasBean.AddrListBean>();
    private SiteListAdapter adapter;
    private SelectSiteAreaDialog dialog;
    private List<InvContentItemBean> areaList=new ArrayList<InvContentItemBean>();

    @Override
    protected void setLayout() {
      setContentView(R.layout.shop_selecte_site);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(this),0,0);
        adapter = new SiteListAdapter(this, R.layout.item_shop_site,dataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(linearLayoutManager);
        rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        if(getIntent().getBooleanExtra("onClick",true)) {
            adapter.setOnItemClickListener(new HaveHeadRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    SiteBean.DatasBean.AddrListBean siteBean = dataList.get(position);
                    Intent intent = new Intent();
                    intent.putExtra("site", siteBean);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
        rcl.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInvDialog();
            }
        });
        ShopPresenter.getSiteList(this);
    }
    @Override
    public void onGetSiteListSuccess(String s) {
        SiteBean siteBean = JsonUtil.toBean(s, SiteBean.class);
        if(siteBean!=null) {
            dataList.addAll(siteBean.getDatas().getAddr_list());
            allSiteList.addAll(siteBean.getDatas().getAddr_list());
            adapter.notifyDataSetChanged();
            InvContentItemBean invItemBean = new InvContentItemBean("所有区域", true);
            areaList.add(invItemBean);
            for (SiteBean.DatasBean.AddrListBean ad : dataList) {
                String area = ad.getDlyp_area_name();
                InvContentItemBean bean = new InvContentItemBean(area, false);
                if (!areaList.contains(bean)) {
                    areaList.add(bean);
                }
            }
        }
    }
    private void showInvDialog() {
        if (!isFinishing() && dialog == null) {
            dialog = new SelectSiteAreaDialog(this, this, areaList);
            dialog.show();
        } else if (!isFinishing() && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onGetInvContentList(String s) {

    }

    @Override
    public void onSelectedInvContentItem(int position) {
        for (int i = 0; i < areaList.size(); i++) {
            if (i == position) {
                areaList.get(i).setSelected(true);
                String invContent = areaList.get(i).getContent();
                tvSiteArea.setText(invContent);
                String sitearea = (String) tvSiteArea.getText();
                if(!"所有区域".equals(sitearea)) {
                    dataList.clear();
                    for (SiteBean.DatasBean.AddrListBean address : allSiteList) {
                        if (address.getDlyp_area_name().equals(sitearea)) {
                            dataList.add(address);
                        }
                    }
                }else {
                    dataList.clear();
                    dataList.addAll(allSiteList);
                }
                adapter.notifyDataSetChanged();
            } else {
                areaList.get(i).setSelected(false);
            }
        }
        dialog.notifyRcl();
    }

}
