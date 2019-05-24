package com.msht.minshengbao.OkhttpUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;

/**
 *
 * @author fighting
 * @date 2017/4/7
 */

public abstract class AbstractCallBackUtil<T> {
    public static Handler mMainHandler = new Handler(Looper.getMainLooper());


    public  void onProgress(float progress, long total ){};

    public  void onError(final Call call, final Exception e){
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(call,e);
            }
        });
    };
    public  void onSeccess(Call call, Response response){
        final T obj = onParseResponse(call, response);
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                onResponse(obj);
            }
        });
    };


    /**
     * 解析response，执行在子线程
     */
    public abstract T onParseResponse(Call call, Response response);

    /**
     * 访问网络失败后被调用，执行在UI线程
     */
    public abstract void onFailure(Call call, Exception e);

    /**
     *
     * 访问网络成功后被调用，执行在UI线程
     */
    public abstract void onResponse(T response);


    public static abstract class CallBackDefault extends AbstractCallBackUtil<Response> {
        @Override
        public Response onParseResponse(Call call, Response response) {
            return response;
        }
    }

    public static abstract class CallBackString extends AbstractCallBackUtil<String> {
        @Override
        public String onParseResponse(Call call, Response response) {
            try {
                if (response.body()!=null){
                    return response.body().string();
                }
            } catch (IOException e) {
                new RuntimeException("failure");
                return "";
            }
            return "";
        }
    }

    public static abstract class CallBackBitmap extends AbstractCallBackUtil<Bitmap> {
        private int mTargetWidth;
        private int mTargetHeight;

        public CallBackBitmap(){};
        public CallBackBitmap(int targetWidth,int targetHeight){
            mTargetWidth = targetWidth;
            mTargetHeight = targetHeight;
        };
        public CallBackBitmap(ImageView imageView){
            int width = imageView.getWidth();
            int height = imageView.getHeight();
            if(width <=0 || height <=0){
                throw new RuntimeException("无法获取ImageView的width或height");
            }
            mTargetWidth = width;
            mTargetHeight = height;
        };
        @Override
        public Bitmap onParseResponse(Call call, Response response) {
            if(mTargetWidth ==0 || mTargetHeight == 0){
                if (response.body()!=null){
                    return BitmapFactory.decodeStream(response.body().byteStream());
                }
            }else {
                return getZoomBitmap(response);
            }
            return null;
        }

        /**
         * 压缩图片，避免OOM异常
         */
        private Bitmap getZoomBitmap(Response response) {
            byte[] data = null;
            try {
                if (response.body()!=null){
                    data = response.body().bytes();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap=null;
            if (data!=null){
                BitmapFactory.decodeByteArray(data,0,data.length,options);
                int picWidth = options.outWidth;
                int picHeight = options.outHeight;
                int sampleSize = 1;
                int heightRatio = (int) Math.floor((float) picWidth / (float) mTargetWidth);
                int widthRatio = (int) Math.floor((float) picHeight / (float) mTargetHeight);
                if (heightRatio > 1 || widthRatio > 1){
                    sampleSize = Math.max(heightRatio,widthRatio);
                }
                options.inSampleSize = sampleSize;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeByteArray(data,0,data.length,options);

            }
            if(bitmap == null){
                throw new RuntimeException("Failed to decode stream.");
            }
            return bitmap;
        }
    }

    /**
     * 下载文件时的回调类
     */
    public static abstract class CallBackFile extends AbstractCallBackUtil<File> {

        private final String mDestFileDir;
        private final String mdestFileName;

        /**
         *
         * @param destFileDir:文件目录
         * @param destFileName：文件名
         */
        public CallBackFile(String destFileDir, String destFileName){
            mDestFileDir = destFileDir;
            mdestFileName = destFileName;
        }
        @Override
        public File onParseResponse(Call call, Response response) {

            InputStream is = null;
            byte[] buf = new byte[1024*8];
            int len = 0;
            FileOutputStream fos = null;
            try{
                if (response.body()!=null){
                    is = response.body().byteStream();
                    final long total = response.body().contentLength();
                    long sum = 0;
                    File dir = new File(mDestFileDir);
                    if (!dir.exists()){
                        dir.mkdirs();
                    }
                    File file = new File(dir, mdestFileName);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1){
                        sum += len;
                        fos.write(buf, 0, len);
                        final long finalSum = sum;
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                onProgress(finalSum * 100.0f / total,total);
                            }
                        });
                    }
                    fos.flush();
                    return file;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                try{
                    if (response.body()!=null){
                        response.body().close();
                    }
                    if (is != null){
                        is.close();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
                try{
                    if (fos != null){
                        fos.close();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

            }
            return null;
        }
    }

}
