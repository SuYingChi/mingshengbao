package com.msht.minshengbao.Utils;

/**
 * Created by hong on 2016/11/15.
 */
public class UrlUtil {
   // public static final String URL_HEADS ="http://test.msbapp.cn";
    public static final String URL_HEADS="http://test.msbapp.cn:8080";
    //public static final String URL_HEAD="http://test.msbapp.cn";
  //  public static final String URL_SHOP="http://shop.msbapp.cn:8090";
    public static final String URL_SHOP="http://dev.msbapp.cn";
  //  public static final String URL_HEADS="https://msbapp.cn";
    public static final String URL_MSSB="http://120.25.195.173:17080/rwapi";
   // public static final String URL_MSSB="http://sb-api.msbapp.cn";
    public static final String URL_Repair="http://repairshop.msbapp.cn:17080";
    //启动页
    public static final  String Launcher_ImgUrl=URL_HEADS+"/api/app/app_start_page?type=2";
    //支付确认
    public static final String PayResult_Notarize=URL_HEADS+"/Gas/payment/queryOrder";
    //电动车
    public static final String ElectricList_Url=URL_Repair+"/repairshop/shop/searchByLocate";
    public static final String Store_Detail=URL_Repair+"/repairshop/shop/get";
    public static final String Search_History=URL_Repair+"/repairshop/searchHistory/list";
    public static final String Clear_History=URL_Repair+"/repairshop/searchHistory/clear";
    //水宝
    public static final String ScanCode_Buy=URL_MSSB+"/order/appScanNotice";
    public static final String WaterAccount_Url=URL_MSSB+"/member/get";
    public static final String WaterCard_Recharge=URL_MSSB+"/order/recharge";
    public static final String WaterCard_PayUrl=URL_MSSB+"/order/payNotice";
    public static final String WaterOrder_ListUrl=URL_MSSB+"/order/list";
    public static final String WaterRecharge_Meal=URL_MSSB+"/pack/list";
    public static final String SearchEstate_Url=URL_MSSB+"/community/search";
    public static final String WaterPrice_Url=URL_MSSB+"/order/getPrice";
    public static final String WaterCreate_Url=URL_MSSB+"/order/create";
    public static final String WaterCancel_Url=URL_MSSB+"/order/oper";
    public static final String Paymethod_Url=URL_HEADS+"/api/app/pay_method";
    //商城
    public static final String Shop_LoginHtml=URL_SHOP+"/wap/tmpl/member/login.html";
    public static final String Shop_HomeUrl= URL_SHOP +"/wap/index.html";
    public static final String Shop_Login= URL_SHOP +"/mobile/index.php?act=login&op=appindex";
    public static final String Shop_Redwine=URL_SHOP +"/wap/tmpl/class_list.html?gc_id=1246";
    public static final String Shop_Disinfectioncabinet=URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1059";
    public static final String Shop_Heatercalorifier=URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1060";
    public static final String Shop_Lampblack=URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1063";
    public static final String Shop_Gasstove=URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1058";
    public static final String Shop_GasHeater=URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1057";
    public static final String Shop_OrderList=URL_SHOP+"/wap/tmpl/member/order_list.html";
    /*首页功能 */
    public static final String App_versionUrl= URL_HEADS +"/Gas/app/version";
    public static final String AllServeCatalog_Url=URL_HEADS+"/api/serve_city/user_app_all_serve";
    public static final String Function_Url= URL_HEADS +"/api/serve_city/user_app_serve";
    public static final String HomeFunction_Url=URL_HEADS+"/api/serve_city/user_app_homepage";
    public static final String Weather_QueryUrl= URL_HEADS +"/pis/weather/query";
    public static final String Inform_Url= URL_HEADS +"/Gas/message/list";
    public static final String Inform_delect= URL_HEADS +"/Gas/message/delete";
    public static final String Inform_detail= URL_HEADS +"/Gas/message/detail";
    public static final String Message_unreadUrl= URL_HEADS +"/Gas/message/unRead";
    public static final String pushDeviceToken= URL_HEADS +"/Gas/user/pushDeviceToken";
    public static final String Userinfo_GasUrl= URL_HEADS +"/Gas/user/info";
    public static final String Read_searchUserhouse= URL_HEADS +"/Gas/usedHouse/search";
    public static final String Pre_deposit_history= URL_HEADS +"/Gas/payment/pre_deposit_history";
    public static final String SpecialTopic_Url=URL_HEADS+"/api/app/specail_topic_activity";

