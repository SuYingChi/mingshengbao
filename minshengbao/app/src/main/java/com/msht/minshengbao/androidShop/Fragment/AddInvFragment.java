package com.msht.minshengbao.androidShop.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseFragment;
import com.msht.minshengbao.androidShop.shopBean.ShopAddressListBean;

import butterknife.BindView;

public class AddInvFragment extends ShopBaseFragment {
    @BindView(R.id.radio_group)
    RadioGroup rg;
    private ShopBaseFragment currentFragment;
    private AddInvPersonFragment addInvPersonFragment;
    private AddInvCompanyFragment addInvCompanyFragment;
    private AddInvListener addInvListener;
    private String CNRecId;
    private String CNSbh;
    private String CNBank;
    private String CNBanknum;
    private String CNComtel;
    private String CNComaddr;
    private String CNInv_content;


    @Override
    protected int setLayoutId() {
        return R.layout.inv_detail;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        addInvListener =(AddInvListener)getActivity();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        addInvPersonFragment = new AddInvPersonFragment();
        addInvCompanyFragment = new AddInvCompanyFragment();
        addInvPersonFragment.setArguments(bundle);
        addInvCompanyFragment.setArguments(bundle);
        switchContent(addInvPersonFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.personal) {
                    switchContent(addInvPersonFragment);
                    addInvListener.onSwitchAddInvChildContent(0);
                } else if (checkedId == R.id.company) {
                    switchContent(addInvCompanyFragment);
                    addInvListener.onSwitchAddInvChildContent(1);
                }
            }
        });
        return mRootView;
    }

    private void switchContent(ShopBaseFragment to) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
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

    public void setPersonInvAdress(ShopAddressListBean.DatasBean.AddressListBean adress) {
        addInvPersonFragment.setAddress(adress);
    }

    public String getPersonInvContent() {
        return addInvPersonFragment.getInvContent();
    }

    public String getType() {
        return addInvCompanyFragment.getType();
    }

    public String getCNRecId() {
        return addInvCompanyFragment.getCNRecId();
    }

    public String CNinv_title() {
        return addInvCompanyFragment.getCNinv_title();
    }

    public String getCNSbh() {
        return addInvCompanyFragment. getCNSbh();
    }

    public String getCNBank() {
        return addInvCompanyFragment.getCNBank();
    }

    public String getCNBanknum() {
        return addInvCompanyFragment.getCNBanknum();
    }

    public String getCNComtel() {
        return addInvCompanyFragment.getCNComtel();
    }

    public String getCNComaddr() {
        return addInvCompanyFragment.getCNComaddr();
    }

    public String getCNInv_content() {
        return addInvCompanyFragment.getCNInv_content();
    }

    public void setCompanyInvAdress(ShopAddressListBean.DatasBean.AddressListBean companyInvAdress) {
        addInvCompanyFragment.setInvAdress(companyInvAdress);
    }

    public interface AddInvListener{
        void onSwitchAddInvChildContent(int tab);
    }

}
