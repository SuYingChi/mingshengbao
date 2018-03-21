package com.msht.minshengbao.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.FunctionView.fragmeht.IcCard;
import com.msht.minshengbao.FunctionView.fragmeht.PayRecord;
import com.msht.minshengbao.FunctionView.fragmeht.RechargeFrag;
import com.msht.minshengbao.FunctionView.fragmeht.SelfhelpPay;

/**
 * Created by hong on 2017/11/8.
 */

public class IccardViewAdapter extends FragmentPagerAdapter {
    private String fragments[] = {"IC卡缴费","充值记录"};
    private String id;
    private String password;
    private IcCard mIcCard;
    private RechargeFrag rechargeFrag;
    public IccardViewAdapter(FragmentManager fm, Context applicationContext, String userId, String passwords) {
        super(fm);
        this.id=userId;
        this.password=passwords;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("id",id);//传值昵称
        bundle.putString("password",password);
        switch (position){
            case 0:
                mIcCard=new IcCard();
                mIcCard.setArguments(bundle);
                return mIcCard;
            case 1:
                rechargeFrag=new RechargeFrag();
                rechargeFrag.setArguments(bundle);
                return rechargeFrag;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return fragments.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position];
    }
}
