package com.msht.minshengbao.androidShop.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.msht.minshengbao.androidShop.shopBean.BaseData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by qyf on 2016/3/1.
 */
public class JsonUtil {
    public static final Gson gson = new Gson();

    public static <T> T toBean(String json, String urg1, String urg2, Class<T> classOfT) {
        try {
            JSONObject object = new JSONObject(json);
            String s1 = object.getString(urg1);
            JSONObject obj = new JSONObject(s1);
            String s2 = obj.getString(urg2);
            return gson.fromJson(s2, classOfT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static <T> T toBean(String json, String urg1, String urg2, Type type) {
        try {
            JSONObject object = new JSONObject(json);
            String s1 = object.getString(urg1);
            JSONObject obj = new JSONObject(s1);
            String s2 = obj.getString(urg2);
            return gson.fromJson(s2, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static <T> T toBean(String json, String urg1, Class<T> classOfT) {
        try {
            JSONObject object = new JSONObject(json);
            String s1 = object.optString(urg1);
            return gson.fromJson(s1, classOfT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static <T> T toBean(String json, String urg1, Type type) {
        try {
            JSONObject object = new JSONObject(json);
            String s1 = object.optString(urg1);
            return gson.fromJson(s1, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static <T> T toBean(String json, Class<T> classOfT) {

        try {
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toBean(String json, Type type) {

        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer toInteger(String json, String urg1) {
        try {
            JSONObject object = new JSONObject(json);
            return Integer.valueOf(object.optString(urg1));
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String toString(String json, String urg1) {
        try {
            JSONObject object = new JSONObject(json);
            return object.optString(urg1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }

    public static String toString(String json, String urg1, String urg2) {
        try {
            JSONObject object = new JSONObject(json);
            JSONObject obj1 = new JSONObject(object.optString(urg1));
            return obj1.optString(urg2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }

    public static String getError(String json) {
        String error = null;
        try {
            JSONObject object = new JSONObject(json);
            error = object.optString("error");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return error;
    }

    public static boolean isJsonHasKey(String json, String key) {
        try {
            JSONObject object = new JSONObject(json);
            return object.has(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }

    /*
    *{"scm":{"key1":"vlaue1","key2":"vlaue2"}}
{"scm":[{"key1":"vlaue1","key2":"vlaue2"},{"key11":"vlaue11","key22":"vlaue22"}]}
     */
    public static void getJsonValue(String json, String key) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            Object listArray = new JSONTokener(jsonObject.optString(key)).nextValue();
            if (listArray instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) listArray;
                for (int k = 0; k < jsonArray.length(); k++) {
                    JSONObject parameterObject = jsonArray.optJSONObject(k);
                    System.out.println(parameterObject);
                }
            } else if (listArray instanceof JSONObject) {
                JSONObject jsonObject3 = (JSONObject) listArray;
                System.out.println(jsonObject3);

            } else if (listArray instanceof String) {
                String s = (String) listArray;
            } else if (listArray instanceof Boolean) {
                Boolean b = (Boolean) listArray;
            } else if (listArray instanceof Integer) {
                Integer i = (Integer) listArray;
            } else if (listArray instanceof Long) {
                Long l = (Long) listArray;
            } else if (listArray instanceof Double) {
                Double d = (Double) listArray;
            } else if (listArray == null) {

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


    public static BaseData getBaseData(String json) {
        try {
            JSONObject object = new JSONObject(json);
            BaseData baseData = new BaseData();
            baseData.setCode(object.optInt("code"));
            baseData.setDatas(object.optString("datas"));
            return baseData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     /*     * 解析json(猜你喜欢商品)
     *
     * @param json
     * @return
     *//*
    public ArrayList<GoodsLiteVo> getGoodsLiteData(String json) {
        ArrayList<GoodsLiteVo> itemGoodsLite = JsonUtil.toBean(json, new TypeToken<List<GoodsLiteVo>>() {
        }.getCompanyInvType());
        return itemGoodsLite;
    }

    */

    /**
     * 解析json(弹窗广告模块)
     *
     * @param json
     * @return
     *//*
    public ItemPopupAd getPopupAdData(String json) {
        ItemPopupAd itemPopupAd = JsonUtil.toBean(json, ItemPopupAd.class);
        return itemPopupAd;
    }*/
    public static <T> ArrayList<T> jsonArrayToList(String json) {
        ArrayList<T> itemGoodsLite = JsonUtil.toBean(json, new TypeToken<List<T>>() {
        }.getType());
        return itemGoodsLite;
    }

    public static <T> T getPopupAdData(String json, Class<T> clazz) {
        T itemPopupAd = JsonUtil.toBean(json, clazz);
        return itemPopupAd;
    }

    public static Map<String, String> jSONObjectUnKnowKeyParse(JSONObject itemJson) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            Iterator<String> keys = itemJson.keys();
            while (keys.hasNext()) {
                String sKey = keys.next().toString();
                LogUtils.e("xxxxx -- " + sKey + "   ----");
                String value = itemJson.optString(sKey);
                LogUtils.e("xxxxx -- " + sKey + "   ----" + "value-----" + value);
                map.put(sKey, value);
            }
            LogUtils.e(map.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<String> getJsonArrayKeyList(JSONArray jSONArray) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jSONArray.length(); i++) {
            list.add(jSONArray.optString(i));
        }
        return list;
    }

    public static  List<String> getJsonObjectKeyList(JSONObject itemJson) {
        List<String> list = new ArrayList<String>();
        try {
            Iterator<String> keys = itemJson.keys();
            while (keys.hasNext()) {
                String sKey = keys.next().toString();
                list.add(sKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
