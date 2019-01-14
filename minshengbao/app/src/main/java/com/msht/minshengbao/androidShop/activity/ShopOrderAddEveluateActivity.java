package com.msht.minshengbao.androidShop.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.androidShop.adapter.AddEvaluateShopOrderAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.AddEvaluateShopOrderInitBean;
import com.msht.minshengbao.androidShop.shopBean.MyAddEvaluateShopOrderBean;
import com.msht.minshengbao.androidShop.shopBean.UploadEvaluatePicBean;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.viewInterface.IPostAddEvelateAllView;
import com.msht.minshengbao.androidShop.viewInterface.IShopInitAddEveluateView;
import com.msht.minshengbao.androidShop.viewInterface.IUploadEveluatePicView;
import com.msht.minshengbao.functionActivity.repairService.EnlargePicActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopOrderAddEveluateActivity extends ShopBaseActivity implements IUploadEveluatePicView, IPostAddEvelateAllView, IShopInitAddEveluateView {
    private String orderId;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.push_evaluate)
    TextView tvPushEvaluate;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    private int item;
    private int childPosition;
    private ArrayList<MyAddEvaluateShopOrderBean> dataList = new ArrayList<MyAddEvaluateShopOrderBean>();
    private AddEvaluateShopOrderAdapter adapter;
    private int totalImageNum = 0;
    private List<UploadEvaluatePicBean> postedPicList = new ArrayList<UploadEvaluatePicBean>();
    private ArrayList<String> photos;
    private int uploadPosition;

    @Override
    protected void setLayout() {
        setContentView(R.layout.add_eveluate_activity);
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
        orderId = getIntent().getStringExtra("id");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        rcl.setNestedScrollingEnabled(false);
        rcl.setLayoutManager(linearLayoutManager);
        rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new AddEvaluateShopOrderAdapter(this, new AddEvaluateShopOrderAdapter.AddEvaluateItemListener() {
            @Override
            public void onClickImageContainer(int item, int childPosition) {
                ShopOrderAddEveluateActivity.this.item = item;
                ShopOrderAddEveluateActivity.this.childPosition = childPosition;
                if (Build.VERSION.SDK_INT >= 23) {
                    onRequestLimitPhoto(childPosition, item);
                } else {
                    LogUtils.e("onClickImageContainer item=" + item);
                    onClickEveluatePictureContainerItem(childPosition, item);
                }
            }

            @Override
            public void onTextChange(String s, int position) {
                dataList.get(position).setComment(s);
                dataList.get(position).setTextNum(s.length());
            }
        });
        adapter.setDatas(dataList);
        rcl.setAdapter(adapter);
        tvPushEvaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* totalImageNum = 0;
                for (MyAddEvaluateShopOrderBean bean : dataList) {
                    totalImageNum += bean.getImagePathList().size();
                }
                if (totalImageNum > 0) {
                    for (int ii = 0; ii < dataList.size(); ii++) {
                        ArrayList<String> postImgList = dataList.get(ii).getImagePathList();
                        if (postImgList.size() != 0) {
                            for (int i = 0; i < postImgList.size(); i++) {
                                final File files = new File(postImgList.get(i));
                                final String fileName = postImgList.get(i);
                                LogUtils.e("ii===" + ii + "i==" + i + "postImgList.get(i)===" + postImgList.get(i));
                     *//*       //压缩的图片
                            Luban.with(ShopOrderAddEveluateActivity.this)
                                    .load(files)
                                    .setCompressListener(new OnCompressListener() {
                                        @Override
                                        public void onStart() {
                                        }

                                        @Override
                                        public void onSuccess(File file) {
                                            ShopPresenter.uploadEvaluatePic(ShopOrderAddEveluateActivity.this, file,fileName);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            // TODO 当压缩过去出现问题时调用
                                            ShopPresenter.uploadEvaluatePic(ShopOrderAddEveluateActivity.this, files,fileName);
                                            Toast.makeText(ShopOrderAddEveluateActivity.this, "图片压缩失败!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }).launch();
                            ShopPresenter.uploadEvaluatePic(ShopOrderAddEveluateActivity.this, files,fileName);
                        }*//*
                                ShopPresenter.uploadEvaluatePic(ShopOrderAddEveluateActivity.this, files, fileName);
                            }
                        }
                    }
                    //没有图片的时候直接上传，不上传图片
                } else {
                    ShopPresenter.postAddEvelateAll(ShopOrderAddEveluateActivity.this, dataList);
                }*/
                ShopPresenter.postAddEvelateAll(ShopOrderAddEveluateActivity.this, dataList);
            }
        });
        ShopPresenter.getInitAddEveluate(this, orderId);
    }

    private static final int MY_PERMISSIONS_REQUEST = 100;

    private void onRequestLimitPhoto(int position, int item) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            AndPermission.with(this)
                    .requestCode(MY_PERMISSIONS_REQUEST)
                    .permission(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .send();
        } else {
            onClickEveluatePictureContainerItem(position, item);
        }
    }

    private void onClickEveluatePictureContainerItem(int childPosition, int item) {
        if (childPosition == dataList.get(item).getImagePathList().size()) {
            PhotoPicker.builder()
                    .setPhotoCount(5)
                    .setShowCamera(true)
                    .setSelected(dataList.get(item).getImagePathList())
                    .setShowGif(true)
                    .setPreviewEnabled(true)
                    .start(ShopOrderAddEveluateActivity.this, PhotoPicker.REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("imgPaths", dataList.get(item).getImagePathList());
            bundle.putInt("position", childPosition);
            Intent intent = new Intent(ShopOrderAddEveluateActivity.this, EnlargePicActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, childPosition);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == MY_PERMISSIONS_REQUEST) {
                onClickEveluatePictureContainerItem(childPosition, item);
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == MY_PERMISSIONS_REQUEST) {
                ToastUtil.ToastText(ShopOrderAddEveluateActivity.this, "授权失败");
            }
        }
    };

    @Override
    public void onPostEvaluatePicSuccess(String s) {
     /*   synchronized (this) {
            LogUtils.e("onPostEvaluatePicSuccess");
            UploadEvaluatePicBean uploadEvaluatePicBean = JsonUtil.toBean(s, UploadEvaluatePicBean.class);
            if (uploadEvaluatePicBean != null) {
                postedPicList.add(uploadEvaluatePicBean);
                for (int i = 0; i < dataList.size(); i++) {
                    MyAddEvaluateShopOrderBean bean = dataList.get(i);
                    for (String path : bean.getImagePathList()) {
                        if (path.lastIndexOf("/") != -1) {
                            path = path.substring(path.lastIndexOf("/") + 1);
                        } else if (path.lastIndexOf("\\") != -1) {
                            path = path.substring(path.lastIndexOf("\\") + 1);
                        }
                        LogUtils.e("path=" + path + "getOrigin_file_name()==" + uploadEvaluatePicBean.getDatas().getOrigin_file_name());
                        if (path.equals(uploadEvaluatePicBean.getDatas().getOrigin_file_name())) {
                            bean.getToUploadimagePathList().add(uploadEvaluatePicBean.getDatas().getFile_name());
                            dataList.set(i, bean);
                            LogUtils.e("ToUploadimagePathList()==" + bean.getToUploadimagePathList().toString());
                        }
                    }
                }
            }
            if (postedPicList.size() == totalImageNum) {
                ShopPresenter.postAddEvelateAll(this, dataList);
            }
        }*/
        UploadEvaluatePicBean uploadEvaluatePicBean = JsonUtil.toBean(s, UploadEvaluatePicBean.class);
        if (uploadEvaluatePicBean != null) {
            postedPicList.add(uploadEvaluatePicBean);
            dataList.get(item).getToUploadimagePathList().add(uploadEvaluatePicBean.getDatas().getFile_name());
            dataList.get(item).getImagePathList().add(photos.get(uploadPosition));
            adapter.notifyDataSetChanged();
        }
        if (uploadPosition < photos.size() - 1) {
            tvPushEvaluate.setBackgroundColor(getResources().getColor(R.color.shop_grey));
            tvPushEvaluate.setClickable(false);
            uploadPosition += 1;
            final File files = new File(photos.get(uploadPosition));
            //final String fileName = photos.get(uploadPosition);
            //压缩的图片
            Luban.with(ShopOrderAddEveluateActivity.this)
                    .load(files)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            ShopPresenter.uploadEvaluatePic(ShopOrderAddEveluateActivity.this, file,file.getName());
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过去出现问题时调用
                            ShopPresenter.uploadEvaluatePic(ShopOrderAddEveluateActivity.this, files,files.getName());;
                        }
                    }).launch();
            // ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files, fileName);
        }else {
            tvPushEvaluate.setBackgroundColor(getResources().getColor(R.color.msb_color));
            tvPushEvaluate.setClickable(true);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
           /*     ArrayList<String> imageList = dataList.get(item).getImagePathList();
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                imageList.clear();
                imageList.addAll(photos);
                dataList.get(item).setImagePathList(imageList);
                LogUtils.e(dataList.toString());
                adapter.notifyDataSetChanged();
                LogUtils.e("onActivityResult item==" + item);*/
                ArrayList<String> photostmp = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                ArrayList<String> imagelisttmp = dataList.get(item).getImagePathList();
                if (!AppUtil.equalList(photostmp, imagelisttmp)) {
                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    /*    dataList.get(item).setImagePathList(photos);*/
                    dataList.get(item).getImagePathList().clear();
                    dataList.get(item).getToUploadimagePathList().clear();
                    final File files = new File(photos.get(0));
                    //final String fileName = photos.get(0);
                    uploadPosition = 0;
                    //压缩的图片
                    Luban.with(ShopOrderAddEveluateActivity.this)
                            .load(files)
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    ShopPresenter.uploadEvaluatePic(ShopOrderAddEveluateActivity.this, file, file.getName());
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过去出现问题时调用
                                    ShopPresenter.uploadEvaluatePic(ShopOrderAddEveluateActivity.this, files, files.getName());
                                    ;
                                }
                            }).launch();
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode >= 0 && requestCode <= 4) {
            //不要随便赋予局部变量，更改的不是数据源的值
            /*ArrayList<String> imageList = dataList.get(item).getImagePathList();
            imageList.remove(requestCode);
            dataList.get(item).setImagePathList(imageList);*/
            dataList.get(item).getImagePathList().remove(requestCode);
            dataList.get(item).getToUploadimagePathList().remove(requestCode);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void onPostAddEvelateAllSuccess(String s) {
        Intent intent = new Intent(ShopOrderAddEveluateActivity.this, ShopSuccessActivity.class);
        intent.putExtra("id", orderId);
        intent.putExtra("state", "addeveluate");
        startActivity(intent);
        finish();
    }


    @Override
    public void onGetInitAddEveluateSuccess(String s) {
        AddEvaluateShopOrderInitBean bean = JsonUtil.toBean(s, AddEvaluateShopOrderInitBean.class);
        List<AddEvaluateShopOrderInitBean.DatasBean.EvaluateGoodsBean> goods = bean.getDatas().getEvaluate_goods();
        for (AddEvaluateShopOrderInitBean.DatasBean.EvaluateGoodsBean goodBean : goods) {
            String imaUrl = goodBean.getGeval_goodsimage();
            String name = goodBean.getGeval_goodsname();
            String goodId = goodBean.getGeval_id();
            String geval_content = goodBean.getGeval_content();
            ArrayList<String> imagePathList = new ArrayList<String>();
            ArrayList<String> toUploadimagePathList = new ArrayList<String>();
            dataList.add(new MyAddEvaluateShopOrderBean(imaUrl, name, goodId, imagePathList, toUploadimagePathList, "", 0, geval_content));
        }
        adapter.notifyDataSetChanged();
    }
//图片上传失败
    @Override
    public void onError(String s) {
        super.onError(s);
        LogUtils.e("图片上传失败"+"uploadPosition==="+uploadPosition+"filename==="+photos.get(uploadPosition));
        if (uploadPosition < photos.size() - 1) {
            tvPushEvaluate.setBackgroundColor(getResources().getColor(R.color.shop_grey));
            tvPushEvaluate.setClickable(false);
            uploadPosition += 1;
            File files = new File(photos.get(uploadPosition));
            String fileName = photos.get(uploadPosition);
            ShopPresenter.uploadEvaluatePic(ShopOrderAddEveluateActivity.this, files, fileName);
        }else {
            tvPushEvaluate.setBackgroundColor(getResources().getColor(R.color.msb_color));
            tvPushEvaluate.setClickable(true);
        }
    }
}
