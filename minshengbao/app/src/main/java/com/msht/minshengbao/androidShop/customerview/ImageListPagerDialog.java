package com.msht.minshengbao.androidShop.customerview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.ShopImageListPagerAdapter;
import com.msht.minshengbao.androidShop.viewInterface.ImageListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageListPagerDialog extends Dialog {
    private  int selectedPosition;
    private Context context;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.ll)
    RelativeLayout ll;
    @BindView(R.id.llIndicators)
    LinearLayout llIndicators;
    private ImageListView imageListView;
    private ShopImageListPagerAdapter ad;

    public ImageListPagerDialog(@NonNull Context context, ImageListView imageListView,int selectedPosition) {
        super(context, R.style.full_screen_dialog);
        this.imageListView = imageListView;
        this.context = context;
        this.selectedPosition = selectedPosition;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (ImageListPagerDialog.this.isShowing()) {
                    ImageListPagerDialog.this.dismiss();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_eveluateimage_list_pager);
        ButterKnife.bind(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageListPagerDialog.this.isShowing()) {
                    ImageListPagerDialog.this.dismiss();
                }
            }
        });
        ad = new ShopImageListPagerAdapter(context, llIndicators, selectedPosition, new ShopImageListPagerAdapter.OnClickImageListener() {
            @Override
            public void onclickImage() {
                if (ImageListPagerDialog.this.isShowing()) {
                    ImageListPagerDialog.this.dismiss();
                }
            }
        });
        ad.setDatas(imageListView.getImageList());
        vp.setOffscreenPageLimit(6);
        vp.setAdapter(ad);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                List<View> indicators = ((ShopImageListPagerAdapter) vp.getAdapter()).getIndicators();
                for (int i = 0; i < indicators.size(); i++) {
                    indicators.get(i).setSelected(i == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        vp.setCurrentItem(imageListView.getSelectedPosition());
    }

}
