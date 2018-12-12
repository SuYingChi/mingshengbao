package com.msht.minshengbao.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.functionActivity.Electricvehicle.ElectricHomeActivity;
import com.msht.minshengbao.functionActivity.GasService.GasEmergencyRescueActivity;
import com.msht.minshengbao.functionActivity.GasService.GasIcCardActivity;
import com.msht.minshengbao.functionActivity.GasService.GasInstallActivity;
import com.msht.minshengbao.functionActivity.GasService.GasIntroduceActivity;
import com.msht.minshengbao.functionActivity.GasService.GasPayFeeActivity;
import com.msht.minshengbao.functionActivity.GasService.GasRepairActivity;
import com.msht.minshengbao.functionActivity.GasService.GasServiceActivity;
import com.msht.minshengbao.functionActivity.GasService.GasWriteTableActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.IntelligentFarmHmlActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.ShopActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.VegetableGentlemenActivity;
import com.msht.minshengbao.functionActivity.LPGActivity.LpgMyAccountActivity;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.msht.minshengbao.functionActivity.Public.AllServiceActivity;
import com.msht.minshengbao.functionActivity.WaterApp.WaterMainActivity;
import com.msht.minshengbao.functionActivity.insurance.InsuranceHome;
import com.msht.minshengbao.functionActivity.repairService.HomeApplianceCleanActivity;
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
 * @author hong
 * @date 2018/11/12  
 */
