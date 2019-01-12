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
     * public static final String URL_HEADS=BuildConfig.URL_HEADS;
     */
    /**
     * 线上环境
     * public static final String URL_HEADS="https://msbapp.cn";
     */
    /**
     * 电动车域名
      */
    public static final String URL_REPAIR ="http://repairshop.msbapp.cn:17080";
    /**
     * LAUNCHER_IMG_URL 启动页广告图链接
     */
    public static final  String LAUNCHER_IMG_URL= BuildConfig.URL_HEADS+"/api/app/app_start_page?type=2";
    /**
     * 支付确认
     */
    public static final String PAY_RESULT_NOTARIZE =BuildConfig.URL_HEADS+"/Gas/payment/queryOrder";
    //电动车

    public static final String ELECTRIC_LIST_URL = URL_REPAIR +"/repairshop/shop/searchByLocate";
    public static final String STORE_DETAIL = URL_REPAIR +"/repairshop/shop/get";
    public static final String SEARCH_HISTORY = URL_REPAIR +"/repairshop/searchHistory/list";
    public static final String CLEAR_HISTORY = URL_REPAIR +"/repairshop/searchHistory/clear";
    /**
     * 水宝
     */
    public static final String WATER_INNER_REGISTER=BuildConfig.URL_MSSB+"/member/innerRegister";
    public static final String PAY_METHOD_URL =BuildConfig.URL_HEADS+"/api/app/pay_method";
    public static final String SEARCH_ESTATE_URL =BuildConfig.URL_MSSB+"/community/search";
    public static final String SCAN_CODE_BUY=BuildConfig.URL_MSSB+"/order/appScanNotice";
    public static final String WATER_ACCOUNT_URL =BuildConfig.URL_MSSB+"/member/get";
    public static final String WATER_CARD_RECHARGE =BuildConfig.URL_MSSB+"/order/recharge";
    public static final String WATER_CARD_PAY_URL =BuildConfig.URL_MSSB+"/order/payNotice";
    public static final String WATER_ORDER_LIST_URL =BuildConfig.URL_MSSB+"/order/list";
    public static final String WATER_RECHARGE_MEAL =BuildConfig.URL_MSSB+"/pack/list";
    public static final String WATER_PRICE_URL =BuildConfig.URL_MSSB+"/order/getPrice";
    public static final String WATER_CREATE_URL =BuildConfig.URL_MSSB+"/order/create";
    public static final String WATER_CANCEL_URL =BuildConfig.URL_MSSB+"/order/oper";
    public static final String WATER_EQUIPMENT_SEARCH=BuildConfig.URL_MSSB+"/equip/search";
    public static final String WATER_BIND_ACCOUNT_LIST =BuildConfig.URL_MSSB+"/member/bindList";
    public static final String WATER_VERIFY_CODE=BuildConfig.URL_MSSB+"/sendVerifyCode";
    public static final String WATER_BIND_ACCOUNT_URL=BuildConfig.URL_MSSB+"/member/bindAccount";
    public static final String WATER_UNBIND_ACCOUNT=BuildConfig.URL_MSSB+"/member/unbindAccount";
    public static final String WATER_SWITCH_ACCOUNT=BuildConfig.URL_MSSB+"/member/switchAccount";
    public static final String WATER_EQUIPMENT_INFORMATION=BuildConfig.URL_MSSB+"/equip/get";
    public static final String WATER_BALANCE_DETAIL=BuildConfig.URL_MSSB+"/wallet/balanceItems";
    public static final String WATER_IMAGE_UPLOAD=BuildConfig.URL_MSSB+"/uploadFile";
    public static final String WATER_MALFUNCTION_UPLOAD=BuildConfig.URL_MSSB+"/failure/report";
    public static final String WATER_PAY_ORDER_URL=BuildConfig.URL_MSSB+"/payment/payOrder";
    public static final String WATER_PRIZES_GIFTS=BuildConfig.URL_MSSB_GIFT+"/rw_front/lqlp.html";
    public static final String WATER_SERVICE_CENTER=BuildConfig.URL_MSSB_GIFT+"/rw_front/customer_center.html";
    public static final String WATER_GIFTS_RECHARGE_WEB="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9e0da2a76aae5110&redirect_uri=http%3a%2f%2fsb-fx.msbapp.cn%2frw_front%2frecharge.html&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
    /**
     * 商城
     */
    public static final String SHOP_LOGIN_HTML =BuildConfig.URL_SHOP+"/wap/tmpl/member/login.html";
    public static final String SHOP_HOME_URL = BuildConfig.URL_SHOP +"/wap/index.html";
    public static final String SHOP_LOGIN = BuildConfig.URL_SHOP +"/mobile/index.php?act=login&op=appindex";
    public static final String SHOP_IMPORT_FOODSTUFF =BuildConfig.URL_SHOP +"/wap/tmpl/class_list.html?gc_id=1246";
    public static final String SHOP_DISINFECTIONCABINET =BuildConfig.URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1059";
    public static final String SHOP_HEATERCALORIFIER =BuildConfig.URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1060";
    public static final String SHOP_LAMPBLACK =BuildConfig.URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1063";
    public static final String SHOP_GAS_STOVE =BuildConfig.URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1058";
    public static final String SHOP_GAS_HEATER =BuildConfig.URL_SHOP+"/wap/tmpl/class_list.html?gc_id=1057";
    public static final String SHOP_ORDER_LIST =BuildConfig.URL_SHOP+"/wap/tmpl/member/order_list.html";
    public static final String SHOP_ORDER_DATA_STATE=BuildConfig.URL_SHOP+"/wap/tmpl/member/order_list.html?data-state=state_new";
    /**
     * 首页功能
     */
    public static final String APP_VERSION_URL = BuildConfig.URL_HEADS +"/Gas/app/version";
    public static final String ALL_SERVE_CATALOG_URL =BuildConfig.URL_HEADS+"/api/serve_city/user_app_all_serve";
    public static final String FUNCTION_URL = BuildConfig.URL_HEADS +"/api/serve_city/user_app_serve";
    public static final String HOME_ADVERTISEMENT_URL=BuildConfig.URL_HEADS+"/api/app/homepage_header_img";
    public static final String HOME_FUNCTION_URL =BuildConfig.URL_HEADS+"/api/serve_city/user_app_homepage";
    public static final String WEATHER_QUERY_URL = BuildConfig.URL_HEADS +"/pis/weather/query";
    public static final String INFORM_URL = BuildConfig.URL_HEADS +"/Gas/message/list";
    public static final String INFORM_DELETE = BuildConfig.URL_HEADS +"/Gas/message/delete";
    public static final String INFORM_DETAIL = BuildConfig.URL_HEADS +"/Gas/message/detail";
    public static final String MESSAGE_UNREAD_URL = BuildConfig.URL_HEADS +"/Gas/message/unRead";
    public static final String PUSH_DEVICE_TOKEN= BuildConfig.URL_HEADS +"/Gas/user/pushDeviceToken";
    public static final String USER_INFO_GAS_URL = BuildConfig.URL_HEADS +"/Gas/user/info";
    public static final String READ_SEARCH_USER_HOUSE =BuildConfig. URL_HEADS +"/Gas/usedHouse/search";
    public static final String PRE_DEPOSIT_HISTORY = BuildConfig.URL_HEADS +"/Gas/payment/pre_deposit_history";
    public static final String SPECIAL_TOPIC_URL =BuildConfig.URL_HEADS+"/api/app/specail_topic_activity";
    public static final String ADVERTISING_URL=BuildConfig.URL_HEADS+"/api/app/get_activity_by_code ";
    public static final String BADGE_COUNT_URL=BuildConfig.URL_HEADS+"/Gas/message/badgeCount";

    /**分享*/
    public static final String SUCCESS_SHARE_URL=BuildConfig.URL_HEADS+"/msht_operation_platform/app_share/app_share_success";

    /**
     * 资讯
     */
    public static final String HOME_MSB_APP_HEADLINE=BuildConfig.MSB_HEADLINE+"/crawler/getNumItems";
    public static final String MSB_APP_HEADLINE_LIST=BuildConfig.MSB_HEADLINE+"/crawler/getPages";
    public static final String MSB_APP_NEWS="http://msbapp.cn/zixunh5/index.html";

    /**
     * 燃气服务模块
     */
    public static final String INSTALL_SERVER_URL =BuildConfig. URL_HEADS +"/Gas/workOrder/add";
    public static final String INSTALL_TYPE_URL = BuildConfig.URL_HEADS +"/Gas/app/gas_install_type";
    public static final String GET_TABLE_URL = BuildConfig.URL_HEADS +"/Gas/meter/rqb";
    public static final String GAS_TABLE_DATA = BuildConfig.URL_HEADS +"/Gas/meter/list";
    public static final String SEND_TABLE_DATA_URL = BuildConfig.URL_HEADS +"/Gas/meter/add";
    public static final String SEARCH_BILL_GAS_URL = BuildConfig.URL_HEADS +"/Gas/debts/search";
    public static final String SELECT_ADDRESS_URL = BuildConfig.URL_HEADS +"/Gas/usedHouse/list";
    public static final String CREATE_ORDER_GAS =BuildConfig.URL_HEADS +"/Gas/gas_fee_pay/createOrder";
    public static final String GAS_EXPENSE_PAY =BuildConfig.URL_HEADS +"/Gas/gas_fee_pay/pay";
    public static final String INTERNET_TABLE_RECORD=BuildConfig.URL_HEADS+"/Gas/payment/iotPaymentDetail";
    public static final String INTERNET_TABLE_LAST_DATA=BuildConfig.URL_HEADS+"/Gas/payment/meterTypeSearch";
    public static final String INTERNET_TABLE_PAYMENT=BuildConfig.URL_HEADS+"/Gas/payment/requestPaymentGas";
    /**
     * ic卡
     */
    public static final String IC_RECHARGE_BILL_URL =BuildConfig.URL_HEADS+"/Gas/payment/icRecharge";
    public static final String IC_RECHARGE_SEARCH_URL =BuildConfig.URL_HEADS+"/Gas/payment/icRechargeSearch";
    public static final String IC_RECHARGE_HISTORY_URL =BuildConfig.URL_HEADS+"/Gas/payment/icRechargeHistory";
    public static final String IC_RECHARGE_BRANCH_URL=BuildConfig.URL_HEADS+"/Gas/payment/icRechargeBranch";
    public static final String IC_OPERATION_STEP_URL="http://msbapp.cn/ic_card/index.html";
    /**抢险  **/
    public static final String GAS_QIANGXIAN_URL = BuildConfig.URL_HEADS +"/Gas/app/rescue";
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
    public static final String RECHARGE_CREATE_ORDER_URL=BuildConfig.URL_HEADS+"/Gas/wallet/recharge_create_order";
    public static final String RECHARGE_PAY_URL=BuildConfig.URL_HEADS+"/Gas/wallet/recharge_pay";
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
    public static final String INSURANCE_BUY_URL =BuildConfig.URL_HEADS +"/Gas/insurance/create_order";
    public static final String INSURANCE_HISTORY_URL = BuildConfig.URL_HEADS +"/Gas/invoice/history";
    public static final String GET_HOUSE_ADDRESS_URL ="http://220.174.234.36:8090/GasAPI/house/customerNoHouse";
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
    public static final String INVOICE_EXPLAIN = BuildConfig.URL_HEADS +"/repair_h5/invoice_note.html";
    public static final String INVOICE_APPLY_URL =BuildConfig. URL_HEADS +"/Gas/invoice/apply";
    public static final String INVOICE_GET_URL = BuildConfig.URL_HEADS +"/Gas/invoice/uninvoce_list";
    public static final String INVOICE_GAS_CUSTOMER_LIST=BuildConfig.URL_HEADS+"/Gas/invoice/gas_customer_list";
    public static final String INVOICE_GAS_NOT_OPEN_LIST=BuildConfig.URL_HEADS+"/Gas/invoice/gas_uninvoice_list";
    public static final String INVOICE_GAS_APPLY=BuildConfig.URL_HEADS+"/Gas/invoice/gas_apply";
    public static final String INVOICE_GAS_HISTORY=BuildConfig.URL_HEADS+"/Gas/invoice/gas_history";
    public static final String INVOICE_GAS_DETAIL=BuildConfig.URL_HEADS+"/Gas/invoice/gas_history_detail";
    public static final String INVOICE_REPAIR_DETAIL=BuildConfig.URL_HEADS+"/Gas/invoice/repair_invoice_detail";
    public static final String INVOICE_EXPRESS_INFORMATION=BuildConfig.URL_HEADS+"/Gas/invoice/repair_invoice_expressInfo";
    public static final String INVOICE_REPAIR_ORDER=BuildConfig.URL_HEADS+"/Gas/invoice/invoice_order_list";
    public static final String INVOICE_CONTROL_URL=BuildConfig.URL_HEADS+"/Gas/invoice/gas_invoice_hide";
    public static final String INVOICE_DATA_TYPE=BuildConfig.URL_HEADS+"/Gas/invoice/gas_invoice_detail";
    public static final String GAS_INVOICE_QUESTION_URL =BuildConfig.URL_HEADS+"/gas_h5/Interrogative_answer.html";

    /**
     * 维修服务
    */
    public static final String HOUSEKEEPING_SERVICE_NOTE=BuildConfig.URL_HEADS+"/repair_h5/service_note_baojie.html";
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
    public static final String HOT_REPAIR_URL = BuildConfig.URL_HEADS +"/api/serve_city/hot_repair";
    /** LPG 接口 **/
    public static final String LPG_NEW_USER_URL=BuildConfig.URL_LPG+"/msht/lpgUser/newUser";
    public static final String LPG_GET_CAPTCHA_URL=BuildConfig.URL_LPG+"/msht/lpgUser/getCaptcha";
    public static final String LPG_BIND_MOBILE_URL=BuildConfig.URL_LPG+"/msht/lpgUser/bindMoblie";
    public static final String LPG_GET_USER_INFO=BuildConfig.URL_LPG+"/msht/lpgUser/getUserInfo";
    public static final String LPG_QUERY_PRICE=BuildConfig.URL_LPG+"/msht/lpgGasPrice/queryGasPrice";
    public static final String LPG_QUERY_DEPOSIT_PRICE=BuildConfig.URL_LPG+"/msht/lpgDepositMsg/queryDepositPrice";
    public static final String LPG_QUERY_GAS_DEPOSIT_PRICE=BuildConfig.URL_LPG+"/msht/lpgDepositMsg/getGasAndDeposit";
    public static final String LPG_QUERY_ALL_ADDRESS=BuildConfig.URL_LPG+"/msht/lpgUser/getAllAddress";
    public static final String LPG_QUERY_ALL_DELIVERY_FEE=BuildConfig.URL_LPG+"/msht/lpgDeliveryFee/getAllDeliveryFee";
    public static final String LPG_CREATE_NEW_ORDER=BuildConfig.URL_LPG+"/msht/lpgOrder/newOrder";
    public static final String LPG_CREATE_NEW_ADDRESS=BuildConfig.URL_LPG+"/msht/lpgUser/newAddress";
    public static final String LPG_MSB_USER_INFO=BuildConfig.URL_LPG+"/msht/lpgUser/getMsbUserInfoTmp";
    public static final String LPG_ORDER_PAY=BuildConfig.URL_LPG+"/msht/lpgOrder/pay";
    public static final String LPG_GET_ALL_ORDER=BuildConfig.URL_LPG+"/msht/lpgOrder/allOrder";
    public static final String LPG_QUERY_ORDER_URL=BuildConfig.URL_LPG+"/msht/lpgOrder/queryOrder";
    public static final String LPG_FAIL_ORDER_URL=BuildConfig.URL_LPG+"/msht/lpgOrder/failOrder";
    public static final String LPG_REPLACE_BOTTLE_URL=BuildConfig.URL_LPG+"/msht/lpgReplacePrice/getReplaceBottleList";
    public static final String LPG_BOTTLE_INFO_URL=BuildConfig.URL_LPG+"/msht/lpgBottle/getBottleInfoById";
    public static final String LPG_ALL_BINDING_USER=BuildConfig.URL_LPG+"/msht/lpgUser/getAllBingUser";
    public static final String LPG_SWITCH_USER_URL=BuildConfig.URL_LPG+"/msht/lpgUser/switchUser";
    public static final String LPG_DELETE_USER_URL=BuildConfig.URL_LPG+"/msht/lpgUser/removeUser";
    public static final String LPG_QUERY_ORDER_FLOW=BuildConfig.URL_LPG+"/msht/lpgOrder/queryFlow";
    public static final String LPG_DEPOSIT_COUNT_URL=BuildConfig.URL_LPG+"/msht/lpgOrder/depositCount";
    public static final String LPG_GAS_AND_DEPOSIT_URL=BuildConfig.URL_LPG+"/msht/lpgDepositMsg/getGasAndDepositNew";
    public static final String LPG_QR_CODE_SCAN_URL="https://lpg.msbapp.cn/msht/lpgBottle/getBottleDetail";
    public static final String LPG_TRANSPORTATION_EXPENSE="http://msbapp.cn/lpg_h5/peisongshuoming.html";
    public static final String LPG_OPEN_TREATY="http://msbapp.cn/lpg_h5/kaihuxieyi.html";
    public static final String LPG_DISCOUNT_TABLE="http://msbapp.cn/lpg_h5/huishouzhejia.html";
    /**
     * HTML5页面
     */
    public static final String APP_PAY_SUCCESS_PAGE =BuildConfig.URL_HEADS+"/Gas/app/app_pay_success_page?";
    public static final String INSURANCE_EXPLAIN_URL="http://msbapp.cn/insurance/toubaoshuoming.html";
    public static final String RECHARGE_BACK_AGREE =BuildConfig.URL_HEADS+"/water_h5/chongfanxieyi.html";
    public static final String REPLACE_PAY_AGREE_URL = BuildConfig.URL_HEADS +"/gas_h5/daikouxieyi.html";
    public static final String VEGETABLE_URL ="http://jsxss.net/app/index.php?i=5&c=entry&m=ewei_shopv2&do=mobile";
}
