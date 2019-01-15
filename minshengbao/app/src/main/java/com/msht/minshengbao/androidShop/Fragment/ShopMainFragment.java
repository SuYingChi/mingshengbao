package com.msht.minshengbao.androidShop.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.BuildConfig;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.ViewUI.CardVMZBanner.MZBannerView;
import com.msht.minshengbao.ViewUI.CardVMZBanner.holder.MZHolderCreator;
import com.msht.minshengbao.ViewUI.CardVMZBanner.holder.MZViewHolder;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.androidShop.activity.ShopClassDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopGoodDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopKeywordListActivity;
import com.msht.minshengbao.androidShop.activity.ShopUrlActivity;
import com.msht.minshengbao.androidShop.activity.TotalMessageListActivity;
import com.msht.minshengbao.androidShop.activity.ShopMoreGoodActivity;
import com.msht.minshengbao.androidShop.activity.ShopSearchActivty;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.ShopHomeChuDianPagerAdapter;
import com.msht.minshengbao.androidShop.adapter.ShopHomeClassPagerAdapter;
import com.msht.minshengbao.androidShop.adapter.ShopHomeGoodsAdapter;
import com.msht.minshengbao.androidShop.adapter.ShopMaiBianQuanQiuPagerAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.customerview.ImageCycleView;
import com.msht.minshengbao.androidShop.customerview.WrapChildHeightViewPager;
import com.msht.minshengbao.androidShop.customerview.XScrollView;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.ClassDetailLeftBean;
import com.msht.minshengbao.androidShop.shopBean.ClassFirstBean;
import com.msht.minshengbao.androidShop.shopBean.MessagePreviewBean;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeGoodsBean;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeGoods_1Bean;
import com.msht.minshengbao.androidShop.shopBean.ShopHome2Bean;
import com.msht.minshengbao.androidShop.shopBean.ShopHome4Bean;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeAdvBean;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeClassBean;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeGoods2Bean;
import com.msht.minshengbao.androidShop.shopBean.ShopHomeGoods_3Bean;
import com.msht.minshengbao.androidShop.util.AddViewHolder;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.IGetMsgCountView;
import com.msht.minshengbao.androidShop.viewInterface.IMessagePreView;
import com.msht.minshengbao.androidShop.viewInterface.IShopAllClassView;
import com.msht.minshengbao.androidShop.viewInterface.IShopMainView;
import com.msht.minshengbao.events.CarNumEvent;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopMainFragment extends ShopBaseLazyFragment implements OnRefreshListener, IShopMainView, IGetMsgCountView, IMessagePreView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.sv)
    XScrollView xScrollView;
    @BindView(R.id.cycleView)
    ImageCycleView imageCycleView;
    @BindView(R.id.homeViewID)
    LinearLayout shopHomeContent;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tvSearchD)
    EditText etSeatch;
    @BindView(R.id.vhint)
    TextView vHint;
    @BindView(R.id.rlt_msg)
    RelativeLayout rltMsg;
    private List<String> advImagelist = new ArrayList<String>();
    private List<ShopHomeClassBean.ClassBean.ItemBean> homeClassList;
    private MZBannerView mMZBanner;
    private Integer msgCount;


    @Override
    protected int setLayoutId() {
        return R.layout.shop_main_fragment;
    }

    //适配民生宝mainactivity add hide show结构，创建fragment的时候就请求数据，懒加载适用于搭配viewpager
    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected void initView() {
        super.initView();
        ViewGroup.LayoutParams bannerParams = imageCycleView.getLayoutParams();
        ViewGroup.LayoutParams titleBarParams = mToolbar.getLayoutParams();
        final int bannerHeight = bannerParams.height - titleBarParams.height;
        Log.e("scrollChanged", "bannerParams.height=" + DimenUtil.px2dip(bannerParams.height) + "titleBarParams.height=" + titleBarParams.height + "ImmersionBar.getStatusBarHeight(getActivity()=" + ImmersionBar.getStatusBarHeight(getActivity()));
        mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                , ContextCompat.getColor(mActivity, R.color.colorPrimary), 0));
        xScrollView.setXScrollViewListener(new XScrollView.XScrollViewListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldx, int oldy) {
                Log.e("scrollChanged", "shop  y=  " + y + "oldy=  " + oldy);
                if (y < 0) {
                    mToolbar.setVisibility(View.VISIBLE);
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(mActivity, R.color.colorPrimary), 1));
                } else if (y <= bannerHeight) {
                    float alpha = (float) y / bannerHeight;
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(mActivity, R.color.colorPrimary), alpha));
                } else {
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(mActivity, R.color.colorPrimary), 1));
                }
            }

            @Override
            public void onScrollOverTop() {
             //   mToolbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollNormal() {
                mToolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollOverBottom() {

            }
        });
        //不允许获取焦点 不可编辑,却可以点击
        etSeatch.setFocusable(false);
        etSeatch.setFocusableInTouchMode(false);
        etSeatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopSearchActivty.class);
                intent.putExtra("main", 1);
                AppUtil.hideSoftInput(getActivity());
                startActivity(intent);
            }
        });
        refreshLayout.setOnRefreshListener(this);
        rltMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(getKey())){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    Intent intent = new Intent(getActivity(), TotalMessageListActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    public void getMessageCount() {
        if (!getKey().equals("")) {
            ShopPresenter.getMsgCount(this);
        }
    }

    @Override
    protected void initData() {
        ShopPresenter.getShopHome(this);
        if (!getKey().equals("")) {
            ShopPresenter.getMsgCount(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    /*    mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();*/
        ImmersionBar.setTitleBar(getActivity(), mToolbar);
    }

    @Override
    protected void initImmersionBar() {
        //super.initImmersionBar();
        //ImmersionBar.setTitleBar(getActivity(), mToolbar);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        shopHomeContent.removeAllViews();
        ShopPresenter.getShopHome(this);

    }

    @Override
    public void onGetShopHomeSuccess(String json) {
        refreshLayout.finishRefresh();
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.finishLoadMoreWithNoMoreData();
        refreshLayout.setNoMoreData(true);
        try {
            JSONObject jsonObject = new JSONObject(json);
            Object listArray = new JSONTokener(jsonObject.optString("datas")).nextValue();
            if (listArray instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) listArray;
                for (int k = 0; k < jsonArray.length(); k++) {
                    JSONObject parameterObject = jsonArray.optJSONObject(k);
                    if (parameterObject.has("adv_list")) {
                        ShopHomeAdvBean shopHomeAdvBean = JsonUtil.toBean(parameterObject.toString(), ShopHomeAdvBean.class);
                        if (shopHomeAdvBean != null) {
                            for (ShopHomeAdvBean.AdvListBean.ItemBean itemBean : shopHomeAdvBean.getAdv_list().getItem()) {
                                advImagelist.add(itemBean.getImage());
                            }
                            showAdv(advImagelist, shopHomeAdvBean.getAdv_list().getItem());
                        }
                    } else if (parameterObject.has("class")) {
                        ShopHomeClassBean shopHomeClassBean = JsonUtil.toBean(parameterObject.toString(), ShopHomeClassBean.class);
                        if (shopHomeClassBean != null) {
                            homeClassList = shopHomeClassBean.getClassX().getItem();
                            showClass();
                        }
                    } else if (parameterObject.has("home2")) {
                        ShopHome2Bean shopHome2Bean = JsonUtil.toBean(parameterObject.toString(), ShopHome2Bean.class);
                        if (shopHome2Bean != null) {
                            showHome2(shopHome2Bean);
                        }
                    } else if (parameterObject.has("goods_2")) {
                        ShopHomeGoods2Bean shopHomeGoods2Bean = JsonUtil.toBean(parameterObject.toString(), ShopHomeGoods2Bean.class);
                        if (shopHomeGoods2Bean != null) {
                            showGoods2(shopHomeGoods2Bean);
                        }
                    } else if (parameterObject.has("home4")) {
                        ShopHome4Bean shopHome4Bean = JsonUtil.toBean(parameterObject.toString(), ShopHome4Bean.class);
                        if (shopHome4Bean != null) {
                            showHome4(shopHome4Bean);
                        }
                    } else if (parameterObject.has("goods_1")) {
                        ShopHomeGoods_1Bean shopHomeGoods_1Bean = JsonUtil.toBean(parameterObject.toString(), ShopHomeGoods_1Bean.class);
                        if (shopHomeGoods_1Bean != null) {
                            showGoods_1(shopHomeGoods_1Bean);
                        }

                    } else if (parameterObject.has("goods_3")) {
                        ShopHomeGoods_3Bean ShopHomeGoods_3Bean = JsonUtil.toBean(parameterObject.toString(), ShopHomeGoods_3Bean.class);
                        if (ShopHomeGoods_3Bean != null) {
                            showGoods_3(ShopHomeGoods_3Bean);
                        }
                    } else if (parameterObject.has("goods")) {
                        ShopHomeGoodsBean shopHomeGoodsBean = JsonUtil.toBean(parameterObject.toString(), ShopHomeGoodsBean.class);
                        if (shopHomeGoodsBean != null) {
                            showGoods(shopHomeGoodsBean);
                        }
                        // 这个方法是在最后一页，没有更多数据时调用的，会在页面底部标记没有更多数据
                        refreshLayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
                        // 这个方法最重要，当在最后一页调用完上一个完成加载并标记没有更多数据的方法时，需要将refreshLayout的状态更改为还有更多数据的状态，此时就需要调用此方法，参数为false代表还有更多数据，true代表没有更多数据
                        refreshLayout.setNoMoreData(true);//恢复没有更多数据的原始状态 1.0.5
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * 轮播
     *
     * @param images
     */
    private void showAdv(List<String> images, final List<ShopHomeAdvBean.AdvListBean.ItemBean> itemDataList) {
        final ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                GlideUtil.loadByWidthFitHeight(getContext(), imageView, imageURL);
            }

            @Override
            public void onImageClick(int position, View imageView) {
                if (itemDataList != null) {
                    ShopHomeAdvBean.AdvListBean.ItemBean itemData = itemDataList.get(position);
                    doShopItemViewClick(itemData.getType(), itemData.getData());
                }
            }
        };

        imageCycleView.setFocusable(true);
        imageCycleView.setFocusableInTouchMode(true);
        imageCycleView.requestFocus();
        imageCycleView.requestFocusFromTouch();

        if (images.size() > 0) {
            imageCycleView.setImageResources(images, mAdCycleViewListener, true, true);
        }
    }

    /**
     * 显示首页的分类导航栏
     *
     *
     *
     */
    private void showClass() {
        AddViewHolder addViewHolder = new AddViewHolder(getContext(), R.layout.shop_home_class);
        LinearLayout llIndicators = addViewHolder.getView(R.id.llIndicators);
        ShopHomeClassPagerAdapter pagerAdapter = new ShopHomeClassPagerAdapter(getContext(), llIndicators, new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!BuildConfig.DEBUG) {
                    if (position != homeClassList.size() - 1) {
                        String data = homeClassList.get(position).getData();
                        if (data.contains("gc_id=")) {
                            Intent intent = new Intent(getActivity(), ShopClassDetailActivity.class);
                            int index = data.indexOf("gc_id=");
                            data = data.substring(index + 6).trim();
                            intent.putExtra("data", data);
                           // intent.putExtra("selectPosition", position);
                            intent.putExtra("title", homeClassList.get(position).getTitle());
                            //Bundle bundle = new Bundle();
                         //   bundle.putSerializable("list", (Serializable) list);
                           // intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (data.contains("keyword=")) {
                            int index = data.indexOf("keyword=");
                            String shopkeyword = data.substring(index + 8).trim();
                            Intent intent = new Intent(getActivity(), ShopKeywordListActivity.class);
                            intent.putExtra("keyword", StringUtil.toURLDecoder(shopkeyword));
                            startActivity(intent);
                        } else if (data.contains("goods_id=")) {
                            int index = data.indexOf("goods_id=");
                            String goodsid = data.substring(index + 9).trim();
                            Intent intent = new Intent(getActivity(), ShopGoodDetailActivity.class);
                            intent.putExtra("goodsid", goodsid);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(getActivity(), ShopMoreGoodActivity.class);
                      //  intent.putExtra("list", (Serializable) list);
                        getActivity().startActivity(intent);
                    }
                } else {
                    String data = homeClassList.get(position).getData();
                    String type = homeClassList.get(position).getType();
                    LogUtils.e(data + "type=" + homeClassList.get(position).getType());
                    if ("keyword".equals(type)) {
                        Intent intent = new Intent(getActivity(), ShopKeywordListActivity.class);
                        intent.putExtra("keyword", StringUtil.toURLDecoder(data));
                        startActivity(intent);
                    } else if ("goods".equals(type)) {
                        Intent intent = new Intent(getActivity(), ShopGoodDetailActivity.class);
                        intent.putExtra("goodsid", data);
                        startActivity(intent);
                    } else if ("url".equals(type)) {
                        if (data.contains("gc_id=")) {
                            Intent intent = new Intent(getActivity(), ShopClassDetailActivity.class);
                            int index = data.indexOf("gc_id=");
                            data = data.substring(index + 6).trim();
                            intent.putExtra("data", data);
                            intent.putExtra("title", homeClassList.get(position).getTitle());
                            startActivity(intent);
                        } else if (NetUtil.getDomain(data).equals(ConstantUtil.DEBUG_SHOP_DOMAIN)) {
                            Intent intent = new Intent(getActivity(), ShopUrlActivity.class);
                            intent.putExtra("url", data);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
        final WrapChildHeightViewPager vp = addViewHolder.getView(R.id.vp);
        vp.setNoScroll(false);
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(pagerAdapter);
        pagerAdapter.setDatas(homeClassList);
        shopHomeContent.addView(addViewHolder.getCustomView());
        vp.setCurrentItem(0);
        pagerAdapter.notifyDataSetChanged();
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                List<View> indicators = ((ShopHomeClassPagerAdapter) vp.getAdapter()).getIndicators();
                for (int i = 0; i < indicators.size(); i++) {
                    indicators.get(i).setSelected(i == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    /**
     * 显示home2 限时折扣，左一右二横排模块
     *
     * @param shopHome2Bean
     */
    public void showHome2(ShopHome2Bean shopHome2Bean) {

        AddViewHolder addViewHolder = new AddViewHolder(getContext(), R.layout.shop_home2);
        addViewHolder.setImage(R.id.ImageViewSquare, shopHome2Bean.getHome2().getSquare_image());
        addViewHolder.setImage(R.id.ImageViewRectangle1, shopHome2Bean.getHome2().getRectangle1_image());
        addViewHolder.setImage(R.id.ImageViewRectangle2, shopHome2Bean.getHome2().getRectangle2_image());
        TextView textView = (TextView) addViewHolder.getView(R.id.title);
        textView.setText(shopHome2Bean.getHome2().getTitle());

        shopHomeContent.addView(addViewHolder.getCustomView());

        onShopItemViewClick(addViewHolder.getView(R.id.ImageViewSquare), shopHome2Bean.getHome2().getSquare_type(), shopHome2Bean.getHome2().getSquare_data(), false);
        onShopItemViewClick(addViewHolder.getView(R.id.ImageViewRectangle1), shopHome2Bean.getHome2().getRectangle1_type(), shopHome2Bean.getHome2().getRectangle1_data(), false);
        onShopItemViewClick(addViewHolder.getView(R.id.ImageViewRectangle2), shopHome2Bean.getHome2().getRectangle2_type(), shopHome2Bean.getHome2().getRectangle2_data(), false);
    }

    /**
     * 甄选推荐 轮播
     *
     * @param shopHomeGoods2Bean
     */

    private void showGoods2(final ShopHomeGoods2Bean shopHomeGoods2Bean) {
        AddViewHolder addViewHolder = new AddViewHolder(getContext(), R.layout.shop_goods2);
        mMZBanner = (MZBannerView) addViewHolder.getView(R.id.banner);
        TextView textView = (TextView) addViewHolder.getView(R.id.title);
        textView.setText(shopHomeGoods2Bean.getGoods_2().getTitle());
        mMZBanner.setIndicatorVisible(true);
        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
              /*  ShopHomeGoods2Bean.Goods2Bean.ItemBean itemBean = shopHomeGoods2Bean.getGoods_2().getItem().get(position);
                Map<String, String> map = new HashMap<String, String>();
                map.put("type", itemBean.getType());
                map.put("data", itemBean.getData());
                doNotAdClick(map);*/
                ShopHomeGoods2Bean.Goods2Bean.ItemBean itemBean = shopHomeGoods2Bean.getGoods_2().getItem().get(position);
                //onShopItemViewClick(view,itemBean.getType(), itemBean.getData(), true);
                doShopItemViewClick(itemBean.getType(), itemBean.getData());
            }
        });
        ArrayList<ShopHomeGoods2Bean.Goods2Bean.ItemBean> list = new ArrayList<ShopHomeGoods2Bean.Goods2Bean.ItemBean>(shopHomeGoods2Bean.getGoods_2().getItem());
        mMZBanner.setPages(list, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, DimenUtil.dip2px(getResources().getDimension(R.dimen.shop_home_area_margin)), 0, 0);
        shopHomeContent.addView(addViewHolder.getCustomView(), params);
        mMZBanner.start();
    }

    /**
     * 精选商品 左二右一
     *
     * @param shopHome4Bean
     */
    private void showHome4(ShopHome4Bean shopHome4Bean) {
        AddViewHolder addViewHolder = new AddViewHolder(getContext(), R.layout.shop_home4);
        addViewHolder.setImage(R.id.ImageViewSquare, shopHome4Bean.getHome4().getSquare_image());
        addViewHolder.setImage(R.id.ImageViewRectangle1, shopHome4Bean.getHome4().getRectangle1_image());
        addViewHolder.setImage(R.id.ImageViewRectangle2, shopHome4Bean.getHome4().getRectangle2_image());
        TextView textView = (TextView) addViewHolder.getView(R.id.title);
        textView.setText(shopHome4Bean.getHome4().getTitle());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, DimenUtil.dip2px(getResources().getDimension(R.dimen.shop_home_area_margin)), 0, 0);
        shopHomeContent.addView(addViewHolder.getCustomView(), params);

        onShopItemViewClick(addViewHolder.getView(R.id.ImageViewSquare), shopHome4Bean.getHome4().getSquare_type(), shopHome4Bean.getHome4().getSquare_data(), false);
        onShopItemViewClick(addViewHolder.getView(R.id.ImageViewRectangle1), shopHome4Bean.getHome4().getRectangle1_type(), shopHome4Bean.getHome4().getRectangle1_data(), false);
        onShopItemViewClick(addViewHolder.getView(R.id.ImageViewRectangle2), shopHome4Bean.getHome4().getRectangle2_type(), shopHome4Bean.getHome4().getRectangle2_data(), false);
    }

    /**
     * 精选厨电
     *
     * @param shopHomeGoods_1Bean
     */
    private void showGoods_1(final ShopHomeGoods_1Bean shopHomeGoods_1Bean) {/*
        AddViewHolder addViewHolder = new AddViewHolder(getContext(), R.layout.shop_goods_1);
        TextView textView = (TextView) addViewHolder.getView(R.id.title);
        textView.setText(shopHomeGoods_1Bean.getGoods_1().getTitle());
        LinearLayout llMore = (LinearLayout) addViewHolder.getView(R.id.more);
        llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = shopHomeGoods_1Bean.getGoods_1().getMore_link();
            }
        });
        MyNoScrollGridView gv = (MyNoScrollGridView) addViewHolder.getView(R.id.gv);
        ShopHomeGoods_1Adapter gvAdapter = new ShopHomeGoods_1Adapter(getContext());
        gv.setAdapter(gvAdapter);
        gvAdapter.setmDatas(shopHomeGoods_1Bean.getGoods_1().getItem());
        gvAdapter.notifyDataSetChanged();
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,String> map = new HashMap<String,String>();
                map.put("type","goods");
                map.put("data",shopHomeGoods_1Bean.getGoods_1().getItem().get(position).getGoods_id());
                map.put("price", shopHomeGoods_1Bean.getGoods_1().getItem().get(position).getGoods_promotion_price());
                doNotAdClick(getContext(),map);
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, DimenUtil.dip2px(getResources().getDimension(R.dimen.shop_home_area_margin)), 0, 0);
        shopHomeContent.addView(addViewHolder.getCustomView(), params);*/

        AddViewHolder addViewHolder = new AddViewHolder(getContext(), R.layout.shop_goods_1);
        TextView textView = (TextView) addViewHolder.getView(R.id.title);
        textView.setText(shopHomeGoods_1Bean.getGoods_1().getTitle());
        LinearLayout llMore = (LinearLayout) addViewHolder.getView(R.id.more);
        llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = shopHomeGoods_1Bean.getGoods_1().getMore_link();
                doShopItemViewClick("url", data);
            }
        });
        AddViewHolder addViewHolder2 = new AddViewHolder(getContext(), R.layout.shop_home_class);
        LinearLayout llIndicators = addViewHolder2.getView(R.id.llIndicators);
        ShopHomeChuDianPagerAdapter pagerAdapter = new ShopHomeChuDianPagerAdapter(getContext(), llIndicators, new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                /*Map<String, String> map = new HashMap<String, String>();
                map.put("type", "goods");
                map.put("data", shopHomeGoods_1Bean.getGoods_1().getItem().get(position).getGoods_id());
                map.put("price", shopHomeGoods_1Bean.getGoods_1().getItem().get(position).getGoods_promotion_price());
                doNotAdClick(map);*/

                doShopItemViewClick("goods", shopHomeGoods_1Bean.getGoods_1().getItem().get(position).getGoods_id());

            }
        });
        final WrapChildHeightViewPager vp = addViewHolder2.getView(R.id.vp);
        vp.setNoScroll(false);
        vp.setOffscreenPageLimit(10);
        vp.setAdapter(pagerAdapter);
        pagerAdapter.setDatas(shopHomeGoods_1Bean.getGoods_1().getItem());
        vp.setCurrentItem(0);
        pagerAdapter.notifyDataSetChanged();

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                List<View> indicators = ((ShopHomeChuDianPagerAdapter) vp.getAdapter()).getIndicators();
                for (int i = 0; i < indicators.size(); i++) {
                    indicators.get(i).setSelected(i == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        shopHomeContent.addView(addViewHolder.getCustomView());
        shopHomeContent.addView(addViewHolder2.getCustomView());
    }
    /*买遍全球*/
    private void showGoods_3(final ShopHomeGoods_3Bean shopHomeGoods_3Bean) {

        AddViewHolder addViewHolder = new AddViewHolder(getContext(), R.layout.shop_goods_1);
        TextView textView = (TextView) addViewHolder.getView(R.id.title);
        textView.setText(shopHomeGoods_3Bean.getGoods_3().getTitle());
        LinearLayout llMore = (LinearLayout) addViewHolder.getView(R.id.more);
        llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = shopHomeGoods_3Bean.getGoods_3().getMore_link();
                doShopItemViewClick("url", data);
            }
        });
        AddViewHolder addViewHolder2 = new AddViewHolder(getContext(), R.layout.shop_home_class);
        LinearLayout llIndicators = addViewHolder2.getView(R.id.llIndicators);
        ShopMaiBianQuanQiuPagerAdapter pagerAdapter = new ShopMaiBianQuanQiuPagerAdapter(getContext(), llIndicators, new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                doShopItemViewClick("goods", shopHomeGoods_3Bean.getGoods_3().getItem().get(position).getGoods_id());
            }
        });
        final WrapChildHeightViewPager vp = addViewHolder2.getView(R.id.vp);
        vp.setNoScroll(false);
        vp.setOffscreenPageLimit(10);
        vp.setAdapter(pagerAdapter);
        pagerAdapter.setDatas(shopHomeGoods_3Bean.getGoods_3().getItem());
        vp.setCurrentItem(0);
        pagerAdapter.notifyDataSetChanged();

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                List<View> indicators = ((ShopMaiBianQuanQiuPagerAdapter) vp.getAdapter()).getIndicators();
                for (int i = 0; i < indicators.size(); i++) {
                    indicators.get(i).setSelected(i == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        shopHomeContent.addView(addViewHolder.getCustomView());
        shopHomeContent.addView(addViewHolder2.getCustomView());
    }
    /**
     * 猜你喜欢
     *
     * @param shopHomeGoodsBean
     */
    private void showGoods(final ShopHomeGoodsBean shopHomeGoodsBean) {
        AddViewHolder addViewHolder = new AddViewHolder(getContext(), R.layout.shop_goods_cainixihuan);
        TextView textView = (TextView) addViewHolder.getView(R.id.title);
        textView.setText(shopHomeGoodsBean.getGoods().getTitle());
        MyNoScrollGridView gv = (MyNoScrollGridView) addViewHolder.getView(R.id.gv);
        ShopHomeGoodsAdapter gvAdapter = new ShopHomeGoodsAdapter(getContext());
        gv.setAdapter(gvAdapter);
        gvAdapter.setmDatas(shopHomeGoodsBean.getGoods().getItem());
        gvAdapter.notifyDataSetChanged();
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          /*      Map<String, String> map = new HashMap<String, String>();
                map.put("type", "goods");
                map.put("data", shopHomeGoodsBean.getGoods().getItem().get(position).getGoods_id());
                map.put("price", shopHomeGoodsBean.getGoods().getItem().get(position).getGoods_promotion_price());
                doNotAdClick(map);*/
                doShopItemViewClick("goods", shopHomeGoodsBean.getGoods().getItem().get(position).getGoods_id());
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, DimenUtil.dip2px(getResources().getDimension(R.dimen.shop_home_area_margin)), 0, 0);
        shopHomeContent.addView(addViewHolder.getCustomView(), params);
    }

    @Override
    public void onGetMsgCountSuccess(String s) {
        try {
            msgCount = Integer.valueOf(new JSONObject(s).optString("datas"));
            ShopPresenter.getMessagePreview(this, SharedPreferencesUtil.getUserId(getContext(), SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(getContext(), SharedPreferencesUtil.Password, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetMessagePreviewSuccess(MessagePreviewBean s) {
        List<MessagePreviewBean.DataBean> list = s.getData();
        for (MessagePreviewBean.DataBean bean : list) {
            msgCount += bean.getUnread_num();
        }
        vHint.setText(msgCount + "");
        if (msgCount > 0) {
            vHint.setVisibility(View.VISIBLE);
        } else {
            vHint.setVisibility(View.GONE);
        }
    }



    public static class BannerViewHolder implements MZViewHolder<ShopHomeGoods2Bean.Goods2Bean.ItemBean> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.mzbanner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, ShopHomeGoods2Bean.Goods2Bean.ItemBean data) {
            // 数据绑定
            String imgurl = data.getImage();
            GlideUtil.loadRemoteImg(context, mImageView, imgurl);
        }
    }

    /**
     * 点击事件
     *
     * @param type keyword 关键字搜索，data中为搜索关键字
     *             special 专题，data中为专题编号
     *             goods 商品，data中为商品编号
     *             store 店铺，data中为店铺编号
     *             category 跳转到分类，data中无数据
     *             cart 跳转到购物车，data中无数据
     *             my 跳转到我的商城，data中无数据
     * @param data
     */
    private void onShopItemViewClick(View view, final String type, final String data, boolean havaClickView) {
        if (havaClickView) {//是轮播
            doShopItemViewClick(type, data);
        } else {  //不是轮播图
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*    Map<String, String> map = new HashMap<String, String>();
                    map.put("type", type);
                    map.put("data", data);
                    doNotAdClick(map);*/
                    doShopItemViewClick(type, data);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMZBanner != null) {
            mMZBanner.pause();
            MobclickAgent.onPageEnd("商城首页");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMZBanner != null) {
            mMZBanner.start();
            MobclickAgent.onPageStart("商城首页");
        }
    }
}
