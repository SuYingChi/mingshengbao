package com.msht.minshengbao.androidShop.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.msht.minshengbao.R;
import com.msht.minshengbao.permissionManager.RuntimeRationale;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import java.util.ArrayList;
import java.util.List;


/**
 * 6.0 运行时权限处理工具类。目前废弃，项目中当前使用Andpermission框架做动态权限处理
 */
public class PermissionUtils {

    private static int mRequestCode = -1;

    public static void requestPermissionsResult(Activity activity, int requestCode
            , String[] permission, PermissionCallBack callback) {
        requestPermissions(activity, requestCode, permission, callback);
    }

    public static void requestPermissionsResult(android.app.Fragment fragment, int requestCode
            , String[] permission, PermissionCallBack callback) {
        requestPermissions(fragment, requestCode, permission, callback);
    }

    public static void requestPermissionsResult(Fragment fragment, int requestCode
            , String[] permission, PermissionCallBack callback) {
        requestPermissions(fragment, requestCode, permission, callback);
    }

    /**
     * 请求权限处理
     *
     * @param object      activity or fragment
     * @param requestCode 请求码
     * @param permissions 需要请求的权限
     * @param callback    结果回调
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static void requestPermissions(Object object, int requestCode
            , String[] permissions, PermissionCallBack callback) {

        checkCallingContextSuitability(object);
        mPermissionCallBack = callback;

        if (checkPermissions(getContext(object), permissions)) {
            if (mPermissionCallBack != null)
                mPermissionCallBack.onPermissionGranted(requestCode);
        } else {
            List<String> deniedPermissions = getNeedToGrantedPermissions(getContext(object), permissions);
            if (deniedPermissions.size() > 0) {
                mRequestCode = requestCode;

                if (object instanceof Activity) {
                    ((Activity) object).requestPermissions(deniedPermissions
                            .toArray(new String[deniedPermissions.size()]), requestCode);
                } else if (object instanceof android.app.Fragment) {
                    ((android.app.Fragment) object).requestPermissions(deniedPermissions
                            .toArray(new String[deniedPermissions.size()]), requestCode);
                } else if (object instanceof android.support.v4.app.Fragment) {
                    ((android.support.v4.app.Fragment) object).requestPermissions(deniedPermissions
                            .toArray(new String[deniedPermissions.size()]), requestCode);
                }
            }
        }
    }

    /**
     * 获取上下文
     */
    private static Context getContext(Object object) {
        Context context;
        if (object instanceof android.app.Fragment) {
            context = ((android.app.Fragment) object).getActivity();
        } else if (object instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) object).getActivity();
        } else {
            context = (Activity) object;
        }
        return context;
    }

    /**
     * 请求权限结果，对应onRequestPermissionsResult()方法。
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == mRequestCode) {
            if (verifyPermissions(grantResults)) {
                if (mPermissionCallBack != null)
                    mPermissionCallBack.onPermissionGranted(requestCode);
            } else {
                if (mPermissionCallBack != null)
                    mPermissionCallBack.onPermissionDenied(requestCode);
            }
        }
    }

    /**
     * 显示提示对话框
     */
    public static void showTipsDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(context);
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    private static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 验证权限是否都已经授权
     */
    private static boolean verifyPermissions(int[] grantResults) {
        // 如果请求被取消，则结果数组为空
        if (grantResults.length <= 0)
            return false;

        // 循环判断每个权限是否被拒绝
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限列表中所有需要授权的权限
     *
     * @param context     上下文
     * @param permissions 权限列表
     * @return
     */
    private static List<String> getNeedToGrantedPermissions(Context context, String... permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    /**
     * 检查所传递对象的正确性
     *
     * @param object 必须为 activity or fragment
     */
    private static void checkCallingContextSuitability(Object object) {
        if (object == null) {
            throw new NullPointerException("Activity or Fragment should not be null");
        }

        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof android.support.v4.app.Fragment;
        boolean isAppFragment = object instanceof android.app.Fragment;

        if (!(isActivity || isSupportFragment || isAppFragment)) {
            throw new IllegalArgumentException(
                    "Caller must be an Activity or a Fragment");
        }
    }

    /**
     * 检查所有的权限是否已经被授权
     *
     * @param permissions 权限列表
     * @return
     */
    private static boolean checkPermissions(Context context, String... permissions) {
        if (isOverMarshmallow()) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkIsSinglePermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断当前手机API版本是否 >= 6.0
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void requestPermissions(final Context context, final PermissionRequestFinishListener permissionRequestFinishListener, String... permissions) {
        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        permissionRequestFinishListener.onPermissionRequestSuccess(permissions);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            showSettingDialog(context, permissions, permissionRequestFinishListener);
                        } else {
                            PopUtil.toastInCenter("权限未开启");
                           // permissionRequestFinishListener.onPermissionRequestDenied(permissions);
                        }

                    }
                })
                .start();
    }

    /**
     * Display setting dialog.
     */
    public static void showSettingDialog(final Context context, final List<String> permissions, final PermissionRequestFinishListener permissionRequestFinishListener) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("Tips")
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission(context, permissionRequestFinishListener);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PopUtil.toastInCenter("权限未开启");
                       // permissionRequestFinishListener.onPermissionRequestDenied(permissions);
                    }
                })
                .show();
    }

    /**
     * Set permissions.
     */
    private static void setPermission(Context context, final PermissionRequestFinishListener permissionRequestFinishListener) {
        AndPermission.with(context)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                       // permissionRequestFinishListener.onBackFromSettingPage();
                    }
                })
                .start();
    }

    public interface PermissionRequestFinishListener {

        void onPermissionRequestSuccess(List<String> permissions);
    }

    public interface PermissionCallBack {
        void onPermissionGranted(int requestCode);

        void onPermissionDenied(int requestCode);
    }

    private static PermissionCallBack mPermissionCallBack;

    /**
     * 返回true 表示可以使用  返回false表示不可以使用
     */
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

}
