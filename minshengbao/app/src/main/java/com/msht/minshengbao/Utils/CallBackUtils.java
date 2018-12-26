package com.msht.minshengbao.Utils;

import com.msht.minshengbao.Interface.UpdateCallBack;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/12/12  
 */
public class CallBackUtils {
    private static UpdateCallBack mUpdateCallBack;
    public static void onUpdateDate(UpdateCallBack updateCallBack){
        updateCallBack.onUpdateClick(0);
    }

}
