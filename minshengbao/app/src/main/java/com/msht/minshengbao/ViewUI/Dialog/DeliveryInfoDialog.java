package com.msht.minshengbao.ViewUI.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.CircleImageView;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/24  
 */
public class DeliveryInfoDialog  {
    private TextView tvName;
    private TextView tvWorkNo;
    private TextView tvSite;
    private TextView tvTotalOrderCount;
    private TextView tvMyOrderCount;
    private Context context;
    private Dialog dialog;
    private Display display;
    private String mPhone;
    private String mUrl;

    private OnCallPhoneClickListener callClickListener;
    public interface OnCallPhoneClickListener {
        /**
         *  回调返回数据
         * @param phone 电话
         */
        void onClick(String phone);
    }
    public DeliveryInfoDialog setOnCallPhoneClickListener(OnCallPhoneClickListener listener){
        this.callClickListener=listener;
        return this;
    }
    public DeliveryInfoDialog(Context context,String employeeHeadUrl,String mPhone) {
        this.context = context;
        this.mUrl=employeeHeadUrl;
        this.mPhone=mPhone;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }
    public DeliveryInfoDialog builder() {
        // 获取Dialog布局
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_lpg_delivery_info,null);
        view.setMinimumWidth(display.getWidth());
        tvName=(TextView)view.findViewById(R.id.id_employer_name);
        tvTotalOrderCount=(TextView)view.findViewById(R.id.id_delivery_order_count);
        tvMyOrderCount=(TextView)view.findViewById(R.id.id_my_order_count);
        tvWorkNo=(TextView)view.findViewById(R.id.id_work_id);
        tvSite=(TextView)view.findViewById(R.id.id_site_text);
        TextView tvPhone=(TextView)view.findViewById(R.id.id_phone_text);
        CircleImageView circleImageView=(CircleImageView)view.findViewById(R.id.id_portrait_view);
        ImageView phoneImage=(ImageView)view.findViewById(R.id.id_phone_img) ;
        ImageView closeImage=(ImageView)view.findViewById(R.id.id_close_roundel);
        String phoneText="电话: "+mPhone;
        tvPhone.setText(phoneText);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        phoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callClickListener!=null){
                    callClickListener.onClick(mPhone);
                }
                dialog.dismiss();
            }
        });
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.potrait);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        Glide.with(context)
                .load(mUrl)
                .apply(requestOptions).into(circleImageView);
        dialog = new Dialog(context, R.style.PromptDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow!=null){
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            dialogWindow.setAttributes(lp);
        }
        return this;
    }

    public DeliveryInfoDialog setEmployeeName(String name){
        tvName.setText(name);
        return this;
    }
    public DeliveryInfoDialog setEmployeeMoId(String moId){
        String workText="工号: "+moId;
        tvWorkNo.setText(workText);
        return this;
    }
    public DeliveryInfoDialog setSendTotal(String count){
        tvTotalOrderCount.setText(count);
        return this;
    }
    public DeliveryInfoDialog setSendForMeCount(String forMeCount){
        tvMyOrderCount.setText(forMeCount);
        return this;
    }
    public DeliveryInfoDialog setSiteName(String siteName){
        String siteNameText="区域: "+siteName;
        tvSite.setText(siteNameText);
        return this;
    }
    public  DeliveryInfoDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public DeliveryInfoDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
