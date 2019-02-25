package com.msht.minshengbao.androidShop.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.customerview.DrawHookByAnimatorView;
import com.msht.minshengbao.androidShop.viewInterface.OnDissmissLisenter;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;


public class PopUtil {

    private static Toast toast;

    public static PopupWindow showPopWindow(Context context, View anchorView, boolean bottom, View popupView, PopupWindow window) {


       /* View popupView = LayoutInflater.from(context).inflate(R.layout.layout_popupwindow, null);

        PopupWindow window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);*/

        window.setAnimationStyle(R.style.popup_window_anim);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setElevation(20);
        }

        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));

        window.setFocusable(true);

        window.setOutsideTouchable(true);

        window.update();

        //这些参数到时再自己调整
        if (bottom) {
            int windowPos[] = calculatePopWindowPos(anchorView, popupView);
            int xOff = 20; // 可以自己调整偏移
            windowPos[0] -= xOff;
            windowPos[1] -= xOff;
            window.showAtLocation(anchorView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
        } else {
            anchorView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            int mShowMorePopupWindowWidth = (int) (-anchorView.getMeasuredWidth() * 2.2);

            window.showAsDropDown(anchorView, mShowMorePopupWindowWidth, 0);
        }
        return window;
    }

    public static void toastInBottom(int stringResourceId) {

        if (toast == null) {
            toast = Toast.makeText(MyApplication.getMsbApplicationContext(), MyApplication.getMsbApplicationContext().getString(stringResourceId), Toast.LENGTH_LONG);
        } else {
            toast.setText(MyApplication.getMsbApplicationContext().getString(stringResourceId));
        }
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 300);
        toast.show();
    }

    public static void toastInBottom(String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getMsbApplicationContext(), string, Toast.LENGTH_LONG);
        } else {
            toast.setText(string);
        }
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 300);
        toast.show();
    }

    public static void toastInCenter(int stringResourceId) {

        if (toast == null) {
            toast = Toast.makeText(MyApplication.getMsbApplicationContext(), MyApplication.getMsbApplicationContext().getString(stringResourceId), Toast.LENGTH_LONG);
        } else {
            toast.setText(MyApplication.getMsbApplicationContext().getString(stringResourceId));
        }
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 300);
        toast.show();
    }

    public static void toastInCenter(String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getMsbApplicationContext(), string, Toast.LENGTH_LONG);
        } else {
            toast.setText(string);
        }
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 300);
        toast.show();
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return popwindow显示的左上角的xOff, yOff坐标
     */
    public static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        final int screenHeight = DimenUtil.getScreenHeight();
        final int screenWidth = DimenUtil.getScreenWidth();
        // 测量contentView
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    public static void hideInput(Context context, EditText et) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public static void showComfirmDialog(Context mContext, String title, String tips, String left, String right,
                                         final View.OnClickListener onCancel,
                                         final View.OnClickListener onOK, boolean canceledOnTouchOutside) {
        LayoutInflater inflaterDl = LayoutInflater.from(mContext);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                R.layout.dialog_tips, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();

        TextView tvTitle = (TextView) layout.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        }

        TextView tvtips = (TextView) layout.findViewById(R.id.tv_delete_tips);
        tvtips.setText(tips);
        dialog.setCancelable(canceledOnTouchOutside);
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        TextView btnCancel = (TextView) layout.findViewById(R.id.dialog_btn_cancel);
        if (!TextUtils.isEmpty(left)) {
            btnCancel.setText(left);
        }
        TextView btnOk = (TextView) layout.findViewById(R.id.dialog_btn_ok);
        if (!TextUtils.isEmpty(right)) {
            btnOk.setText(right);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onCancel != null)
                    onCancel.onClick(v);

            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onOK != null)
                    onOK.onClick(v);

            }
        });
    }

    public static void showAutoDissHookDialog(final Context mContext, String tips, int delayMillis) {
        LayoutInflater inflaterDl = LayoutInflater.from(mContext);
        final LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                R.layout.dialog_autodissmiss_tips, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext, R.style.Loading_dialog).create();
        TextView tvtips = (TextView) layout.findViewById(R.id.tv_delete_tips);
        tvtips.setText(tips);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mContext instanceof Activity) {
                    if(!((Activity) mContext).isFinishing()) {
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                    } }
            }
        }, delayMillis);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mContext instanceof Activity) {
                            if (!((Activity) mContext).isFinishing()) {
                                if (dialog != null)
                                    dialog.dismiss();
                            }
                        }
                    }
                }, 1500);
            }
        });
    }
        public static void showAutoDissHookDialog(Context mContext, String tips, int delayMillis, final OnDissmissLisenter onDissmissLisenter) {
            LayoutInflater inflaterDl = LayoutInflater.from(mContext);
            final LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                    R.layout.dialog_autodissmiss_tips, null);
            final AlertDialog dialog = new AlertDialog.Builder(mContext, R.style.Loading_dialog).create();
            TextView tvtips = (TextView) layout.findViewById(R.id.tv_delete_tips);
            tvtips.setText(tips);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                }
            }, delayMillis);
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null)
                                dialog.dismiss();
                            onDissmissLisenter.onDissmiss();
                        }
                    }, 1500);
                }
            });
    }

    public static void showWebViewDialog(final ShopBaseActivity activity, String url) {
        LayoutInflater inflaterDl = LayoutInflater.from(activity);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(
                R.layout.h5_tips, null);
        final AlertDialog tel_dialog = new AlertDialog.Builder(activity).create();
        tel_dialog.show();
        tel_dialog.getWindow().setContentView(layout);
        WindowManager.LayoutParams attributes = tel_dialog.getWindow().getAttributes();
        attributes.width = DimenUtil.getScreenWidth() - DimenUtil.dip2px(activity.getResources().getDimension(R.dimen.margin_Modules) * 2);
        attributes.height = DimenUtil.getScreenHeight() - DimenUtil.dip2px(activity.getResources().getDimension(R.dimen.margin_Modules) * 2);
        attributes.gravity = Gravity.CENTER;
        tel_dialog.getWindow().setAttributes(attributes);
        tel_dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        WebView webView = (WebView) layout.findViewById(R.id.web_view);
        TextView btnOk = (TextView) layout.findViewById(R.id.dialog_btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tel_dialog.dismiss();
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                activity.showLoading();

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                activity.dismissLoading();

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                activity.onError("网页加载错误，请稍后重试");

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
        webView.loadUrl(url);
    }


}
