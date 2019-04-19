package com.msht.minshengbao.functionActivity.myActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.msht.minshengbao.R;
import com.msht.minshengbao.base.BaseActivity;

public class RebindWeiChatActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebind_wei_chat);
        context=this;
    }
}
