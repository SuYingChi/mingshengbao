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
    public static int citypos;
    public static int paypos;
    public static int MealPos=-1;
    public static String balance;
    public static String City;
    public static boolean Boolselect;
    public static boolean BoolCode;
    public static ArrayList<String> options1item=new ArrayList<String>();
    public static ArrayList<ArrayList<String>> options2item=new ArrayList<ArrayList<String>>();
    public static ArrayList<ArrayList<ArrayList<String>>> options3item=new ArrayList<>();
    public static ArrayList<HashMap<String,  String>> detailList = new ArrayList<HashMap<String,  String>>();

    public static String SECURITY_ENCRYPT_KEY="www.mssb.com2017!@#";
    public static String SECURITY_SIGN_KEY="www.msht.com2017v1!@#";


    public static double TwoDecinmal2(double amount) {   //保留两位小数
        BigDecimal bg=new BigDecimal(amount);
        double double1=bg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return double1;
    }
}
