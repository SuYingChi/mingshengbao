package com.msht.minshengbao.functionActivity.MyActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.AppPackageUtil;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class AboutMineActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_mine);
        context=this;
        mPageName ="关于我们";
        setCommonHeader(mPageName);
        TextView tvVersionName=(TextView)findViewById(R.id.id_versionName);
        String mName= AppPackageUtil.getPackageVersionName(context);
        if (TextUtils.isEmpty(mName)&&mName!=null){
            mName="版本:"+mName;
            tvVersionName.setText(mName);
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
