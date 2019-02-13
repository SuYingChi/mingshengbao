package com.msht.minshengbao.androidShop.basefragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.BarParams;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OSUtils;
import com.msht.minshengbao.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 当使用viewpager加载Fragment，沉浸式的使用，原理懒加载,当可见的时候才加载数据,如果只需要加载一次页面的数据才使用，否则使用basefrgment
 */
public abstract class ShopBaseLazyFragment extends ShopBaseFragment {

    protected Activity mActivity;
    protected View mRootView;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完onViewCreated方法后即为true
     */
    protected boolean mIsPrepare;

    /**
     * 是否加载完成
     * 当执行完onViewCreated方法后即为true
     */
    protected boolean mIsImmersion;

    protected ImmersionBar mImmersionBar;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, mRootView);
        initView();
        if (isLazyLoad()) {
            mIsPrepare = true;
            mIsImmersion = true;
            onLazyLoad();
        } else {
            initData();
            if (isImmersionBarEnabled())
                initImmersionBar();
        }

        setListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(unbinder!=null){
            unbinder.unbind();
        }
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    /**
     * 是否懒加载
     *
     * @return the boolean
     */
    protected boolean isLazyLoad() {
        return true;
    }

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onVisible() {
        onLazyLoad();
    }

    private void onLazyLoad() {
        if (mIsVisible && mIsPrepare) {
            mIsPrepare = false;
            initData();
        }
        if (mIsVisible && mIsImmersion && isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }


    /**
     * 初始化数据
     */
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (OSUtils.isEMUI3_0()||OSUtils.isEMUI3_1()) {
            initImmersionBar();
        }
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        if(getActivity()!=null) {
            mImmersionBar = ImmersionBar.with(getActivity(), this);
            //白色状态栏处理
            mImmersionBar.statusBarDarkFont(true, 0.2f);
            if (ImmersionBar.hasNavigationBar(getActivity())) {
                BarParams barParams = ImmersionBar.with(this).getBarParams();
                //如果在有虚拟导航栏的时候全屏显示了，则取消全屏
                if (barParams.fullScreen) {
                    mImmersionBar.fullScreen(false).navigationBarColor(R.color.black).init();
                } else {
                    mImmersionBar.init();
                }
            } else {
                mImmersionBar.init();
            }
        }
    }

    /**
     * view与数据绑定
     */
    protected void initView() {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }

    /**
     * 用户不可见执行
     */
    protected void onInvisible() {

    }

    /**
     * 找到activity的控件
     *
     * @param <T> the type parameter
     * @param id  the id
     * @return the t
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findActivityViewById(@IdRes int id) {
        return (T) mActivity.findViewById(id);
    }


}
