package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.adapter.EvaluateShopOrderAdapter;
import com.msht.minshengbao.androidShop.adapter.EvaluateStartsAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadAndFootRecyclerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.EvaluateShopOrderBean;
import com.msht.minshengbao.androidShop.shopBean.MyEvaluateShopOrderBean;
import com.msht.minshengbao.androidShop.shopBean.UploadEvaluatePicBean;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PermissionUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.IPostEvelateAllView;
import com.msht.minshengbao.androidShop.viewInterface.IShopInitEveluateView;
import com.msht.minshengbao.androidShop.viewInterface.IUploadEveluatePicView;
import com.msht.minshengbao.functionActivity.myActivity.LoginActivity;
import com.msht.minshengbao.functionActivity.repairService.EnlargePicActivity;
import com.yanzhenjie.permission.Permission;


import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopOrderEveluateActivity extends ShopBaseActivity implements IShopInitEveluateView, IUploadEveluatePicView, IPostEvelateAllView {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.wuliu_start_rcl)
    RecyclerView wuliuRcl;
    @BindView(R.id.fuwu_start_rcl)
    RecyclerView fuwuRcl;
    @BindView(R.id.push_evaluate)
    TextView tvPushEvaluate;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    private EvaluateShopOrderAdapter adapter;
    private ArrayList<MyEvaluateShopOrderBean> dataList = new ArrayList<MyEvaluateShopOrderBean>();
    private String orderId;
    private int item;
    private int childPosition;
    private EvaluateStartsAdapter wuliuStarAdapter;
    private EvaluateStartsAdapter fuwuStarAdapter;
    private List<UploadEvaluatePicBean> postedPicList = new ArrayList<UploadEvaluatePicBean>();
    private int totalImageNum=0;
    private ArrayList<String> photos;
    private int uploadPosition;
    private boolean isCompress=true;

    @Override
    protected void setLayout() {
        setContentView(R.layout.orders_evaluate_order);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(this),0,0);
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
        adapter = new EvaluateShopOrderAdapter(this, new EvaluateShopOrderAdapter.EvaluateItemListener() {

            @Override
            public void onClickStar(int itemPosition, int childPosition) {
                if (childPosition + 1 != dataList.get(itemPosition).getStarts()) {
                    dataList.get(itemPosition).setStarts(childPosition + 1);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onClickImageContainer(int item, int childPosition) {
                ShopOrderEveluateActivity.this.item = item;
                ShopOrderEveluateActivity.this.childPosition = childPosition;
                if (Build.VERSION.SDK_INT >= 23) {
                    onRequestLimitPhoto(childPosition, item);
                } else {
                    LogUtils.e("onClickImageContainer item="+item);
                    onClickEveluatePictureContainerItem(childPosition, item);
                }
            }


            @Override
            public void isNiming(int itemPosition, boolean isChecked) {
                if (dataList.get(itemPosition).isNiming() != isChecked) {
                    dataList.get(itemPosition).setNiming(isChecked);
                }
            }

            @Override
            public void onTextchanged(String s, int positon) {
                dataList.get(positon).setEvaluateText(s);
                dataList.get(positon).setTextNum(s.length());
            }
        });
        adapter.setDatas(dataList);
        rcl.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        wuliuRcl.setNestedScrollingEnabled(false);
        wuliuRcl.setLayoutManager(linearLayoutManager2);
        wuliuStarAdapter = new EvaluateStartsAdapter(this, 5);
        wuliuStarAdapter.setOnItemClickListener(new MyHaveHeadAndFootRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                wuliuStarAdapter.setStarts(position + 1);
                wuliuStarAdapter.notifyDataSetChanged();
            }
        });
        wuliuRcl.setAdapter(wuliuStarAdapter);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        fuwuRcl.setNestedScrollingEnabled(false);
        fuwuRcl.setLayoutManager(linearLayoutManager3);
        fuwuStarAdapter = new EvaluateStartsAdapter(this, 5);
        fuwuStarAdapter.setOnItemClickListener(new MyHaveHeadAndFootRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                fuwuStarAdapter.setStarts(position + 1);
                fuwuStarAdapter.notifyDataSetChanged();
            }
        });
        fuwuRcl.setAdapter(fuwuStarAdapter);
        tvPushEvaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         /*       totalImageNum = 0;
                for (MyEvaluateShopOrderBean bean : dataList) {
                    totalImageNum += bean.getImagePathList().size();
                }
                if(totalImageNum>0) {
                   for (int ii = 0; ii < dataList.size(); ii++) {
                        ArrayList<String> postImgList = dataList.get(ii).getImagePathList();
                        if (postImgList.size() != 0) {
                            for (int i = 0; i < postImgList.size(); i++) {
                                 File files = new File(postImgList.get(i));
                                 String fileName = postImgList.get(i);
                                LogUtils.e("ii===" + ii + "i==" + i + "postImgList.get(i)===" + postImgList.get(i));
                        *//*    //压缩的图片
                            Luban.with(ShopOrderEveluateActivity.this)
                                    .load(files)
                                    .setCompressListener(new OnCompressListener() {
                                        @Override
                                        public void onStart() {
                                        }

                                        @Override
                                        public void onSuccess(File file) {
                                            ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, file,fileName);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            // TODO 当压缩过去出现问题时调用
                                            ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files,fileName);
                                            Toast.makeText(ShopOrderEveluateActivity.this, "图片压缩失败!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }).launch();*//*
                                ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files, fileName);
                            }
                        }
                    }
                    //没有图片的时候直接上传，不上传图片
                }  else {
                        ShopPresenter.postEvelateAll(ShopOrderEveluateActivity.this,dataList);
                    }*/
                ShopPresenter.postEvelateAll(ShopOrderEveluateActivity.this,dataList);
            }
        });
        ShopPresenter.getInitEveluate(this);
    }

