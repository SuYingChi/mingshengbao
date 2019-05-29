package com.msht.minshengbao.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hong on 2017/1/11.
 */
public class VariableUtil {

    public static int firstCome=0;
    public static int mPos;
    public static int cityPos;
    public static int payPos;
    public static int MealPos=-1;
    public static int estatePos=-1;
    public static int messageNum=0;
    public static int deleteFlag=0;
    public static String balance;
    public static String City="海口";
    public static String cityId="1";
    public static boolean boolSelect;
    public static boolean BoolCode;
    public static boolean networkStatus=true;
    public static boolean isCurrent=false;
    public static String waterAccount="";
    public static String mDateString="";
    public static String wallet;
    public static String totalCouponNum;
    public static final  String NULL_VALUE="null";
    public static final  String VALUE_ZERO= "0";
    public static final  String VALUE_ONE=  "1";
    public static final  String VALUE_TWO=  "2";
    public static final  String VALUE_THREE="3";
    public static final  String VALUE_FOUR= "4";
    public static final  String VALUE_FIVE= "5";
    public static final  String VALUE_SIX=  "6";
    public static final  String VALUE_SEVER="7";
    public static final  String VALUE_EIGHT="8";
    public static final  String VALUE_ELEVEN="11";
    public static final  String VALUE_TWELVE="12";
    public static final  String VALUE_SEVENTEEN="17";
    public static final  String VALUE_ZERO1="0.0";
    public static final  String VALUE_ZERO2="0.00";
    public static ArrayList<HashMap<String,  String>> detailList = new ArrayList<HashMap<String,  String>>();

    public static String SECURITY_ENCRYPT_KEY="www.mssb.com2017!@#";
    public static String SECURITY_SIGN_KEY="www.msht.com2017v1!@#";


    public static double twoDecinmal2(double amount) {   //保留两位小数
        BigDecimal bg=new BigDecimal(amount);
        double double1=bg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return double1;
    }
}
