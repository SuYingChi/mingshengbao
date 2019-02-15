package com.msht.minshengbao.androidShop.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.androidShop.adapter.RefundAllFormPhotoPickerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.customerview.SelectRefundReasonIdDialog;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.PostRefundPicBean;
import com.msht.minshengbao.androidShop.shopBean.RefundFormBean;
import com.msht.minshengbao.androidShop.shopBean.RefundReasonItemBean;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PermissionUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.IOnSelectedReasonItemView;
import com.msht.minshengbao.androidShop.viewInterface.IPostRefundPicView;
import com.msht.minshengbao.androidShop.viewInterface.IPostRefundView;
import com.msht.minshengbao.androidShop.viewInterface.OnDissmissLisenter;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.functionActivity.repairService.EnlargePicActivity;
import com.yanzhenjie.permission.Permission;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import butterknife.BindView;

public class RefundFormMoneyActivity extends ShopBaseActivity implements IOnSelectedReasonItemView, IPostRefundPicView, IPostRefundView {
    @BindView(R.id.store)
    TextView store;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.ll_reason)
    LinearLayout llReason;
    @BindView(R.id.reason)
    TextView tvReason;
    @BindView(R.id.tv_amount)
    EditText etAmount;
    @BindView(R.id.noScrollgridview)
    GridView gv;
    @BindView(R.id.post)
    TextView tvPost;
    @BindView(R.id.et_reason)
    EditText etReason;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    private List<String> postedPicList = new ArrayList<String>();
    private SelectRefundReasonIdDialog selectRefundReasonDialog;
    private RefundFormBean refundFormBean;
    private List<RefundReasonItemBean> reasonList = new ArrayList<RefundReasonItemBean>();
    private String reasonId="";
    private Double maxAmount;
    private Double refundMoney=0.0;
    private ArrayList<String> adapterImgList = new ArrayList<String>();
    private RefundAllFormPhotoPickerAdapter mAdapter;
    private int thisPosition = -1;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private ArrayList<String> photos;
    private int uploadPosition;
    private boolean isCompress=true;

    @Override
    protected void setLayout() {
        setContentView(R.layout.refund_form_money);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(this),0,0);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refundFormBean = (RefundFormBean) getIntent().getSerializableExtra("data");
        if (refundFormBean != null) {
            store.setText(refundFormBean.getDatas().getOrder().getStore_name());
            GlideUtil.loadByImageView(this, iv, refundFormBean.getDatas().getGoods().getGoods_img_360());
            price.setText(StringUtil.getPriceSpannable12String(this, refundFormBean.getDatas().getGoods().getGoods_price(), R.style.big_money, R.style.big_money));
            num.setText(String.format("X%s", refundFormBean.getDatas().getGoods().getGoods_num()));
            if (refundFormBean.getDatas().getGoods().getGoods_spec() != null) {
                desc.setVisibility(View.VISIBLE);
                desc.setText(refundFormBean.getDatas().getGoods().getGoods_spec().toString());
            }else {
                desc.setVisibility(View.GONE);
            }
            tvName.setText(refundFormBean.getDatas().getGoods().getGoods_name());
            List<RefundFormBean.DatasBean.ReasonListBean> reasonList = refundFormBean.getDatas().getReason_list();
            for (int i = 0; i < reasonList.size(); i++) {
                String id = reasonList.get(i).getReason_id();
                String reason = reasonList.get(i).getReason_info();
                RefundReasonItemBean itemBean;
                if (i == 3) {
                    itemBean = new RefundReasonItemBean(reason, id, true);
                    //默认退款填写
                    tvReason.setText(refundFormBean.getDatas().getReason_list().get(i).getReason_info());
                    reasonId=refundFormBean.getDatas().getReason_list().get(i).getReason_id();
                } else {
                    itemBean = new RefundReasonItemBean(reason, id, false);
                }
                this.reasonList.add(itemBean);
            }
            llReason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReasonDialog();
                }
            });
          //  maxAmount = Double.valueOf(refundFormBean.getDatas().getGoods().getGoods_pay_price());
            maxAmount = Double.valueOf(refundFormBean.getDatas().getOrder().getOrder_amount());
            refundMoney = maxAmount;
            etAmount.setHint("最多可退" + " ￥ " + maxAmount);
            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(s.toString())) {
                       double refundMoney = Double.valueOf(s.toString());
                        if (refundMoney > maxAmount) {
                            RefundFormMoneyActivity.this.refundMoney = maxAmount;
                            etAmount.setText(String.format("%s", maxAmount));
                            PopUtil.showComfirmDialog(RefundFormMoneyActivity.this, null, "退款金额不能超过商品金额", null, "知道了", null, null, true);
                        }else if(refundMoney <= maxAmount&&refundMoney!=RefundFormMoneyActivity.this.refundMoney){
                            RefundFormMoneyActivity.this.refundMoney = refundMoney;
                        }
                    } else {
                        PopUtil.showComfirmDialog(RefundFormMoneyActivity.this, null, "退款金额默认购买金额", null, "知道了", null, null, true);
                        etAmount.setText(String.format("%s", maxAmount));
                    }
                }
            });

            mAdapter = new RefundAllFormPhotoPickerAdapter(adapterImgList);
            gv.setAdapter(mAdapter);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        thisPosition = position;
                        onRequestLimitPhoto(position);
                    } else {
                        onClickRefundPictureContainerItem(position);
                    }
                }
            });
            tvPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /*     if (adapterImgList.size() != 0) {
                        for (int i = 0; i < adapterImgList.size(); i++) {
                            final File files = new File(adapterImgList.get(i));
                         *//*   //压缩的图片
                            Luban.with(RefundFormMoneyActivity.this)
                                    .load(files)
                                    .setCompressListener(new OnCompressListener() {
                                        @Override
                                        public void onStart() {
                                        }

                                        @Override
                                        public void onSuccess(File file) {
                                            ShopPresenter.postRefundPic(RefundFormMoneyActivity.this, file);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            // TODO 当压缩过去出现问题时调用
                                            ShopPresenter.postRefundPic(RefundFormMoneyActivity.this, files);
                                            Toast.makeText(RefundFormMoneyActivity.this, "图片压缩失败!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }).launch();*//*
                            ShopPresenter.postRefundPic(RefundFormMoneyActivity.this, files);
                        }
                    }else {
                        ShopPresenter.postRefund(RefundFormMoneyActivity.this, new ArrayList<String>());
                    }*/
                        if(TextUtils.isEmpty(etReason.getText().toString())){
                           PopUtil.showComfirmDialog(RefundFormMoneyActivity.this,"","请填写退款说明","","知道了",null,null,true);
                        }else if("".equals(getRefund_amount())||"0".equals(getRefund_amount())){
                            PopUtil.showComfirmDialog(RefundFormMoneyActivity.this,"","请填写退款金额","","知道了",null,null,true);
                        }else if("".equals(getReasonId())){
                            PopUtil.showComfirmDialog(RefundFormMoneyActivity.this,"","请选择退款原因","","知道了",null,null,true);
                        }
                        else {
                            ShopPresenter.postRefund(RefundFormMoneyActivity.this, postedPicList);
                        }
                }
            });
        }
    }
    private void showReasonDialog() {
        if (!isFinishing() && selectRefundReasonDialog == null) {
            selectRefundReasonDialog = new SelectRefundReasonIdDialog(this, this, reasonList);
            selectRefundReasonDialog.show();
        } else if (!isFinishing() && !selectRefundReasonDialog.isShowing()) {
            selectRefundReasonDialog.show();
        }
    }

    @Override
    public void onSelectedReasonItem(int position) {
        for (int i = 0; i < reasonList.size(); i++) {
            if (i == position) {
                reasonList.get(i).setIsSelected(true);
                String reason = reasonList.get(i).getReason();
                tvReason.setText(reason);
                reasonId = reasonList.get(i).getId();
            } else {
                reasonList.get(i).setIsSelected(false);
            }
        }
        selectRefundReasonDialog.notifyRcl();
    }

    private void onRequestLimitPhoto(final int position) {
      /*  if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
     */      /* AndPermission.with(this)
                    .requestCode(MY_PERMISSIONS_REQUEST)
                    .permission(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .send();*/
            PermissionUtils.requestPermissions(this, new PermissionUtils.PermissionRequestFinishListener() {
                @Override
                public void onPermissionRequestSuccess(List<String> permissions) {
                    onClickRefundPictureContainerItem(position);
                }
            }, Permission.WRITE_EXTERNAL_STORAGE);
       /* } else {
            onClickRefundPictureContainerItem(position);
        }*/
    }

    private void onClickRefundPictureContainerItem(int position) {
        if (position == adapterImgList.size()) {
            PhotoPicker.builder()
                    .setPhotoCount(3)
                    .setShowCamera(true)
                    .setSelected(adapterImgList)
                    .setShowGif(true)
                    .setPreviewEnabled(true)
                    .start(RefundFormMoneyActivity.this, PhotoPicker.REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("imgPaths", adapterImgList);
            bundle.putInt("position", position);
            Intent intent = new Intent(RefundFormMoneyActivity.this, EnlargePicActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, position);
        }
    }

  /*  private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == MY_PERMISSIONS_REQUEST) {
                onClickRefundPictureContainerItem(thisPosition);
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == MY_PERMISSIONS_REQUEST) {
                ToastUtil.ToastText(RefundFormMoneyActivity.this, "授权失败");
            }
        }
    };*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                /*adapterImgList.clear();
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                adapterImgList.addAll(photos);
                mAdapter.notifyDataSetChanged();*/
                ArrayList<String> photostmp = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (!AppUtil.equalList(photostmp, adapterImgList)) {
                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    /*    dataList.get(item).setImagePathList(photos);*/
                    adapterImgList.clear();
                    postedPicList.clear();
                    final File files = new File(photos.get(0));
                    uploadPosition = 0;
                    //压缩的图片
                    Luban.with(RefundFormMoneyActivity.this)
                            .load(files)
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    isCompress = true;
                                    ShopPresenter.postRefundPic(RefundFormMoneyActivity.this, file);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过去出现问题时调用
                                    isCompress = false;
                                    ShopPresenter.postRefundPic(RefundFormMoneyActivity.this, files);
                                    ;
                                }
                            }).launch();
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode >= 0 && requestCode <= 2) {
            adapterImgList.remove(requestCode);
            postedPicList.remove(requestCode);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPostRefundPicSuccess(String s) {
        /*PostRefundPicBean postRefundPicBean = JsonUtil.toBean(s, PostRefundPicBean.class);
        if (postRefundPicBean != null) {
            postedPicList.add(postRefundPicBean.getDatas().getFile_name());
        }
        if (postedPicList.size() == adapterImgList.size()) {
            ShopPresenter.postRefund(this, postedPicList);
        }*/
        PostRefundPicBean postRefundPicBean = JsonUtil.toBean(s, PostRefundPicBean.class);
        if (postRefundPicBean != null) {
            postedPicList.add(postRefundPicBean.getDatas().getFile_name());
            adapterImgList.add(photos.get(uploadPosition));
            mAdapter.notifyDataSetChanged();
        }
        if (uploadPosition < photos.size() - 1) {
            tvPost.setBackgroundColor(getResources().getColor(R.color.shop_grey));
            tvPost.setClickable(false);
            uploadPosition += 1;
            final File files = new File(photos.get(uploadPosition));
            // final String fileName = photos.get(uploadPosition);
            //压缩的图片
            Luban.with(RefundFormMoneyActivity.this)
                    .load(files)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            isCompress = true;
                            ShopPresenter.postRefundPic(RefundFormMoneyActivity.this, file);
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过去出现问题时调用
                            isCompress = false;
                            ShopPresenter.postRefundPic(RefundFormMoneyActivity.this, files);;
                        }
                    }).launch();
            // ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files, fileName);
        }else {
            tvPost.setBackgroundColor(getResources().getColor(R.color.msb_color));
            tvPost.setClickable(true);
        }
    }

    @Override
    public String getOrderId() {
        return refundFormBean.getDatas().getOrder().getOrder_id();
    }

    @Override
    public String getReasonId() {
        return reasonId;
    }

    @Override
    public String getBuyerMessage() {
        return etReason.getText().toString();
    }

    @Override
    public String getOrder_goods_id() {
        return refundFormBean.getDatas().getGoods().getOrder_goods_id();
    }

    @Override
    public String getRefund_amount() {
        return refundMoney+"";
    }

    @Override
    public String getGoods_num() {
        return "0";
    }

    @Override
    public String getRefund_type() {
        return "1";
    }

    @Override
    public void onPostRefundSuccess(String s) {
        PopUtil.showAutoDissHookDialog(this, "部分退款成功", 100, new OnDissmissLisenter() {
            @Override
            public void onDissmiss() {
                Intent intent =  new Intent(RefundFormMoneyActivity.this,ShopSuccessActivity.class);
                intent.putExtra("state","refund");
                intent.putExtra("id",refundFormBean.getDatas().getOrder().getOrder_id());
                startActivity(intent);
                finish();
            }
        });
    }
    //图片上传失败
    @Override
    public void onError(String s) {
      /*  super.onError(s);
        LogUtils.e("图片上传失败"+"uploadPosition==="+uploadPosition+"filename==="+photos.get(uploadPosition));
        if (uploadPosition < photos.size() - 1) {
            tvPost.setBackgroundColor(getResources().getColor(R.color.shop_grey));
            tvPost.setClickable(false);
            uploadPosition += 1;
            File files = new File(photos.get(uploadPosition));
            ShopPresenter.postRefundPic(RefundFormMoneyActivity.this, files);
        }else {
            tvPost.setBackgroundColor(getResources().getColor(R.color.msb_color));
            tvPost.setClickable(true);
        }*/
        if (!AppUtil.isNetworkAvailable()) {
            PopUtil.showComfirmDialog(this, "", getResources().getString(R.string.network_error), "", "", null, null, true);
            onNetError();
        } else if (TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey()) || "未登录".equals(s)) {
            PopUtil.toastInBottom("请登录商城");
            Intent goLogin = new Intent(this, LoginActivity.class);
            startActivity(goLogin);
        } else if (!isCompress) {
            //原图上传失败才显示
            PopUtil.toastInCenter(s);
        }
        if (uploadPosition < photos.size() - 1) {
            tvPost.setBackgroundColor(getResources().getColor(R.color.shop_grey));
            tvPost.setClickable(false);
            //压缩上传不成功，则用原图再次尝试上传，原图上传不成功，则上传下一张
            if (isCompress) {
                final File files = new File(photos.get(uploadPosition));
                isCompress =false;
                ShopPresenter.postRefundPic(RefundFormMoneyActivity.this, files);
            } else {
                uploadPosition += 1;
                final File files = new File(photos.get(uploadPosition));
                Luban.with(RefundFormMoneyActivity.this)
                        .load(files)
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                isCompress = true;
                                ShopPresenter.postRefundPic(RefundFormMoneyActivity.this, file);
                            }

                            @Override
                            public void onError(Throwable e) {
                                isCompress = false;
                                ShopPresenter.postRefundPic(RefundFormMoneyActivity.this, files);

                            }
                        }).launch();
            }
        } else if (isCompress && uploadPosition == photos.size() - 1) {
            final File files = new File(photos.get(uploadPosition));
            isCompress = false;
            ShopPresenter.postRefundPic(RefundFormMoneyActivity.this, files);
        } else if (!isCompress && uploadPosition == photos.size() - 1) {
            tvPost.setBackgroundColor(getResources().getColor(R.color.msb_color));
            tvPost.setClickable(true);
        }
    }
}