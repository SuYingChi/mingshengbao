package com.msht.minshengbao.androidShop.basefragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;
import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.androidShop.LogoutEventBusEvent;
import com.msht.minshengbao.androidShop.activity.ShopClassDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopGoodDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopKeywordListActivity;
import com.msht.minshengbao.androidShop.activity.ShopSpecialActivity;
import com.msht.minshengbao.androidShop.activity.ShopStoreMainActivity;
import com.msht.minshengbao.androidShop.customerview.LoadingDialog;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.IBaseView;
import com.msht.minshengbao.functionActivity.htmlWeb.HtmlPageActivity;
import com.msht.minshengbao.functionActivity.myActivity.LoginActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.Unbinder;

//如果需要多次加载数据，页面可见时就加载 则使用basefragment,在onresume里开始请求数据
public abstract class ShopBaseFragment extends Fragment implements IBaseView {

    private ImmersionBar mImmersionBar;
    protected LoadingDialog centerLoadingDialog;
    protected View mRootView;
    private Unbinder unbinder;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initStateBar();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(setLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void showLoading() {
        showCenterLodaingDialog();

    }

    @Override
    public void dismissLoading() {
        hideCenterLoadingDialog();
    }

    protected void initStateBar() {

    }

    protected void showCenterLodaingDialog() {
        if (this.getActivity() != null && !this.getActivity().isFinishing() && centerLoadingDialog == null) {
            centerLoadingDialog = new LoadingDialog(this.getContext());
            centerLoadingDialog.show();
        } else if (this.getActivity() != null && !this.getActivity().isFinishing() && !centerLoadingDialog.isShowing()) {
            centerLoadingDialog.show();
        }
    }


    protected void hideCenterLoadingDialog() {
        if (centerLoadingDialog != null && centerLoadingDialog.isShowing() && getActivity()!=null&&!getActivity().isFinishing()) {
            centerLoadingDialog.dismiss();
        }
    }

    @Override
    public void onError(String s) {
        if (!AppUtil.isNetworkAvailable()) {
            PopUtil.showComfirmDialog(getContext(),"",getResources().getString(R.string.network_error),"","",null,null,true);
        } else if (TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())||"未登录".equals(s)) {
            PopUtil.toastInBottom("请登录");
        } else if(!TextUtils.isEmpty(s)){
            PopUtil.toastInCenter(s);
        }
    }

    @Override
    public String getKey() {
        return ShopSharePreferenceUtil.getInstance().getKey();
    }

    @Override
    public String getUserId() {
        return ShopSharePreferenceUtil.getInstance().getUserId();
    }

    @Override
    public String getLoginPassword() {
        return ShopSharePreferenceUtil.getInstance().getPassword();
    }






    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        OkHttpUtils.getInstance().cancelTag(this);

    }


    //扫描二维码的frgament 需要重写这三个方法
    public void handleDecode(Result obj, Bitmap barcode) {
    }


    public void drawViewfinder() {
    }


    public Handler getHandler() {
        return null;
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();
/*    http://dev.msbapp.cn/wap/tmpl/class_list.html?gc_id=1059http://dev.msbapp.cn/wap/tmpl/class_list.html?gc_id=1060*/
    protected void doShopItemViewClick(String type, String data) {
        if (TextUtils.equals(type, "goods")) {
            Intent intent = new Intent(getActivity(), ShopGoodDetailActivity.class);
            intent.putExtra("type", "2");
            intent.putExtra("goodsid", data);
            startActivity(intent);
        } else if (TextUtils.equals(type, "keyword")) {
            Intent intent = new Intent(getActivity(), ShopKeywordListActivity.class);
            intent.putExtra("keyword", data);
            startActivity(intent);
        } else if ("url".equals(type)) {
            if (Uri.parse(data).getQueryParameterNames().contains("gc_id")) {
                Intent intent = new Intent(getActivity(), ShopClassDetailActivity.class);
                intent.putExtra("data",Uri.parse(data).getQueryParameter("gc_id"));
                startActivity(intent);
            }  else if (data.contains("gc_id=")) {
                Intent intent = new Intent(getActivity(), ShopClassDetailActivity.class);
                int index = data.lastIndexOf("gc_id=");
                data = data.substring(index + 6).trim();
                intent.putExtra("data", data);
                startActivity(intent);
            }else if (NetUtil.getDomain(data).equals(ConstantUtil.FIANL_SHOP_DOMAIN)) {
                Intent intent = new Intent(getActivity(), HtmlPageActivity.class);
                intent.putExtra("url", data);
                startActivity(intent);
            }
        }
    }

    protected void doShopItemViewClickByUrl(String url) {
        if(TextUtils.isEmpty(url)){
            return;
        }
        Set<String> keys = Uri.parse(url).getQueryParameterNames();
        String data;
        if (keys.contains("goods")) {
            data = Uri.parse(url).getQueryParameter("goods");
            Intent intent = new Intent(getActivity(), ShopGoodDetailActivity.class);
            intent.putExtra("type", "2");
            intent.putExtra("goodsid", data);
            startActivity(intent);
        } else if (keys.contains("keyword")) {
            data = Uri.parse(url).getQueryParameter("keyword");
            Intent intent = new Intent(getActivity(), ShopKeywordListActivity.class);
            intent.putExtra("keyword", data);
            startActivity(intent);
        } else if (keys.contains("gc_id")) {
                Intent intent = new Intent(getActivity(), ShopClassDetailActivity.class);
                intent.putExtra("data",Uri.parse(url).getQueryParameter("gc_id"));
                startActivity(intent);
        } else if(keys.contains("store_id")){
            data = Uri.parse(url).getQueryParameter("store_id");
            Intent intent = new Intent(getActivity(), ShopStoreMainActivity.class);
            intent.putExtra("id", data);
            startActivity(intent);
        } else if (url.contains("gc_id=")) {
            Intent intent = new Intent(getActivity(), ShopClassDetailActivity.class);
            int index = url.lastIndexOf("gc_id=");
            data = url.substring(index + 6).trim();
            intent.putExtra("data", data);
            startActivity(intent);
        }else if (NetUtil.getDomain(url).equals(ConstantUtil.FIANL_SHOP_DOMAIN)) {
            Intent intent = new Intent(getActivity(), HtmlPageActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }

}
