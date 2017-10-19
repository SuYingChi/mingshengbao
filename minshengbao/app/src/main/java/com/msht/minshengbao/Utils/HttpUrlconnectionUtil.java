package com.msht.minshengbao.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.msht.minshengbao.Callback.ResultImgListenner;
import com.msht.minshengbao.Callback.ResultListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hong on 2016/12/28.
 */
public class HttpUrlconnectionUtil  {
    private static final String ERROR_NETWORK = "服务器连接失败，请稍后再试";
    private static final String ERROR_SERVICE = "服务器异常，请稍后再试";
    private static final String ERROR_OVER_TIME = "网络连接超时，请检查您的网络";
    public static void executeGet(final String  url,final ResultListener resultListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataInputStream dis = null;
                try {
                    URL mUrl = new URL(url);
                    conn=(HttpURLConnection)mUrl.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    conn.setRequestProperty("accept","*/*");
                    conn.setRequestProperty("connection","Keep-Alive");
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        String resultStr = NetUtil.readString(is);
                        resultListener.onResultSuccess(resultStr);
                    }else {
                        resultListener.onResultFail(ERROR_SERVICE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    resultListener.onResultFail(ERROR_NETWORK);
                }finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
    public static void executeGetTwo(final String  url,final ResultListener resultListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataInputStream dis = null;
                try {
                    URL mUrl = new URL(url);
                    conn=(HttpURLConnection)mUrl.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    conn.setRequestProperty("accept","*/*");
                    conn.setRequestProperty("connection","Keep-Alive");
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        String resultStr = NetUtil.readString(is);
                        resultListener.onResultSuccess(resultStr);
                    }else {
                        resultListener.onResultFail(ERROR_SERVICE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    resultListener.onResultFail(ERROR_NETWORK);
                }finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
    public static void multipleexecutepost(String url, HashMap<String, String> parameters, ResultListener resultListener){

    }
    public static void executepost(final String url,final Map<String, String> parameters, final ResultListener resultListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataInputStream dis = null;
                try {
                    URL mUrl = new URL(url);
                    conn = (HttpURLConnection) mUrl.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setUseCaches(false);//新加
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true); // 发送POST请求必须设置允许输入
                    conn.setDoOutput(true); // 发送POST请求必须设置允许输出
                    conn.setRequestProperty("Charset", "UTF-8");//设置编码
                    conn.setRequestProperty("ser-Agent", "Fiddler");
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
                    OutputStream os = conn.getOutputStream();
                    DataOutputStream ds = new DataOutputStream(os);
                    NetUtil.writeStringParams(parameters, ds);
                    NetUtil.paramsEnd(ds);
                    os.flush();
                    os.close();
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        String resultStr = NetUtil.readString(is);
                        resultListener.onResultSuccess(resultStr);
                    }else {
                        resultListener.onResultFail(ERROR_SERVICE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    resultListener.onResultFail(ERROR_NETWORK);
                }finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
    public static void executepostTwo(final String url,final Map<String, String> parameters, final ResultListener resultListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataInputStream dis = null;
                try {
                    URL mUrl = new URL(url);
                    conn = (HttpURLConnection) mUrl.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setUseCaches(false);//新加
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true); // 发送POST请求必须设置允许输入
                    conn.setDoOutput(true); // 发送POST请求必须设置允许输出
                    conn.setRequestProperty("Charset", "UTF-8");//设置编码
                    conn.setRequestProperty("ser-Agent", "Fiddler");
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
                    OutputStream os = conn.getOutputStream();
                    DataOutputStream ds = new DataOutputStream(os);
                    NetUtil.writeStringParams(parameters, ds);
                    NetUtil.paramsEnd(ds);
                    os.flush();
                    os.close();
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        String resultStr = NetUtil.readString(is);
                        resultListener.onResultSuccess(resultStr);
                    }else {
                        resultListener.onResultFail(ERROR_SERVICE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    resultListener.onResultFail(ERROR_NETWORK);
                }finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
    public static void postFile(String url, HashMap<String, File> parameters, ResultListener resultListener) {

    }
    public static void multipleFileParameters(final String url, final Map<String, String> parameters,final Map<String, File> fileparame, final ResultListener resultListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataInputStream dis = null;
                try {
                    URL mUrl = new URL(url);
                    conn = (HttpURLConnection) mUrl.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setUseCaches(false);//新加
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true); // 发送POST请求必须设置允许输入
                    conn.setDoOutput(true); // 发送POST请求必须设置允许输出
                    conn.setRequestProperty("Charset", "UTF-8");//设置编码
                    conn.setRequestProperty("ser-Agent", "Fiddler");
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
                    OutputStream os = conn.getOutputStream();
                    DataOutputStream ds = new DataOutputStream(os);
                    NetUtil.writeStringParams(parameters, ds);
                    NetUtil.writeFileParams(fileparame, ds);
                    NetUtil.paramsEnd(ds);
                    os.flush();
                    os.close();
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        String resultStr = NetUtil.readString(is);
                        resultListener.onResultSuccess(resultStr);
                    }else {
                        resultListener.onResultFail(ERROR_SERVICE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    resultListener.onResultFail(ERROR_NETWORK);
                }finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
    /*获取网络图片 */
    public static void BitmapGet(final String  url, final ResultImgListenner resultListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap img = null;
                HttpURLConnection conn = null;
                try {
                    URL picurl = new URL(url);
                    conn = (HttpURLConnection)picurl.openConnection();
                    conn.setConnectTimeout(6000);//设置超时
                    conn.setDoInput(true);
                    conn.setUseCaches(false);//不缓存
                    conn.connect();
                    InputStream is = conn.getInputStream();//获得图片的数据流
                    img = BitmapFactory.decodeStream(is);
                    resultListener.Success(img);
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    resultListener.Failure(ERROR_NETWORK);
                }finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
}
