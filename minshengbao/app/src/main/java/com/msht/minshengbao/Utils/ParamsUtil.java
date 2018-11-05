package com.msht.minshengbao.Utils;

import android.util.Log;

import com.msht.minshengbao.Model.YiPayModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/10/23
 */
public class ParamsUtil {
    /**
     * 下单参数，订单金额传分
     *
     * @return
     */
    public static String buildOrderParams(YiPayModel model, String merchantKey, String riskControlInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("MERCHANTID").append("=").append(model.getMERCHANTID()).append("&")
                .append("ORDERAMT").append("=").append(yuan2cent(model.getORDERAMOUNT())).append("&")
                .append("ORDERSEQ").append("=").append(model.getORDERSEQ()).append("&")
                .append("ORDERREQTRANSEQ").append("=").append(model.getORDERREQTRANSEQ()).append("&")
                .append("ORDERREQTIME").append("=").append(model.getORDERTIME()).append("&")
                .append("TRANSCODE").append("=").append("01").append("&")
                .append("DIVDETAILS").append("=").append("").append("&")
                .append("ORDERREQTIME").append("=").append("").append("&")
                .append("SERVICECODE").append("=").append("05").append("&")
                .append("PRODUCTDESC").append("=").append("").append("&")
                .append("ENCODETYPE").append("=").append("1").append("&")
                .append("RISKCONTROLINFO").append("=").append(riskControlInfo).append("&")
                .append("MAC").append("=").append(getMac(model, merchantKey,riskControlInfo));
        return sb.toString();
    }

    /**
     * 将单位为元的金额转化成分
     *
     * @param yuan
     * @return
     */
    private static String yuan2cent(String yuan) {
        BigDecimal bigDecimalYuan = new BigDecimal(yuan);
        return bigDecimalYuan.multiply(new BigDecimal(100)).toString();
    }

    /**
     * 获取下单加密串防止参数被篡改
     *
     * @param model
     * @param merchantKey
     * @return
     */
    private static String getMac(YiPayModel model, String merchantKey,String riskControlInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("MERCHANTID").append("=").append(model.getMERCHANTID()).append("&")
                .append("ORDERSEQ").append("=").append(model.getORDERSEQ()).append("&")
                .append("ORDERREQTRANSEQ").append("=").append(model.getORDERREQTRANSEQ()).append("&")
                .append("ORDERREQTIME").append("=").append(model.getORDERTIME()).append("&")
                .append("RISKCONTROLINFO").append("=").append(riskControlInfo).append("&")
                .append("KEY").append("=").append(merchantKey);
        String mac = "";
        try {
            mac = MD5.md5Digests(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac;
    }

    /**
     * 进入收银台的参数拼接
     *
     * @return
     */
    public static String buildPayParams(YiPayModel model) {
        StringBuilder paramsSb = new StringBuilder();
        Field[] fields = model.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            String attribute = name.substring(0, 1).toUpperCase() + name.substring(1);
            Method m;
            String value = "";
            try {
                m = model.getClass().getMethod("get" + attribute);
                value = (String) m.invoke(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
            paramsSb.append(name).append("=").append(value).append("&");
        }
        return paramsSb.toString().substring(0, paramsSb.length() - 1);
    }
}
