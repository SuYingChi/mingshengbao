package com.msht.minshengbao.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.androidShop.activity.MessageListActivity;
import com.msht.minshengbao.androidShop.activity.ShopClassDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopGoodDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopKeywordListActivity;
import com.msht.minshengbao.androidShop.activity.TotalMessageListActivity;
import com.msht.minshengbao.androidShop.activity.WarnMessageDetailActivity;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.functionActivity.Electricvehicle.ElectricHomeActivity;
import com.msht.minshengbao.functionActivity.GasService.GasEmergencyRescueActivity;
import com.msht.minshengbao.functionActivity.GasService.GasIcCardActivity;
import com.msht.minshengbao.functionActivity.GasService.GasInstallActivity;
import com.msht.minshengbao.functionActivity.GasService.GasInternetTableActivity;
import com.msht.minshengbao.functionActivity.GasService.GasIntroduceActivity;
import com.msht.minshengbao.functionActivity.GasService.GasPayFeeActivity;
import com.msht.minshengbao.functionActivity.GasService.GasPayFeeHomeActivity;
import com.msht.minshengbao.functionActivity.GasService.GasRepairActivity;
import com.msht.minshengbao.functionActivity.GasService.GasServiceActivity;
import com.msht.minshengbao.functionActivity.GasService.GasWriteTableActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.HouseHoldCleanWeb;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.IntelligentFarmHmlActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.VegetableGentlemenActivity;
import com.msht.minshengbao.functionActivity.LPGActivity.LpgMyAccountActivity;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.functionActivity.Public.AllServiceActivity;
import com.msht.minshengbao.functionActivity.WaterApp.WaterMainActivity;
import com.msht.minshengbao.functionActivity.insurance.InsuranceHome;
import com.msht.minshengbao.functionActivity.insurance.InsuranceListActivity;
import com.msht.minshengbao.functionActivity.repairService.HomeMaintenanceActivity;
import com.msht.minshengbao.functionActivity.repairService.HouseApplianceFixActivity;
import com.msht.minshengbao.functionActivity.repairService.HouseKeepingActivity;
import com.msht.minshengbao.functionActivity.repairService.LampCircuitActivity;
import com.msht.minshengbao.functionActivity.repairService.OtherRepairActivity;
import com.msht.minshengbao.functionActivity.repairService.SanitaryWareActivity;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author hong
 * @date 2018/11/12  
 */
public class AppActivityUtil {
    public static void onAppActivityType(Context context, String url, String title, String share, String desc, String activityCode, String backUrl) {
        if (context != null && (!TextUtils.isEmpty(url))) {
            switch (LinkUrlUtil.getDomain(url)) {
                case ConstantUtil.SHOP_DOMAIN:
                case ConstantUtil.DEBUG_SHOP_DOMAIN:
                    onShopMallPage(context, url);
                    break;
                case ConstantUtil.VEGETABLE_DOMAIN:
                    Intent intent = new Intent(context,ShopKeywordListActivity.class);
                    intent.putExtra("gcid","24");
                    context.startActivity(intent);
                    break;
                default:
                    if (url.startsWith(ConstantUtil.MSB_APP)) {
                        startActivityAdvertising(context, url, title, share, desc);
                    } else {
                        if (!url.equals(VariableUtil.NULL_VALUE)) {
                            if (isLoginState(context)) {
                                onStartHtmlActivity(context, url, title, share, desc, activityCode, backUrl);
                            } else {
                                onStartLoginActivity(context, "");
                            }
                        }
                    }

                    break;
            }
        }
    }

