package com.msht.minshengbao;

import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.umeng.socialize.utils.UrlUtil;

/**
 *
 * @author wangfei
 * @date 16/7/12
 */
public class ShareDefaultContent {
   public static String url = "http://msbapp.cn/app/download/scan?qid=95";
    public static String text = "我获得5元优惠券，你来就有10元，约吗?民生宝家居维修官方渠道有保障！";
    public static String title = "民生宝";
    public static String imageurl = "http://img.hackhome.com/img2016/5/10/2016051072133505.jpg";
    public static String videourl = "http://video.sina.com.cn/p/sports/cba/v/2013-10-22/144463050817.html";
    public static String musicurl = "http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3";
    public static String HeadLine="民生头条";
    public static String waterTitle="民生水宝";
    public static String waterText="好友邀请您加入民生水宝，从此告别水质污染！";
   // public static String waterShareUrl=BuildConfig.URL_MSSB_APP+"/rw_front/";
    public static String waterShareUrl=BuildConfig.URL_MSSB_SHARE+"/rw_front/";
    public static String WATER_RED_PACKET_SHARE_TITLE="【民生水宝】送你一个拼手气红包，开启健康饮水生活";
    public static String WATER_RED_PACKET_SHARE_TEXT="最高30元充值大包随机出没，等你来拿！";


}
