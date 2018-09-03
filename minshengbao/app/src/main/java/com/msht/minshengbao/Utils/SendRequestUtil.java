package com.msht.minshengbao.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
 * Demo class
 *
 * @author hong
 * @date 2016/10/31
 */
public class SendrequestUtil {
    private static final String ERROR_NETWORK = "网络连接异常，请查看网络状态";
    private static final String ERROR_SERVICE = "服务器异常，请稍后再试";
    private static final String ERROR_OVER_TIME = "网络连接超时，请检查您的网络";
    public static final int SUCCESS=1;
    public static final int FAILURE=0;
    public static final String SUCCESS_VALUE="success";
    public static final String FAILURE_VALUE="fail";
    public static final String CANCEL_VALUE="cancel";
    public static void ShortTimeGet(final String  url, final Handler mhandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataInputStream dis = null;
                try {
                    URL mUrl = new URL(url);
                    conn=(HttpURLConnection)mUrl.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(4000);
                    conn.setReadTimeout(4000);
                    conn.setRequestProperty("accept","*/*");
                    conn.setRequestProperty("connection","Keep-Alive");
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        String resultStr = NetUtil.readString(is);
                        Message msg = new Message();
                        msg.obj = resultStr;
                        msg.what = SUCCESS;
                        mhandler.sendMessage(msg);
                    }else {
                        Message msg = new Message();
                        msg.what =FAILURE;
                        msg.obj=ERROR_SERVICE;
                        mhandler.sendMessage(msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj=ERROR_NETWORK;
                    mhandler.sendMessage(msg);
                }finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
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
    public static void executeGetThree(final String  url,final ResultListener resultListener) {
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
                    conn.setRequestProperty("User-Agent", "Fiddler");
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
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ NetUtil.BOUNDARY);
                    OutputStream os = conn.getOutputStream();
                    DataOutputStream ds = new DataOutputStream(os);
                    NetUtil.writeStringParams(parameters, ds);
                    NetUtil.paramsEnd(ds);
                    os.flush();
                    os.close();
                    conn.connect();
                    if (conn.getResponseCode() ==200) {
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
                    if (conn.getResponseCode()== 200) {
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
    /**
     * 获取网络图片
     * @param url  图片链接
     * @param mHandler
     */
    public static void  getBitmapFromService(final String  url, final Handler mHandler ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap img = null;
                HttpURLConnection conn = null;
                try {
                    URL picurl = new URL(url);
                    conn = (HttpURLConnection)picurl.openConnection();
                    conn.setConnectTimeout(6000);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    img = BitmapFactory.decodeStream(is);
                    Message message = new Message();
                    message.what = SUCCESS;
                    message.obj =img;
                    mHandler.sendMessage(message);
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj=ERROR_SERVICE;
                    mHandler.sendMessage(msg);
                }finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
    /**
     * 通过Get方式获取数据
     */
    public static void getDataFromService(final String validateURL, final Handler mHandler) {
        new Thread() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {
                    URL url = new URL(validateURL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestProperty("accept", "*/*");
                    connection.setRequestProperty("connection", "Keep-Alive");
                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        String resultStr = NetUtil.readString(is);
                        Message message = new Message();
                        message.what = SUCCESS;
                        message.obj = resultStr;
                        mHandler.sendMessage(message);
                    } else {
                        Message msg = new Message();
                        msg.what = FAILURE;
                        msg.obj=ERROR_SERVICE;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e)//内部捕获异常并做处理
                {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj=ERROR_NETWORK;
                    mHandler.sendMessage(msg);
                } finally {
                    //最后，释放连接
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }.start();
    }
    /**
     * 通过Get方式获取数据
     */
    public static void getDataFromServiceTwo(final String validateURL, final Handler mHandler) {
        new Thread() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {
                    URL url = new URL(validateURL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestProperty("accept", "*/*");
                    connection.setRequestProperty("connection", "Keep-Alive");
                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        String resultStr = NetUtil.readString(is);
                        Message message = new Message();
                        message.what = SUCCESS;
                        message.obj = resultStr;
                        mHandler.sendMessage(message);
                    } else {
                        Message msg = new Message();
                        msg.what = FAILURE;
                        msg.obj=ERROR_SERVICE;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e)//内部捕获异常并做处理
                {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj=ERROR_NETWORK;
                    mHandler.sendMessage(msg);
                } finally {
                    //最后，释放连接
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }.start();
    }
    /**
     * 通过Get方式获取数据  带参数
     */
    public static void getDataFromService(final String validateURL, final String param, final Handler mHandler) {
        new Thread() {
            @Override
            public void run() {
                String fullurl = validateURL + param;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(fullurl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestProperty("accept", "*/*");
                    connection.setRequestProperty("connection", "Keep-Alive");
                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        String resultStr = NetUtil.readString(is);
                        Message message = new Message();
                        message.what = SUCCESS;
                        message.obj = resultStr;
                        mHandler.sendMessage(message);
                    } else {
                        Message msg = new Message();
                        msg.what = FAILURE;
                        msg.obj=ERROR_SERVICE;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e)//内部捕获异常并做处理
                {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj=ERROR_NETWORK;
                    mHandler.sendMessage(msg);
                } finally {
                    //最后，释放连接
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }.start();
    }

    /**
     * 通过Post方式获取数据
     */
    public static void postDataFromService(final String validateURL, final Map<String, String> params, final Handler mhandler) {

        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataInputStream dis = null;
                Map<String, String> textParams = params;
                try {
                    URL url = new URL(validateURL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.setRequestProperty("User-Agent", "Fiddler");
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
                    OutputStream os = conn.getOutputStream();
                    DataOutputStream ds = new DataOutputStream(os);
                    NetUtil.writeStringParams(textParams, ds);
                    NetUtil.paramsEnd(ds);
                    os.flush();
                    os.close();
                    conn.connect();
                    if (conn.getResponseCode()== 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamTools.readInputStream(is);
                        Message msg = new Message();
                        msg.obj = result;
                        msg.what = SUCCESS;
                        mhandler.sendMessage(msg);

                    } else {
                        Message msg = new Message();
                        msg.what =FAILURE;
                        msg.obj=ERROR_SERVICE;
                        mhandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj=ERROR_NETWORK;
                    mhandler.sendMessage(msg);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }.start();
    }
    /**
     * 通过Post方式获取数据
     */
    public static void postDataFromServiceTwo(final String validateURL, final Map<String, String> params, final Handler mhandler) {

        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataInputStream dis = null;
                Map<String, String> textParams = params;
                try {
                    URL url = new URL(validateURL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    // 发送POST请求必须设置允许输入
                    conn.setDoInput(true);
                    // 发送POST请求必须设置允许输出
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.setRequestProperty("User-Agent", "Fiddler");
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
                    OutputStream os = conn.getOutputStream();
                    DataOutputStream ds = new DataOutputStream(os);
                    NetUtil.writeStringParams(textParams, ds);
                    NetUtil.paramsEnd(ds);
                    os.flush();
                    os.close();
                    conn.connect();
                    // 从Internet获取网页,发送请求,将网页以流的形式读回来
                    if (conn.getResponseCode()== 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamTools.readInputStream(is);
                        Message msg = new Message();
                        msg.obj = result;
                        msg.what = SUCCESS;
                        mhandler.sendMessage(msg);

                    } else {
                        Message msg = new Message();
                        msg.what =FAILURE;
                        msg.obj=ERROR_SERVICE;
                        mhandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj=ERROR_NETWORK;
                    mhandler.sendMessage(msg);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }.start();
    }
    /**
     * 通过Post方式获取数据
     */
    public static void postDataFromServiceThree(final String validateURL, final Map<String, String> params, final Handler mhandler) {

        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataInputStream dis = null;
                Map<String, String> textParams = params;
                try {
                    URL url = new URL(validateURL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.setRequestProperty("User-Agent", "Fiddler");
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
                    OutputStream os = conn.getOutputStream();
                    DataOutputStream ds = new DataOutputStream(os);
                    NetUtil.writeStringParams(textParams, ds);
                    NetUtil.paramsEnd(ds);
                    os.flush();
                    os.close();
                    conn.connect();
                    // 从Internet获取网页,发送请求,将网页以流的形式读回来
                    if (conn.getResponseCode()== 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamTools.readInputStream(is);
                        Message msg = new Message();
                        msg.obj = result;
                        msg.what = SUCCESS;
                        mhandler.sendMessage(msg);

                    } else {
                        Message msg = new Message();
                        msg.what =FAILURE;
                        msg.obj=ERROR_SERVICE;
                        mhandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj=ERROR_NETWORK;
                    mhandler.sendMessage(msg);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }.start();
    }
    public static void postFileToServer(final Map<String, String> textparams, final Map<String, File> fileparams, final String validateURL, final Handler mhandler) {
        new Thread(){
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataInputStream dis = null;
                try {
                    URL url = new URL(validateURL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.setRequestProperty("ser-Agent", "Fiddler");
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
                    OutputStream os = conn.getOutputStream();
                    DataOutputStream ds = new DataOutputStream(os);
                    NetUtil.writeStringParams(textparams, ds);
                    NetUtil.writeFileParams(fileparams, ds);
                    NetUtil.paramsEnd(ds);
                    os.flush();
                    os.close();
                    conn.connect();
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamTools.readInputStream(is);
                        Message msg = new Message();
                        msg.obj = result;
                        msg.what = SUCCESS;
                        mhandler.sendMessage(msg);

                    } else {
                        Message msg = new Message();
                        msg.what =FAILURE;
                        msg.obj=ERROR_SERVICE;
                        mhandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = FAILURE;
                    msg.obj=ERROR_NETWORK;
                    mhandler.sendMessage(msg);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }.start();
    }
}
