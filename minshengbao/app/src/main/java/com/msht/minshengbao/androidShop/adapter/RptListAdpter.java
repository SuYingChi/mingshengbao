package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.RptBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.viewInterface.ISelectedRptView;

import java.util.List;

public class RptListAdpter extends HaveHeadAndFootRecyclerAdapter<RptBean>{
    private ISelectedRptView iSelectedRptView;
    private boolean isUseRpt = true;

    public RptListAdpter(Context context, int layoutId, List<RptBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerHolder holder, final RptBean rptBean, final int position) {
        if (holder.getItemViewType() == Integer.MAX_VALUE) {
            final CheckBox checkBox = (CheckBox) holder.getView(R.id.select);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        //选中后不可更改
                        checkBox.setClickable(false);
                    } else {
                        //取消选中后，可以更改
                        checkBox.setClickable(true);
                    }
                    iSelectedRptView.noSelectedRpt(isChecked);
                }
            });
            checkBox.setChecked(!isUseRpt);
        } else {
            holder.setText(R.id.user_limit, "使用说明:订单满" + rptBean.getRpacket_limit() + "元可用");
            holder.setText(R.id.amount, "¥" + rptBean.getRpacket_price());
            holder.setText(R.id.time_limit, "有效期" + rptBean.getRpacket_start_time_text() + "—" + rptBean.getRpacket_end_time_text());
            holder.setText(R.id.title, "平台红包");
            final CheckBox checkBox = (CheckBox) holder.getView(R.id.select);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        //选中后不可更改
                        checkBox.setClickable(false);
                    } else {
                        //取消选中后，可以更改
                        checkBox.setClickable(true);
                    }
                    //用户check的状态
                    iSelectedRptView.onRptItemCheckedChange(position,rptBean.getRpacket_t_id(), isChecked);
                }
            });
            //列表数据刷新的是绑定的数据，因为与之前用户check的状态一致，不会再走进去oncheckchange
            checkBox.setChecked(rptBean.getCheck());
        }
    }

    public void setiSelectedRptView(ISelectedRptView iSelectedRptView) {
        this.iSelectedRptView = iSelectedRptView;
    }

    public void isUseRpt(boolean isUseRpt) {
        this.isUseRpt = isUseRpt;
    }

}
