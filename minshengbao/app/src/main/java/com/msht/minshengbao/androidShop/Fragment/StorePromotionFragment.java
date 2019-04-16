package com.msht.minshengbao.androidShop.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.PromotionBean;
import com.msht.minshengbao.androidShop.util.DateUtils;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.viewInterface.IStorePromotionView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class StorePromotionFragment extends ShopBaseLazyFragment implements IStorePromotionView {
    private String storeId;
    private Timer timer;

    @Override
    protected int setLayoutId() {
        return R.layout.shop_activity_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeId=getArguments().getString("id");
    }

    @Override
    protected void initView() {
        super.initView();

    }

    @Override
    protected void initData() {
        super.initData();
        ShopPresenter.getStorePromotion(this);
    }

    @Override
    public void onGetStoreActivitySuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            JSONArray promotionList = obj.optJSONObject("datas").optJSONObject("promotion").optJSONArray("list");
            if(promotionList.length()>=1){
                for(int i=0;i<promotionList.length();i++){
                    JSONObject jsonobj = promotionList.optJSONObject(i);
                     PromotionBean promotionBean = JsonUtil.toBean(jsonobj.toString(), PromotionBean.class);
                     Long lefttime = promotionBean.getPromotion_left_time();
                     timer= new Timer();
                     timer.schedule(new CountDownTimeTask(lefttime),0,1000);

                }
            }else {

            }
        } catch (JSONException e) {

        }
    }

    @Override
    public String getStoreId() {
        return storeId;
    }
    private class CountDownTimeTask extends  TimerTask{
        private  long cursecond;

        CountDownTimeTask(long cursecond){
            this.cursecond = cursecond;
        }
        @Override
        public void run() {
            LogUtils.e(DateUtils.secondFormatToLeftDay(cursecond));
            cursecond = cursecond-1;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
