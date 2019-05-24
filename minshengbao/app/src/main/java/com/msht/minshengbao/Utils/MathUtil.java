package com.msht.minshengbao.Utils;

import java.math.BigDecimal;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/5/10  
 */
public class MathUtil {

    public static double twoDecimal2(double amount) {   //保留两位小数
        BigDecimal bg=new BigDecimal(amount);
        return bg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 保留1小数点位数
     * @param amount 原值
     * @return
     */
    public static float oneDecimal(float amount) {
        BigDecimal bg=new BigDecimal(amount);
        return bg.setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();
    }
    /**
     * 保留小数点位数
     * @param amount 原值
     * @param digital 保留位数
     * @return
     */
    public static double getDoubleDecimal(double amount,int digital) {
        BigDecimal bg=new BigDecimal(amount);
        return bg.setScale(digital,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * 保留小数点位数
     * @param amount 原值
     * @param digital 保留位数
     * @return
     */
    public static float getFloatDecimal(double amount,int digital) {
        BigDecimal bg=new BigDecimal(amount);
        return bg.setScale(digital,BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