public class AppActivityUtil {
    public static  void onAppActivityType(Context context,String url,String title,String share,String desc,String activityCode,String backUrl){
        if (context!=null&&(!TextUtils.isEmpty(url))){
            switch (LinkUrlUtil.getDomain(url)){
                case ConstantUtil.SHOP_DOMAIN:
                    onShopMallPage(context,url);
                    break;
                case ConstantUtil.VEGETABLE_DOMAIN:
                    if (isLoginState(context)){
                        onStartVegetableActivity(context,url);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                default:
                    if (url.startsWith(ConstantUtil.MSB_APP)){
                        startActivityAdvertising(context,url,title,share,desc);
                    }else {
                        if (!url.equals(VariableUtil.NULL_VALUE)){
                            if (isLoginState(context)){
                                onStartHtmlActivity(context,url,title,share,desc,activityCode,backUrl);
                            }else {
                                onStartLoginActivity(context);
                            }
                        }
                    }

                    break;
            }
        }
    }
    private static void startActivityAdvertising(Context context,String url,String title,String share,String desc){
        String code=Uri.parse(url).getQueryParameter("code");
        String id;
        switch (code){
            case ConstantUtil.SHOP:
                onShopMall(context);
                break;
            case ConstantUtil.WATER:
                if (isLoginState(context)){
                    onDrinkingWater(context);
                }else {
                    onStartLoginActivity(context);
                }
                break;
            case ConstantUtil.DRINKING_WATER:
                if (isLoginState(context)){
                    onDrinkingWater(context);
                }else {
                    onStartLoginActivity(context);
                }
                break;
            case ConstantUtil.LPG_NAME:
                if (isLoginState(context)){
                    onLpgService(context);
                }else {
                    onStartLoginActivity(context);
                }
                break;
            case ConstantUtil.VEGETABLE:
                if (isLoginState(context)){
                    onVegetableModel(context);
                }else {
                    onStartLoginActivity(context);
                }
                break;
            case ConstantUtil.INSURANCE:
                if (isLoginState(context)){
                    onInsurance(context);
                }else {
                    onStartLoginActivity(context);
                }
                break;
            case ConstantUtil.GAS_METER:
                if (isLoginState(context)){
                    onGasMeter(context);
                }else {
                    onStartLoginActivity(context);
                }
                break;
            case ConstantUtil.GAS_PAY:
                if (isLoginState(context)){
                    onGasPay(context);
                }else {
                    onStartLoginActivity(context);
                }
                break;
            case ConstantUtil.GAS_IC_CARD:
                if (isLoginState(context)){
                    onIcCard(context);
                }else {
                    AppActivityUtil.onStartLoginActivity(context);
                }
                break;
            case ConstantUtil.HOUSEKEEPING_CLEAN:
                id=Uri.parse(url).getQueryParameter("id");
                onHouseKeepingClean(context,id,"家政保洁");
                break;
            case ConstantUtil.HOUSEHOLD_CLEAN:
                id=Uri.parse(url).getQueryParameter("id");
                onHouseHoldClean(context,id,"家电清洗");
                break;
            case ConstantUtil.HOME_MAINTENANCE:
                id=Uri.parse(url).getQueryParameter("id");
                onHomeMaintenance(context,id,"家居维修");
                break;
                default:
                    break;
        }
    }
    public static void startActivityCode(Context context,String code,String id,String name,String hasNext){
        if (context!=null){
            switch (code){
                case ConstantUtil.HOUSEHOLD_CLEAN:
                    if (hasNext.equals(ConstantUtil.VALUE_ONE)){
                        onHomeMaintenance(context,id,name);
                    }else {
                        onHouseHoldClean(context,id,name);
                    }
                    break;
                case ConstantUtil.HOUSEHOLD_REPAIR:
                    if (hasNext.equals(ConstantUtil.VALUE_ONE)){
                        onHomeMaintenance(context,id,name);
                    }else {
                        onHouseHoldRepair(context,id,name,hasNext);
                    }
                    break;
                case ConstantUtil.SANITARY_WARE:
                    if (hasNext.equals(ConstantUtil.VALUE_ONE)){
                        onHomeMaintenance(context,id,name);
                    }else {
                        onSanitaryWare(context,id,name,hasNext);
                    }
                    break;
                case ConstantUtil.LAMP_CIRCUIT:
                    if (hasNext.equals(ConstantUtil.VALUE_ONE)){
                        onHomeMaintenance(context,id,name);
                    }else {
                        onLampCircuit(context,id,name,hasNext);
                    }
                    break;
                case ConstantUtil.OTHER_REPAIR:
                    if (hasNext.equals(ConstantUtil.VALUE_ONE)){
                        onHomeMaintenance(context,id,name);
                    }else {
                        onOtherRepair(context,id,name,hasNext);
                    }
                    break;
                case ConstantUtil.GAS_SERVE:
                    onGasServe(context,id,name);
                    break;
                case ConstantUtil.ELECTRIC_VEHICLE_REPAIR:
                    onElectricVehicleRepair(context);
                    break;
                case ConstantUtil.INSURANCE:
                    if (isLoginState(context)){
                        onInsurance(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.ALL_SERVICE:
                    if (isLoginState(context)){
                        onAllService(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.INTELLIGENT_FARM:
                    if (isLoginState(context)){
                        onIntelligentFarm(context);
                    }else {
                       onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.DRINKING_WATER:
                    if (isLoginState(context)){
                        onDrinkingWater(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.VEGETABLE_SCXS:
                    if (isLoginState(context)){
                        onVegetableModel(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.HOUSEKEEPING_CLEAN:
                    if (hasNext.equals(ConstantUtil.VALUE_ONE)){
                        onHomeMaintenance(context,id,name);
                    }else {
                        onHouseKeepingClean(context,id,name);
                    }
                    break;
                case ConstantUtil.HOME_MAINTENANCE:
                    onHomeMaintenance(context,id,name);
                    break;
                case ConstantUtil.SHOP:
                    onShopMall(context);
                    break;
                case ConstantUtil.GAS_PAY:
                    if (isLoginState(context)){
                        onGasPay(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.GAS_METER:
                    if (isLoginState(context)){
                        onGasMeter(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.GAS_IC_CARD:
                    if (isLoginState(context)){
                        onIcCard(context);
                    }else {
                        AppActivityUtil.onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.LPG_NAME:
                    if (isLoginState(context)){
                        onLpgService(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                default:
                    showNotify(context,"民生宝" ,"已推出新版本，如果您想使用该服务，请点击更新！");
                    break;

            }
        }
    }
    public static void startActivityTopCode(Context context,String code,String id,String name){
        if (context!=null){
            switch (code){
                case ConstantUtil.SHOP:
                    onShopMall(context);
                    break;
                case ConstantUtil.GAS_PAY:
                    if (isLoginState(context)){
                        onGasPay(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.GAS_METER:
                    if (isLoginState(context)){
                        onGasMeter(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.GAS_IC_CARD:
                    if (isLoginState(context)){
                        onIcCard(context);
                    }else {
                        AppActivityUtil.onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.LPG_NAME:
                    if (isLoginState(context)){
                        onLpgService(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.GAS_REPAIR:
                    if (isLoginState(context)){
                        onGasRepair(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.GAS_INSTALL:
                    if (isLoginState(context)){
                        onGasInstall(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.GAS_RESCUE:
                    if (isLoginState(context)){
                        onGasRescue(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.GAS_INTRODUCE:
                    if (isLoginState(context)){
                        onGasIntroduce(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                case ConstantUtil.GAS_SERVE:
                    onGasServe(context,id,name);
                    break;
                case ConstantUtil.DRINKING_WATER:
                    if (isLoginState(context)){
                        onDrinkingWater(context);
                    }else {
                        onStartLoginActivity(context);
                    }
                    break;
                default:
                    showNotify(context,"民生宝" ,"已推出新版本，如果您想使用该服务，请点击更新！");
                    break;
            }
        }
    }
    public static void onAllServiceStartActivity(Context context ,String mainCode,String code,String id,String name,String hasNext){
        if (context!=null){
            switch (mainCode){
                case ConstantUtil.GAS_SERVE:
                    startActivityTopCode(context,code,id,name);
                    break;
                case ConstantUtil.REPAIR:
                    startActivityCode(context,code,id,name,hasNext);
                    break;
                case ConstantUtil.CLEAN:
                    startActivityCode(context,code,id,name,hasNext);
                    break;
                case ConstantUtil.CONVENIENCE_SERVICE:
                    startActivityCode(context,code,id,name,hasNext);
                    break;
                case ConstantUtil.SHOP:
                    startActivityShop(context,code);
                    break;
                    default:
                        startActivityCode(context,code,id,name,hasNext);
                        break;
            }
        }
    }
    private static void startActivityShop(Context context,String code){
        if (context!=null){
            switch (code){
                case ConstantUtil.SHOP_ELECTRIC_WATER_HEATER:
                    onShopMallPage(context,UrlUtil.SHOP_HEATERCALORIFIER);
                    break;
                case ConstantUtil.SHOP_LAMPBLACK_MACHINE:
                    onShopMallPage(context,UrlUtil.SHOP_LAMPBLACK);
                    break;
                case ConstantUtil.SHOP_GAS_STOVE:
                    onShopMallPage(context,UrlUtil.SHOP_GAS_STOVE);
                    break;
                case ConstantUtil.SHOP_WATER_HEATER:
                    onShopMallPage(context,UrlUtil.SHOP_GAS_HEATER);
                    break;
                case ConstantUtil.SHOP_IMPORTED_RED_WINE:
                    onShopMallPage(context,UrlUtil.SHOP_IMPORT_FOODSTUFF);
                    break;
                case ConstantUtil.SHOP_STERILIZER:
                    onShopMallPage(context,UrlUtil.SHOP_DISINFECTIONCABINET);
                    break;
                    default:
                        onShopMall(context);
                        break;
            }
        }
    }
    private static void onShopMallPage(Context context, String url) {
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("first",1);
        context.startActivity(intent);
    }

    private static void onGasIntroduce(Context context) {
        Intent intent=new Intent(context,GasIntroduceActivity.class);
        context.startActivity(intent);
    }
    private static void onGasRescue(Context context) {
        Intent intent=new Intent(context,GasEmergencyRescueActivity.class);
        context.startActivity(intent);
    }

    private static void onGasInstall(Context context) {
        Intent intent=new Intent(context,GasInstallActivity.class);
        context.startActivity(intent);
    }

    private static void onGasRepair(Context context) {
        Intent intent=new Intent(context,GasRepairActivity.class);
        context.startActivity(intent);
    }
    private static void onShopMall(Context context) {
        Intent intent=new Intent(context, ShopActivity.class);
        context.startActivity(intent);
    }
    private static void onGasPay(Context context) {
        Intent intent=new Intent(context,GasPayFeeActivity.class);
        context.startActivity(intent);
    }

    private static void onGasMeter(Context context) {
        Intent intent=new Intent(context,GasWriteTableActivity.class);
        context.startActivity(intent);
    }
    private static void onIcCard(Context context) {
        Intent card=new Intent(context,GasIcCardActivity.class);
        context.startActivity(card);
    }
    private static void onLpgService(Context context) {
        Intent intent=new Intent(context, LpgMyAccountActivity.class);
        context.startActivity(intent);
    }
    public static void showNotify(Context context, String title, String s) {
        if (context!=null){
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
    private static void onHouseHoldClean(Context context,String id, String name) {
        Intent intent=new Intent(context, HomeApplianceCleanActivity.class);
        intent.putExtra("pid",id);
        intent.putExtra("typeName",name);
        context.startActivity(intent);
    }
    private static void onHouseHoldRepair(Context context, String id, String name, String hasNext) {
        Intent intent=new Intent(context, HouseApplianceFixActivity.class);
        intent.putExtra("pid",id);
        intent.putExtra("typeName",name);
        context.startActivity(intent);
    }
    private static void onSanitaryWare(Context context, String id, String name, String hasNext) {
        Intent intent=new Intent(context, SanitaryWareActivity.class);
        intent.putExtra("pid",id);
        intent.putExtra("typeName",name);
        context.startActivity(intent);
    }
    private static void onLampCircuit(Context context, String id, String name,String hasNext) {
        Intent intent=new Intent(context,LampCircuitActivity.class);
        intent.putExtra("pid",id);
        intent.putExtra("typeName",name);
        context.startActivity(intent);
    }
    private static void onOtherRepair(Context context, String id, String name, String hasNext) {
        Intent intent=new Intent(context,OtherRepairActivity.class);
        intent.putExtra("pid",id);
        intent.putExtra("typeName",name);
        context.startActivity(intent);
    }
    private static void onGasServe(Context context, String id, String name) {
        Intent intent=new Intent(context,GasServiceActivity.class);
        intent.putExtra("pid",id);
        intent.putExtra("name",name);
        context.startActivity(intent);
    }
    private static void onElectricVehicleRepair(Context context) {
        Intent intent=new Intent(context, ElectricHomeActivity.class);
        context.startActivity(intent);
    }
    private static void onInsurance(Context context) {
        Intent intent=new Intent(context, InsuranceHome.class);
        context.startActivity(intent);
    }
    private static void onAllService(Context context) {
        Intent serve=new Intent(context,AllServiceActivity.class);
        context.startActivity(serve);
    }
    private static void onIntelligentFarm(Context context) {
        String url=UrlUtil.Intelligent_FarmUrl;
        Intent intent=new Intent(context, IntelligentFarmHmlActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","智慧农贸");
        context.startActivity(intent);
    }
    private static void onDrinkingWater(Context context) {
        Intent serve=new Intent(context,WaterMainActivity.class);
        context.startActivity(serve);
    }
    private static void onVegetableModel(Context context) {
        Intent intent=new Intent(context, VegetableGentlemenActivity.class);
        context.startActivity(intent);
    }
    private static void onHouseKeepingClean(Context context, String id, String name) {

        Intent intent=new Intent(context, HouseKeepingActivity.class);
        intent.putExtra("pid",id);
        intent.putExtra("typeName",name);
        context.startActivity(intent);
    }
    private static void onHomeMaintenance(Context context,  String id, String name) {
        Intent intent=new Intent(context, HomeMaintenanceActivity.class);
        intent.putExtra("pid",id);
        intent.putExtra("typeName",name);
        context.startActivity(intent);
    }
    private static void onStartHtmlActivity(Context context, String url, String title, String share,String desc,String activityCode,String backUrl) {
        Intent intent=new Intent(context, HtmlPageActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","民生宝");
        intent.putExtra("title",title);
        intent.putExtra("share",share);
        intent.putExtra("desc",desc);
        intent.putExtra("activityCode",activityCode);
        intent.putExtra("backUrl",backUrl);
        context.startActivity(intent);
    }
    public static void onStartLoginActivity(Context context) {
        Intent login=new Intent(context, LoginActivity.class);
        context.startActivity(login);
    }
    private static void onStartVegetableActivity(Context context, String url) {
        Intent intent=new Intent(context, VegetableGentlemenActivity.class);
        context.startActivity(intent);
    }

    public static boolean isLoginState(Context mContext){
        return SharedPreferencesUtil.getLstate(mContext, SharedPreferencesUtil.Lstate, false);
    }
}
