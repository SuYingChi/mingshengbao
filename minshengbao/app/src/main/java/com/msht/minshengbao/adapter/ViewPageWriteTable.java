package com.msht.minshengbao.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msht.minshengbao.functionActivity.fragment.SelfWriteFrage;
import com.msht.minshengbao.functionActivity.fragment.TableRecordFrag;


/**
 * Created by hong on 2017/1/9.
 */
public class ViewPageWriteTable extends FragmentPagerAdapter {
    private String fragments[] = {"自助抄表","抄表记录"};
    private SelfWriteFrage mSelfWrite;
    private TableRecordFrag mTablerecord;

    public ViewPageWriteTable(FragmentManager supportFragmentManager, Context applicationContext) {
        super(supportFragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                mSelfWrite=new SelfWriteFrage();
                return mSelfWrite;
            case 1:
                mTablerecord=new TableRecordFrag();
                return  mTablerecord;
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
