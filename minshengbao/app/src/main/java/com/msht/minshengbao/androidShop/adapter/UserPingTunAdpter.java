package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.UserPinTunBean;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

public class UserPingTunAdpter extends HaveHeadRecyclerAdapter<UserPinTunBean>{
    public UserPingTunAdpter(Context context, List<UserPinTunBean> datas) {
        super(context, R.layout.item_user_pintun, datas);
    }

    @Override
    public void convert(RecyclerHolder holder, UserPinTunBean userPinTunBean, int position) {
            GlideUtil.loadRemoteCircleImg(context,(ImageView) holder.getView(R.id.iv),userPinTunBean.getAvatar());
    }

    @Override
    public int getItemCount() {
        return datas.size()>3?3:datas.size();
    }
}
