package com.msht.minshengbao.functionActivity.Public;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.msht.minshengbao.BuildConfig;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.adapter.AllServerAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Control.FullyLinearLayoutManager;
import com.msht.minshengbao.androidShop.activity.MessageListActivity;
import com.msht.minshengbao.androidShop.activity.ShopClassDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopGoodDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopKeywordListActivity;
import com.msht.minshengbao.androidShop.activity.ShopMoreGoodActivity;
import com.msht.minshengbao.androidShop.activity.ShopUrlActivity;
import com.msht.minshengbao.androidShop.util.StringUtil;
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

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author hong
 * @date 2018/7/2  
 */
public class AllServiceActivity extends BaseActivity {
    private String serveId;
    private String mCity = "";
    private String cityId = "";
    private MyRecyclerView myRecyclerView;
    private AllServerAdapter allServerAdapter;
    private ArrayList<AllServiceModel.MainCategory.ServeCategory> categories = null;
    private ArrayList<AllServiceModel.MainCategory.ServeCategory.ChildCategory> childCategories = null;
    private final ServerTypeHandler serverTypeHandler = new ServerTypeHandler(this);

    private static class ServerTypeHandler extends Handler {
        private WeakReference<AllServiceActivity> mWeakReference;

        public ServerTypeHandler(AllServiceActivity activity) {
            mWeakReference = new WeakReference<AllServiceActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final AllServiceActivity activity = mWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        Gson gson = new Gson();
                        AllServiceModel model = gson.fromJson(msg.obj.toString(), AllServiceModel.class);
                        if (model.result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            ArrayList<AllServiceModel.MainCategory.ServeCategory> data = model.data.serve;
                            int shopPosition = -1;
                            for(int i=0;i<data.size();i++){
                                if("shop".equals(data.get(i).code)){
                                    shopPosition=i;
                                    break;
                                }
                            }
                            data.get(shopPosition).child.add(new AllServiceModel.MainCategory.ServeCategory.ChildCategory("更多分类"));
                            activity.categories = data;
                            activity.allServerAdapter.clear();
                            activity.allServerAdapter.addAll(data);
                        }
                    } catch (Exception e) {
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
        context = this;
        setCommonHeader("全部");
        mCity = VariableUtil.City;
        cityId = VariableUtil.cityId;
        initView();
        myRecyclerView.setLayoutManager(new FullyLinearLayoutManager(getApplicationContext()));
        allServerAdapter = new AllServerAdapter(this);
        myRecyclerView.setAdapter(allServerAdapter);
        intData();
        allServerAdapter.SetOnItemClickListener(new AllServerAdapter.OnItemClickListener() {
            @Override
            public void ItemClick(View view, int mainPosition, int secondPosition) {
                childCategories = categories.get(mainPosition).child;
                String mainCode = categories.get(mainPosition).code;
                String code = childCategories.get(secondPosition).code;
                String url = childCategories.get(secondPosition).content;
                String name = childCategories.get(secondPosition).name;
                serveId = String.valueOf(childCategories.get(secondPosition).id);
                if ("shop".equals(mainCode)) {
                    if (secondPosition < childCategories.size() - 1) {
                        Intent intent = new Intent(AllServiceActivity.this, ShopClassDetailActivity.class);
                        String gcId = Uri.parse(childCategories.get(secondPosition).url).getQueryParameter("gc_id");
                        intent.putExtra("data", gcId);
                        intent.putExtra("title", String.valueOf(childCategories.get(secondPosition).name));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(AllServiceActivity.this, ShopMoreGoodActivity.class);
                        startActivity(intent);
                    }
                } else if (!TextUtils.isEmpty(url)){
                    AppActivityUtil.onStartUrl(context,url,code);
                }else {
                    AppActivityUtil.onAllServiceStartActivity(context,mainCode,code,serveId,name,"0");
                }
            }
        });
    }
    private void initView() {
        myRecyclerView = (MyRecyclerView) findViewById(R.id.id_serve_view);
    }

    private void intData() {
        String functionUrl = UrlUtil.ALL_SERVE_CATALOG_URL;
        String function = "";
        try {
            function = functionUrl + "?city_id=" + URLEncoder.encode(cityId, "UTF-8") + "&city_name=" + URLEncoder.encode(mCity, "UTF-8") + "&version=" + URLEncoder.encode("201902", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SendRequestUtil.getDataFromService(function, serverTypeHandler);
    }
}
