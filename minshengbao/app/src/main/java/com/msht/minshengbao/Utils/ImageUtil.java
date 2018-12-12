package com.msht.minshengbao.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;


public class ImageUtil {

    private static final String TAG ="ImageUtil";
    private static final String PHOTO_PATH =Environment.getExternalStorageDirectory().getAbsolutePath() + "/Livelihool/MsbCameraCache";


    /**
     * go for Album.
     */
    public static final Intent choosePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return Intent.createChooser(intent, null);
    }

    /**
     * go for camera.
     */
    public static final Intent takeBigPicture(Context context,String ImagrfileName) {
        File photoDir=new File(getDirPath());
        if (!photoDir.exists()){
            photoDir.mkdirs();// 创建照片的存储目录
        }
        File photoFile=new File(photoDir,ImagrfileName);
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Uri imageUri = FileProvider.getUriForFile(context, "com.msht.minshengbao.fileProvider", photoFile);//
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        }else {
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
            intent.putExtra(MediaStore.EXTRA_OUTPUT, newPictureUri(getNewPhotoPath()));
        }
        return intent;
    }

    public static final String getDirPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Livelihool/MsbCameraCache";
    }

    public static final String getExistPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Livelihool/MsbCameraCache/";
    }
    private static final String getNewPhotoPath() {
        return getDirPath() + "/" + System.currentTimeMillis() + ".jpg";
    }

    public static final String retrievePath(Context context, Intent sourceIntent, Intent dataIntent) {
        String picPath = null;
        try {
            Uri uri;
            if (dataIntent != null) {
                uri = dataIntent.getData();
                if (uri != null) {
                    picPath = ContentUtil.getPath(context, uri);
                }
                if (isFileExists(picPath)) {
                    return picPath;
                }

                Log.w(TAG, String.format("retrievePath failed from dataIntent:%s, extras:%s", dataIntent, dataIntent.getExtras()));
            }

            if (sourceIntent != null) {
                uri = sourceIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                if (uri != null) {
                    String scheme = uri.getScheme();
                    if (scheme != null && scheme.startsWith("file")) {
                        picPath = uri.getPath();
                    }
                }
                if (!TextUtils.isEmpty(picPath)) {
                    File file = new File(picPath);
                    if (!file.exists() || !file.isFile()) {
                        Log.w(TAG, String.format("retrievePath file not found from sourceIntent path:%s", picPath));
                    }
                }
            }
            return picPath;
        } finally {
            Log.d(TAG, "retrievePath(" + sourceIntent + "," + dataIntent + ") ret: " + picPath);
        }
    }

    private static final Uri newPictureUri(String path) {
        return Uri.fromFile(new File(path));
    }

    private static final boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File f = new File(path);
        if (!f.exists()) {
            return false;
        }
        return true;
    }



    public static LayerDrawable createLayerDrawable(@NonNull Drawable upper, @NonNull Drawable lower) {
        Drawable[] layers = new Drawable[2];
        layers[0] = lower;
        layers[1] = upper;
        return new LayerDrawable(layers);
    }

    public static LayerDrawable drawColorOnLayer(LayerDrawable transform, @ColorInt int color) {
        Drawable background = transform.getDrawable(0);
        background.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return transform;
    }

    public static Bitmap drawableToBitmap(@NonNull Drawable drawable, int width, int height, @NonNull Bitmap.Config config) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, config);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), config);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
