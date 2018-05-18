package com.msht.minshengbao.FunctionView.Public;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.msht.minshengbao.Adapter.AllServerAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.Control.FullyLinearLayoutManager;
import com.msht.minshengbao.FunctionView.Electricvehicle.ElectricHome;
import com.msht.minshengbao.FunctionView.GasService.GasIccard;
import com.msht.minshengbao.FunctionView.GasService.GasInstall;
import com.msht.minshengbao.FunctionView.GasService.GasIntroduce;
import com.msht.minshengbao.FunctionView.GasService.GasPayfee;
import com.msht.minshengbao.FunctionView.GasService.GasRepair;
import com.msht.minshengbao.FunctionView.GasService.GasWriteTable;
import com.msht.minshengbao.FunctionView.GasService.Gasqianxian;
import com.msht.minshengbao.FunctionView.HtmlWeb.HtmlPage;
import com.msht.minshengbao.FunctionView.HtmlWeb.IntelligentFarmHml;
import com.msht.minshengbao.FunctionView.HtmlWeb.ShopActivity;
import com.msht.minshengbao.FunctionView.HtmlWeb.VegetableGentlemen;
import com.msht.minshengbao.FunctionView.WaterApp.WaterHome;
import com.msht.minshengbao.FunctionView.insurance.InsuranceHome;
import com.msht.minshengbao.FunctionView.insurance.InsurancePurchase;
import com.msht.minshengbao.FunctionView.repairService.HomeAppliancescClean;
import com.msht.minshengbao.FunctionView.repairService.HouseApplianceFix;
import com.msht.minshengbao.FunctionView.repairService.LampCircuit;
import com.msht.minshengbao.FunctionView.repairService.OtherRepair;
import com.msht.minshengbao.FunctionView.repairService.SanitaryWare;
import com.msht.minshengbao.Model.AllseviceModel;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.MyRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AllService extends BaseActivity {
    private String serveId;
    private String mCity="";
    private String cityId="";
    private MyRecyclerView myRecyclerView;
    private AllServerAdapter allServerAdapter;
    private ArrayList<AllseviceModel.MainCategory.ServeCategory> categories=null;
    private ArrayList<AllseviceModel.MainCategory.ServeCategory.ChildCategory> childcategories=null;
    private CustomDialog customDialog;
    private static final int SUCCESS=1;
    private static final int FAILURE = 0;
    Handler ServeTypehandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        Gson gson = new Gson();
                        AllseviceModel model = gson.fromJson(msg.obj.toString(), AllseviceModel.class);
                        if (model.result.equals("success")) {
                            ArrayList<AllseviceModel.MainCategory.ServeCategory> data = model.data.serve;
                            categories=data;
                            allServerAdapter.clear();
                            allServerAdapter.addAll(data);
                        } else {

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_service);
        context=this;
        setCommonHeader("全部");
        customDialog=new CustomDialog(this, "正在加载");
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
                startSerevr(code);
            }
        });
    }
    private void initView() {
        myRecyclerView=(MyRecyclerView)findViewById(R.id.id_serve_view);
    }
    private void startSerevr(String code) {
        switch (code){
            case "gas_pay":
                gaspay();
                break;
            case "gas_meter":
                Gasmeter();
                break;
            case "gas_iccard":
                Gasiccard();
                break;
            case "gas_repair":
                Gasrepair();
                break;
            case "gas_install":
                Gasinstall();
                break;
            case "gas_rescue":
                gas_rescue();
                break;
            case "gas_introduce":
                gas_introduce();
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
                other_repair();
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
                IntelligentFarm();
                break;
            case "drinking_water":
                Drinkingwater();
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
                DegetableScxs();
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
    private void DegetableScxs() {
        Intent intent=new Intent(context, VegetableGentlemen.class);
        startActivity(intent);
    }
    private void Drinkingwater() {
        Intent serve=new Intent(context,WaterHome.class);
        startActivity(serve);
    }
    private void IntelligentFarm() {
        String url=UrlUtil.Intelligent_FarmUrl;
        Intent intent=new Intent(context, IntelligentFarmHml.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","智慧农贸");
        startActivity(intent);
    }
    private void Gasiccard() {
        Intent card=new Intent(context,GasIccard.class);
        startActivity(card);
    }
    private void gaspay() {
        Intent selete=new Intent(context,GasPayfee.class);
        startActivity(selete);
    }
    private void Gasmeter() {
        Intent selete=new Intent(context,GasWriteTable.class);
        startActivity(selete);

    }
    private void Gasrepair() {
        Intent selete=new Intent(context,GasRepair.class);
        startActivity(selete);
    }
    private void Gasinstall() {
        Intent selete=new Intent(context,GasInstall.class);
        startActivity(selete);
    }

    private void gas_rescue() {
        Intent selete=new Intent(context,Gasqianxian.class);
        startActivity(selete);
    }

    private void gas_introduce() {
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

    private void other_repair() {

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
        String shop_url=UrlUtil.Shop_Redwine;
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
        HttpUrlconnectionUtil.executeGetTwo(function, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = success;
                ServeTypehandler.sendMessage(message);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.what = FAILURE;
                msg.obj = fail;
                ServeTypehandler.sendMessage(msg);
            }
        });
    }
}
