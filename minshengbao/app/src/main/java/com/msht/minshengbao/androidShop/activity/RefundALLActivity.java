package com.msht.minshengbao.androidShop.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.androidShop.adapter.RefundAllFormGoodListAdapter;
import com.msht.minshengbao.androidShop.adapter.RefundAllFormPhotoPickerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.customerview.SelectRefundReasonIdDialog;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.PostRefundPicBean;
import com.msht.minshengbao.androidShop.shopBean.RefundAllFormBean;
import com.msht.minshengbao.androidShop.shopBean.RefundReasonItemBean;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PermissionUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IOnSelectedReasonItemView;
import com.msht.minshengbao.androidShop.viewInterface.IPostRefundPicView;
import com.msht.minshengbao.androidShop.viewInterface.IPostRefundAllView;
import com.msht.minshengbao.androidShop.viewInterface.IRefundAllFormView;
import com.msht.minshengbao.androidShop.viewInterface.OnDissmissLisenter;
import com.msht.minshengbao.functionActivity.repairService.EnlargePicActivity;
import com.yanzhenjie.permission.Permission;


import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RefundALLActivity extends ShopBaseActivity implements IRefundAllFormView, IPostRefundPicView, IPostRefundAllView, IOnSelectedReasonItemView {

    private static final int MY_PERMISSIONS_REQUEST = 100;
    @BindView(R.id.back)
    ImageView iv;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.ll_reason)
    LinearLayout llReason;
    @BindView(R.id.tv_amount)
    TextView amount;
    @BindView(R.id.et_reason)
    EditText et;
    @BindView(R.id.noScrollgridview)
    GridView gv;
    @BindView(R.id.store)
    TextView store;
    @BindView(R.id.post)
    TextView tvPost;
    @BindView(R.id.reason)
    TextView tvReason;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String orderId;
    private ArrayList<String> adapterImgList = new ArrayList<>();
    private List<RefundAllFormBean.DatasBean.GoodsListBean> goodList = new ArrayList<RefundAllFormBean.DatasBean.GoodsListBean>();
    private RefundAllFormGoodListAdapter adapter;
    private RefundAllFormPhotoPickerAdapter mAdapter;
    private int thisPosition = -1;
    private List<String> postedPicList = new ArrayList<String>();
    private List<RefundReasonItemBean> reasonList = new ArrayList<RefundReasonItemBean>();
    private String reasonId;
    private SelectRefundReasonIdDialog selectRefundReasonDialog;
    private ArrayList<String> photos;
    private int uploadPosition;


    @Override
    protected void setLayout() {
        setContentView(R.layout.refund_all_form_activity);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).navigationBarColor(R.color.black).navigationBarWithKitkatEnable(false).init();
        ImmersionBar.setTitleBar(this, mToolbar);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        orderId = getIntent().getStringExtra("data");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        rcl.setNestedScrollingEnabled(false);
        rcl.setLayoutManager(linearLayoutManager);
        rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new RefundAllFormGoodListAdapter(this);
        adapter.setDatas(goodList);
        rcl.setAdapter(adapter);
        llReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReasonDialog();
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
        ShopPresenter.getRefundAllForm(this);
        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    if (adapterImgList.size() != 0) {
                    for (int i = 0; i < adapterImgList.size(); i++) {
                        File files = new File(adapterImgList.get(i));
                       *//* //压缩的图片
                        Luban.with(RefundALLActivity.this)
                                .load(files)
                                .setCompressListener(new OnCompressListener() {
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        ShopPresenter.postRefundPic(RefundALLActivity.this, file);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        // TODO 当压缩过去出现问题时调用
                                        ShopPresenter.postRefundPic(RefundALLActivity.this, files);
                                        Toast.makeText(RefundALLActivity.this, "图片压缩失败!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).launch();*//*
                        ShopPresenter.postRefundPic(RefundALLActivity.this, files);
                    }
                } else {
                    ShopPresenter.postRefundAll(RefundALLActivity.this, new ArrayList<String>());
                }*/
                if (TextUtils.isEmpty(et.getText().toString())) {
                    PopUtil.showComfirmDialog(RefundALLActivity.this, "", "请填写退款说明", "", "知道了", null, null, true);
                } else if ("".equals(getReasonId())) {
                    PopUtil.showComfirmDialog(RefundALLActivity.this, "", "请选择退款原因", "", "知道了", null, null, true);
                } else {
                    ShopPresenter.postRefundAll(RefundALLActivity.this, postedPicList);
                }
            }
        });
    }


    private void onRequestLimitPhoto(int position) {
           /*      AndPermission.with(this)
                    .requestCode(MY_PERMISSIONS_REQUEST)
                    .permission(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .send();*/
            PermissionUtils.requestPermissions(this, new PermissionUtils.PermissionRequestFinishListener() {
                @Override
                public void onPermissionRequestSuccess(List<String> permissions) {
                    onClickRefundPictureContainerItem(thisPosition);
                }
            }, Permission.WRITE_EXTERNAL_STORAGE);

    }

    private void showReasonDialog() {
        if (!isFinishing() && selectRefundReasonDialog == null) {
            selectRefundReasonDialog = new SelectRefundReasonIdDialog(this, this, reasonList);
            selectRefundReasonDialog.show();
        } else if (!isFinishing() && !selectRefundReasonDialog.isShowing()) {
            selectRefundReasonDialog.show();
        }
    }

    private void onClickRefundPictureContainerItem(int position) {
        if (position == adapterImgList.size()) {
            PhotoPicker.builder()
                    .setPhotoCount(3)
                    .setShowCamera(true)
                    .setSelected(adapterImgList)
                    .setShowGif(true)
                    .setPreviewEnabled(true)
                    .start(RefundALLActivity.this, PhotoPicker.REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("imgPaths", adapterImgList);
            bundle.putInt("position", position);
            Intent intent = new Intent(RefundALLActivity.this, EnlargePicActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, position);
        }
    }

    @Override
    public void onPostRefundSuccess(String s) {
        PopUtil.showAutoDissHookDialog(this, "提交全部退款", 100, new OnDissmissLisenter() {
            @Override
            public void onDissmiss() {
                Intent intent = new Intent(RefundALLActivity.this, ShopSuccessActivity.class);
                intent.putExtra("state", "refund");
                intent.putExtra("id", orderId);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public String getBuyerMessage() {
        return et.getText().toString();
    }

    @Override
    public String getReasonId() {
        return reasonId;
    }

    @Override
    public void onGetRefundAllFormSuccess(String s) {
        RefundAllFormBean bean = JsonUtil.toBean(s, RefundAllFormBean.class);
        RefundAllFormBean.DatasBean.OrderBean order = null;
        if (bean != null) {
            order = bean.getDatas().getOrder();
            store.setText(order.getStore_name());
            goodList.clear();
            reasonList.clear();
            goodList.addAll(bean.getDatas().getGoods_list());
            adapter.notifyDataSetChanged();
            amount.setText(order.getOrder_amount());
            List<RefundAllFormBean.DatasBean.ReasonListBean> reasonList = bean.getDatas().getReason_list();
            for (int i = 0; i < reasonList.size(); i++) {
                String id = reasonList.get(i).getReason_id();
                String reason = reasonList.get(i).getReason_info();
                RefundReasonItemBean itemBean;
                if (i == 3) {
                    itemBean = new RefundReasonItemBean(reason, id, true);
                    //默认退款填写
                    tvReason.setText(reasonList.get(i).getReason_info());
                    reasonId = reasonList.get(i).getReason_id();
                } else {
                    itemBean = new RefundReasonItemBean(reason, id, false);
                }
                this.reasonList.add(itemBean);
            }
        }
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == MY_PERMISSIONS_REQUEST) {
                onClickRefundPictureContainerItem(thisPosition);
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == MY_PERMISSIONS_REQUEST) {
                ToastUtil.ToastText(RefundALLActivity.this, "授权失败");
            }
        }
    };*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
               /* adapterImgList.clear();
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
                    Luban.with(RefundALLActivity.this)
                            .load(files)
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    ShopPresenter.postRefundPic(RefundALLActivity.this, file);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过去出现问题时调用
                                    ShopPresenter.postRefundPic(RefundALLActivity.this, files);
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
        PostRefundPicBean postRefundPicBean = JsonUtil.toBean(s, PostRefundPicBean.class);
        if (postRefundPicBean != null) {
            postedPicList.add(postRefundPicBean.getDatas().getFile_name());
            adapterImgList.add(photos.get(uploadPosition));
            mAdapter.notifyDataSetChanged();
        }
            /*if (postedPicList.size() == adapterImgList.size()) {
                ShopPresenter.postRefundAll(this, postedPicList);
            }*/
        if (uploadPosition < photos.size() - 1) {
            tvPost.setBackgroundColor(getResources().getColor(R.color.shop_grey));
            tvPost.setClickable(false);
            uploadPosition += 1;
            final File files = new File(photos.get(uploadPosition));
            // final String fileName = photos.get(uploadPosition);
            //压缩的图片
            Luban.with(RefundALLActivity.this)
                    .load(files)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            ShopPresenter.postRefundPic(RefundALLActivity.this, file);
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过去出现问题时调用
                            ShopPresenter.postRefundPic(RefundALLActivity.this, files);
                            ;
                        }
                    }).launch();
            // ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files, fileName);
        } else {
            tvPost.setBackgroundColor(getResources().getColor(R.color.msb_color));
            tvPost.setClickable(true);
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

    //图片上传失败
    @Override
    public void onError(String s) {
        super.onError(s);
        LogUtils.e("图片上传失败" + "uploadPosition===" + uploadPosition + "filename===" + photos.get(uploadPosition));
        if (uploadPosition < photos.size() - 1) {
            tvPost.setBackgroundColor(getResources().getColor(R.color.shop_grey));
            tvPost.setClickable(false);
            uploadPosition += 1;
            File files = new File(photos.get(uploadPosition));
            ShopPresenter.postRefundPic(RefundALLActivity.this, files);
        } else {
            tvPost.setBackgroundColor(getResources().getColor(R.color.msb_color));
            tvPost.setClickable(true);
        }
    }
}
