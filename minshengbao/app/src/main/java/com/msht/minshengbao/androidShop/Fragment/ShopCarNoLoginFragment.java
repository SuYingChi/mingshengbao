package com.msht.minshengbao.androidShop.Fragment;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.event.GoShopMainEvent;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class ShopCarNoLoginFragment extends ShopBaseLazyFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.login)
    TextView tvLogin;
    @BindView(R.id.return_home)
    TextView tvReturnHome;
    @Override
    protected int setLayoutId() {
        return R.layout.no_login_car;
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.setTitleBar(getActivity(),mToolbar);
    }
    @Override
    protected void initView() {

      tvLogin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
          }
      });
        tvReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new GoShopMainEvent());
            }
        });
    }

}
