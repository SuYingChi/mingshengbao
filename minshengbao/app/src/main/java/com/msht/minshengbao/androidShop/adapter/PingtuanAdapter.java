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
import com.msht.minshengbao.androidShop.shopBean.PingTuanBean;
import com.msht.minshengbao.androidShop.util.DateUtils;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;

import java.util.List;

public  class  PingtuanAdapter extends RecyclerView.Adapter<PingtuanAdapter.ViewHolder> {

    private  Context context;
    private List<PingTuanBean> mDatas;
    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownMap;
    private AdapterInterface aDapterinterface;

    public PingtuanAdapter(Context context, List<PingTuanBean> datas) {
        this.context = context;
        mDatas = datas;
        countDownMap = new SparseArray<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pingtuan_gooddetail, parent, false);
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
        final PingTuanBean data = mDatas.get(position);
        GlideUtil.loadRemoteCircleImg(context,holder.iv,data.getAvatar());
        holder.leftnum.setText(data.getNum()+"");
        holder.tvname.setText(data.getBuyer_name());
        long time = data.getEnd_time_left()*1000;
        //将前一个缓存清除
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (time > 0) {
            holder.countDownTimer = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mDatas.get(position).setEnd_time_left(millisUntilFinished/1000);
                    List<String> list = DateUtils.secondFormatToLeftDay(millisUntilFinished/1000);
                    holder.hour.setText(list.get(1));
                    holder.minute.setText(list.get(2));
                    holder.second.setText(list.get(3));
                    holder.cantuan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          aDapterinterface.onClickCanTuan(data.getPintuan_id());
                        }
                    });
                }
                @Override
                public void onFinish() {
                    holder.cantuan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopUtil.toastInCenter("活动已经结束");
                        }
                    });
                }
            }.start();
            countDownMap.put(holder.iv.hashCode(), holder.countDownTimer);
        } else {
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
        if (mDatas != null && !mDatas.isEmpty()&&mDatas.size()<=2) {
            return mDatas.size();
        }else if(mDatas != null && !mDatas.isEmpty()&&mDatas.size()>2){
            return 2;
        }
        return 0;
    }

    public void notifyChange() {
        if (countDownMap == null) {
            return;
        }
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

        public TextView cantuan;
        public TextView hour;
        public TextView minute;
        public TextView second;
        public TextView leftnum;
        public TextView tvname;
        public CountDownTimer countDownTimer;
        public ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            tvname = (TextView)itemView.findViewById(R.id.name);
            leftnum = (TextView)itemView.findViewById(R.id.leftnum);
            hour = (TextView)itemView.findViewById(R.id.hour);
            minute = (TextView)itemView.findViewById(R.id.minute);
            second = (TextView) itemView.findViewById(R.id.second);
            cantuan = (TextView) itemView.findViewById(R.id.cantuan);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
    public interface AdapterInterface {
        void onClickCanTuan(String pingTuanId);
    }
    public  void setAdapterInterface(AdapterInterface aDapterinterface){
        this.aDapterinterface = aDapterinterface;
    }
}
