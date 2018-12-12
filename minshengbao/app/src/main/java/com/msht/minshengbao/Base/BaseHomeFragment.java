package com.msht.minshengbao.Base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;

/**
 * A simple {@link Fragment} subclass.
 */
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/11/27 
 */
public  abstract class BaseHomeFragment extends Fragment {
    protected View mRootView;
    public Context mContext;
    public BaseHomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mRootView == null) {
            mRootView = initFindView();
        }
        return mRootView;
    }
    public boolean isLoginState(Context context){
        if (context!=null){
            context=mContext;
        }
        return SharedPreferencesUtil.getLstate(context, SharedPreferencesUtil.Lstate, false);
    }
    /**
     * 获取View Id
     * @return
     */
    public abstract View  initFindView();

}
