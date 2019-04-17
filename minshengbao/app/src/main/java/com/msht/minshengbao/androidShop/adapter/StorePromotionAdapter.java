package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.PromotionBean;
import com.msht.minshengbao.androidShop.util.DateUtils;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;

import java.util.List;


public  class  StorePromotionAdapter extends RecyclerView.Adapter<StorePromotionAdapter.ViewHolder> {

    private  Context context;
    private List<PromotionBean> mDatas;
    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownMap;
    private SpaInterface spaInterface;

    public StorePromotionAdapter(Context context, List<PromotionBean> datas) {
        this.context = context;
        mDatas = datas;
        countDownMap = new SparseArray<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotion, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }
        LogUtils.e(  "size :  " + countDownMap.size());
        for (int i = 0,length = countDownMap.size(); i < length; i++) {
            CountDownTimer cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PromotionBean data = mDatas.get(position);
        holder.name.setText(data.getPromotion_title());
       GlideUtil.loadRemoteImg(context,holder.iv,data.getPromotion_banner());
       long time = data.getPromotion_left_time()*1000;
        //将前一个缓存清除
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               spaInterface.onClick(data.getPromotion_type(),data.getPromotion_id());
            }
        });
        if (time > 0) {
            holder.countDownTimer = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mDatas.get(position).setPromotion_left_time(millisUntilFinished/1000);
                    List<String> list = DateUtils.secondFormatToLeftDay(millisUntilFinished/1000);
                    holder.day.setText(list.get(0));
                    holder.hour.setText(list.get(1));
                    holder.minute.setText(list.get(2));
                    holder.second.setText(list.get(3));
                }
                @Override
                public void onFinish() {
                 holder.itemView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         PopUtil.toastInCenter("活动已经结束");
                     }
                 });
                }
            }.start();
            countDownMap.put(holder.iv.hashCode(), holder.countDownTimer);
        } else {
            holder.day.setText("00");
            holder.hour.setText("00");
            holder.minute.setText("00");
            holder.second.setText("00");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopUtil.toastInCenter("活动已经结束");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (mDatas != null && !mDatas.isEmpty()) {
            return mDatas.size();
        }
        return 0;
    }

    public void notifyChange() {
        if (countDownMap == null) {
            return;
        }
        LogUtils.e(  "size :  " + countDownMap.size());
        for (int i = 0,length = countDownMap.size(); i < length; i++) {
            CountDownTimer cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
        countDownMap.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView day;
        private  TextView minute;
        private  TextView hour;
        private  TextView second;
        private TextView name;
        public ImageView iv;

        public CountDownTimer countDownTimer;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            name = (TextView) itemView.findViewById(R.id.name);
            day = (TextView) itemView.findViewById(R.id.day);
            hour = (TextView) itemView.findViewById(R.id.hour);
            minute = (TextView) itemView.findViewById(R.id.minute);
            second = (TextView) itemView.findViewById(R.id.second);

        }
    }
    public interface SpaInterface {
       void onClick(int promotion_type, String promotion_id);
    }
    public  void setSpaInterface(SpaInterface spaInterface){
        this.spaInterface = spaInterface;
    }
}