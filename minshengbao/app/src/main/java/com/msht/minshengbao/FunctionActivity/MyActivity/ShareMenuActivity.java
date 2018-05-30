package com.msht.minshengbao.FunctionActivity.MyActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.Defaultcontent;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShareMenuActivity extends BaseActivity {
    private Button     appshare;
    private String     userId,password;
    private final int  SUCCESS = 1;
    private final int  FAILURE = 0;
    private int num=63912;
    private byte[] numbyte=new byte[1];
    private ShareAction mShareAction;
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            initShow();    //提示评价成功
                        }else {
                            failure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(context,msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }



    };
    private void initShow() {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("已确认您的分享")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        setResult(1);
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
    private void failure(String error) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_menu);
        setCommonHeader("分享应用");
        context=this;
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initPlatforms();
        initView();
       // initDATA();
    }
    private void initDATA() {
        String string=IntToBinary(128);
        String X=string.substring(0,1);
        char  y=string.charAt(0);
        Toast.makeText(context,"x="+X+",y="+String.valueOf(y),Toast.LENGTH_SHORT).show();
    }

    //十进制转八位二进制
    public static String IntToBinary(int input) {
        String binaryString = Integer.toBinaryString(input);//1111
        int binaryInt = Integer.parseInt(binaryString);//1111
        return String.format("%08d",binaryInt);
    }
    private void initPlatforms() {
        mShareAction=new ShareAction(ShareMenuActivity.this).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        UMWeb web = new UMWeb(Defaultcontent.url);
                        web.setTitle(Defaultcontent.title);
                        web.setDescription(Defaultcontent.text+"——来自民生宝分享面板");
                        web.setThumb(new UMImage(ShareMenuActivity.this, Defaultcontent.imageurl));
                        new ShareAction(ShareMenuActivity.this).withMedia(web)
                                .setPlatform(share_media)
                                .setCallback(umShareListener)
                                .share();
                    }
                });
    }

    private void initView() {
        appshare=(Button)findViewById(R.id.share_menu);
        appshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAction.open();
            }
        });
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {}
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if(!platform.name().equals("WEIXIN_FAVORITE")){
                if (password!=null) {
                    shareEnsure();
                }else {
                    Toast.makeText(context,platform + " 分享成功啦",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){}
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
    private void shareEnsure() {
        String validateURL = UrlUtil.Shara_appUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                requestHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                requestHandler.sendMessage(msg);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        Log.d("result","onActivityResult");
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ShareBoardConfig config = new ShareBoardConfig();
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        mShareAction.open(config);
    }
}
