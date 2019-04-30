package com.msht.minshengbao.androidShop.Fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.androidShop.adapter.GiftArrayAdapter;
import com.msht.minshengbao.androidShop.adapter.PingtuanAdapter;
import com.msht.minshengbao.androidShop.activity.ShopComfirmOrdersActivity;
import com.msht.minshengbao.androidShop.activity.ShopSelectSiteActivity;
import com.msht.minshengbao.androidShop.activity.ShopStoreMainActivity;
import com.msht.minshengbao.androidShop.adapter.HorizontalVoucherAdpter;
import com.msht.minshengbao.androidShop.customerview.GoodFmVoucherDialog;
import com.msht.minshengbao.androidShop.customerview.GoodPintuanDialog;
import com.msht.minshengbao.androidShop.customerview.UserPintuanDialog;
import com.msht.minshengbao.androidShop.shopBean.ComfirmShopGoodBean;
import com.msht.minshengbao.androidShop.shopBean.GiftBean;
import com.msht.minshengbao.androidShop.shopBean.GuiGeBean;
import com.msht.minshengbao.androidShop.shopBean.PingTuanBean;
import com.msht.minshengbao.androidShop.shopBean.SimpleCarBean;
import com.msht.minshengbao.androidShop.shopBean.VoucherBean;
import com.msht.minshengbao.androidShop.util.DateUtils;
import com.msht.minshengbao.androidShop.util.DimenUtil;
import com.msht.minshengbao.androidShop.util.DrawbleUtil;
import com.msht.minshengbao.androidShop.util.PermissionUtils;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.viewInterface.DialogInterface;
import com.msht.minshengbao.androidShop.viewInterface.IGetVoucherView;
import com.msht.minshengbao.androidShop.viewInterface.IModifyCarGoodNumView;
import com.msht.minshengbao.androidShop.viewInterface.OnDissmissLisenter;
import com.msht.minshengbao.androidShop.wxapi.WXEntryActivity;
import com.msht.minshengbao.androidShop.adapter.GoodsDetailRecommendAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.customerview.ImageCycleView;
import com.msht.minshengbao.androidShop.customerview.XScrollView;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.AddCarBean;
import com.msht.minshengbao.androidShop.shopBean.GoodCommendBean;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.GoodDetailActivityListener;
import com.msht.minshengbao.androidShop.viewInterface.IAddCollectionView;
import com.msht.minshengbao.androidShop.viewInterface.ICarListView;
import com.msht.minshengbao.androidShop.viewInterface.IGetShareUrlView;
import com.msht.minshengbao.androidShop.viewInterface.IShopDeleteCollectionView;
import com.msht.minshengbao.androidShop.viewInterface.IShopGoodDetailView;
import com.msht.minshengbao.functionActivity.myActivity.LoginActivity;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yanzhenjie.permission.Permission;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GoodFragment extends ShopBaseLazyFragment implements IShopGoodDetailView, ICarListView, IAddCollectionView, IGetShareUrlView, IShopDeleteCollectionView, IModifyCarGoodNumView, IGetVoucherView, DialogInterface {
    public static final int THUMB_SIZE = 150;
    public static final int NOT_ALLOW = 0;
    public static final int JOIN_DEFAULT_TUAN = 1;
    public static final int SELECT_TUAN = 2;
    public static final int CREAT_NEW_TUAN = 3;
    private String goodsid;
    @BindView(R.id.cycleView)
    ImageCycleView imageCycleView;
    @BindView(R.id.sv)
    XScrollView xScrollView;
    private int toolbarHeight;
    @BindView(R.id.goods_name)
    TextView tvgoods_name;
    @BindView(R.id.goods_jingle)
    TextView tvgoods_jingle;
    @BindView(R.id.price)
    TextView tvprice;
    @BindView(R.id.goods_marketprice)
    TextView tvgoods_marketprice;
    @BindView(R.id.kuaidi)
    TextView tvkuaidi;
    @BindView(R.id.saleCount)
    TextView tvgoods_salenum;
    @BindView(R.id.percent)
    TextView tvGood_percent;
    @BindView(R.id.all)
    TextView tvall;
    @BindView(R.id.gv)
    MyNoScrollGridView gv;
    @BindView(R.id.collected)
    ImageView iv;
    @BindView(R.id.share)
    ImageView ivShare;
    @BindView(R.id.ll_2)
    LinearLayout ll_2;
    @BindView(R.id.ll_3)
    LinearLayout ll_3;
    @BindView(R.id.yixuan)
    TextView tvYixuan;
    @BindView(R.id.kuaidiinfo)
    TextView kuaidiInfo;
    @BindView(R.id.storeinfo)
    TextView tvstore;
    @BindView(R.id.ispickupself)
    TextView tvPickupself;
    @BindView(R.id.zitistore)
    TextView tvZiti;
    @BindView(R.id.ll_voucher)
    LinearLayout llvouvher;
    @BindView(R.id.rcl_voucher)
    RecyclerView rclVoucher;
    @BindView(R.id.storeiv)
    ImageView storeiv;
    @BindView(R.id.storetv)
    TextView storetv;
    @BindView(R.id.storeall)
    TextView storeall;
    @BindView(R.id.storekan)
    TextView storeKan;
    @BindView(R.id.pingtaunRcl)
    RecyclerView pingtuanRcl;
    @BindView(R.id.pingtuanhead)
    LinearLayout llpingtuan;
    @BindView(R.id.tv1)
    TextView pingtuanNum;
    @BindView(R.id.tv2)
    TextView tvMorePingtuan;
    @BindView(R.id.pingtuan_price)
    TextView tvPingtuan_price;
    @BindView(R.id.pingtuan_market_price)
    TextView tvPingtuan_market_price;
    @BindView(R.id.pingednum)
    TextView tvPingTuanNum;
    @BindView(R.id.day)
    TextView tvDay;
    @BindView(R.id.hour)
    TextView tvHour;
    @BindView(R.id.minute)
    TextView tvMinute;
    @BindView(R.id.second)
    TextView tvSecond;
    @BindView(R.id.ll_pingtuan)
    LinearLayout ll_PingTuan;
    @BindView(R.id.ll_miaosha)
    LinearLayout ll_miaosha;
    @BindView(R.id.miaosha_market_price)
    TextView tvMsmarketprice;
    @BindView(R.id.miaosha_price)
    TextView tvMiaoshaPrice;
    @BindView(R.id.ms_day)
    TextView tvMsDay;
    @BindView(R.id.ms_hour)
    TextView tvMsHour;
    @BindView(R.id.ms_minute)
    TextView tvMsMinute;
    @BindView(R.id.ms_second)
    TextView tvMsSecond;
    @BindView(R.id.ll_gift)
    LinearLayout llgift;
    @BindView(R.id.gift_rcl)
    RecyclerView giftRcl;
    List<VoucherBean> voucherList = new ArrayList<VoucherBean>();
    private GoodDetailActivityListener goodDetailActivityListener;
    private TypedArray actionbarSizeTypedArray;
    private String goods_name;
    private String goods_price;
    private String goods_jingle;
    private String goods_marketprice;
    private String kuaidi;
    private String goods_salenum;
    private int selectedGoodNum = 1;
    private String goods_storage;
    private String imageUrlDialog;
    private boolean initedView = false;
    private String shareUrl;
    private boolean is_favorate;
    private String tid = "";
    private String shareImageUrl;
    private String copyLink;
    private String shareQrCodeImageUrl;
    private String imageUrlShare;
    private int MY_PERMISSIONS_REQUEST = 200;
    private Drawable qrCodeImage;
    private String storeName;
    private String storeId;
    private String isPickup_self;
    private int goodStorage;
    private String guigename;
    private List<GuiGeBean> guigeList = new ArrayList<GuiGeBean>();
    private int selectedGuigePosition = 0;
    private List<SimpleCarBean> caridlist = new ArrayList<SimpleCarBean>();
    private String carid;
    private ArrayList<String> imagelist = new ArrayList<String>();
    private List<PingTuanBean> pingTuanlist = new ArrayList<PingTuanBean>();
    private String selectedGuigeName = "";
    private String pintuan_promotion;
    private GoodFmVoucherDialog voucherDialog;
    private PingtuanAdapter pingtuanAdapter;
    private CountDownTimer countDownTimer;
    private String goods_promotion_type;
    private CountDownTimer miaoshaCountDownTimer;
    private List<GiftBean> giftArraylist = new ArrayList<GiftBean>();
    private GiftArrayAdapter giftArrayAdapter;
    private UserPintuanDialog userPinTunDialog;
    private GoodPintuanDialog goodPingTunDialog;
    public long millisUntilFinished;
    private int pintuan_max_num = 1;
    private int pingtuanType;

    public String getPintuan_default_log_id() {
        return pintuan_default_log_id;
    }

    public String getPintuan_default_buyer_id() {
        return pintuan_default_buyer_id;
    }

    private String pintuan_default_log_id;
    private String pintuan_default_buyer_id;

    @Override
    protected int setLayoutId() {
        return R.layout.good_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        if (arg != null) {
            goodsid = arg.getString("goodsid");
            String type = arg.getString("type");
        }
        actionbarSizeTypedArray = getContext().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        toolbarHeight = (int) (actionbarSizeTypedArray.getDimension(0, 0) * 2);
    }

    @Override
    protected void initView() {
        super.initView();
        ViewGroup.LayoutParams bannerParams = imageCycleView.getLayoutParams();
        final int bannerHeight = bannerParams.height - toolbarHeight - ImmersionBar.getStatusBarHeight(getActivity());
        xScrollView.setXScrollViewListener(new XScrollView.XScrollViewListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldx, int oldy) {
                Log.e("scrollChanged", "shop  y=  " + y + "oldy=  " + oldy);
                if (y < 0) {
                    goodDetailActivityListener.onScrollChange1();
                } else if (y <= bannerHeight) {
                    goodDetailActivityListener.onScrollChange2(y, bannerHeight);
                } else {
                    goodDetailActivityListener.onScrollChange3();
                }
            }

            @Override
            public void onScrollOverTop() {
                goodDetailActivityListener.onScrollChange4();
            }

            @Override
            public void onScrollNormal() {
                goodDetailActivityListener.onScrollChange5();
            }

            @Override
            public void onScrollOverBottom() {

            }
        });
        initedView = true;
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_favorate) {
                    ShopPresenter.deleteCollect(GoodFragment.this, goodsid);
                } else {
                    ShopPresenter.addCollection(GoodFragment.this, goodsid);
                }
            }
        });
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflaterDl = LayoutInflater.from(getContext());
                LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                        R.layout.item_share_bottom, null);
                RecyclerHolder holder = new RecyclerHolder(getContext(), layout);
                final AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.ActionSheetDialogStyle).create();
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
                            PopUtil.showComfirmDialog(getContext(), "", "未安装微信", "", "", null, null, true);
                        } else {
                            Glide.with(GoodFragment.this).load(shareImageUrl).into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    WXWebpageObject webpage = new WXWebpageObject();
                                    webpage.webpageUrl = shareUrl;
                                    WXMediaMessage msg = new WXMediaMessage(webpage);
                                    msg.title = goods_name;
                                    String s = goods_jingle.replace("\r", "");
                                    s = s.replace("\t", "");
                                    msg.description = s;
                                    Bitmap bmp = DrawbleUtil.drawableToBitmap(resource);
                                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                    msg.thumbData = bmpToByteArray(thumbBmp, true);
                                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                                    req.transaction = buildTransaction("webpage");
                                    req.message = msg;
                                    req.scene = SendMessageToWX.Req.WXSceneSession;
                                    IWXAPI api = WXAPIFactory.createWXAPI(getContext(), WXEntryActivity.APP_ID);
                                    api.sendReq(req);
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
                holder.getView(R.id.ll_share_wxq).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isWeChatAppInstalled()) {
                            PopUtil.showComfirmDialog(getContext(), "", "未安装微信", "", "", null, null, true);
                        } else {
                            Glide.with(GoodFragment.this).load(shareImageUrl).into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    WXWebpageObject webpage = new WXWebpageObject();
                                    webpage.webpageUrl = shareUrl;
                                    WXMediaMessage msg = new WXMediaMessage(webpage);
                                    msg.title = goods_name;
                                    String s = goods_jingle.replace("\r", "");
                                    s = s.replace("\t", "");
                                    msg.description = s;
                                    Bitmap bmp = DrawbleUtil.drawableToBitmap(resource);
                                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                    msg.thumbData = bmpToByteArray(thumbBmp, true);
                                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                                    req.transaction = buildTransaction("webpage");
                                    req.message = msg;
                                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                                    IWXAPI api = WXAPIFactory.createWXAPI(getContext(), WXEntryActivity.APP_ID);
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
                        LayoutInflater inflaterDl = LayoutInflater.from(getContext());
                        LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                                R.layout.dialog_share_qrcode, null);
                        RecyclerHolder holder = new RecyclerHolder(getContext(), layout);
                        final AlertDialog dialog2 = new AlertDialog.Builder(getContext(), R.style.share_qrcode_dialog).create();
                        final ImageView ivQrcode = holder.getView(R.id.qrcode);
                        ViewGroup.LayoutParams layoutParams = ivQrcode.getLayoutParams();
                        layoutParams.width= DimenUtil.getScreenWidth()/3;
                        layoutParams.height=DimenUtil.getScreenWidth()/3;
                        ivQrcode.setLayoutParams(layoutParams);
                        ImageView ivv  = holder.getView(R.id.image);
                        ViewGroup.LayoutParams layoutParams2 = ivv.getLayoutParams();
                        layoutParams2.height=DimenUtil.getScreenHeight()/3;
                        ivv.setLayoutParams(layoutParams2);
                        GlideUtil.loadByImageView(getContext(),ivv,imagelist.get(0));
                        Glide.with(GoodFragment.this).load(shareQrCodeImageUrl).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                qrCodeImage = resource;
                                ivQrcode.setImageDrawable(resource);
                            }
                        });
                        holder.setText(R.id.good_name, goods_name);
                        holder.setText(R.id.good_jingle, goods_jingle);
                        holder.setText(R.id.good_price, StringUtil.getPriceSpannable12String(getContext(), goods_price, R.style.big_money, R.style.big_money));
                        TextView tvorigprice = holder.getView(R.id.good_orginal_price);
                        tvorigprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //设置中划线并加清晰
                        tvorigprice.setText(StringUtil.getPriceSpannable12String(getContext(), goods_marketprice, R.style.small_money, R.style.small_money));
                        holder.getView(R.id.save).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (qrCodeImage != null) {
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        PermissionUtils.requestPermissions(getContext(), new PermissionUtils.PermissionRequestFinishListener() {
                                            @Override
                                            public void onPermissionRequestSuccess(List<String> permissions) {
                                                Bitmap bitmap = DrawbleUtil.drawableToBitmap(qrCodeImage);
                                                if (DrawbleUtil.saveImageToGallery(getContext(), bitmap) != null) {
                                                    PopUtil.showAutoDissHookDialog(getContext(), "已保存到本地相册", 200);
                                                }
                                            }
                                        }, Permission.WRITE_EXTERNAL_STORAGE);
                                    } else {
                                        Bitmap bitmap = DrawbleUtil.drawableToBitmap(qrCodeImage);
                                        if (DrawbleUtil.saveImageToGallery(getContext(), bitmap) != null) {
                                            PopUtil.showAutoDissHookDialog(getContext(), "已保存到本地相册", 200);
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
                holder.getView(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                holder.getView(R.id.ll_share_copy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
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
        pingtuanAdapter = new PingtuanAdapter(getContext(), pingTuanlist);
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        lm.setAutoMeasureEnabled(true);
        pingtuanAdapter.setAdapterInterface(new PingtuanAdapter.AdapterInterface() {
            @Override
            public void onClickCanTuan(String pingTuanId) {
                showUserPingtunDialog(pingTuanId);
            }
        });
        pingtuanRcl.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        pingtuanRcl.setLayoutManager(lm);
        pingtuanRcl.setNestedScrollingEnabled(false);
        pingtuanRcl.setAdapter(pingtuanAdapter);

        giftArrayAdapter = new GiftArrayAdapter(getContext(), giftArraylist);
        LinearLayoutManager lm2 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        lm.setAutoMeasureEnabled(true);
        giftRcl.setLayoutManager(lm2);
        giftRcl.setNestedScrollingEnabled(false);
        giftRcl.setAdapter(giftArrayAdapter);
    }

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean isWeChatAppInstalled() {
        IWXAPI api = WXAPIFactory.createWXAPI(getContext(), WXEntryActivity.APP_ID);
        if (api.isWXAppInstalled() && api.isWXAppSupportAPI()) {
            return true;
        } else {
            final PackageManager packageManager = getContext().getPackageManager();
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
    protected void initData() {
        super.initData();
        if (TextUtils.isEmpty(getKey())) {
        } else {
            ShopPresenter.getCarList(this, true);
        }
        ShopPresenter.getShareUrl(this, "1");
        ShopPresenter.getShareUrl(this, "3");
        ShopPresenter.getShareUrl(this, "2");
        ShopPresenter.getGoodDetail(this);
    }

    @Override
    public void onGetGoodDetailSuccess(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject datas = jsonObject.getJSONObject("datas");
            shareImageUrl = datas.optJSONArray("spec_image").optString(0);
            tid = datas.optJSONObject("store_info").optString("member_id");
            storeId = datas.optJSONObject("store_info").optString("store_id");
            storeName = datas.optJSONObject("store_info").optString("store_name");
            tvstore.setText("本产品由" + storeName + "提供售后服务和产品支持");
            is_favorate = datas.optBoolean("is_favorate");
            if (is_favorate) {
                iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_good_collected));
            } else {
                iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_good_no_collected));
            }
            JSONArray imageList = datas.getJSONArray("image_list");
            imageUrlDialog = imageList.optJSONObject(0).optString("_mid");
            imageUrlShare = imageList.optJSONObject(0).optString("_big");
            for (int k = 0; k < imageList.length(); k++) {
                JSONObject parameterObject = imageList.optJSONObject(k);
                String mid = parameterObject.optString("_mid");
                imagelist.add(mid);
            }
            showAdv(imagelist);
            final JSONObject goods_info = datas.getJSONObject("goods_info");
            pintuan_promotion = goods_info.optString("pintuan_promotion");
            goods_promotion_type = goods_info.optString("goods_promotion_type");
            JSONObject guigenameobj = goods_info.optJSONObject("spec_name");
            JSONObject spec_valueobj = goods_info.optJSONObject("spec_value");
            JSONObject spec_listObj = datas.optJSONObject("spec_list");
            List<String> guigekey = JsonUtil.getJsonObjectKeyList(guigenameobj);
            if (guigekey.size() > 0) {
                String guigenamekey = guigekey.get(0);
                JSONObject guigevalueobj = spec_valueobj.optJSONObject(guigenamekey);
                guigename = guigenameobj.optString(guigenamekey);
                List<String> guigeidlist = JsonUtil.getJsonObjectKeyList(guigevalueobj);
                guigeList.clear();
                for (int i = 0; i < guigeidlist.size(); i++) {
                    String id = guigeidlist.get(i);
                    String guigegoodid = spec_listObj.optString(id);
                    String guigeName = guigevalueobj.optString(guigeidlist.get(i));
                    if (i == selectedGuigePosition) {
                        guigeList.add(new GuiGeBean(id, guigeName, guigegoodid, true));
                        selectedGuigeName = guigeName;
                        if (selectedGuigeName == null || guigeName.equals("null")) {
                            selectedGuigeName = "";
                        }
                    } else {
                        guigeList.add(new GuiGeBean(id, guigeName, guigegoodid, false));
                    }
                }
            } else {
                guigename = "";
                selectedGuigeName = "";
                guigeList.clear();
            }
            isPickup_self = goods_info.optString("pickup_self");
            if (TextUtils.equals(isPickup_self, "1")) {
                tvPickupself.setText("限自提");
                tvPickupself.setVisibility(View.VISIBLE);
                tvZiti.setVisibility(View.VISIBLE);
                tvZiti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ShopSelectSiteActivity.class);
                        intent.putExtra("onClick", false);
                        startActivity(intent);
                    }
                });
            } else {
                tvPickupself.setVisibility(View.INVISIBLE);
                tvZiti.setVisibility(View.INVISIBLE);
            }
            goods_name = goods_info.optString("goods_name");
            tvgoods_name.setText(goods_name);
            goods_storage = goods_info.optString("goods_storage");
            if (TextUtils.isEmpty(goods_storage)) {
                goodStorage = -1;
            } else {
                goodStorage = Integer.valueOf(goods_storage);
            }
            goodDetailActivityListener.onStorageChange(goodStorage);
            goods_jingle = goods_info.optString("goods_jingle");
            if (TextUtils.isEmpty(goods_jingle)) {
                tvgoods_jingle.setVisibility(View.GONE);
            } else {
                tvgoods_jingle.setVisibility(View.VISIBLE);
                tvgoods_jingle.setText(goods_jingle);
            }
            if (TextUtils.equals(pintuan_promotion, "1")) {
                goodDetailActivityListener.isPingTuan(true);
                goods_price = goods_info.optString("pintuan_price");
                ll_PingTuan.setVisibility(View.VISIBLE);
                ll_miaosha.setVisibility(View.GONE);
                tvprice.setVisibility(View.GONE);
                tvgoods_marketprice.setVisibility(View.GONE);
                tvPingtuan_price.setText(StringUtil.getPriceSpannable12String(getContext(), goods_price, R.style.big_money, R.style.big_money));
                tvPingtuan_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                tvPingtuan_market_price.setText(StringUtil.getPriceSpannable12String(getContext(), goods_info.optString("pintuan_goods_price"), R.style.small_money, R.style.small_money));
                tvPingTuanNum.setText(goods_info.optString("pintuan_sole_num") + "件");
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer = new CountDownTimer(goods_info.optLong("pintuan_end_time") * 1000, 1000) {


                    @Override
                    public void onTick(long millisUntilFinished) {
                        GoodFragment.this.millisUntilFinished = millisUntilFinished;
                        List<String> list = DateUtils.secondFormatToLeftDay(millisUntilFinished / 1000);
                        tvDay.setText(list.get(0));
                        tvHour.setText(list.get(1));
                        tvMinute.setText(list.get(2));
                        tvSecond.setText(list.get(3));
                    }

                    @Override
                    public void onFinish() {
                        goodDetailActivityListener.isPingTuan(false);
                        ll_PingTuan.setVisibility(View.GONE);
                        tvprice.setVisibility(View.VISIBLE);
                        tvgoods_marketprice.setVisibility(View.VISIBLE);
                        if (TextUtils.isEmpty(goods_info.optString("promotion_price"))) {
                            if (TextUtils.isEmpty(goods_info.optString("goods_promotion_price"))) {
                                if (TextUtils.isEmpty(goods_info.optString("goods_price"))) {
                                    PopUtil.toastInCenter("没有商品价格");
                                } else {
                                    goods_price = goods_info.optString("goods_price");
                                }
                            } else {
                                goods_price = goods_info.optString("goods_promotion_price");
                            }
                        } else {
                            goods_price = goods_info.optString("promotion_price");
                        }
                        tvprice.setText(StringUtil.getPriceSpannable12String(getContext(), goods_price, R.style.big_money, R.style.big_money));
                        if (TextUtils.equals(goods_info.optString("promotion_type"), "xianshi")) {
                            goods_marketprice = goods_info.optString("goods_price");
                        } else {
                            goods_marketprice = goods_info.optString("goods_marketprice");
                        }
                        tvgoods_marketprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //设置中划线并加清晰
                        tvgoods_marketprice.setText(StringUtil.getPriceSpannable12String(getContext(), goods_marketprice, R.style.small_money, R.style.small_money));
                    }
                }.start();
            } else {
                goodDetailActivityListener.isPingTuan(false);
                ll_PingTuan.setVisibility(View.GONE);
                if (TextUtils.isEmpty(goods_info.optString("promotion_price"))) {
                    if (TextUtils.isEmpty(goods_info.optString("goods_promotion_price"))) {
                        if (TextUtils.isEmpty(goods_info.optString("goods_price"))) {
                            PopUtil.toastInCenter("没有商品价格");
                        } else {
                            goods_price = goods_info.optString("goods_price");
                        }
                    } else {
                        goods_price = goods_info.optString("goods_promotion_price");
                    }
                } else {
                    goods_price = goods_info.optString("promotion_price");
                }
                //1团购 2限时 3秒杀
                switch (goods_promotion_type) {
                    case "1":
                        break;
                    case "2":
                        break;
                    case "3":
                        ll_miaosha.setVisibility(View.VISIBLE);
                        tvprice.setVisibility(View.GONE);
                        tvgoods_marketprice.setVisibility(View.GONE);
                        tvMiaoshaPrice.setText(StringUtil.getPriceSpannable12String(getContext(), goods_price, R.style.big_money, R.style.big_money));
                        tvMsmarketprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        tvMsmarketprice.setText(StringUtil.getPriceSpannable12String(getContext(), goods_info.optString("goods_marketprice"), R.style.small_money, R.style.small_money));
                        miaoshaCountDownTimer = new CountDownTimer(goods_info.optLong("left_end_time") * 1000, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                List<String> list = DateUtils.secondFormatToLeftDay(millisUntilFinished / 1000);
                                tvMsDay.setText(list.get(0));
                                tvMsHour.setText(list.get(1));
                                tvMsMinute.setText(list.get(2));
                                tvMsSecond.setText(list.get(3));
                            }

                            @Override
                            public void onFinish() {
                                ll_miaosha.setVisibility(View.GONE);
                                tvprice.setVisibility(View.VISIBLE);
                                tvgoods_marketprice.setVisibility(View.VISIBLE);
                                if (TextUtils.isEmpty(goods_info.optString("promotion_price"))) {
                                    if (TextUtils.isEmpty(goods_info.optString("goods_promotion_price"))) {
                                        if (TextUtils.isEmpty(goods_info.optString("goods_price"))) {
                                            PopUtil.toastInCenter("没有商品价格");
                                        } else {
                                            goods_price = goods_info.optString("goods_price");
                                        }
                                    } else {
                                        goods_price = goods_info.optString("goods_promotion_price");
                                    }
                                } else {
                                    goods_price = goods_info.optString("promotion_price");
                                }
                                tvprice.setText(StringUtil.getPriceSpannable12String(getContext(), goods_price, R.style.big_money, R.style.big_money));
                                if (TextUtils.equals(goods_info.optString("promotion_type"), "xianshi")) {
                                    goods_marketprice = goods_info.optString("goods_price");
                                } else {
                                    goods_marketprice = goods_info.optString("goods_marketprice");
                                }
                                tvgoods_marketprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //设置中划线并加清晰
                                tvgoods_marketprice.setText(StringUtil.getPriceSpannable12String(getContext(), goods_marketprice, R.style.small_money, R.style.small_money));
                            }
                        }.start();
                        break;
                    default:
                        tvprice.setVisibility(View.VISIBLE);
                        tvgoods_marketprice.setVisibility(View.VISIBLE);
                        break;
                }
            }
            tvprice.setText(StringUtil.getPriceSpannable12String(getContext(), goods_price, R.style.big_money, R.style.big_money));
            goods_salenum = goods_info.optString("goods_salenum");
            tvgoods_salenum.setText(String.format("销售量：%s件", goods_salenum));
            if (TextUtils.equals(goods_info.optString("promotion_type"), "xianshi")) {
                goods_marketprice = goods_info.optString("goods_price");
            } else {
                goods_marketprice = goods_info.optString("goods_marketprice");
            }
            tvgoods_marketprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //设置中划线并加清晰
            tvgoods_marketprice.setText(StringUtil.getPriceSpannable12String(getContext(), goods_marketprice, R.style.small_money, R.style.small_money));
            JSONObject goods_hair_info = datas.getJSONObject("goods_hair_info");
            kuaidi = goods_hair_info.optString("content");
            kuaidiInfo.setText(new StringBuilder().append("送至  ").append(goods_hair_info.optString("area_name")).append("  ").append(goods_hair_info.optString("if_store_cn")).toString());
            tvkuaidi.setText(String.format("快递：%s", kuaidi));
            JSONObject goods_evaluate_info = datas.getJSONObject("goods_evaluate_info");
            String good_percent = goods_evaluate_info.optString("good_percent");
            tvGood_percent.setText(String.format("商品评价         好评率%s%%", good_percent));
            String all = goods_evaluate_info.optString("all");
            tvall.setText(String.format("%s人评价", all));
            JSONArray goods_commend_list = datas.getJSONArray("goods_commend_list");

            final List<GoodCommendBean> goodCommendBeanList = new ArrayList<GoodCommendBean>();
            for (int k = 0; k < goods_commend_list.length(); k++) {
                JSONObject parameterObject = goods_commend_list.optJSONObject(k);
                GoodCommendBean bean = JsonUtil.toBean(parameterObject.toString(), GoodCommendBean.class);
                goodCommendBeanList.add(bean);
            }
            GoodsDetailRecommendAdapter gvAdapter = new GoodsDetailRecommendAdapter(getContext());
            gv.setAdapter(gvAdapter);
            gvAdapter.setmDatas(goodCommendBeanList);
            gvAdapter.notifyDataSetChanged();
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String goodsId = goodCommendBeanList.get(position).getGoods_id();
                    doShopItemViewClick("goods", goodsId);
                }
            });
            ll_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodDetailActivityListener.showBottomDialog(false);
                }
            });
            if (selectedGoodNum == 1) {
                tvYixuan.setText(selectedGuigeName + "默认x1");
            } else {
                tvYixuan.setText(selectedGuigeName + "  " + selectedGoodNum + "件");
            }
            ll_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodDetailActivityListener.goEveluateFragment();
                }
            });
            if (datas.has("voucher")) {
                JSONArray voucherArray = datas.optJSONArray("voucher");
                if (voucherArray.length() > 0) {
                    llvouvher.setVisibility(View.VISIBLE);
                    for (int i = 0; i < voucherArray.length(); i++) {
                        voucherList.add(JsonUtil.toBean(voucherArray.optJSONObject(i).toString(), VoucherBean.class));
                    }
                    HorizontalVoucherAdpter adapter = new HorizontalVoucherAdpter(getContext(), R.layout.voucher_text, voucherList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                    rclVoucher.setLayoutManager(linearLayoutManager);
                    rclVoucher.setAdapter(adapter);
                    llvouvher.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showVoucherCarDialog();
                        }
                    });
                } else {
                    llvouvher.setVisibility(View.GONE);
                }
            } else {
                llvouvher.setVisibility(View.GONE);
            }
            JSONObject storeinfo = datas.optJSONObject("store_info");
            GlideUtil.loadRemoteImg(getContext(), storeiv, storeinfo.optString("store_avatar"));
            final String storeid = storeinfo.optString("store_id");
            storeKan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShopStoreMainActivity.class);
                    intent.putExtra("id", storeid);
                    intent.putExtra("tabindex", 0);
                    startActivity(intent);
                }
            });
            storetv.setText(storeinfo.optString("store_name"));
            JSONArray pintuan_list = goods_info.optJSONArray("pintuan_list");
            pingTuanlist.clear();
            if (pintuan_list != null && pintuan_list.length() > 0) {
                for (int i = 0; i < pintuan_list.length(); i++) {
                    JSONObject obj = pintuan_list.optJSONObject(i);
                    PingTuanBean b = JsonUtil.toBean(obj.toString(), PingTuanBean.class);
                    pingTuanlist.add(b);
                }
                llpingtuan.setVisibility(View.VISIBLE);
                pingtuanNum.setText(pingTuanlist.size() + "");
                tvMorePingtuan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showGoodAllPingtunDialog();
                    }
                });
            } else {
                llpingtuan.setVisibility(View.GONE);
            }
            pingtuanAdapter.notifyChange();
            if (goodPingTunDialog != null) {
                goodPingTunDialog.refresh();
            }
            if (userPinTunDialog != null) {
                userPinTunDialog.refresh();
            }
            JSONArray gift_array = datas.optJSONArray("gift_array");
            giftArraylist.clear();
            if (gift_array != null && gift_array.length() > 0) {
                for (int i = 0; i < gift_array.length(); i++) {
                    JSONObject obj = gift_array.optJSONObject(i);
                    GiftBean b = JsonUtil.toBean(obj.toString(), GiftBean.class);
                    giftArraylist.add(b);
                }
                llgift.setVisibility(View.VISIBLE);
            } else {
                llgift.setVisibility(View.GONE);
            }
            giftArrayAdapter.notifyDataSetChanged();
            if (goods_info.optInt("pintuan_allow_new") == 0 && (TextUtils.isEmpty(goods_info.optString("pintuan_default_log_id")) || TextUtils.isEmpty(goods_info.optString("pintuan_default_buyer_id")))) {
                pingtuanType = NOT_ALLOW;
                goodDetailActivityListener.pingTuan(NOT_ALLOW);
            } else if (goods_info.optInt("pintuan_allow_new") == 0 && pingTuanlist.size() == 0 && !TextUtils.isEmpty(goods_info.optString("pintuan_default_log_id")) && !TextUtils.isEmpty(goods_info.optString("pintuan_default_buyer_id"))) {
               pingtuanType = JOIN_DEFAULT_TUAN;
                pintuan_default_log_id = goods_info.optString("pintuan_default_log_id");
                pintuan_default_buyer_id = goods_info.optString("pintuan_default_buyer_id");
                goodDetailActivityListener.pingTuan(JOIN_DEFAULT_TUAN);
            } else if (goods_info.optInt("pintuan_allow_new") == 0 && pingTuanlist.size() > 0) {
                pingtuanType = SELECT_TUAN;
                goodDetailActivityListener.pingTuan(SELECT_TUAN);
            } else if (goods_info.optInt("pintuan_allow_new") != 0) {
                pingtuanType = CREAT_NEW_TUAN;
                goodDetailActivityListener.pingTuan(CREAT_NEW_TUAN);
            }
            if (goods_info.optInt("cart") == 1) {
                goodDetailActivityListener.isAllowAddCar(true);
            } else {
                goodDetailActivityListener.isAllowAddCar(false);
            }
            if (goods_info.has("pintuan_max_num")) {
                pintuan_max_num = Integer.valueOf(goods_info.optString("pintuan_max_num"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        goodDetailActivityListener.onGetGoodDetailSuccess();
    }

    public String getTid() {
        return tid;
    }

    @Override
    public String getGoodsid() {
        return goodsid;
    }

    private void showVoucherCarDialog() {
        if (getActivity() != null && !getActivity().isFinishing() && voucherDialog == null) {
            voucherDialog = new GoodFmVoucherDialog(getContext(), this, voucherList);
            voucherDialog.show();
        } else if (getActivity() != null && !getActivity().isFinishing() && !voucherDialog.isShowing()) {
            voucherDialog.show();
        }
    }

    @Override
    public void addCar() {
        if (TextUtils.isEmpty(getKey())) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            for (SimpleCarBean bean : caridlist) {
                if (bean.getGoods_id().equals(goodsid)) {
                    carid = bean.getCart_id();
                    ShopPresenter.modifyGoodNum(this, this);
                    return;
                }
            }
            ShopPresenter.addCar(this);
        }
    }

    @Override
    public void buyGood(boolean ispingTuan) {
        if (!TextUtils.isEmpty(getKey())) {
            List<ComfirmShopGoodBean> list = new ArrayList<ComfirmShopGoodBean>();
            List<ComfirmShopGoodBean.GoodsBean> list2 = new ArrayList<ComfirmShopGoodBean.GoodsBean>();
            list2.add(new ComfirmShopGoodBean.GoodsBean(storeName, storeId, imagelist.get(0), goods_name, selectedGoodNum + "", goods_price, goodsid));
            ComfirmShopGoodBean comfirmShopGoodBean = new ComfirmShopGoodBean();
            comfirmShopGoodBean.setStore_id(storeId);
            comfirmShopGoodBean.setStore_name(storeName);
            comfirmShopGoodBean.setGoods(list2);
            list.add(comfirmShopGoodBean);
            if (!TextUtils.isEmpty(isPickup_self)) {
                Intent intent = new Intent(getActivity(), ShopComfirmOrdersActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ifCar", "0");
                bundle.putString("isPickup_self", isPickup_self);
                if (ispingTuan) {
                    bundle.putString("isPingtuan", "1");
                    if (pingtuanType==JOIN_DEFAULT_TUAN&&!TextUtils.isEmpty(pintuan_default_log_id)) {
                        bundle.putString("pingTuanid", pintuan_default_log_id);
                    }
                    if (pingtuanType==JOIN_DEFAULT_TUAN&&!TextUtils.isEmpty(pintuan_default_buyer_id)) {
                        bundle.putString("buyerId", pintuan_default_buyer_id);
                    }
                }
                bundle.putSerializable("data", (Serializable) list);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                PopUtil.toastInBottom("商品已下架或不支持购买");
            }
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }


    @Override
    public int getSelectedGoodNum() {
        return selectedGoodNum;
    }

    @Override
    public void onAddCarSuccess(String s) {
        AddCarBean bean = JsonUtil.toBean(s, AddCarBean.class);
        if (TextUtils.equals(bean.getDatas(), "1")) {
            PopUtil.showAutoDissHookDialog(getContext(), "添加购物车成功", 100, new OnDissmissLisenter() {
                @Override
                public void onDissmiss() {
                    if (!TextUtils.isEmpty(getKey())) {
                        ShopPresenter.getCarList(GoodFragment.this, false);
                    }
                }
            });
        }
    }

    @Override
    public String getCarId() {
        return carid;
    }

    @Override
    public String getCarItemNum() {
        return selectedGoodNum + "";
    }

    @Override
    public void onModifyGoodNumSuccess(String s) {
        PopUtil.showAutoDissHookDialog(getContext(), "购物车数量修改成功", 100);
        ShopPresenter.getCarList(this, false);
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public String getGuigeName() {
        return guigename;
    }

    @Override
    public List<GuiGeBean> getGuigeList() {
        return guigeList;
    }

    @Override
    public void onSelectGoodId(int childposition) {
        if (childposition != selectedGuigePosition) {
            selectedGuigePosition = childposition;
            goodsid = guigeList.get(childposition).getGuigeGoodId();
            guigename = guigeList.get(childposition).getGuigeName();
            if (selectedGoodNum == 1) {
                tvYixuan.setText(selectedGuigeName + "默认x1");
            } else {
                tvYixuan.setText(selectedGuigeName + "  " + selectedGoodNum + "件");
            }
            ShopPresenter.getGoodDetail(this);
        }
    }

    @Override
    public long getleftTime() {
        return millisUntilFinished;
    }

    @Override
    public void showAddCarDialog() {
        goodDetailActivityListener.showBottomDialog(false);
    }

    @Override
    public int getPingTuanMaxNum() {
        return pintuan_max_num;
    }


    @Override
    public void setSelectedGoodNum(int num) {
        this.selectedGoodNum = num;
        if (selectedGoodNum == 1) {
            tvYixuan.setText(selectedGuigeName + "默认x1");
        } else {
            tvYixuan.setText(selectedGuigeName + "   " + selectedGoodNum + "件");
        }
    }

    @Override
    public String getNameDialog() {
        return goods_name;
    }

    @Override
    public String getPrice() {
        return goods_price;
    }

    @Override
    public String getRemainNum() {
        return goods_storage;
    }

    @Override
    public String getImageUrl() {
        return imageUrlDialog;
    }

    private void showAdv(List<String> images) {
        final ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                GlideUtil.loadByImageView(getContext(), imageView, imageURL);

            }

            @Override
            public void onImageClick(int position, View imageView) {

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.goodDetailActivityListener = (GoodDetailActivityListener) getActivity();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        actionbarSizeTypedArray.recycle();
        if (pingtuanAdapter != null) {
            pingtuanAdapter.cancelAllTimers();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (miaoshaCountDownTimer != null) {
            miaoshaCountDownTimer.cancel();
        }
        if (goodPingTunDialog != null) {
            goodPingTunDialog.cancelAllTimers();
        }
        if (userPinTunDialog != null) {
            userPinTunDialog.cancelAllTimers();
        }
    }

    @Override
    public void onGetCarListSuccess(String s) {
        try {
            JSONObject jsonObj = new JSONObject(s);
            JSONObject dataObj = jsonObj.getJSONObject("datas");
            //String totalAddedCarNum = dataObj.optString("cart_count");
            JSONArray jsonArray = dataObj.optJSONArray("cart_list");
            int carnum = 0;
            caridlist.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray good = jsonArray.optJSONObject(i).optJSONArray("goods");
                carnum += good.length();
                for (int ii = 0; ii < good.length(); ii++) {
                    //  carnum += Integer.valueOf(good.optJSONObject(ii).optString("goods_num"));
                    caridlist.add(new SimpleCarBean(good.optJSONObject(ii).optString("goods_id"), good.optJSONObject(ii).optString("cart_id")));
                }
            }
            if (carnum > 0) {
                if (goodDetailActivityListener != null) {
                    goodDetailActivityListener.hasAddedCar(carnum + "");
                }
            } else {
                if (goodDetailActivityListener != null) {
                    goodDetailActivityListener.noAddedCar();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void refreshCarList() {
        if (!TextUtils.isEmpty(getKey())) {
            ShopPresenter.getCarList(this, true);
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (initedView) {
            if (!TextUtils.isEmpty(getKey())) {
                ShopPresenter.getCarList(this, true);
            }
        }
    }

    @Override
    public void onAddCollectSuccess(String s) {
        PopUtil.showAutoDissHookDialog(getContext(), "添加到我的收藏夹", 100);
        is_favorate = true;
        iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_good_collected));
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


    @Override
    public void onDeleteCollectSuccess(String s, int position) {
        PopUtil.showAutoDissHookDialog(getContext(), "取消收藏", 100);
        is_favorate = false;
        iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_good_no_collected));
    }

    @Override
    public void onGetVoucher(String voucherid) {
        ShopPresenter.getVoucher(this, voucherid);
    }

    @Override
    public void onGetVoucherSuccess(String s) {
        PopUtil.showAutoDissHookDialog(getContext(), "成功领取代金券", 0);
    }

    public void showGoodAllPingtunDialog() {
        if (this.getActivity() != null && !this.getActivity().isFinishing() && goodPingTunDialog == null) {
            goodPingTunDialog = new GoodPintuanDialog(getContext(), goodsid, this);
            goodPingTunDialog.show();
        } else if (this.getActivity() != null && !this.getActivity().isFinishing() && !goodPingTunDialog.isShowing()) {
            goodPingTunDialog.show();
        }
    }

    private void showUserPingtunDialog(String pingtuanid) {
        if (this.getActivity() != null && !this.getActivity().isFinishing()) {
            if (userPinTunDialog == null) {
                userPinTunDialog = new UserPintuanDialog(getContext(), pingtuanid, this);
                userPinTunDialog.show();
            } else if (userPinTunDialog.getPingtuanid().equals(pingtuanid) && !userPinTunDialog.isShowing()) {
                userPinTunDialog.show();
            } else if (!userPinTunDialog.getPingtuanid().equals(pingtuanid)) {
                userPinTunDialog.cancelAllTimers();
                userPinTunDialog = new UserPintuanDialog(getContext(), pingtuanid, this);
                userPinTunDialog.show();
            }
        }
    }

    @Override
    public void onClickPingTuan(String pingTuanid, String buyerId) {
        if (!TextUtils.isEmpty(getKey())) {
            List<ComfirmShopGoodBean> list = new ArrayList<ComfirmShopGoodBean>();
            List<ComfirmShopGoodBean.GoodsBean> list2 = new ArrayList<ComfirmShopGoodBean.GoodsBean>();
            list2.add(new ComfirmShopGoodBean.GoodsBean(storeName, storeId, imagelist.get(0), goods_name, selectedGoodNum + "", goods_price, goodsid));
            ComfirmShopGoodBean comfirmShopGoodBean = new ComfirmShopGoodBean();
            comfirmShopGoodBean.setStore_id(storeId);
            comfirmShopGoodBean.setStore_name(storeName);
            comfirmShopGoodBean.setGoods(list2);
            list.add(comfirmShopGoodBean);
            if (!TextUtils.isEmpty(isPickup_self)) {
                Intent intent = new Intent(getActivity(), ShopComfirmOrdersActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ifCar", "0");
                bundle.putString("isPickup_self", isPickup_self);
                bundle.putString("isPingtuan", "1");
                bundle.putString("pingTuanid", pingTuanid);
                bundle.putString("buyerId", buyerId);
                bundle.putSerializable("data", (Serializable) list);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                PopUtil.toastInBottom("商品已下架或不支持购买");
            }
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }


    public void refreshGoodDetail() {
        ShopPresenter.getGoodDetail(this);
    }
}