    /*燃气服务模块 */
    //自助抄表
    public static final String InstallServer_Url= URL_HEADS +"/Gas/workOrder/add";
    public static final String InstallType_Url= URL_HEADS +"/Gas/app/gas_install_type";
    public static final String GetTable_Url= URL_HEADS +"/Gas/meter/rqb";
    public static final String GasTable_data= URL_HEADS +"/Gas/meter/list";
    public static final String SendTable_dataUrl= URL_HEADS +"/Gas/meter/add";
    public static final String Searchbill_GasUrl= URL_HEADS +"/Gas/debts/search";
    public static final String SelectAddress_Url= URL_HEADS +"/Gas/usedHouse/list";
    public static final String CreateOrder_Gas=URL_HEADS +"/Gas/gas_fee_pay/createOrder";
    public static final String GasExpense_Pay=URL_HEADS +"/Gas/gas_fee_pay/pay";
    //ic卡
    public static final String IcRecharge_BillUrl=URL_HEADS+"/Gas/payment/icRecharge";
    public static final String IcRechargeSearch_Url=URL_HEADS+"/Gas/payment/icRechargeSearch";
    public static final String IcRechargeHistory_Url=URL_HEADS+"/Gas/payment/icRechargeHistory";
    //抢险
    public static final String GasQiangxian_Url= URL_HEADS +"/Gas/app/rescue";
    /*HTML5Web*/
    //智慧农贸
    public static final String Intelligent_FarmUrl="http://msbapp.cn/wyh/index.html";

    //燃气须知
    public static final String Companyprofile_Url= URL_HEADS +"/Gas/xuzhi/company";
    public static final String GasJiaoNa_Url= URL_HEADS +"/Gas/xuzhi/gas_payment";
    public static final String Gasprice_Url= URL_HEADS +"/gas_h5/qijiashuoming.html";
    public static final String GasSafety_Url= URL_HEADS +"/Gas/xuzhi/safe";
    public static final String GasToolUse_Url= URL_HEADS +"/Gas/xuzhi/gas_appliance";
    public static final String GasYeWu_Url= URL_HEADS +"/Gas/xuzhi/business_guid";
    public static final String uninstall_Url= URL_HEADS +"/Gas/xuzhi/uninstall";
    public static final String ServicePromise_Url= URL_HEADS +"/Gas/xuzhi/service_promise";
 //柜台业务
    public static final String Guitai_Url= URL_HEADS +"/Gas/guitai/guitai";
    public static final String YingyeSite_Url= URL_HEADS +"/Gas/guitai/wangdian";
    //缴费
    public static final String PaySubtract_Url=URL_HEADS+"/msht_operation_platform/activity/get_event_activity";
    public static final String AgreeTreayt_Url= URL_HEADS +"/repair_h5/regist_agreement.html";
    public static final String AutomataPay_Url= URL_HEADS +"/Gas/gasWithhold/list";
    public static final String Addautomate_AddUrl= URL_HEADS +"/Gas/gasWithhold/add";
    public static final String DelectAutopay_AddUrl= URL_HEADS +"/Gas/gasWithhold/delete";
    public static final String Mywallet_balanceUrl= URL_HEADS +"/Gas/wallet/balance";
    public static final String MyIncome_ExpenseUrl= URL_HEADS +"/Gas/wallet/history";
    public static final String PayfeeWay_Url= URL_HEADS +"/Gas/payment/createOrder";
    public static final String PayRecors_HistoryUrl= URL_HEADS +"/Gas/payment/customerno_history";
    public static final String PayCustomerNo_Url= URL_HEADS +"/Gas/payment/app_pay_customerno";
    public static final String Voucher_CanuseUrl= URL_HEADS +"/Gas/coupon/can_use_list";
    /*城市服务 */
    public static final String SelectCity_Url= URL_HEADS +"/api/serve_city/list";
    //违章查询

    public static final String BrulesInfoma_Url= URL_HEADS +"/pis/peccancy/query";
    public static final String GdetailInfo_Url= URL_HEADS +"/pis/info/view";

    //Fragment
    public static final String Evalute_UrL= URL_HEADS +"/Gas/repairman/evaluate_list";
    public static final String maintainservise_Url= URL_HEADS +"/Gas/repairOrder/list";
    public static final String AllMyservice_Url= URL_HEADS +"/Gas/workOrder/userWorkOrder";
    public static final String CityDynamic_Url= URL_HEADS +"/pis/info/list";
    public static final String Counpon_Url= URL_HEADS +"/Gas/coupon/list";
    public static final String imgavatar_Url= URL_HEADS +"/Gas/app/headerImg";
    public static final String PayRecord_Url= URL_HEADS +"/Gas/payment/list";

