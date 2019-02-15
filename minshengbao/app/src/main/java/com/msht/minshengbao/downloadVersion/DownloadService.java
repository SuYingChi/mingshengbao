package com.msht.minshengbao.downloadVersion;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/4/2
 */
public class DownloadService extends Service {
    public static final String DOWNLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath()+
                    "/Msbdownloads/";
    public static final String TAG = "download";
    /**下载链接*/
    private String url;
    /**文件长度*/
    private int length;
    /**文件名 */
    private String fileName=null;
    private NotificationCompat.Builder builder;
    private RemoteViews contentView;
    private NotificationManager notificationManager;
    private static final int SC_OK=200;
    private static final int MSG_INIT = 0;
    private static final int URL_ERROR = 1;
    private static final int NET_ERROR = 2;
    private static final int DOWNLOAD_SUCCESS = 3;
    private Context mContext;
    private final RequestHandler mHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<DownloadService> mWeakReference;
        public RequestHandler(DownloadService downloadService) {
            mWeakReference = new WeakReference<DownloadService>(downloadService);
        }
        @Override
        public void handleMessage(Message msg) {
            final DownloadService reference =mWeakReference.get();
            if (reference == null) {
                return;
            }
            switch (msg.what) {
                case MSG_INIT:
                    reference.length = (int) msg.obj;
                    reference.onDownThread();
                    reference.createNotification();
                    break;
                case DOWNLOAD_SUCCESS:
                    //下载完成
                    reference.notifyNotification(100, 100,"安装包下载完成");
                    installApk(reference.mContext,new File(DOWNLOAD_PATH,reference.fileName));
                    Toast.makeText(reference.mContext, "下载完成", Toast.LENGTH_SHORT).show();
                    break;
                case URL_ERROR:
                    Toast.makeText(reference.mContext, "下载地址错误",Toast.LENGTH_SHORT).show();
                    break;
                case NET_ERROR:
                    Toast.makeText(reference.mContext, "连接失败，请检查网络设置",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onDownThread() {
        new DownloadThread(url,length).start();
    }
    public DownloadService() {}
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            mContext=this;
            url = intent.getStringExtra("url");
            if(url != null && !TextUtils.isEmpty(url)){
                new InitThread(url).start();
            }else{
                mHandler.sendEmptyMessage(URL_ERROR);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    private class InitThread extends Thread{
        String url = "";
        public InitThread(String url) {
            this.url = url;
        }
        @Override
        public void run() {
            HttpURLConnection conn= null;
            RandomAccessFile raf = null;
            try {
                //连接网络文件
                URL url = new URL(this.url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(6000);
                conn.setRequestMethod("GET");
                int length = -1;
                if(conn.getResponseCode() ==SC_OK){
                    //获得文件长度
                    length = conn.getContentLength();
                }
                if(length <= 0){
                    return;
                }
                File dir = new File(DOWNLOAD_PATH);
                if(!dir.exists()){
                    dir.mkdir();
                }
                fileName = this.url.substring(this.url.lastIndexOf("/")+1, this.url.length());
                if(fileName==null && TextUtils.isEmpty(fileName) && !fileName.contains(".apk")){
                    fileName = getPackageName()+".apk";
                }
                File file = new File(dir, fileName);
                raf = new RandomAccessFile(file, "rwd");
                //设置文件长度
                raf.setLength(length);
                mHandler.obtainMessage(MSG_INIT,length).sendToTarget();
            } catch (Exception e) {
                mHandler.sendEmptyMessage(URL_ERROR);
                e.printStackTrace();
            } finally{
                try {
                    conn.disconnect();
                    raf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class DownloadThread extends Thread{
        String url;
        int length;
        public DownloadThread(String url, int length) {
            this.url = url;
            this.length = length;
        }
        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream input = null;
            try {
                URL url = new URL(this.url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(6000);
                conn.setRequestMethod("GET");
                //设置下载位置
                int start =0;
                // conn.setRequestProperty("Range", "bytes="+0+"-"+length);
                //设置文件写入位置
                File file = new File(DownloadService.DOWNLOAD_PATH,fileName);
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                long mFinished = 0;
                //开始下载
                //这里判断  SC_OK=200,实为206
                if(conn.getResponseCode() ==SC_OK||conn.getResponseCode()==206){
                    input = conn.getInputStream();
                    byte[] buffer = new byte[1024*4];
                    int len = -1;
                    long speed = 0;
                    long time = System.currentTimeMillis();
                    while((len = input.read(buffer)) != -1){
                        //写入文件
                        raf.write(buffer,0,len);
                        //把下载进度发送广播给Activity
                        mFinished += len;
                        speed += len;
                        if(System.currentTimeMillis() - time > 1000){
                            time = System.currentTimeMillis();
                            notifyNotification(mFinished,length,"安装包正在下载...");
                            Log.i(TAG, "mFinished=="+mFinished);
                            Log.i(TAG, "length=="+length);
                            Log.i(TAG, "speed=="+speed);
                            speed = 0;
                        }
                    }
                    mHandler.sendEmptyMessage(DOWNLOAD_SUCCESS);
                }else{   //conn.getResponseCode()!=200,
                    mHandler.sendEmptyMessage(NET_ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                try {
                    if(conn != null){
                        conn.disconnect();
                    }
                    if(raf != null){
                        raf.close();
                    }
                    if(input != null ){
                        input.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @SuppressWarnings("deprecation")
    public void createNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager!=null){
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                NotificationChannel mChannel=new NotificationChannel("channel_download","应用下载",NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(mChannel);
            }
        }
        builder = new NotificationCompat.Builder(this,"channel_download");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("下载");
        builder.setContentText("安装包正在下载...");
        /*** 自定义  Notification 的显示****/
        contentView = new RemoteViews(getPackageName(), R.layout.item_notification);
        contentView.setProgressBar(R.id.progress, 100, 0, false);
        contentView.setTextViewText(R.id.tv_progress, "0%");
        contentView.setTextViewText(R.id.id_tv_download,"安装包正在下载...");
        builder.setContent(contentView);
        if (notificationManager!=null){
            notificationManager.notify(R.layout.item_notification, builder.build());
        }
    }
    private void notifyNotification(long percent,long length,String mNotice){
        contentView.setTextViewText(R.id.tv_progress, (percent*100/length)+"%");
        contentView.setProgressBar(R.id.progress, (int)length,(int)percent, false);
        contentView.setTextViewText(R.id.id_tv_download,mNotice);
        builder.setContent(contentView);
        notificationManager.notify(R.layout.item_notification, builder.build());
    }
    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件
     */
    public static void installApk(Context context, File file) {
        //清除原有数据
        SharedPreferencesUtil.clearPreference(context,"open_app");
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            Uri apkUri= FileProvider.getUriForFile(context,"com.msht.minshengbao.fileProvider",file);
            Intent install=new Intent();
            install.setAction(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri,"application/vnd.android.package-archive");
            context.startActivity(install);
        }else {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }
}