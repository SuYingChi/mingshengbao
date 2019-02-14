package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.ShopSelectSiteActivity;
import com.msht.minshengbao.androidShop.shopBean.SiteBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class SiteListAdapter extends HaveHeadRecyclerAdapter<SiteBean.DatasBean.AddrListBean>{


    public SiteListAdapter(Context context, int layoutId, List<SiteBean.DatasBean.AddrListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerHolder holder, SiteBean.DatasBean.AddrListBean addrListBean, final int position) {
      holder.setText(R.id.sitename,addrListBean.getDlyp_address_name());
      holder.setText(R.id.sitePhone,addrListBean.getDlyp_mobile());
      holder.setText(R.id.siteAddress,addrListBean.getDlyp_address());
      holder.getConvertView().setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              onItemClickListener.onItemClick(position);
          }
      });
    }
}
