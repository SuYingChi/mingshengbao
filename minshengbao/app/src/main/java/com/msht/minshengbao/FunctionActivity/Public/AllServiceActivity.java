package com.msht.minshengbao.FunctionActivity.Public;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.msht.minshengbao.Adapter.AllServerAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Control.FullyLinearLayoutManager;
import com.msht.minshengbao.FunctionActivity.Electricvehicle.ElectricHome;
import com.msht.minshengbao.FunctionActivity.GasService.GasIccard;
import com.msht.minshengbao.FunctionActivity.GasService.GasInstall;
import com.msht.minshengbao.FunctionActivity.GasService.GasIntroduce;
import com.msht.minshengbao.FunctionActivity.GasService.GasPayfee;
import com.msht.minshengbao.FunctionActivity.GasService.GasRepair;
import com.msht.minshengbao.FunctionActivity.GasService.GasWriteTable;
import com.msht.minshengbao.FunctionActivity.GasService.Gasqianxian;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.IntelligentFarmHml;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.ShopActivity;
import com.msht.minshengbao.FunctionActivity.HtmlWeb.VegetableGentlemen;
import com.msht.minshengbao.FunctionActivity.WaterApp.WaterHomeActivity;
import com.msht.minshengbao.FunctionActivity.insurance.InsuranceHome;
import com.msht.minshengbao.FunctionActivity.repairService.HomeAppliancescClean;
import com.msht.minshengbao.FunctionActivity.repairService.HouseApplianceFix;
import com.msht.minshengbao.FunctionActivity.repairService.LampCircuit;
import com.msht.minshengbao.FunctionActivity.repairService.OtherRepair;
import com.msht.minshengbao.FunctionActivity.repairService.SanitaryWare;
import com.msht.minshengbao.Model.AllServiceModel;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
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
                case SendrequestUtil.SUCCESS:
                    try {
                        Gson gson = new Gson();
                        AllServiceModel model = gson.fromJson(msg.obj.toString(), AllServiceModel.class);
                        if (model.result.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            ArrayList<AllServiceModel.MainCategory.ServeCategory> data = model.data.serve;
                            activity.categories=data;
                            activity.allServerAdapter.clear();
                            activity.allServerAdapter.addAll(data);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
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
                serveId=String.valueOf(childcategories.get(secondPosition).id);
                startServer(code);
            }
        });
    }
    private void initView() {
        myRecyclerView=(MyRecyclerView)findViewById(R.id.id_serve_view);
    }
    private void startServer(String code) {
        switch (code){
            case "gas_pay":
                gaspay();
                break;
            case "gas_meter":
                gasMeter();
                break;
            case "gas_iccard":
                gasIcCard();
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
    private void vegetableScxs() {
        Intent intent=new Intent(context, VegetableGentlemen.class);
        startActivity(intent);
    }
    private void drinkingWater() {
        Intent serve=new Intent(context,WaterHomeActivity.class);
        startActivity(serve);
    }
    private void intelligentFarm() {
        String url=UrlUtil.Intelligent_FarmUrl;
        Intent intent=new Intent(context, IntelligentFarmHml.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","智慧农贸");
        startActivity(intent);
    }
    private void gasIcCard() {
        Intent card=new Intent(context,GasIccard.class);
        startActivity(card);
    }
    private void gaspay() {
        Intent selete=new Intent(context,GasPayfee.class);
        startActivity(selete);
    }
    private void gasMeter() {
        Intent selete=new Intent(context,GasWriteTable.class);
        startActivity(selete);
    }
    private void gasRepair() {
        Intent selete=new Intent(context,GasRepair.class);
        startActivity(selete);
    }
    private void gasInstall() {
        Intent selete=new Intent(context,GasInstall.class);
        startActivity(selete);
    }
    private void gasRescue() {
        Intent selete=new Intent(context,Gasqianxian.class);
        startActivity(selete);
    }
    private void gasIntroduce() {
        Intent selete=new Intent(context,GasIntroduce.class);
        startActivity(selete);
    }
    private void householdrepair() {
        Intent intent=new Intent(context, HouseApplianceFix.class);
        intent.putExtra("pid",serveId);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void sanitaryware() {
        Intent intent=new Intent(context, SanitaryWare.class);
        intent.putExtra("pid",serveId);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void lampcircuit() {
        Intent intent=new Intent(context,LampCircuit.class);
        intent.putExtra("pid",serveId);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void otherRepair() {
        Intent intent=new Intent(context,OtherRepair.class);
        intent.putExtra("pid",serveId);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void householdclean() {
        Intent intent=new Intent(context, HomeAppliancescClean.class);
        intent.putExtra("pid",serveId);
        intent.putExtra("city_id",cityId);
        startActivity(intent);
    }
    private void vehiclerepair() {
        Intent selete=new Intent(context,ElectricHome.class);
        startActivity(selete);
    }
    private void insurance() {
        Intent intent=new Intent(context, InsuranceHome.class);
        startActivity(intent);
    }
    private void sterilizer() {
        String shop_url=UrlUtil.Shop_Disinfectioncabinet;
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",shop_url);
        intent.putExtra("first",1);
        startActivity(intent);
    }
    private void electricwater() {
        String shop_url=UrlUtil.Shop_Heatercalorifier;
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",shop_url);
        intent.putExtra("first",1);
        startActivity(intent);
    }
    private void lampblackmachine() {
        String shop_url=UrlUtil.Shop_Lampblack;
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",shop_url);
        intent.putExtra("first",1);
        startActivity(intent);
    }
    private void gasstove() {
        String shop_url=UrlUtil.Shop_Gasstove;
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",shop_url);
        intent.putExtra("first",1);
        startActivity(intent);
    }
    private void waterheater() {
        String shop_url=UrlUtil.Shop_GasHeater;
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",shop_url);
        intent.putExtra("first",1);
        startActivity(intent);
    }
    private void redwind() {
        String shop_url=UrlUtil.Shop_IMPORT_FOODSTUFF;
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",shop_url);
        intent.putExtra("first",1);
        startActivity(intent);
    }
    private void intData() {
        String functionUrl= UrlUtil.AllServeCatalog_Url;
        String function="";
        try {
            function =functionUrl +"?city_id="+ URLEncoder.encode(cityId, "UTF-8")+"&city_name="+URLEncoder.encode(mCity, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SendrequestUtil.getDataFromService(function,serverTypeHandler);
    }
}
