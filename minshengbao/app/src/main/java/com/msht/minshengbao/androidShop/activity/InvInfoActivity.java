package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.Fragment.AddInvFragment;
import com.msht.minshengbao.androidShop.Fragment.NeedInvFrgment;
import com.msht.minshengbao.androidShop.Fragment.NoNeedInvFragment;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseFragment;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.InvItemBean;
import com.msht.minshengbao.androidShop.shopBean.ShopAddressListBean;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IAddCompanyNormalInvView;
import com.msht.minshengbao.androidShop.viewInterface.IAddPersonalInvInvView;

import butterknife.BindView;

import static com.msht.minshengbao.androidShop.Fragment.AddInvCompanyFragment.REQUESTCODE_INV_COMPANY;
import static com.msht.minshengbao.androidShop.Fragment.AddInvPersonFragment.REQUESTCODE_INV_PERSON;

public class InvInfoActivity extends ShopBaseActivity implements NeedInvFrgment.InvFrgmentListener, AddInvFragment.AddInvListener, IAddPersonalInvInvView, IAddCompanyNormalInvView {

    private ShopBaseFragment currentFragment;
    private String order_amount;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.no_need)
    RadioButton noNeed;
    @BindView(R.id.need)
    RadioButton need;
    @BindView(R.id.ok)
    TextView okBtn;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    private NoNeedInvFragment noNeedFrag;
    private NeedInvFrgment needFrag;
    private AddInvFragment addInvFragment;
    private InvItemBean selectedInvItem;
    private ShopAddressListBean.DatasBean.AddressListBean companyAddressBean;
    private int addInvChildTab = 0;

    private ShopAddressListBean.DatasBean.AddressListBean personAddress;


    @Override
    protected void setLayout() {
        setContentView(R.layout.inv_info);
    }

    @Override
    protected void initImmersionBar() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Intent intent = getIntent();
        order_amount = intent.getStringExtra("data");
        companyAddressBean = (ShopAddressListBean.DatasBean.AddressListBean) intent.getSerializableExtra("address");
        personAddress = (ShopAddressListBean.DatasBean.AddressListBean) intent.getSerializableExtra("address");;
        noNeedFrag = new NoNeedInvFragment();
        needFrag = new NeedInvFrgment();
        addInvFragment = new AddInvFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("address", companyAddressBean);
        bundle.putString("data", order_amount);
        addInvFragment.setArguments(bundle);
        switchContent(noNeedFrag);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.no_need) {
                    noNeed.setTextColor(getResources().getColor(R.color.white));
                    need.setTextColor(getResources().getColor(R.color.nc_bg));
                    switchContent(noNeedFrag);
                } else if (checkedId == R.id.need) {
                    need.setTextColor(getResources().getColor(R.color.white));
                    noNeed.setTextColor(getResources().getColor(R.color.nc_bg));
                    switchContent(needFrag);
                }
            }
        });
        final InvItemBean noNeedInvBean = new InvItemBean("不需要发票", true, "", "", "");
        final Intent intent1 = new Intent();
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment.equals(noNeedFrag)) {
                    intent1.putExtra("inv", noNeedInvBean);
                    setResult(RESULT_OK, intent1);
                    finish();
                } else if (currentFragment.equals(needFrag)) {
                    intent1.putExtra("inv", selectedInvItem);
                    setResult(RESULT_OK, intent1);
                    finish();
                } else if (currentFragment.equals(addInvFragment)) {
                    if (addInvChildTab == 0) {
                        ShopPresenter.addPersonalInvItem(InvInfoActivity.this);
                    } else if (addInvChildTab == 1) {
                        ShopPresenter.addCompanyNormalInvItem(InvInfoActivity.this);
                    }

                }

            }
        });
    }


    private void switchContent(ShopBaseFragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment == null) {
            currentFragment = to;
            transaction.add(R.id.container, currentFragment).commit();
        } else if (!currentFragment.equals(to)) {
            if (!to.isAdded()) {
                transaction.hide(currentFragment).add(R.id.container, to).commit();
            } else {
                transaction.hide(currentFragment).show(to).commit();
            }
            currentFragment = to;
        }
    }

    @Override
    public void onAddNewInv() {
        switchContent(addInvFragment);
    }

    @Override
    public void onSelectedInv(InvItemBean selectedInvItem) {
        this.selectedInvItem = selectedInvItem;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_INV_PERSON && resultCode == RESULT_OK) {
            if (data != null) {
                personAddress = (ShopAddressListBean.DatasBean.AddressListBean) data.getSerializableExtra("data");
                addInvFragment.setPersonInvAdress(personAddress);
            }
        }
        if (requestCode == REQUESTCODE_INV_COMPANY && resultCode == RESULT_OK) {
            if (data != null) {
                companyAddressBean = (ShopAddressListBean.DatasBean.AddressListBean) data.getSerializableExtra("data");
                addInvFragment.setCompanyInvAdress(companyAddressBean);
            }
        }
    }

    @Override
    public void onSwitchAddInvChildContent(int tab) {
        addInvChildTab = tab;
    }

    @Override
    public void onAddPersonalInvSuccess(String s) {
        PopUtil.showAutoDissHookDialog(this, "新增常用发票成功", 0);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(needFrag).commit();
        needFrag = new NeedInvFrgment();
        switchContent(needFrag);
    }


    @Override
    public String getRecId() {
        return personAddress.getAddress_id();
    }

    @Override
    public String getInv_content() {
        return addInvFragment.getPersonInvContent();
    }

    @Override
    public String getCNRecId() {
        return addInvFragment.getCNRecId();
    }

    @Override
    public String CNinv_title() {
        return addInvFragment.CNinv_title();
    }

    @Override
    public String getCNSbh() {
        return addInvFragment.getCNSbh();
    }

    @Override
    public String getCNBank() {
        return addInvFragment.getCNBank();
    }

    @Override
    public String getCNBanknum() {
        return addInvFragment.getCNBanknum();
    }

    @Override
    public String getCNComtel() {
        return addInvFragment.getCNComtel();
    }

    @Override
    public String getCNComaddr() {
        return addInvFragment.getCNComaddr();
    }

    @Override
    public String getCNInv_content() {
        return addInvFragment.getCNInv_content();
    }

    @Override
    public void onAddCompanyNormalSuccess(String s) {
        PopUtil.showAutoDissHookDialog(this, "新增常用发票成功", 0);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(needFrag).commit();
        needFrag = new NeedInvFrgment();
        switchContent(needFrag);
        ;
    }

    @Override
    public String getCompanyInvType() {
        return addInvFragment.getType();
    }
}
