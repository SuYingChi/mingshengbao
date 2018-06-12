package com.msht.minshengbao.Utils;

import com.msht.minshengbao.BuildConfig;
/**
 * Demo class
 * 接口工具
 * @author hong
 * @date 2016/11/15
 */
public class UrlUtil {
    /**
     * 测试环境
     * public static final String URL_HEADS ="http://test.msbapp.cn";
     * public static final String URL_HEADS="http://test.msbapp.cn:8080";
     */
    /**
     * 线上环境
     * public static final String URL_HEADS="https://msbapp.cn";
     */
    /**
     * 电动车域名
      */
    public static final String URL_Repair="http://repairshop.msbapp.cn:17080";
    /**
     * LAUNCHER_IMG_URL 启动页广告图链接
     */
    public static final  String LAUNCHER_IMG_URL= BuildConfig.URL_HEADS+"/api/app/app_start_page?type=2";
    /**
     * 支付确认
     */
    public static final String PAY_RESULT_NOTARIZE =BuildConfig.URL_HEADS+"/Gas/payment/queryOrder";
    //电动车

    public static final String ELECTRIC_LIST_URL =URL_Repair+"/repairshop/shop/searchByLocate";
    public static final String STORE_DETAIL =URL_Repair+"/repairshop/shop/get";
    public static final String SEARCH_HISTORY =URL_Repair+"/repairshop/searchHistory/list";
    public static final String CLEAR_HISTORY =URL_Repair+"/repairshop/searchHistory/clear";
    /**
     * 水宝
     */
    public static final String PAY_METHOD_URL =BuildConfig.URL_HEADS+"/api/app/pay_method";
    public static final String SearchEstate_Url=BuildConfig.URL_MSSB+"/community/search";
    public static final String SCAN_CODE_BUY=BuildConfig.URL_MSSB+"/order/appScanNotice";
    public static final String WaterAccount_Url=BuildConfig.URL_MSSB+"/member/get";
    public static final String WaterCard_Recharge=BuildConfig.URL_MSSB+"/order/recharge";
    public static final String WaterCard_PayUrl=BuildConfig.URL_MSSB+"/order/payNotice";
    public static final String WaterOrder_ListUrl=BuildConfig.URL_MSSB+"/order/list";
    public static final String WaterRecharge_Meal=BuildConfig.URL_MSSB+"/pack/list";
    public static final String WaterPrice_Url=BuildConfig.URL_MSSB+"/order/getPrice";
    public static final String WaterCreate_Url=BuildConfig.URL_MSSB+"/order/create";
    public static final String WaterCancel_Url=BuildConfig.URL_MSSB+"/order/oper";
    /**
     * 商城
     */
    public static final String Shop_LoginHtml=BuildConfig.URL_SHOP+"/wap/tmpl/member/login.html";
    public static final String Shop_HomeUrl= BuildConfig.URL_SHOP +"/wap/index.html";
    public static final String Shop_Login= BuildConfig.URL_SHOP +"/mobile/index.php?act=login&op=appindex";
    public static final String Shop_IMPORT_FOODSTUFF=BuildConfig.URL_SHOP +"/wap/tmpl/class_list.html?gc_id=1246";
    public static final String Shop_Disinfectioncabinet=BuildConfig.URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1059";
    public static final String Shop_Heatercalorifier=BuildConfig.URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1060";
    public static final String Shop_Lampblack=BuildConfig.URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1063";
    public static final String Shop_Gasstove=BuildConfig.URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1058";
    public static final String Shop_GasHeater=BuildConfig.URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1057";
    public static final String Shop_OrderList=BuildConfig.URL_SHOP+"/wap/tmpl/member/order_list.html";
    /**
     * 首页功能
     */
    public static final String App_versionUrl= BuildConfig.URL_HEADS +"/Gas/app/version";
    public static final String AllServeCatalog_Url=BuildConfig.URL_HEADS+"/api/serve_city/user_app_all_serve";
    public static final String Function_Url= BuildConfig.URL_HEADS +"/api/serve_city/user_app_serve";
    public static final String HomeFunction_Url=BuildConfig.URL_HEADS+"/api/serve_city/user_app_homepage";
    public static final String Weather_QueryUrl= BuildConfig.URL_HEADS +"/pis/weather/query";
    public static final String Inform_Url= BuildConfig.URL_HEADS +"/Gas/message/list";
    public static final String Inform_delect= BuildConfig.URL_HEADS +"/Gas/message/delete";
    public static final String Inform_detail= BuildConfig.URL_HEADS +"/Gas/message/detail";
    public static final String Message_unreadUrl= BuildConfig.URL_HEADS +"/Gas/message/unRead";
    public static final String pushDeviceToken= BuildConfig.URL_HEADS +"/Gas/user/pushDeviceToken";
    public static final String Userinfo_GasUrl= BuildConfig.URL_HEADS +"/Gas/user/info";
    public static final String Read_searchUserhouse=BuildConfig. URL_HEADS +"/Gas/usedHouse/search";
    public static final String Pre_deposit_history= BuildConfig.URL_HEADS +"/Gas/payment/pre_deposit_history";
    public static final String SpecialTopic_Url=BuildConfig.URL_HEADS+"/api/app/specail_topic_activity";
    /**
     * 燃气服务模块
     */
    public static final String InstallServer_Url=BuildConfig. URL_HEADS +"/Gas/workOrder/add";
    public static final String InstallType_Url= BuildConfig.URL_HEADS +"/Gas/app/gas_install_type";
    public static final String GetTable_Url= BuildConfig.URL_HEADS +"/Gas/meter/rqb";
    public static final String GasTable_data= BuildConfig.URL_HEADS +"/Gas/meter/list";
    public static final String SendTable_dataUrl= BuildConfig.URL_HEADS +"/Gas/meter/add";
    public static final String Searchbill_GasUrl= BuildConfig.URL_HEADS +"/Gas/debts/search";
    public static final String SelectAddress_Url= BuildConfig.URL_HEADS +"/Gas/usedHouse/list";
    public static final String CreateOrder_Gas=BuildConfig.URL_HEADS +"/Gas/gas_fee_pay/createOrder";
    public static final String GasExpense_Pay=BuildConfig.URL_HEADS +"/Gas/gas_fee_pay/pay";
    /**
     * ic卡
     */
    public static final String IcRecharge_BillUrl=BuildConfig.URL_HEADS+"/Gas/payment/icRecharge";
    public static final String IcRechargeSearch_Url=BuildConfig.URL_HEADS+"/Gas/payment/icRechargeSearch";
    public static final String IcRechargeHistory_Url=BuildConfig.URL_HEADS+"/Gas/payment/icRechargeHistory";
    //抢险
    public static final String GasQiangxian_Url= BuildConfig.URL_HEADS +"/Gas/app/rescue";
    /**
     * HTML5Web
      */
    public static final String Intelligent_FarmUrl="http://msbapp.cn/wyh/index.html";
    public static final String Companyprofile_Url=BuildConfig. URL_HEADS +"/Gas/xuzhi/company";
    public static final String GasJiaoNa_Url= BuildConfig.URL_HEADS +"/Gas/xuzhi/gas_payment";
    public static final String Gasprice_Url= BuildConfig.URL_HEADS +"/gas_h5/qijiashuoming.html";
    public static final String GasSafety_Url= BuildConfig.URL_HEADS +"/Gas/xuzhi/safe";
    public static final String GasToolUse_Url= BuildConfig.URL_HEADS +"/Gas/xuzhi/gas_appliance";
    public static final String GasYeWu_Url= BuildConfig.URL_HEADS +"/Gas/xuzhi/business_guid";
    public static final String uninstall_Url= BuildConfig.URL_HEADS +"/Gas/xuzhi/uninstall";
    public static final String ServicePromise_Url= BuildConfig.URL_HEADS +"/Gas/xuzhi/service_promise";
    public static final String Guitai_Url= BuildConfig.URL_HEADS +"/Gas/guitai/guitai";
    public static final String YingyeSite_Url= BuildConfig.URL_HEADS +"/Gas/guitai/wangdian";
    /**
     * 缴费
     */
    public static final String PaySubtract_Url=BuildConfig.URL_HEADS+"/msht_operation_platform/activity/get_event_activity";
    public static final String AgreeTreayt_Url= BuildConfig.URL_HEADS +"/repair_h5/regist_agreement.html";
    public static final String AutomataPay_Url= BuildConfig.URL_HEADS +"/Gas/gasWithhold/list";
    public static final String Addautomate_AddUrl= BuildConfig.URL_HEADS +"/Gas/gasWithhold/add";
    public static final String DelectAutopay_AddUrl= BuildConfig.URL_HEADS +"/Gas/gasWithhold/delete";
    public static final String Mywallet_balanceUrl= BuildConfig.URL_HEADS +"/Gas/wallet/balance";
    public static final String MyIncome_ExpenseUrl= BuildConfig.URL_HEADS +"/Gas/wallet/history";
    public static final String PayfeeWay_Url= BuildConfig.URL_HEADS +"/Gas/payment/createOrder";
    public static final String PayRecors_HistoryUrl= BuildConfig.URL_HEADS +"/Gas/payment/customerno_history";
    public static final String PayCustomerNo_Url= BuildConfig.URL_HEADS +"/Gas/payment/app_pay_customerno";
    public static final String Voucher_CanuseUrl= BuildConfig.URL_HEADS +"/Gas/coupon/can_use_list";
    /**
     * 城市服务
     */
    public static final String SelectCity_Url= BuildConfig.URL_HEADS +"/api/serve_city/list";
    public static final String BrulesInfoma_Url= BuildConfig.URL_HEADS +"/pis/peccancy/query";
    public static final String GdetailInfo_Url= BuildConfig.URL_HEADS +"/pis/info/view";
    /**
     * Fragment
     */
    public static final String Evalute_UrL= BuildConfig.URL_HEADS +"/Gas/repairman/evaluate_list";
    public static final String maintainservise_Url= BuildConfig.URL_HEADS +"/Gas/repairOrder/list";
    public static final String AllMyservice_Url= BuildConfig.URL_HEADS +"/Gas/workOrder/userWorkOrder";
    public static final String CityDynamic_Url= BuildConfig.URL_HEADS +"/pis/info/list";
    public static final String Counpon_Url= BuildConfig.URL_HEADS +"/Gas/coupon/list";
    public static final String imgavatar_Url= BuildConfig.URL_HEADS +"/Gas/app/headerImg";
    public static final String PayRecord_Url= BuildConfig.URL_HEADS +"/Gas/payment/list";
    /**
     *  保险
    */
    public static final String Insurance_buy_Url=BuildConfig.URL_HEADS +"/Gas/insurance/create_order";
    public static final String Insurance_history_Url= BuildConfig.URL_HEADS +"/Gas/invoice/history";
    public static final String GethouseAddress_Url="http://220.174.234.36:8090/GasAPI/house/customerNoHouse";
    public static final String INSURANCE_PAY_URL=BuildConfig.URL_HEADS+"/Gas/views/insurance/pay.html";
    /**
     *myView
     */
    public static final String ADD_ADDRESS_URL = BuildConfig.URL_HEADS +"/Gas/usedHouse/add";
    public static final String HouseSearch_Url= BuildConfig.URL_HEADS +"/Gas/usedHouse/search";
    public static final String Address_delectUrl= BuildConfig.URL_HEADS +"/Gas/usedHouse/delete";
    public static final String Evalute_workUrl= BuildConfig.URL_HEADS +"/Gas/workOrder/eval";
    public static final String FeedbackIdea_Url= BuildConfig.URL_HEADS +"/Gas/user/feedback";
    public static final String Captcha_CodeUrl= BuildConfig.URL_HEADS +"/Gas/user/captcha";
    public static final String Resetpwd_Url= BuildConfig.URL_HEADS +"/Gas/user/passwRecover";
    public static final String Login_Url= BuildConfig.URL_HEADS +"/Gas/user/login";
    public static final String Servirce_detailUrl= BuildConfig.URL_HEADS +"/Gas/workOrder/view";
    public static final String GasCancel_workUrl= BuildConfig.URL_HEADS +"/Gas/workOrder/cancel";
    public static final String GasmodifyInfo_Url=BuildConfig.URL_HEADS +"/Gas/user/modifyInfo";
    public static final String Register_Url=BuildConfig.URL_HEADS +"/Gas/user/register";
    public static final String Shara_appUrl=BuildConfig.URL_HEADS +"/Gas/coupon/share_add";
    public static final String BoundCustomerNo_URL= BuildConfig.URL_HEADS +"/Gas/usedHouse/bind";
    public static final String AddressManage_Url= BuildConfig.URL_HEADS +"/Gas/address/list";
    public static final String AddAddress_Url=BuildConfig.URL_HEADS +"/Gas/address/add";
    public static final String ModifyAddress_Url=BuildConfig.URL_HEADS+"/Gas/address/modify";
    public static final String NewAddAddress_Url=BuildConfig.URL_HEADS+"/Gas/address/add_with_user_info";
    public static final String NewModifyAddress_Url=BuildConfig.URL_HEADS+"/Gas/address/modify_with_user_info";
    public static final String DelectAddress_Url=BuildConfig.URL_HEADS +"/Gas/address/delete";
    public static final String SetDefaultAddr_Url=BuildConfig.URL_HEADS +"/Gas/address/set_default_address";
    public static final String SetPassword_Url=BuildConfig.URL_HEADS+"/Gas/user/modifyPassword";
    /**
     *发票
     */
    public static final String Invoice_explain= BuildConfig.URL_HEADS +"/repair_h5/invoice_note.html";
    public static final String Invoice_applyUrl=BuildConfig. URL_HEADS +"/Gas/invoice/apply";
    public static final String Invoice_getUrl= BuildConfig.URL_HEADS +"/Gas/invoice/uninvoce_list";
    /**
     * 维修服务
    */
    public static final String Service_noteUrl= BuildConfig.URL_HEADS +"/repair_h5/service_note.html";
    public static final String RepairOrder_detailUrl= BuildConfig.URL_HEADS +"/Gas/repairOrder/view";
    public static final String RepairOrder_cancelUrl= BuildConfig.URL_HEADS +"/Gas/repairOrder/cancel";
    public static final String MasterDetail_Url= BuildConfig.URL_HEADS +"/Gas/repairman/detail";
    public static final String PublishOrder_Url= BuildConfig.URL_HEADS +"/Gas/repairOrder/add";
    public static final String UploadImage_Url= BuildConfig.URL_HEADS +"/Gas/repairOrder/addImage";
    public static final String RepairOrder_EvalUrl= BuildConfig.URL_HEADS +"/Gas/repairOrder/eval";
    public static final String RepairOrder_QuestionUrl= BuildConfig.URL_HEADS +"/Gas/repairCategoryProblem/problem_list";
    public static final String RepairMater_countUrl= BuildConfig.URL_HEADS +"/Gas/repairman/evaluate_status_count";
    public static final String SecondService_Url= BuildConfig.URL_HEADS +"/api/serve_city/user_app_sub_serve";
    public static final String LookEvalute_Url= BuildConfig.URL_HEADS +"/Gas/repairOrder/eval_info";
    public static final String RefundApply_Url= BuildConfig.URL_HEADS +"/Gas/repairOrder/refund";
    public static final String RefundImg_Url= BuildConfig.URL_HEADS +"/Gas/repairOrder/refund_img";
    /**
     * 热门维修
     */
    public static final String HotRepair_Url= BuildConfig.URL_HEADS +"/api/serve_city/hot_repair";
    /**
     * HTML5页面
     */
    public static final String ApppaySuccess_Page=BuildConfig.URL_HEADS+"/Gas/app/app_pay_success_page?";
    public static final String INSURANCE_EXPLAIN_URL="http://msbapp.cn/insurance/toubaoshuoming.html";
    public static final String Recharge_BackAgree=BuildConfig.URL_HEADS+"/water_h5/chongfanxieyi.html";
    public static final String Replacepay_agreeUrl= BuildConfig.URL_HEADS +"/gas_h5/daikouxieyi.html";
    public static final String Vegetable_Url="http://jsxss.net/app/index.php?i=5&c=entry&m=ewei_shopv2&do=mobile";
}
