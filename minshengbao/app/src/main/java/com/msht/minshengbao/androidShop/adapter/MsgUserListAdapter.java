package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxy.dexlibs.ComplexRecyclerViewAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopChatUserBean;
import com.msht.minshengbao.androidShop.util.GlideUtil;

import java.util.List;

public class MsgUserListAdapter extends ComplexRecyclerViewAdapter {

    public MsgUserListAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    protected View onBindLayout(LayoutInflater layoutInflater, ViewGroup parent, int i) {
        return inflater.inflate(R.layout.item_msg_user, parent, false);
    }

    @Override
    protected void onBindViewData(Object o, ComplexViewHolder complexViewHolder, int i) {
        Object item = datas.get(i);
        if(item instanceof ShopChatUserBean){
            String avatar = ((ShopChatUserBean) item).getAvatar();
            GlideUtil.loadRemoteCircleImg(mContext,complexViewHolder.getImageView(R.id.iv),avatar);
            complexViewHolder.setText(R.id.time,((ShopChatUserBean) item).getTime());
            complexViewHolder.setText(R.id.name, ((ShopChatUserBean) item).getU_name());
            complexViewHolder.setText(R.id.price,((ShopChatUserBean) item).getT_msg());
        }
    }
}
