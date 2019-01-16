package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
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
           if(!TextUtils.isEmpty(((ShopChatUserBean) item).getMsg_count())&&!((ShopChatUserBean) item).getMsg_count().equals("0")){
               complexViewHolder.getView(R.id.vhint).setVisibility(View.VISIBLE);
               complexViewHolder.setText(R.id.vhint,((ShopChatUserBean) item).getMsg_count());
           }else {
               complexViewHolder.getView(R.id.vhint).setVisibility(View.INVISIBLE);
           }
            GlideUtil.loadRemoteCircleImg(mContext,complexViewHolder.getImageView(R.id.iv),avatar);
            complexViewHolder.setText(R.id.time,((ShopChatUserBean) item).getTime());
            complexViewHolder.setText(R.id.name, ((ShopChatUserBean) item).getU_name());
            String tMsg = ((ShopChatUserBean) item).getT_msg();
            if(tMsg.startsWith(":")&&tMsg.endsWith(":")){
                String gifName = tMsg.substring(1, tMsg.length() - 1);
                int gifId = mContext.getResources().getIdentifier(gifName, "raw", mContext.getPackageName());
                Glide.with(mContext).asGif().load(gifId).into(complexViewHolder.getImageView(R.id.emoji));
            }else {
                complexViewHolder.setText(R.id.price, ((ShopChatUserBean) item).getT_msg());
            }
        }
    }
}