/*
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
                ToastUtil.ToastText(ShopOrderEveluateActivity.this, "授权失败");
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }*/

    private static final int MY_PERMISSIONS_REQUEST = 100;

    private void onRequestLimitPhoto(final int position, final int item) {
    /*    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
    */       /* AndPermission.with(this)
                    .requestCode(MY_PERMISSIONS_REQUEST)
                    .permission(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .send();*/
            PermissionUtils.requestPermissions(this, new PermissionUtils.PermissionRequestFinishListener() {
                @Override
                public void onPermissionRequestSuccess(List<String> permissions) {
                    onClickEveluatePictureContainerItem(position, item);
                }
            }, Permission.WRITE_EXTERNAL_STORAGE);
       /* } else {
            onClickEveluatePictureContainerItem(position, item);
        }*/
    }

    private void onClickEveluatePictureContainerItem(int childPosition, int item) {
        if (childPosition == dataList.get(item).getImagePathList().size()) {
            PhotoPicker.builder()
                    .setPhotoCount(5)
                    .setShowCamera(true)
                    .setSelected(dataList.get(item).getImagePathList())
                    .setShowGif(true)
                    .setPreviewEnabled(true)
                    .start(ShopOrderEveluateActivity.this, PhotoPicker.REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("imgPaths", dataList.get(item).getImagePathList());
            bundle.putInt("position", childPosition);
            Intent intent = new Intent(ShopOrderEveluateActivity.this, EnlargePicActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, childPosition);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photostmp = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                ArrayList<String> imagelisttmp = dataList.get(item).getImagePathList();
                if (!AppUtil.equalList(photostmp,imagelisttmp)) {
                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    /*    dataList.get(item).setImagePathList(photos);*/
                    dataList.get(item).getImagePathList().clear();
                    dataList.get(item).getToUploadimagePathList().clear();
                    final File files = new File(photos.get(0));
                   // final String fileName = photos.get(0);
                    //final String fileName = files.getName();
                    uploadPosition = 0;
                    //压缩的图片
                    Luban.with(ShopOrderEveluateActivity.this)
                            .load(files)
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    isCompress = true;
                                    ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, file, file.getName());
                                }

                                @Override
                                public void onError(Throwable e) {
                                    isCompress = false;
                                    ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files,  files.getName());
                                    ;
                                }
                            }).launch();
                    // ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files, fileName);
                    // adapter.notifyDataSetChanged();
                   /* ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files, fileName);*/
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode >= 0 && requestCode <= 4) {
            dataList.get(item).getImagePathList().remove(requestCode);
            dataList.get(item).getToUploadimagePathList().remove(requestCode);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public String getOrderid() {
        return orderId;
    }

    @Override
    public void onGetInitEveluateSuccess(String s) {
        EvaluateShopOrderBean bean = JsonUtil.toBean(s, EvaluateShopOrderBean.class);
        List<EvaluateShopOrderBean.DatasBean.OrderGoodsBean> goods = bean.getDatas().getOrder_goods();
        for (EvaluateShopOrderBean.DatasBean.OrderGoodsBean goodBean : goods) {
            String imaUrl = goodBean.getGoods_image_url();
            String name = goodBean.getGoods_name();
            String goodId = goodBean.getRec_id();
            ArrayList<String> imagePathList = new ArrayList<String>();
            ArrayList<String> toUploadimagePathList = new ArrayList<String>();
            //初始5颗星评价，没有图片添加
            dataList.add(new MyEvaluateShopOrderBean(imaUrl, name, goodId, 5, imagePathList, toUploadimagePathList, false, "", 0));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPostEvaluatePicSuccess(String s) {
       //先添加刷新显示，再一次性全部异步上传，万一哪张图片无法上传，用户会疑惑觉得为啥我上传了但不显示。并且后续不好得知是哪张图片不能上传，就算得知也不好提示用户，用户还是会选择那张图片
     /*   synchronized (this) {
            LogUtils.e("onPostEvaluatePicSuccess");
            UploadEvaluatePicBean uploadEvaluatePicBean = JsonUtil.toBean(s, UploadEvaluatePicBean.class);
            if (uploadEvaluatePicBean != null) {
                postedPicList.add(uploadEvaluatePicBean);
                for (int i=0;i<dataList.size();i++) {
                    MyEvaluateShopOrderBean bean = dataList.get(i);
                    for (String path : bean.getImagePathList()) {
                       if(path.lastIndexOf("/")!=-1){
                           path = path.substring(path.lastIndexOf("/")+1);
                       }else if(path.lastIndexOf("\\")!=-1){
                           path = path.substring(path.lastIndexOf("\\")+1);
                       }
                        LogUtils.e("path="+path+"getOrigin_file_name()=="+uploadEvaluatePicBean.getDatas().getOrigin_file_name());
                       //判断是订单里哪个商品上传的图片
                        if (path.equals(uploadEvaluatePicBean.getDatas().getOrigin_file_name())) {
                            bean.getToUploadimagePathList().add(uploadEvaluatePicBean.getDatas().getFile_name());
                            dataList.set(i,bean);
                            LogUtils.e("ToUploadimagePathList()=="+bean.getToUploadimagePathList().toString());
                        }
                    }
                }
            }
            if (postedPicList.size() == totalImageNum) {
                ShopPresenter.postEvelateAll(this, dataList);
            }
        }*/
        //添加图片的时候就上传，上传成功后再刷新显示，这样方便用户操作是哪张图片不能上传
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
           // final String fileName = photos.get(uploadPosition);
            //压缩的图片
            Luban.with(ShopOrderEveluateActivity.this)
                    .load(files)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            isCompress = true;
                            ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, file,file.getName());
                        }

                        @Override
                        public void onError(Throwable e) {
                            isCompress = false;
                            ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files,files.getName());;
                        }
                    }).launch();
           // ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files, fileName);
        }else {
            tvPushEvaluate.setBackgroundColor(getResources().getColor(R.color.msb_color));
            tvPushEvaluate.setClickable(true);
        }
    }


    //图片上传失败
    @Override
    public void onError(String s) {
        if (!AppUtil.isNetworkAvailable()) {
            PopUtil.showComfirmDialog(this,"",getResources().getString(R.string.network_error),"","",null,null,true);
        } else if (TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())||"未登录".equals(s)) {
            PopUtil.toastInBottom("请登录");
        } else if(!isCompress){
            //原图上传失败才显示
            PopUtil.toastInCenter(s);
        }
        if (uploadPosition < photos.size() - 1) {
            tvPushEvaluate.setBackgroundColor(getResources().getColor(R.color.shop_grey));
            tvPushEvaluate.setClickable(false);
            //压缩上传不成功，则用原图再次尝试上传，原图上传不成功，则上传下一张
            if(isCompress) {
                final File files = new File(photos.get(uploadPosition));
                isCompress =false;
                ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files, files.getName());
            }else {
                uploadPosition += 1;
                final File files = new File(photos.get(uploadPosition));
                Luban.with(ShopOrderEveluateActivity.this)
                        .load(files)
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                isCompress = true;
                                ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, file, file.getName());
                            }

                            @Override
                            public void onError(Throwable e) {
                                isCompress = false;
                                ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files, files.getName());

                            }
                        }).launch();
            }
        }else if(isCompress&&uploadPosition == photos.size() - 1){
            final File files = new File(photos.get(uploadPosition));
            isCompress = false;
            ShopPresenter.uploadEvaluatePic(ShopOrderEveluateActivity.this, files, files.getName());
        }else if(!isCompress&&uploadPosition == photos.size() - 1){
            tvPushEvaluate.setBackgroundColor(getResources().getColor(R.color.msb_color));
            tvPushEvaluate.setClickable(true);
        }
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void onPostEvelateAllSuccess(String s) {
                Intent intent = new Intent(ShopOrderEveluateActivity.this,ShopSuccessActivity.class);
                intent.putExtra("id",orderId);
                intent.putExtra("state","eveluate");
                startActivity(intent);
                finish();
            }

}
