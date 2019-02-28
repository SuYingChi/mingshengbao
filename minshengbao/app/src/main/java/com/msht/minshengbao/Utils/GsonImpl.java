package com.msht.minshengbao.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/2/19  
 */
public class GsonImpl extends AbstractJson{

    private Gson gson = new Gson();
    @Override
    public String toJson(Object src) {
        return gson.toJson(src);
    }

    @Override
    public <T> T toObject(String json, Class<T> claxx) {
        return gson.fromJson(json, claxx);
    }

    @Override
    public <T> T toObject(byte[] bytes, Class<T> claxx) {
        return gson.fromJson(new String(bytes), claxx);
    }

    @Override
    public <T> List<T> toList(String json, Class<T> claxx) {
        Type type = new TypeToken<ArrayList<T>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public static ArrayList <HashMap<String ,String>> getAdditionalList(JSONArray jsonArray){
        ArrayList <HashMap<String ,String>> additionalList=new ArrayList <HashMap<String ,String>>();
        for (int i = 0; i<jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", jsonObject.optString("name"));
            map.put("code", jsonObject.optString("code"));
            map.put("value",jsonObject.optString("value"));
            additionalList.add(map);
        }
        return additionalList;
    }
    public static ArrayList <HashMap<String ,String>> getAdditionalList(String additional){
        ArrayList <HashMap<String ,String>> additionalList=new ArrayList <HashMap<String ,String>>();
        try {
            JSONArray jsonArray=new JSONArray(additional);
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", jsonObject.optString("name"));
                map.put("code", jsonObject.optString("code"));
                map.put("value",jsonObject.optString("value"));
                additionalList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return additionalList;
    }
}
