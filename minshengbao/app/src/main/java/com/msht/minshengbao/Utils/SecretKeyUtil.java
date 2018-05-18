package com.msht.minshengbao.Utils;

import android.text.TextUtils;
import android.util.Log;

import com.msht.minshengbao.Bean.WaterAppBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by hong on 2017/12/7.
 */

public class SecretKeyUtil {
    public static String getStringSign(WaterAppBean bean){
        String type=bean.getTypes();
        String equipment=bean.getEquipmentNo(); //设备
        String account=bean.getAccount();      //zhanghu
        String payFee=bean.getPayFee();
        String giveFee=bean.getGiveFee();

        String orderNo=bean.getOrderNo();
        String payAccount=bean.getPayAccounte();
        String payType=bean.getPayType();
        String payTime=bean.getPayTime();
        String amount=bean.getAmount();
        String resultMsg=bean.getResultMsg();
        String resultCode=bean.getResultCode();
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        if (type!=null){
            treeMap.put("type",type);
        }
        if (equipment!=null){
            treeMap.put("equipmentNo", equipment);
        }
        if (account!=null){
            treeMap.put("account", account);
        }
        if (payFee!=null){
            treeMap.put("payFee", payFee);
        }
        if (giveFee!=null){
            treeMap.put("giveFee", giveFee);
        }
        if (orderNo!=null){
            treeMap.put("orderNo", orderNo);
        }
        if (payAccount!=null){
            treeMap.put("payAccount", payAccount);
        }
        if (payType!=null){
            treeMap.put("payType", payType);
        }
        if (payTime!=null){
            treeMap.put("payTime", payTime);
        }
        if (amount!=null){
            treeMap.put("amount", amount);
        }
        if (resultMsg!=null){
            treeMap.put("resultMsg", resultMsg);
        }
        if (resultCode!=null){
            treeMap.put("resultCode", resultCode);
        }
        StringBuffer buffer = new StringBuffer();
        Iterator<String> it = treeMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = treeMap.get(key);
            if (!TextUtils.isEmpty(value)) {
                buffer.append(value);
            }
        }
        String sign = MD5.sign(buffer.toString(), VariableUtil.SECURITY_SIGN_KEY, "UTF-8");
        return sign;
    }
    public static String getextParams(WaterAppBean bean){
        String json = CreateJson(bean);
        String extParams="";
        try{
            extParams=DesUtil.encrypt(json,VariableUtil.SECURITY_ENCRYPT_KEY);
        }catch (Exception e){
            e.printStackTrace();
        }
        return extParams;
    }
    public static String CreateJson(WaterAppBean bean ) {
        String type=bean.getTypes();
        String equipment=bean.getEquipmentNo();
        String account=bean.getAccount();
        String payFee=bean.getPayFee();
        String giveFee=bean.getGiveFee();
        String orderNo=bean.getOrderNo();
        String payAccount=bean.getPayAccounte();
        String payType=bean.getPayType();
        String payTime=bean.getPayTime();
        String amount=bean.getAmount();
        String resultMsg=bean.getResultMsg();
        String resultCode=bean.getResultCode();
        String jsonresult="";
        JSONObject object=new JSONObject();
        try{
            if (type!=null){
                object.put("type",type);
            }
            if (equipment!=null){
                object.put("equipmentNo", equipment);
            }
            if (account!=null){
                object.put("account", account);
            }
            if (payFee!=null){
                object.put("payFee", payFee);
            }
            if (giveFee!=null){
                object.put("giveFee", giveFee);
            }
            if (orderNo!=null){
                object.put("orderNo", orderNo);
            }
            if (payAccount!=null){
                object.put("payAccount", payAccount);
            }
            if (payType!=null){
                object.put("payType", payType);
            }
            if (payTime!=null){
                object.put("payTime", payTime);
            }
            if (amount!=null){
                object.put("amount", amount);
            }
            if (resultMsg!=null){
                object.put("resultMsg", resultMsg);
            }
            if (resultCode!=null){
                object.put("resultCode", resultCode);
            }
            jsonresult=object.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonresult;
    }

    public static String getKeyextParams(String object){
        Log.d("ParamsData数据=",object);
        String extParams="";
        try{
            extParams=DesUtil.encrypt(object,VariableUtil.SECURITY_ENCRYPT_KEY);
        }catch (Exception e){
            e.printStackTrace();
        }
        return extParams;
    }
    private static String CreateKeyJson(JSONObject object) {
        String  jsonresult=object.toString();
        return jsonresult;
    }
    public static String getKeySign(TreeMap<String, String> treeMap){
        StringBuffer buffer = new StringBuffer();
        Iterator<String> it = treeMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = treeMap.get(key);
            if (!TextUtils.isEmpty(value)) {
                buffer.append(value);
            }
        }
        String sign = MD5.sign(buffer.toString(), VariableUtil.SECURITY_SIGN_KEY, "UTF-8");
        return sign;
    }
}
