package com.msht.minshengbao.functionActivity.MyActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.AppPackageUtil;


public class AboutMine extends BaseActivity {
    private TextView tv_versionName;
    private String Name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_mine);
        context=this;
        mPageName ="关于我们";
        setCommonHeader(mPageName);
        tv_versionName=(TextView)findViewById(R.id.id_versionName);
        Name= AppPackageUtil.getPackageVersionName(context);
        if (Name!=null){
            tv_versionName.setText("版本:"+Name);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
