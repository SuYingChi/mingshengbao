package com.msht.minshengbao.functionActivity.Public;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.adapter.AllServerAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Control.FullyLinearLayoutManager;
import com.msht.minshengbao.functionActivity.Electricvehicle.ElectricHomeActivity;
import com.msht.minshengbao.functionActivity.GasService.GasIcCardActivity;
import com.msht.minshengbao.functionActivity.GasService.GasInstallActivity;
import com.msht.minshengbao.functionActivity.GasService.GasIntroduceActivity;
import com.msht.minshengbao.functionActivity.GasService.GasPayFeeActivity;
import com.msht.minshengbao.functionActivity.GasService.GasRepairActivity;
import com.msht.minshengbao.functionActivity.GasService.GasWriteTableActivity;
import com.msht.minshengbao.functionActivity.GasService.GasEmergencyRescueActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.IntelligentFarmHmlActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.ShopActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.VegetableGentlemenActivity;
import com.msht.minshengbao.functionActivity.LPGActivity.LpgMyAccountActivity;
import com.msht.minshengbao.functionActivity.WaterApp.WaterMainActivity;
import com.msht.minshengbao.functionActivity.insurance.InsuranceHome;
import com.msht.minshengbao.functionActivity.repairService.HomeApplianceCleanActivity;
import com.msht.minshengbao.functionActivity.repairService.HouseApplianceFixActivity;
import com.msht.minshengbao.functionActivity.repairService.LampCircuitActivity;
import com.msht.minshengbao.functionActivity.repairService.OtherRepairActivity;
import com.msht.minshengbao.functionActivity.repairService.SanitaryWareActivity;
import com.msht.minshengbao.Model.AllServiceModel;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.MyRecyclerView;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AllServiceActivity extends BaseActivity {
    private String serveId;
    private String mCity="";
    private String cityId="";
    private MyRecyclerView myRecyclerView;
    private AllServerAdapter allServerAdapter;
    private ArrayList<AllServiceModel.MainCategory.ServeCategory> categories=null;
    private ArrayList<AllServiceModel.MainCategory.ServeCategory.ChildCategory> childcategories=null;
    private final ServerTypeHandler serverTypeHandler=new ServerTypeHandler(this);
    private static class ServerTypeHandler extends Handler{
        private WeakReference<AllServiceActivity> mWeakReference;
        public ServerTypeHandler(AllServiceActivity activity) {
            mWeakReference=new WeakReference<AllServiceActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final AllServiceActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        Log.d("msg.obj=",msg.obj.toString());
                        Gson gson = new Gson();
                        AllServiceModel model = gson.fromJson(msg.obj.toString(), AllServiceModel.class);
                        if (model.result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            ArrayList<AllServiceModel.MainCategory.ServeCategory> data = model.data.serve;
                            activity.categories=data;
                            activity.allServerAdapter.clear();
                            activity.allServerAdapter.addAll(data);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_service);
        context=this;
        setCommonHeader("全部");
        Intent data=getIntent();
        mCity=data.getStringExtra("mCity");
        cityId=data.getStringExtra("cityId");
        initView();
        myRecyclerView.setLayoutManager(new FullyLinearLayoutManager(getApplicationContext()));
        allServerAdapter = new AllServerAdapter(this);
        myRecyclerView.setAdapter(allServerAdapter);
        intData();
        allServerAdapter.SetOnItemClickListener(new AllServerAdapter.OnItemClickListener() {
            @Override
            public void ItemClick(View view, int mainPosition, int secondPosition) {
                childcategories=categories.get(mainPosition).child;
                String code=childcategories.get(secondPosition).code;
                String url=childcategories.get(secondPosition).url;
                serveId=String.valueOf(childcategories.get(secondPosition).id);
                if (!TextUtils.isEmpty(url)){
                    startUrl(url);
                }else {
                    startServer(code);
                }

            }
        });
    }

    private void initView() {
        myRecyclerView=(MyRecyclerView)findViewById(R.id.id_serve_view);
    }
    private void startUrl(String url) {
        if (NetUtil.getDomain(url).equals(ConstantUtil.SHOP_DOMAIN)){
            Intent intent=new Intent(context, ShopActivity.class);
            intent.putExtra("url",url);
            intent.putExtra("first",1);
            startActivity(intent);
        }else {
            Intent other=new Intent(context, HtmlPageActivity.class);
            other.putExtra("url",url);
            other.putExtra("navigate","民生宝");
            startActivity(other);
        }
    }
    private void startServer(String code) {
        switch (code){
            case ConstantUtil.GAS_PAY:
                gaspay();
                break;
            case ConstantUtil.GAS_METER:
                gasMeter();
                break;
            case ConstantUtil.GAS_IC_CARD:
                gasIcCard();
                break;
            case ConstantUtil.LPG_NAME:
                lpgService();
                break;
            case "gas_repair":
                gasRepair();
                break;
            case "gas_install":
                gasInstall();
                break;
            case "gas_rescue":
                gasRescue();
                break;
            case "gas_introduce":
                gasIntroduce();
                break;
            case "household_repair":
                householdrepair();
                break;
            case "sanitary_ware":
                sanitaryware();
                break;
            case "lamp_circuit":
                lampcircuit();
                break;
            case "other_repair":
                otherRepair();
                break;
            case "household_clean":
                householdclean();
                break;
            case "electric_vehicle_repair":
                vehiclerepair();
                break;
            case "insurance":
                insurance();
                break;
            case "shop_sterilizer":
                sterilizer();
                break;
            case "intelligent_farm":
                intelligentFarm();
                break;
            case "drinking_water":
                drinkingWater();
                break;
            case "shop_electric_water_heater":
                electricwater();
                break;
            case "shop_lampblack_machine":
                lampblackmachine();
                break;
            case "shop_gas_stove":
                gasstove();
                break;
            case "shop_water_heater":
                waterheater();
                break;
            case "shop_imported_red_wine":
                redwind();
                break;
           case "vegetables_scxs":
                vegetableScxs();
                break;
            case ConstantUtil.SHOP:
                break;
            default:
                showNotify("民生宝" ,"你的版本过老，如若需使用，请点击更新");
                break;
        }
    }



    private void showNotify(String title, String s) {
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
    private void lpgService() {
        Intent intent=new Intent(context, LpgMyAccountActivity.class);
        startActivity(intent);
    }
    private void vegetableScxs() {
        Intent intent=new Intent(context, VegetableGentlemenActivity.class);
        startActivity(intent);
    }
    private void drinkingWater() {
        Intent serve=new Intent(context,WaterMainActivity.class);
        startActivity(serve);
    }
    private void intelligentFarm() {
        String url=UrlUtil.Intelligent_FarmUrl;
        Intent intent=new Intent(context, IntelligentFarmHmlActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","智慧农贸");
        startActivity(intent);
    }
    private void gasIcCard() {
        Intent card=new Intent(context,GasIcCardActivity.class);
        startActivity(card);
    }
    private void gaspay() {
        Intent selete=new Intent(context,GasPayFeeActivity.class);
        startActivity(selete);
    }
    private void gasMeter() {
        Intent selete=new Intent(context,GasWriteTableActivity.class);
        startActivity(selete);
    }
    private void gasRepair() {
        Intent selete=new Intent(context,GasRepairActivity.class);
        startActivity(selete);
    }
    private void gasInstall() {
        Intent selete=new Intent(context,GasInstallActivity.class);
        startActivity(selete);
    }
    private void gasRescue() {
        Intent selete=new Intent(context,GasEmergencyRescueActivity.class);
        startActivity(selete);
    }
    private void gasIntroduce() {
        Intent selete=new Intent(context,GasIntroduceActivity.class);
        startActivity(selete);
    }
    private void householdrepair() {
        Intent intent=new Intent(context, HouseApplianceFixActivity.class);
        intent.putExtra("pid",serveId);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void sanitaryware() {
        Intent intent=new Intent(context, SanitaryWareActivity.class);
        intent.putExtra("pid",serveId);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void lampcircuit() {
        Intent intent=new Intent(context,LampCircuitActivity.class);
        intent.putExtra("pid",serveId);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void otherRepair() {
        Intent intent=new Intent(context,OtherRepairActivity.class);
        intent.putExtra("pid",serveId);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void householdclean() {
        Intent intent=new Intent(context, HomeApplianceCleanActivity.class);
        intent.putExtra("pid",serveId);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void vehiclerepair() {
        Intent selete=new Intent(context,ElectricHomeActivity.class);
        startActivity(selete);
    }
    private void insurance() {
        Intent intent=new Intent(context, InsuranceHome.class);
        startActivity(intent);
    }
    private void sterilizer() {
        String shop_url=UrlUtil.SHOP_DISINFECTIONCABINET;
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",shop_url);
        intent.putExtra("first",1);
        startActivity(intent);
    }
    private void electricwater() {
        String shop_url=UrlUtil.SHOP_HEATERCALORIFIER;
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",shop_url);
        intent.putExtra("first",1);
        startActivity(intent);
    }
    private void lampblackmachine() {
        String shop_url=UrlUtil.SHOP_LAMPBLACK;
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",shop_url);
        intent.putExtra("first",1);
        startActivity(intent);
    }
    private void gasstove() {
        String shop_url=UrlUtil.SHOP_GAS_STOVE;
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",shop_url);
        intent.putExtra("first",1);
        startActivity(intent);
    }
    private void waterheater() {
        String shop_url=UrlUtil.SHOP_GAS_HEATER;
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",shop_url);
        intent.putExtra("first",1);
        startActivity(intent);
    }
    private void redwind() {
        String shop_url=UrlUtil.SHOP_IMPORT_FOODSTUFF;
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",shop_url);
        intent.putExtra("first",1);
        startActivity(intent);
    }
    private void intData() {
        String functionUrl= UrlUtil.ALL_SERVE_CATALOG_URL;
        String function="";
        try {
            function =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&city_name="+URLEncoder.encode(mCity, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SendRequestUtil.getDataFromService(function,serverTypeHandler);
    }
}