    private static void startActivityAdvertising(Context context, String url, String title, String share, String desc) {
        String code = Uri.parse(url).getQueryParameter("code");
        String id;
        switch (code) {
            case ConstantUtil.SHOP:
                onShopMallPage(context,url);
                break;
            case ConstantUtil.WATER:
                onDrinkingWater(context, "");
                break;
            case ConstantUtil.DRINKING_WATER:
                onDrinkingWater(context, "");
                break;
            case ConstantUtil.LPG_NAME:
                onLpgService(context, "");
                break;
            case ConstantUtil.VEGETABLE:
                onVegetableModel(context, "");
                break;
            case ConstantUtil.INSURANCE:
                onInsurance(context, "");
                break;
            case ConstantUtil.GAS_METER:
                onGasMeter(context, "");
                break;
            case ConstantUtil.GAS_PAY:
                onGasPay(context, "");
                break;
            case ConstantUtil.GAS_IC_CARD:
                onIcCard(context, "");
                break;
            case ConstantUtil.HOUSEKEEPING_CLEAN:
                id = Uri.parse(url).getQueryParameter("id");
                onHouseKeepingClean(context, id, "家政保洁");
                break;
            case ConstantUtil.HOUSEHOLD_CLEAN:
                id = Uri.parse(url).getQueryParameter("id");
                if (isLoginState(context)){
                    onHouseHoldClean(context, id,  "家电清洗",code);
                }else {
                    onStartLoginActivity(context, url);
                }

                break;
            case ConstantUtil.HOME_MAINTENANCE:
                id = Uri.parse(url).getQueryParameter("id");
                onHomeMaintenance(context, id, "家居维修");
                break;
            default:
                break;
        }
    }

    public static void startActivityCode(Context context, String code, String id, String name, String hasNext) {
        if (context != null) {
            switch (code) {
                case ConstantUtil.HOUSEHOLD_CLEAN:
                    if (hasNext.equals(ConstantUtil.VALUE_ONE)) {
                        onHomeMaintenance(context, id, name);
                    } else {
                        if (isLoginState(context)){
                            onHouseHoldClean(context, id, name,code);
                        }else {
                            onStartLoginActivity(context,"");
                        }

                    }
                    break;
                case ConstantUtil.HOUSEHOLD_REPAIR:
                    if (hasNext.equals(ConstantUtil.VALUE_ONE)) {
                        onHomeMaintenance(context, id, name);
                    } else {
                        onHouseHoldRepair(context, id, name, hasNext);
                    }
                    break;
                case ConstantUtil.SANITARY_WARE:
                    if (hasNext.equals(ConstantUtil.VALUE_ONE)) {
                        onHomeMaintenance(context, id, name);
                    } else {
                        onSanitaryWare(context, id, name, hasNext);
                    }
                    break;
                case ConstantUtil.LAMP_CIRCUIT:
                    if (hasNext.equals(ConstantUtil.VALUE_ONE)) {
                        onHomeMaintenance(context, id, name);
                    } else {
                        onLampCircuit(context, id, name, hasNext);
                    }
                    break;
                case ConstantUtil.OTHER_REPAIR:
                    if (hasNext.equals(ConstantUtil.VALUE_ONE)) {
                        onHomeMaintenance(context, id, name);
                    } else {
                        onOtherRepair(context, id, name, hasNext);
                    }
                    break;
                case ConstantUtil.GAS_SERVE:
                    onGasServe(context, id, name);
                    break;
                case ConstantUtil.ELECTRIC_VEHICLE_REPAIR:
                    onElectricVehicleRepair(context);
                    break;
                case ConstantUtil.INSURANCE:
                    onInsurance(context, "");
                    break;
                case ConstantUtil.ALL_SERVICE:
                    onAllService(context);
                    break;
                case ConstantUtil.INTELLIGENT_FARM:
                    onIntelligentFarm(context);
                    break;
                case ConstantUtil.DRINKING_WATER:
                    onDrinkingWater(context, "");
                    break;
                case ConstantUtil.VEGETABLE_SCXS:
                    onVegetableModel(context, "");
                    break;
                case ConstantUtil.HOUSEKEEPING_CLEAN:
                    if (hasNext.equals(ConstantUtil.VALUE_ONE)) {
                        onHomeMaintenance(context, id, name);
                    } else {
                        onHouseKeepingClean(context, id, name);
                    }
                    break;
                case ConstantUtil.HOME_MAINTENANCE:
                    onHomeMaintenance(context, id, name);
                    break;
                case ConstantUtil.SHOP:
                    onShopMall(context);
                    break;
                case ConstantUtil.GAS_PAY:
                    onGasPay(context, "");
                    break;
                case ConstantUtil.GAS_METER:
                    onGasMeter(context, "");
                    break;
                case ConstantUtil.GAS_IC_CARD:
                    onIcCard(context, "");
                    break;
                case ConstantUtil.GAS_NORMAL_PAY:
                    onStartGasNormalPay(context, code);
                    break;
                case ConstantUtil.GAS_IOT:
                    onStartGasIot(context, code);
                    break;
                case ConstantUtil.LPG_NAME:
                    onLpgService(context, "");
                    break;
                default:
                    showNotify(context, "民生宝", "已推出新版本，如果您想使用该服务，请点击更新！");
                    break;
            }
        }
    }
    public static void startActivityTopCode(Context context, String code, String id, String name) {
        if (context != null) {
            switch (code) {
                case ConstantUtil.SHOP:
                    onShopMall(context);
                    break;
                case ConstantUtil.GAS_PAY:
                    onGasPay(context, "");
                    break;
                case ConstantUtil.GAS_NORMAL_PAY:
                    onStartGasNormalPay(context, code);
                    break;
                case ConstantUtil.GAS_IOT:
                    onStartGasIot(context, code);
                    break;
                case ConstantUtil.GAS_METER:
                    onGasMeter(context, "");
                    break;
                case ConstantUtil.GAS_IC_CARD:
                    onIcCard(context, "");
                    break;
                case ConstantUtil.LPG_NAME:
                    onLpgService(context, "");
                    break;
                case ConstantUtil.GAS_REPAIR:
                    onGasRepair(context, "");
                    break;
                case ConstantUtil.GAS_INSTALL:
                    onGasInstall(context, "");
                    break;
                case ConstantUtil.GAS_RESCUE:
                    onGasRescue(context);
                    break;
                case ConstantUtil.GAS_INTRODUCE:
                    onGasIntroduce(context);
                    break;
                case ConstantUtil.GAS_SERVE:
                    onGasServe(context, id, name);
                    break;
                case ConstantUtil.DRINKING_WATER:
                    onDrinkingWater(context, "");
                    break;
                case ConstantUtil.HOUSEHOLD_CLEAN:
                    if (isLoginState(context)){
                        onHouseHoldClean(context, id, name, code);
                    }else {
                        onStartLoginActivity(context,"");
                    }

                    break;
                case ConstantUtil.HOUSEKEEPING_CLEAN:
                    onHouseKeepingClean(context, id, name);
                    break;
                case ConstantUtil.HOUSEHOLD_REPAIR:
                    onHouseHoldRepair(context, id, name, "0");
                    break;
                case ConstantUtil.INSURANCE:
                    onInsurance(context, "");
                    break;
                case ConstantUtil.SANITARY_WARE:
                    onSanitaryWare(context, id, name, "0");
                    break;
                case ConstantUtil.HOME_MAINTENANCE:
                    onHomeMaintenance(context, id, name);
                    break;
                default:
                    showNotify(context, "民生宝", "已推出新版本，如果您想使用该服务，请点击更新！");
                    break;
            }
        }
    }

