package com.msht.minshengbao.Utils;


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 * Des 对称加密解密 
 * @author lindaofen
 */
public class DesUtil {  

    /** 加密、解密key. */  
    private static final String PASSWORD_CRYPT_KEY = "wtoip2016!@#IWELOAKGTTIC32";  
    /** 加密算法,可用 DES,DESede,Blowfish. */  
    private final static String ALGORITHM = "DES";  
  /*  public static void main(String[] args) throws Exception {  
		 
	TreeMap<String, String> tree = new TreeMap<String,String>();
	tree.put("mechatId", "1");
	tree.put("operatorType", "0");
	tree.put("totalMoney", "601");
	tree.put("tradeMoney","601");
	tree.put("orderId","2016050311235996");
	tree.put("subOrderId", "subOrderId");
	tree.put("subject", "subject");
	tree.put("notifyUrl", "http://192.168.30.214:8902/api/payOrder/update");
	tree.put("synUrl", "http://192.168.30.214:8078/order/list");
	tree.put( "memberId", "117355" ); 
	logger.info(JSON.toJSONString(tree));
    	
        String md5Password = "235435,23435,中过,ksladfjlksd,1234rt5.222,323542,linguangliang,zhonguotian,哦文明ｓｄ卡了放假了撒看到飞洒的空间放上大方送达方式的罚款了撒旦分离卡水电费，xia,womende mingtian 11";  
		md5Password = "ext_params=eyJwYXlfZmF2Ijoibm8iLCJoYXNfb3JkZXJfZmFpbCI6MCwib3JkZXJzIjoiMzQwMDAwMDAwMDA1Njg0ODUiLCJvcmRlcl9zbiI6IjE2MDUzMTY3NzcwMjE5IiwicGF5X3R5cGUiOjAsInBheV90eXBlX2lkIjowLCJwYXlfaWQiOjAsImJhbmtfaWQiOiIiLCJvcGVyYXRlIjoid2ViIn0%3D&sign=00ca272c52a88975df012041ee37026d1E091FFF12429396FE2C143EC756EBAB37DE5A0598162E040F2AADD6165199489C350300717EE5D866817BC5FA7D6399A7444A25FDB7C9C88EC1A17A6921FA9E63E0F5B7E47125CAD924FEA5DD17585CC4041840FBC7046E1369ECAD8E0D46697953750DB1F66202 唯品会的支付请求";
		md5Password ="mechatId=1&operatorType=0&totalMoney=601&tradeMoney=601&orderId=2016050311235996&subject=1%20%20%20%20%20&notifyUrl=http://192.168.30.214:8902/api/payOrder/update&synUrl=http://192.168.30.214:8078/order/list&sign=ed31ef27a73e6a69646b5a9fe9557f3f";
		md5Password = JSON.toJSONString(tree);
		
		
		 Map map2 =  getMapFromJsonObjStr(md5Password);
		 
		System.out.println("synUrl ::: " +  map2.get("synUrl").toString());
		
		String str = DesUtil.encrypt(md5Password);
        System.out.println("length():: " + str.length() + "  str: " + str);  
        str = DesUtil.decrypt(str);  
        System.out.println("length():: " + str.length() +"  str: " + str);  
    }  
    
    public static Map getMapFromJsonObjStr(String jsonObjStr) {   
        JSONObject jsonObject = JSONObject.fromObject(jsonObjStr);   
  
       Map map = new HashMap();   
        for (Iterator iter = jsonObject.keys(); iter.hasNext();) {   
            String key = (String) iter.next();   
            map.put(key, jsonObject.get(key));   
        }   
        return map;   
    }*/
      
    /** 
     * 对数据进行DES加密. 
     * @param data 待进行DES加密的数据 
     * @return 返回经过DES加密后的数据 
     * @throws Exception 
     * @author 
     */  
    public final static String decrypt(String data) throws Exception {  
        return new String(decrypt(hex2byte(data.getBytes()),  
                PASSWORD_CRYPT_KEY.getBytes()));  
    }  
    /** 
     * 对用DES加密过的数据进行解密. 
     * @param data DES加密数据 
     * @return 返回解密后的数据 
     * @throws Exception 
     * @author 
     */  
    public final static String encrypt(String data) throws Exception  {  
        return byte2hex(encrypt(data.getBytes(), PASSWORD_CRYPT_KEY  
                .getBytes()));  
    }  
      
    /** 
     * 用指定的key对数据进行DES加密. 
     * @param data 待加密的数据 
     * @param key DES加密的key 
     * @return 返回DES加密后的数据 
     * @throws Exception 
     * @author 
     */  
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {  
        // DES算法要求有一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();  
        // 从原始密匙数据创建DESKeySpec对象  
        DESKeySpec dks = new DESKeySpec(key);  
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成  
        // 一个SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);  
        SecretKey securekey = keyFactory.generateSecret(dks);  
        // Cipher对象实际完成加密操作  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        // 用密匙初始化Cipher对象  
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);  
        // 现在，获取数据并加密  
        // 正式执行加密操作  
        return cipher.doFinal(data);  
    }  
    /** 
     * 用指定的key对数据进行DES解密. 
     * @param data 待解密的数据 
     * @param key DES解密的key 
     * @return 返回DES解密后的数据 
     * @throws Exception 
     * @author
     */  
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {  
        // DES算法要求有一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();  
        // 从原始密匙数据创建一个DESKeySpec对象  
        DESKeySpec dks = new DESKeySpec(key);  
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成  
        // 一个SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);  
        SecretKey securekey = keyFactory.generateSecret(dks);  
        // Cipher对象实际完成解密操作  
        Cipher cipher = Cipher.getInstance(ALGORITHM);  
        // 用密匙初始化Cipher对象  
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);  
        // 现在，获取数据并解密  
        // 正式执行解密操作  
        return cipher.doFinal(data);  
    }  
    public static byte[] hex2byte(byte[] b) {  
        if ((b.length % 2) != 0)  
            throw new IllegalArgumentException("长度不是偶数");  
        byte[] b2 = new byte[b.length / 2];  
        for (int n = 0; n < b.length; n += 2) {  
            String item = new String(b, n, 2);  
            b2[n / 2] = (byte) Integer.parseInt(item, 16);  
        }  
        return b2;  
    }  
    public static String byte2hex(byte[] b) {  
        String hs = "";  
        String stmp = "";  
        for (int n = 0; n < b.length; n++) {  
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)  
                hs = hs + "0" + stmp;  
            else  
                hs = hs + stmp;  
        }  
        return hs.toUpperCase();  
    }  
    
    /** 
     * 对用DES加密过的数据进行解密. 
     * @param data DES加密数据 
     * @return 返回解密后的数据 
     * @throws Exception 
     * @author 
     */  
    public static String encrypt(String data,String key) throws Exception  {  
        return byte2hex(encrypt(data.getBytes(), key  
                .getBytes()));  
    }  
    
    public static String decrypt(String data,String key) throws Exception  {  
        return new String(decrypt(hex2byte(data.getBytes()),  
        		key.getBytes()));  
    }
    

      
}  