    /*保险*/
    public static final String Insurance_buy_Url= URL_HEADS +"/Gas/insurance/create_order";
    public static final String Insurance_history_Url= URL_HEADS +"/Gas/invoice/history";
    public static final String GethouseAddress_Url="http://220.174.234.36:8090/GasAPI/house/customerNoHouse";
    public static final String INSURANCE_PAY_URL=URL_HEADS+"/Gas/views/insurance/pay.html";
    /*myView   */
    public static final String AddAdress_Url= URL_HEADS +"/Gas/usedHouse/add";
    public static final String HouseSearch_Url= URL_HEADS +"/Gas/usedHouse/search";
    public static final String Address_delectUrl= URL_HEADS +"/Gas/usedHouse/delete";
    public static final String Evalute_workUrl= URL_HEADS +"/Gas/workOrder/eval";
    public static final String FeedbackIdea_Url= URL_HEADS +"/Gas/user/feedback";
    public static final String Captcha_CodeUrl= URL_HEADS +"/Gas/user/captcha";
    public static final String Resetpwd_Url= URL_HEADS +"/Gas/user/passwRecover";
    public static final String Login_Url= URL_HEADS +"/Gas/user/login";
    public static final String Servirce_detailUrl= URL_HEADS +"/Gas/workOrder/view";
    public static final String GasCancel_workUrl= URL_HEADS +"/Gas/workOrder/cancel";
    public static final String GasmodifyInfo_Url= URL_HEADS +"/Gas/user/modifyInfo";
    public static final String Register_Url=URL_HEADS +"/Gas/user/register";
    public static final String Shara_appUrl=URL_HEADS +"/Gas/coupon/share_add";
    public static final String BoundCustomerNo_URL= URL_HEADS +"/Gas/usedHouse/bind";
    public static final String AddressManage_Url= URL_HEADS +"/Gas/address/list";
    public static final String AddAddress_Url=URL_HEADS +"/Gas/address/add";
    public static final String ModifyAddress_Url=URL_HEADS+"/Gas/address/modify";
    public static final String NewAddAddress_Url=URL_HEADS+"/Gas/address/add_with_user_info";
    public static final String NewModifyAddress_Url=URL_HEADS+"/Gas/address/modify_with_user_info";
    public static final String DelectAddress_Url=URL_HEADS +"/Gas/address/delete";
    public static final String SetDefaultAddr_Url=URL_HEADS +"/Gas/address/set_default_address";
    public static final String SetPassword_Url=URL_HEADS+"/Gas/user/modifyPassword";
    /*发票*/
    public static final String Invoice_explain= URL_HEADS +"/repair_h5/invoice_note.html";
    public static final String Invoice_applyUrl= URL_HEADS +"/Gas/invoice/apply";
    public static final String Invoice_getUrl= URL_HEADS +"/Gas/invoice/uninvoce_list";//获取未开发票列表
    /*维修服务*/
    public static final String Service_noteUrl= URL_HEADS +"/repair_h5/service_note.html";
    public static final String RepairOrder_detailUrl= URL_HEADS +"/Gas/repairOrder/view";
    public static final String RepairOrder_cancelUrl= URL_HEADS +"/Gas/repairOrder/cancel";
    public static final String MasterDetail_Url= URL_HEADS +"/Gas/repairman/detail";
    public static final String PublishOrder_Url= URL_HEADS +"/Gas/repairOrder/add";
    public static final String UploadImage_Url= URL_HEADS +"/Gas/repairOrder/addImage";
    public static final String RepairOrder_EvalUrl= URL_HEADS +"/Gas/repairOrder/eval";
    public static final String RepairOrder_QuestionUrl= URL_HEADS +"/Gas/repairCategoryProblem/problem_list";
    public static final String RepairMater_countUrl= URL_HEADS +"/Gas/repairman/evaluate_status_count";
    public static final String SecondService_Url= URL_HEADS +"/api/serve_city/user_app_sub_serve";
    public static final String LookEvalute_Url= URL_HEADS +"/Gas/repairOrder/eval_info";
    public static final String RefundApply_Url= URL_HEADS +"/Gas/repairOrder/refund";
    public static final String RefundImg_Url= URL_HEADS +"/Gas/repairOrder/refund_img";
    /*热门维修 */
    public static final String HotRepair_Url= URL_HEADS +"/api/serve_city/hot_repair";
    //HTML5页面
    public static final String ApppaySuccess_Page=URL_HEADS+"/Gas/app/app_pay_success_page?";
    public static final String Recharge_BackAgree=URL_HEADS+"/water_h5/chongfanxieyi.html";
    public static final String Replacepay_agreeUrl= URL_HEADS +"/gas_h5/daikouxieyi.html";
    public static final String Vegetable_Url="http://jsxss.net/app/index.php?i=5&c=entry&m=ewei_shopv2&do=mobile";
}