    public static void onAllServiceStartActivity(Context context, String mainCode, String code, String id, String name, String hasNext) {
        if (context != null) {
            switch (mainCode) {
                case ConstantUtil.GAS_SERVE:
                    startActivityTopCode(context, code, id, name);
                    break;
                case ConstantUtil.REPAIR:
                    startActivityCode(context, code, id, name, hasNext);
                    break;
                case ConstantUtil.CLEAN:
                    startActivityCode(context, code, id, name, hasNext);
                    break;
                case ConstantUtil.CONVENIENCE_SERVICE:
                    startActivityCode(context, code, id, name, hasNext);
                    break;
                case ConstantUtil.SHOP:
                    startActivityShop(context, code);
                    break;
                default:
                    startActivityCode(context, code, id, name, hasNext);
                    break;
            }
        }
    }


    private static void startActivityShop(Context context, String code) {
        if (context != null) {
            switch (code) {
                case ConstantUtil.SHOP_ELECTRIC_WATER_HEATER:
                    onShopMallPage(context, UrlUtil.SHOP_HEATERCALORIFIER);
                    break;
                case ConstantUtil.SHOP_LAMPBLACK_MACHINE:
                    onShopMallPage(context, UrlUtil.SHOP_LAMPBLACK);
                    break;
                case ConstantUtil.SHOP_GAS_STOVE:
                    onShopMallPage(context, UrlUtil.SHOP_GAS_STOVE);
                    break;
                case ConstantUtil.SHOP_WATER_HEATER:
                    onShopMallPage(context, UrlUtil.SHOP_GAS_HEATER);
                    break;
                case ConstantUtil.SHOP_IMPORTED_RED_WINE:
                    onShopMallPage(context, UrlUtil.SHOP_IMPORT_FOODSTUFF);
                    break;
                case ConstantUtil.SHOP_STERILIZER:
                    onShopMallPage(context, UrlUtil.SHOP_DISINFECTIONCABINET);
                    break;
                default:
                    onShopMall(context);
                    break;
            }
        }
    }

