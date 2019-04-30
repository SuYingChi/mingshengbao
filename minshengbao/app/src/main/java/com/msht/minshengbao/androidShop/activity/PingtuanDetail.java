package com.msht.minshengbao.androidShop.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.Fragment.GoodFragment;
import com.msht.minshengbao.androidShop.adapter.UserPingTunAdpter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.customerview.XScrollView;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.Imagebean;
import com.msht.minshengbao.androidShop.shopBean.UserPinTunBean;
import com.msht.minshengbao.androidShop.util.AddViewHolder;
import com.msht.minshengbao.androidShop.util.DateUtils;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.DrawbleUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PermissionUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.IGetShareUrlView;
import com.msht.minshengbao.androidShop.viewInterface.IPingTuanDetailView;
import com.msht.minshengbao.androidShop.viewInterface.IPromotionRuleView;
import com.msht.minshengbao.androidShop.wxapi.WXEntryActivity;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yanzhenjie.permission.Permission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.msht.minshengbao.androidShop.Fragment.GoodFragment.THUMB_SIZE;
import static com.msht.minshengbao.androidShop.Fragment.GoodFragment.bmpToByteArray;
import static com.msht.minshengbao.androidShop.Fragment.GoodFragment.buildTransaction;

public class PingtuanDetail extends ShopBaseActivity implements IPingTuanDetailView, IPromotionRuleView, IGetShareUrlView {
    private String pingtuanid;
    private String buyerid;
    @BindView(R.id.xsl)
    XScrollView xsl;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.name)
    TextView goodname;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.marketprice)
    TextView marketprice;
    @BindView(R.id.hour)
    TextView hour;
    @BindView(R.id.minute)
    TextView minute;
    @BindView(R.id.second)
    TextView second;
    @BindView(R.id.leftnum)
    TextView leftnum;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.share)
    TextView share;
    @BindView(R.id.backmain)
    TextView backmain;
    @BindView(R.id.pingtuanrule)
    TextView pingtuanrule;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.ll_2)
    LinearLayout ll_2;
    @BindView(R.id.ll_1)
    LinearLayout ll_1;
    @BindView(R.id.success)
    TextView tvSuccess;
    @BindView(R.id.ll_container)
    LinearLayout llcontainer;

    private List<UserPinTunBean> list=new ArrayList<UserPinTunBean>();
    private UserPingTunAdpter adapter;
    private CountDownTimer countDownTimer;
    private String copyLink;
    private String goods_name;
    private String goods_image_url;
    private String pintuan_price;
    private String goods_price;
    private Object shareQrCodeImageUrl;
    private Drawable qrCodeImage;
    private String goodsid;
    private String shareUrl;
    private String goods_jingle;
    private String title;
    private String jingle;

    @Override
    protected void setLayout() {
        setContentView(R.layout.pintuan);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pingtuanid = getIntent().getStringExtra("pingtuanid");
        buyerid = getIntent().getStringExtra("buyer_id");
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        llm.setAutoMeasureEnabled(true);
        rcl.setLayoutManager(llm);
        adapter = new UserPingTunAdpter(this, list);
        rcl.setAdapter(adapter);
        toolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(this),0,0);
        TypedArray actionbarSizeTypedArray = obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int toolbarHeight = (int) (actionbarSizeTypedArray.getDimension(0, 0));
        final int bannerHeight = DimenUtil.dip2px(150) - toolbarHeight;
         toolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                , ContextCompat.getColor(this, R.color.white), 0));
        xsl.setXScrollViewListener(new XScrollView.XScrollViewListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldx, int oldy) {
                Log.e("scrollChanged", "shop  y=  " + y + "oldy=  " + oldy);
                if (y < 0) {
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(PingtuanDetail.this, R.color.white), 1));
                } else if (y <= bannerHeight) {
                    float alpha = (float) y / bannerHeight;
                    toolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(PingtuanDetail.this, R.color.white), alpha));
                } else {
                    toolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(PingtuanDetail.this, R.color.white), 1));
                }
            }

            @Override
            public void onScrollOverTop() {
                //   mToolbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollNormal() {
                toolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollOverBottom() {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShopPresenter.getPingTuanDetail(this);
        ShopPresenter.getPromotionRule(this);
    }

    @Override
    public void onGetPingTuanDetail(String s) {
        try {
            JSONObject pintuan_info = new JSONObject(s).optJSONObject("datas").optJSONObject("pintuan_info");
            if(pintuan_info==null){
                PopUtil.toastInCenter("无拼团信息");
                finish();
            }else {
                if (pintuan_info.has("goods_jingle")) {
                    goods_jingle = pintuan_info.optString("goods_jingle");
                }
                goodsid = pintuan_info.optString("goods_id");
                JSONArray log_list = pintuan_info.optJSONArray("log_list");
                list.clear();
                for (int i = 0; i < log_list.length(); i++) {
                    list.add(JsonUtil.toBean(log_list.optJSONObject(i).toString(), UserPinTunBean.class));
                }
                Long pintuan_end_time = pintuan_info.optLong("pintuan_end_time");
                int left = pintuan_info.optInt("num");
                int minnum = Integer.valueOf(pintuan_info.optString("min_num"));
                leftnum.setText(left + "人");
                adapter.notifyDataSetChanged();
                llcontainer.removeAllViews();
                if (minnum > list.size()) {
                    for (int i = 0; i < minnum - list.size(); i++) {
                        AddViewHolder ad = new AddViewHolder(PingtuanDetail.this, R.layout.item_userpingtuan_foot);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(DimenUtil.dip2px(getResources().getDimension(R.dimen.shop_home_area_margin)), 0, 0, 0);
                        llcontainer.addView(ad.getCustomView(), params);
                    }
                }
                if(countDownTimer!=null){
                    countDownTimer.cancel();
                }
                if (left > 0) {
                    ll_2.setVisibility(View.VISIBLE);
                    ll_1.setVisibility(View.VISIBLE);
                    tvSuccess.setVisibility(View.GONE);
                    share.setVisibility(View.VISIBLE);
                    ShopPresenter.getShareUrl(this, "1", pingtuanid, buyerid);
                    ShopPresenter.getShareUrl(this, "3", pingtuanid, buyerid);
                    ShopPresenter.getShareUrl(this, "2", pingtuanid, buyerid);
                    if (pintuan_end_time > 0) {
                        countDownTimer = new CountDownTimer(pintuan_end_time * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                List<String> list = DateUtils.secondFormatToLeftDay(millisUntilFinished / 1000);
                                hour.setText(list.get(1));
                                minute.setText(list.get(2));
                                second.setText(list.get(3));
                                share.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        LayoutInflater inflaterDl = LayoutInflater.from(PingtuanDetail.this);
                                        LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                                                R.layout.item_share_bottom, null);
                                        RecyclerHolder holder = new RecyclerHolder(PingtuanDetail.this, layout);
                                        final AlertDialog dialog = new AlertDialog.Builder(PingtuanDetail.this, R.style.ActionSheetDialogStyle).create();
                                        layout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });
                                        holder.getView(R.id.ll_share_wx).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (!isWeChatAppInstalled()) {
                                                    PopUtil.showComfirmDialog(PingtuanDetail.this, "", "未安装微信", "", "", null, null, true);
                                                } else {
                                                    Glide.with(PingtuanDetail.this).load(goods_image_url).into(new SimpleTarget<Drawable>() {
                                                        @Override
                                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                            WXWebpageObject webpage = new WXWebpageObject();
                                                            webpage.webpageUrl = shareUrl;
                                                            WXMediaMessage msg = new WXMediaMessage(webpage);
                                                            msg.title = PingtuanDetail.this.title;
                                                            String s;
                                                            if (TextUtils.isEmpty(jingle)) {
                                                                s = goods_name.replace("\r", "");
                                                            } else {
                                                                s = jingle.replace("\r", "");
                                                            }
                                                            msg.description = s;
                                                            Bitmap bmp = DrawbleUtil.drawableToBitmap(resource);
                                                            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                                            msg.thumbData = bmpToByteArray(thumbBmp, true);
                                                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                                                            req.transaction = buildTransaction("webpage");
                                                            req.message = msg;
                                                            req.scene = SendMessageToWX.Req.WXSceneSession;
                                                            IWXAPI api = WXAPIFactory.createWXAPI(PingtuanDetail.this, WXEntryActivity.APP_ID);
                                                            api.sendReq(req);
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        holder.getView(R.id.ll_share_qrcode).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                LayoutInflater inflaterDl = LayoutInflater.from(PingtuanDetail.this);
                                                LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                                                        R.layout.dialog_share_qrcode, null);
                                                RecyclerHolder holder = new RecyclerHolder(PingtuanDetail.this, layout);
                                                final AlertDialog dialog2 = new AlertDialog.Builder(PingtuanDetail.this, R.style.share_qrcode_dialog).create();
                                                final ImageView ivQrcode = holder.getView(R.id.qrcode);
                                                ViewGroup.LayoutParams layoutParams = ivQrcode.getLayoutParams();
                                                layoutParams.width= DimenUtil.getScreenWidth()/3;
                                                layoutParams.height=DimenUtil.getScreenWidth()/3;
                                                ivQrcode.setLayoutParams(layoutParams);
                                                ImageView ivv  = holder.getView(R.id.image);
                                                ViewGroup.LayoutParams layoutParams2 = ivv.getLayoutParams();
                                                layoutParams2.height=DimenUtil.getScreenHeight()/3;
                                                ivv.setLayoutParams(layoutParams2);
                                                GlideUtil.loadRemoteImg(PingtuanDetail.this,ivv,goods_image_url);
                                                Glide.with(PingtuanDetail.this).load(shareQrCodeImageUrl).into(new SimpleTarget<Drawable>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                        qrCodeImage = resource;
                                                        ivQrcode.setImageDrawable(resource);
                                                    }
                                                });
                                                holder.setText(R.id.good_name, goods_name);
                                                holder.setText(R.id.good_jingle, goods_jingle);
                                                holder.setText(R.id.good_price, StringUtil.getPriceSpannable12String(PingtuanDetail.this, goods_price, R.style.big_money, R.style.big_money));
                                                TextView tvorigprice = holder.getView(R.id.good_orginal_price);
                                                tvorigprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //设置中划线并加清晰
                                                tvorigprice.setText(StringUtil.getPriceSpannable12String(PingtuanDetail.this, goods_price, R.style.small_money, R.style.small_money));
                                                holder.getView(R.id.save).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (qrCodeImage != null) {
                                                            if (Build.VERSION.SDK_INT >= 23) {
                                                                PermissionUtils.requestPermissions(PingtuanDetail.this, new PermissionUtils.PermissionRequestFinishListener() {
                                                                    @Override
                                                                    public void onPermissionRequestSuccess(List<String> permissions) {
                                                                        Bitmap bitmap = DrawbleUtil.drawableToBitmap(qrCodeImage);
                                                                        if (DrawbleUtil.saveImageToGallery(PingtuanDetail.this, bitmap) != null) {
                                                                            PopUtil.showAutoDissHookDialog(PingtuanDetail.this, "已保存到本地相册", 200);
                                                                        }
                                                                    }
                                                                }, Permission.WRITE_EXTERNAL_STORAGE);
                                                            } else {
                                                                Bitmap bitmap = DrawbleUtil.drawableToBitmap(qrCodeImage);
                                                                if (DrawbleUtil.saveImageToGallery(PingtuanDetail.this, bitmap) != null) {
                                                                    PopUtil.showAutoDissHookDialog(PingtuanDetail.this, "已保存到本地相册", 200);
                                                                }
                                                            }

                                                        }
                                                    }
                                                });
                                                layout.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog2.dismiss();
                                                    }
                                                });
                                                dialog2.setCancelable(true);
                                                dialog2.setCanceledOnTouchOutside(true);
                                                dialog2.show();
                                                dialog2.getWindow().setContentView(layout);
                                                dialog.dismiss();

                                            }
                                        });
                                        holder.getView(R.id.ll_share_wxq).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (!isWeChatAppInstalled()) {
                                                    PopUtil.showComfirmDialog(PingtuanDetail.this, "", "未安装微信", "", "", null, null, true);
                                                } else {
                                                    Glide.with(PingtuanDetail.this).load(goods_image_url).into(new SimpleTarget<Drawable>() {
                                                        @Override
                                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                            WXWebpageObject webpage = new WXWebpageObject();
                                                            webpage.webpageUrl = shareUrl;
                                                            WXMediaMessage msg = new WXMediaMessage(webpage);
                                                            msg.title = PingtuanDetail.this.title;
                                                            String s;
                                                            if (TextUtils.isEmpty(jingle)) {
                                                                s = goods_name.replace("\r", "");
                                                            } else {
                                                                s = jingle.replace("\r", "");
                                                            }
                                                            s = s.replace("\t", "");
                                                            msg.description = s;
                                                            Bitmap bmp = DrawbleUtil.drawableToBitmap(resource);
                                                            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                                            msg.thumbData = bmpToByteArray(thumbBmp, true);
                                                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                                                            req.transaction = buildTransaction("webpage");
                                                            req.message = msg;
                                                            req.scene = SendMessageToWX.Req.WXSceneTimeline;
                                                            IWXAPI api = WXAPIFactory.createWXAPI(PingtuanDetail.this, WXEntryActivity.APP_ID);
                                                            api.sendReq(req);
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        holder.getView(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });
                                        holder.getView(R.id.ll_share_copy).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                                // 将文本内容放到系统剪贴板里。
                                                cm.setText(copyLink);
                                                PopUtil.toastInBottom("已复制");
                                            }
                                        });
                                        dialog.setCancelable(true);
                                        dialog.setCanceledOnTouchOutside(true);
                                        dialog.show();
                                        dialog.getWindow().setContentView(layout);
                                        //dialog属性编辑放在show方法后边
                                        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
                                        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
                                        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                        attributes.gravity = Gravity.BOTTOM;
                                        dialog.getWindow().setAttributes(attributes);
                                    }
                                });
                            }

                            @Override
                            public void onFinish() {
                                ll_2.setVisibility(View.GONE);
                                ll_1.setVisibility(View.GONE);
                                tvSuccess.setVisibility(View.VISIBLE);
                                tvSuccess.setText("拼团已经结束");
                                share.setVisibility(View.GONE);
                            }
                        }.start();
                    } else {
                        ll_2.setVisibility(View.GONE);
                        ll_1.setVisibility(View.GONE);
                        tvSuccess.setVisibility(View.VISIBLE);
                        tvSuccess.setText("拼团已经结束");
                        share.setVisibility(View.GONE);
                    }
                } else {
                    ll_2.setVisibility(View.GONE);
                    ll_1.setVisibility(View.GONE);
                    tvSuccess.setVisibility(View.VISIBLE);
                    tvSuccess.setText("拼团已经成功，请前往订单页面查询");
                    share.setVisibility(View.GONE);
                }
                goods_name = pintuan_info.optString("goods_name");
                goodname.setText(goods_name);
                goods_image_url = pintuan_info.optString("goods_image_url");
                GlideUtil.loadRemoteImg(this, iv, goods_image_url);
                pintuan_price = pintuan_info.optString("pintuan_price");
                goods_price = pintuan_info.optString("goods_price");
                price.setText(StringUtil.getPriceSpannable12String(this, pintuan_price, R.style.big_money, R.style.big_money));
                marketprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //设置中划线并加清晰
                marketprice.setText(StringUtil.getPriceSpannable12String(this, goods_price, R.style.small_money, R.style.small_money));
                backmain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PingtuanDetail.this, MainActivity.class);
                        intent.putExtra("index", 1);
                        startActivity(intent);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPingTuanId() {
        return pingtuanid;
    }

    @Override
    public String getBuyerId() {
        return buyerid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public String getPromotionId() {
        return pingtuanid;
    }

    @Override
    public void onGetPromotionRule(String s) {
        try {
            String pintuan_rules = new JSONObject(s).optJSONObject("datas").optString("pintuan_rules");
            pingtuanrule.setText(pintuan_rules);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private boolean isWeChatAppInstalled() {
        IWXAPI api = WXAPIFactory.createWXAPI(this, WXEntryActivity.APP_ID);
        if (api.isWXAppInstalled() && api.isWXAppSupportAPI()) {
            return true;
        } else {
            final PackageManager packageManager = getPackageManager();
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equalsIgnoreCase("com.tencent.mm")) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public String getGoodId() {
        return goodsid;
    }

    @Override
    public void onGetShareUrlSuccess(String s, String type) {
        if (type.equals("1")) {
            try {
                JSONObject obj = new JSONObject(s);
                title = obj.optString("title");
                jingle =  obj.optString("jingle");
                JSONObject objj = obj.optJSONObject("datas");
                shareUrl = objj.optString("urlStr");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }//二维码
        else if (type.equals("3")) {
            try {
                JSONObject obj = new JSONObject(s);
                JSONObject objj = obj.optJSONObject("datas");
                shareQrCodeImageUrl = objj.optString("QRImage");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //复制链接
        else if (type.equals("2")) {
            try {
                JSONObject obj = new JSONObject(s);
                JSONObject objj = obj.optJSONObject("datas");
                copyLink = objj.optString("urlStr");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