    /**
     * 消息推送
     *
     * @param context 上下文
     * @param url     连接
     */
    public static void onPushStartActivity(Context context, String url) {
        String code = Uri.parse(url).getQueryParameter("code");
        String id;
        if(TextUtils.isEmpty(code)){
            if(LinkUrlUtil.getDomain(url).equals(ConstantUtil.FIANL_SHOP_DOMAIN)){
                onPushStartShop(context,url);
            }else if (url.startsWith(ConstantUtil.HTTP)) {
                url = LinkUrlUtil.containMark(context, url);
                if (isLoginState(context)) {
                    onStartHtmlActivity(context, url, "民生宝", "0", "民生宝", code, "");
                } else {
                    onStartLoginActivity(context, url);
                }
            }
        }else {
            switch (code) {
                case ConstantUtil.SHOP:
                    onPushStartShop(context,url);
                    break;
                case ConstantUtil.LPG_NAME:
                    onPushStartLpg(context, url);
                    break;
                case ConstantUtil.WATER:
                case ConstantUtil.DRINKING_WATER:
                    onPushStartWater(context, url);
                    break;
                case ConstantUtil.INSURANCE:
                    onPushInsurance(context, url);
                    break;
                case ConstantUtil.GAS_METER:
                    onPushGasMeter(context, url);
                    break;
                case ConstantUtil.GAS_NORMAL_PAY:
                    onPushGasNormalPay(context,code);
                    break;
                case ConstantUtil.GAS_IOT:
                    onPushGasIot(context, code);
                    break;
                case ConstantUtil.GAS_PAY:
                    onPushGasPay(context, url);
                    break;
                case ConstantUtil.GAS_IC_CARD:
                    onPushIcCard(context, url);
                    break;
                case ConstantUtil.HOUSEKEEPING_CLEAN:
                    id = Uri.parse(url).getQueryParameter("id");
                    onPushHouseKeepingClean(context, id, "家政保洁");
                    break;
                case ConstantUtil.HOUSEHOLD_CLEAN:
                    id = Uri.parse(url).getQueryParameter("id");
                    if (isLoginState(context)){
                        onPushHouseHoldClean(context, id, "家电清洗",code);
                    }else {
                        onStartLoginActivity(context,url);
                    }
                    break;
                case ConstantUtil.HOME_MAINTENANCE:
                    id = Uri.parse(url).getQueryParameter("id");
                    onPushHomeMaintenance(context, id, "家居维修");
                    break;
                case ConstantUtil.MESSAGE:
                    onPushStartMessage(context, url);
                    break;
                case ConstantUtil.MESSAGE_DETAIL:
                    onStartMessageDetail(context, url, 1);
                    break;
                case ConstantUtil.MESSAGE_LIST:
                    onStartMessageList(context, url, 1);
                    break;
                default:
                    if (url.startsWith(ConstantUtil.HTTP)) {
                        url = LinkUrlUtil.containMark(context, url);
                        if (isLoginState(context)) {
                            onStartHtmlActivity(context, url, "民生宝", "0", "民生宝", code, "");
                        } else {
                            onStartLoginActivity(context, url);
                        }
                    }
                    break;
            }
        }
    }

    private static void onPushGasIot(Context context, String code) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasInternetTableActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, "");
        }
    }
    private static void onPushGasNormalPay(Context context, String code) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasPayFeeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, "");
        }
    }


    public static void onPushActivity(Context context, String url) {
        String code = Uri.parse(url).getQueryParameter("code");
        String id;
        switch (code) {
            case ConstantUtil.SHOP:
                onShopMallPage(context,url);
                break;
            case ConstantUtil.WATER:
                onDrinkingWater(context, url);
                break;
            case ConstantUtil.DRINKING_WATER:
                onDrinkingWater(context, url);
                break;
            case ConstantUtil.LPG_NAME:
                onLpgService(context, url);
                break;
            case ConstantUtil.VEGETABLE:
                onVegetableModel(context, url);
                break;
            case ConstantUtil.INSURANCE:
                onInsurance(context, url);
                break;
            case ConstantUtil.GAS_METER:
                onGasMeter(context, url);
                break;
            case ConstantUtil.GAS_PAY:
                onGasPay(context, url);
                break;
            case ConstantUtil.GAS_IC_CARD:
                onIcCard(context, url);
                break;
            case ConstantUtil.HOUSEKEEPING_CLEAN:
                id = Uri.parse(url).getQueryParameter("id");
                onHouseKeepingClean(context, id, "家政保洁");
                break;
            case ConstantUtil.HOUSEHOLD_CLEAN:
                id = Uri.parse(url).getQueryParameter("id");
                if (isLoginState(context)){
                    onHouseHoldClean(context, id, "家电清洗",code);
                }else {
                    onStartLoginActivity(context,url);
                }

                break;
            case ConstantUtil.HOME_MAINTENANCE:
                id = Uri.parse(url).getQueryParameter("id");
                onHomeMaintenance(context, id, "家居维修");
                break;
            case ConstantUtil.MESSAGE:
                onStartMessage(context,url);
                break;
            case ConstantUtil.MESSAGE_DETAIL:
                if(!TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())){
                    onStartMessageDetail(context, url, 0);
                }else {
                    AppActivityUtil.onStartLoginActivity(context, url);
                }
                break;
            case ConstantUtil.MESSAGE_LIST:
                if(!TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())){
                    onStartMessageList(context, url, 0);
                }else {
                    AppActivityUtil.onStartLoginActivity(context, url);
                }
                break;
            default:
                if (url.startsWith(ConstantUtil.HTTP)) {
                    url = LinkUrlUtil.containMark(context, url);
                    if (isLoginState(context)) {
                        onStartHtmlActivity(context, url, "民生宝", "0", "民生宝", code, "");
                    } else {
                        onStartLoginActivity(context, url);
                    }
                }
                break;
        }
    }

    /**
     * 消息列表
     *
     * @param context
     * @param url
     */
    private static void onStartMessageList(Context context, String url, int i) {
        String type = Uri.parse(url).getQueryParameter("type");
        Intent intent = null;
        switch (type) {
            /**燃气工单**/
            case ConstantUtil.VALUE_ONE:
                intent = new Intent(context, MessageListActivity.class);
                break;
            /**紧急通知**/
            case ConstantUtil.VALUE_TWO:
                intent = new Intent(context, MessageListActivity.class);
                break;
            /**物流列表**/
            case ConstantUtil.VALUE_THREE:
                intent = new Intent(context, MessageListActivity.class);
                break;
            /**优惠促销**/
            case ConstantUtil.VALUE_FOUR:
                intent = new Intent(context, MessageListActivity.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            if (i == 1) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.putExtra("type", Integer.valueOf(type));
            context.startActivity(intent);
        }
    }
    /**
     * 消息详情
     *
     * @param context
     * @param url
     * @param i
     */
    private static void onStartMessageDetail(Context context, String url, int i) {
        String type = Uri.parse(url).getQueryParameter("type");
        String id = Uri.parse(url).getQueryParameter("id");
        Intent intent = null;
        switch (type) {
            /**燃气工单**/
            case ConstantUtil.VALUE_ONE:
              //  intent = new Intent(context, MessageDetailActivity.class);
                break;
            /**紧急通知**/
            case ConstantUtil.VALUE_TWO:
                intent = new Intent(context, WarnMessageDetailActivity.class);
                    if (i == 1) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                break;
            /**物流列表**/
            case ConstantUtil.VALUE_THREE:
             //   intent = new Intent(context, MessageDetailActivity.class);
                break;
            /**优惠促销**/
            case ConstantUtil.VALUE_FOUR:
             //   intent = new Intent(context, MessageDetailActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 未推出后台点击消息
     * @param context
     * @param pushUrl
     */
    private static void onPushStartMessage(Context context, String pushUrl) {

        if(!TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())){
            Intent intent = new Intent(context, TotalMessageListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else {
            AppActivityUtil.onLoginActivity(context, pushUrl);
        }
    }
    /**
     * 消息中心
     *
     * @param context
     * @param pushUrl
     */
    private static void onStartMessage(Context context, String pushUrl) {
        if(!TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())){
            context.startActivity(new Intent(context, TotalMessageListActivity.class));
        }else {
            AppActivityUtil.onStartLoginActivity(context, pushUrl);
        }
    }
    private static void onPushHomeMaintenance(Context context, String id, String name) {
        Intent intent = new Intent(context, HomeMaintenanceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pid", id);
        intent.putExtra("typeName", name);
        context.startActivity(intent);
    }
    private static void onPushHouseHoldClean(Context context, String id, String name,String code) {
      //  Intent intent = new Intent(context, HomeApplianceCleanActivity.class);
        Intent intent = new Intent(context, HouseHoldCleanWeb.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        /*intent.putExtra("pid", id);
        intent.putExtra("typeName", name);*/
        intent.putExtra("activityCode",code);
        context.startActivity(intent);
    }
    private static void onPushHouseKeepingClean(Context context, String id, String name) {
        Intent intent = new Intent(context, HouseKeepingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pid", id);
        intent.putExtra("typeName", name);
        context.startActivity(intent);
    }
    private static void onPushIcCard(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasIcCardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            AppActivityUtil.onStartLoginActivity(context, pushUrl);
        }
    }
    private static void onPushGasPay(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasPayFeeHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, pushUrl);
        }
    }

    private static void onStartGasIot(Context context, String code) {

        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasInternetTableActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, "");
        }
    }

    private static void onStartGasNormalPay(Context context, String code) {

        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasPayFeeActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, "");
        }
    }
    private static void onPushGasMeter(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasWriteTableActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, pushUrl);
        }
    }

    private static void onPushStartWater(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, WaterMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, pushUrl);
        }
    }

    private static void onPushStartLpg(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, LpgMyAccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, pushUrl);
        }
    }

    private static void onPushStartShop(Context context,String rightUrl) {
        if ((!TextUtils.isEmpty(rightUrl)) && (!rightUrl.equals(VariableUtil.NULL_VALUE))) {
            if (rightUrl.contains("keyword=")) {
                int index = rightUrl.indexOf("keyword=");
                String shopkeyword = rightUrl.substring(index + 8).trim();
                Intent intent = new Intent(context, ShopKeywordListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("keyword", StringUtil.toURLDecoder(shopkeyword));
                context.startActivity(intent);
            } else if (rightUrl.contains("goods_id=")) {
                int index = rightUrl.indexOf("goods_id=");
                String goodsid = rightUrl.substring(index + 9).trim();
                Intent intent = new Intent(context, ShopGoodDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("goodsid", goodsid);
                context.startActivity(intent);
            } else if (rightUrl.contains("gc_id=")) {
                Intent intent = new Intent(context, ShopClassDetailActivity.class);
                int index = rightUrl.indexOf("gc_id=");
                rightUrl = rightUrl.substring(index + 6).trim();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data", rightUrl);
                context.startActivity(intent);
            } else if (NetUtil.getDomain(rightUrl).equals(ConstantUtil.SHOP_DOMAIN) || NetUtil.getDomain(rightUrl).equals(ConstantUtil.DEBUG_SHOP_DOMAIN)) {
                Intent intent = new Intent(context, HtmlPageActivity.class);
                intent.putExtra("url", rightUrl);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("index",1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

    private static void onPushInsurance(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, InsuranceHome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, pushUrl);
        }
    }

    public static void onStartUrl(Context context, String rightUrl,String code) {
        if (NetUtil.getDomain(rightUrl).equals(ConstantUtil.FIANL_SHOP_DOMAIN)) {
                if (rightUrl.contains("keyword=")) {
                    int index = rightUrl.indexOf("keyword=");
                    String shopkeyword = rightUrl.substring(index + 8).trim();
                    Intent intent = new Intent(context, ShopKeywordListActivity.class);
                    intent.putExtra("keyword", StringUtil.toURLDecoder(shopkeyword));
                    context.startActivity(intent);
                } else if (rightUrl.contains("goods_id=")) {
                    int index = rightUrl.indexOf("goods_id=");
                    String goodsid = rightUrl.substring(index + 9).trim();
                    Intent intent = new Intent(context, ShopGoodDetailActivity.class);
                    intent.putExtra("goodsid", goodsid);
                    context.startActivity(intent);
                } else if (rightUrl.contains("gc_id=")) {
                    Intent intent = new Intent(context, ShopClassDetailActivity.class);
                    int index = rightUrl.indexOf("gc_id=");
                    rightUrl = rightUrl.substring(index + 6).trim();
                    intent.putExtra("data", rightUrl);
                    context.startActivity(intent);
                } else if (NetUtil.getDomain(rightUrl).equals(ConstantUtil.FIANL_SHOP_DOMAIN)) {
                    Intent intent = new Intent(context, HtmlPageActivity.class);
                    intent.putExtra("url", rightUrl);
                    context.startActivity(intent);
                } /*else {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("index",1);
                    context.startActivity(intent);
                }*/
        }else {
            if (!rightUrl.equals(VariableUtil.NULL_VALUE)) {
                if (isLoginState(context)) {
                    onStartHtmlActivity(context, rightUrl, "民生宝", "", "", code, "");
                } else {
                    onStartLoginActivity(context, "");
                }
            }
        }
    }

    private static void onShopMallPage(Context context, String rightUrl) {
        if ((!TextUtils.isEmpty(rightUrl)) && (!rightUrl.equals(VariableUtil.NULL_VALUE))) {
            if (rightUrl.contains("keyword=")) {
                int index = rightUrl.indexOf("keyword=");
                String shopkeyword = rightUrl.substring(index + 8).trim();
                Intent intent = new Intent(context, ShopKeywordListActivity.class);
                intent.putExtra("keyword", StringUtil.toURLDecoder(shopkeyword));
                context.startActivity(intent);
            } else if (rightUrl.contains("goods_id=")) {
                int index = rightUrl.indexOf("goods_id=");
                String goodsid = rightUrl.substring(index + 9).trim();
                Intent intent = new Intent(context, ShopGoodDetailActivity.class);
                intent.putExtra("goodsid", goodsid);
                context.startActivity(intent);
            } else if (rightUrl.contains("gc_id=")) {
                Intent intent = new Intent(context, ShopClassDetailActivity.class);
                int index = rightUrl.indexOf("gc_id=");
                rightUrl = rightUrl.substring(index + 6).trim();
                intent.putExtra("data", rightUrl);
                context.startActivity(intent);
            } else if (NetUtil.getDomain(rightUrl).equals(ConstantUtil.FIANL_SHOP_DOMAIN)) {
                Intent intent = new Intent(context, HtmlPageActivity.class);
                intent.putExtra("url", rightUrl);
                context.startActivity(intent);
            }
        }
    }

    private static void onGasIntroduce(Context context) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasIntroduceActivity.class);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, "");
        }
    }

    private static void onGasRescue(Context context) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasEmergencyRescueActivity.class);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, "");
        }
    }

    private static void onGasInstall(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasInstallActivity.class);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, pushUrl);
        }
    }

    private static void onGasRepair(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasRepairActivity.class);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, pushUrl);
        }
    }

    private static void onShopMall(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("index",1);
        context.startActivity(intent);
    }

    private static void onGasPay(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasPayFeeHomeActivity.class);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, pushUrl);
        }
    }

    private static void onGasMeter(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, GasWriteTableActivity.class);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, pushUrl);
        }
    }

    private static void onIcCard(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent card = new Intent(context, GasIcCardActivity.class);
            context.startActivity(card);
        } else {
            AppActivityUtil.onStartLoginActivity(context, pushUrl);
        }
    }

    private static void onLpgService(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent intent = new Intent(context, LpgMyAccountActivity.class);
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, pushUrl);
        }
    }

    public static void showNotify(Context context, String title, String s) {
        if (context != null) {
            new PromptDialog.Builder(context)
                    .setTitle(title)
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage(s)
                    .setButton1("确定", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
        }
    }

    private static void onHouseHoldClean(Context context,  String id, String name,String code) {
        //Intent intent = new Intent(context, HomeApplianceCleanActivity.class);
        Intent intent = new Intent(context, HouseHoldCleanWeb.class);
        /*intent.putExtra("pid", id);
        intent.putExtra("typeName", name);*/
        intent.putExtra("activityCode",code);
        context.startActivity(intent);
    }

    private static void onHouseHoldRepair(Context context, String id, String name, String hasNext) {
        Intent intent = new Intent(context, HouseApplianceFixActivity.class);
        intent.putExtra("pid", id);
        intent.putExtra("typeName", name);
        context.startActivity(intent);
    }

    private static void onSanitaryWare(Context context, String id, String name, String hasNext) {
        Intent intent = new Intent(context, SanitaryWareActivity.class);
        intent.putExtra("pid", id);
        intent.putExtra("typeName", name);
        context.startActivity(intent);
    }

    private static void onLampCircuit(Context context, String id, String name, String hasNext) {
        Intent intent = new Intent(context, LampCircuitActivity.class);
        intent.putExtra("pid", id);
        intent.putExtra("typeName", name);
        context.startActivity(intent);
    }

    private static void onOtherRepair(Context context, String id, String name, String hasNext) {
        Intent intent = new Intent(context, OtherRepairActivity.class);
        intent.putExtra("pid", id);
        intent.putExtra("typeName", name);
        context.startActivity(intent);
    }

    private static void onGasServe(Context context, String id, String name) {
        Intent intent = new Intent(context, GasServiceActivity.class);
        intent.putExtra("pid", id);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    private static void onElectricVehicleRepair(Context context) {
        Intent intent = new Intent(context, ElectricHomeActivity.class);
        context.startActivity(intent);
    }

    private static void onInsurance(Context context, String pushUrl) {
        if (isLoginState(context)) {
          //  Intent intent = new Intent(context, InsuranceHome.class);
            Intent intent = new Intent(context, InsuranceListActivity.class);
            context.startActivity(intent);

        } else {
            onStartLoginActivity(context, pushUrl);
        }
    }

    private static void onAllService(Context context) {
        if (isLoginState(context)) {
            Intent serve = new Intent(context, AllServiceActivity.class);
            context.startActivity(serve);
        } else {
            onStartLoginActivity(context, "");
        }
    }

    private static void onIntelligentFarm(Context context) {
        if (isLoginState(context)) {
            String url = UrlUtil.Intelligent_FarmUrl;
            Intent intent = new Intent(context, IntelligentFarmHmlActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("navigate", "智慧农贸");
            context.startActivity(intent);
        } else {
            onStartLoginActivity(context, "");
        }
    }

    private static void onDrinkingWater(Context context, String pushUrl) {
        if (isLoginState(context)) {
            Intent serve = new Intent(context, WaterMainActivity.class);
            context.startActivity(serve);
        } else {
            onStartLoginActivity(context, pushUrl);
        }
    }

    private static void onVegetableModel(Context context, String pushUrl) {
        Intent intent = new Intent(context,ShopKeywordListActivity.class);
        intent.putExtra("gcid","24");
        context.startActivity(intent);
    }

    private static void onHouseKeepingClean(Context context, String id, String name) {

        Intent intent = new Intent(context, HouseKeepingActivity.class);
        intent.putExtra("pid", id);
        intent.putExtra("typeName", name);
        context.startActivity(intent);
    }

    private static void onHomeMaintenance(Context context, String id, String name) {
        Intent intent = new Intent(context, HomeMaintenanceActivity.class);
        intent.putExtra("pid", id);
        intent.putExtra("typeName", name);
        context.startActivity(intent);
    }

    private static void onStartHtmlActivity(Context context, String url, String title, String share, String desc, String activityCode, String backUrl) {
        Intent intent = new Intent(context, HtmlPageActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("navigate", "民生宝");
        intent.putExtra("title", title);
        intent.putExtra("share", share);
        intent.putExtra("desc", desc);
        intent.putExtra("activityCode", activityCode);
        intent.putExtra("backUrl", backUrl);
        context.startActivity(intent);
    }

    private static void onStartVegetableActivity(Context context, String url) {
        Intent intent = new Intent(context, VegetableGentlemenActivity.class);
        context.startActivity(intent);
    }

    public static void onStartLoginActivity(Context context, String pushUrl) {
        Intent login = new Intent(context, LoginActivity.class);
        login.putExtra("pushUrl", pushUrl);
        context.startActivity(login);
    }

    public static void onLoginActivity(Context context, String pushUrl) {
        Intent login = new Intent(context, LoginActivity.class);
        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        login.putExtra("pushUrl", pushUrl);
        context.startActivity(login);
    }

    public static boolean isLoginState(Context mContext) {
        return SharedPreferencesUtil.getLstate(mContext, SharedPreferencesUtil.Lstate, false);
    }

    public static boolean isAppAlive(Context mContext) {
        return SharedPreferencesUtil.getAppAliveState(mContext, SharedPreferencesUtil.IS_App_ALIVE, false);
    }
}
