package com.msht.minshengbao.androidShop.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.ShopAddressListActivity;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseFragment;
import com.msht.minshengbao.androidShop.customerview.SelectInvContentDialog;
import com.msht.minshengbao.androidShop.shopBean.InvContentItemBean;
import com.msht.minshengbao.androidShop.shopBean.ShopAddressListBean;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.IGetInvContentView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddInvCompanyFragment extends ShopBaseFragment implements IGetInvContentView {
    @BindView(R.id.location_name)
    TextView tvLocName;
    @BindView(R.id.location)
    TextView tvLoca;
    @BindView(R.id.tv_inv_content)
    TextView tvInvContent;
    @BindView(R.id.et_inv_title)
    EditText etInvTitle;
    @BindView(R.id.et_sbh)
    EditText etSbh;
    @BindView(R.id.et_bank)
    EditText etBank;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_num)
    EditText etNum;
    public static final int REQUESTCODE_INV_COMPANY = 500;
    private String amount;
    private ShopAddressListBean.DatasBean.AddressListBean address;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.radio_group)
    RadioGroup rg;
    private String type="1";
    private SelectInvContentDialog selectInvContentDialog;
    private List<InvContentItemBean> invContentList = new ArrayList<InvContentItemBean>();

    @Override
    protected int setLayoutId() {
        return R.layout.inv_company_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        amount = bundle.getString("data");
        address = (ShopAddressListBean.DatasBean.AddressListBean) bundle.getSerializable("address");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        tvAmount.setText(StringUtil.getPriceSpannable12String(getContext(),amount,R.style.big_money,R.style.big_money));
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.normal){
                    type="1";
                }else if(checkedId==R.id.zengzhi){
                    type="2";
                }
            }
        });
        return mRootView;
    }


    public String getType() {
        return type;
    }

    public String getCNComaddr() {
        return etLocation.getText().toString();
    }

    public String getCNRecId() {
        return address.getAddress_id();
    }

    @OnClick({R.id.rlt_location, R.id.ll_inv_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlt_location:
                Intent intent = new Intent(getActivity(), ShopAddressListActivity.class);
                //如果直接startActivityForResult会导致requestcode为默认值
                getActivity().startActivityForResult(intent, REQUESTCODE_INV_COMPANY);
                break;
            case R.id.ll_inv_content:
                showInvDialog();
            default:
                break;
        }
    }


        private void showInvDialog() {
            if (isVisible() && selectInvContentDialog == null) {
                selectInvContentDialog = new SelectInvContentDialog(getContext(), this, invContentList);
                selectInvContentDialog.show();
            } else if (isVisible() && !selectInvContentDialog.isShowing()) {
                selectInvContentDialog.show();
            }
        }


    public void setInvAdress(ShopAddressListBean.DatasBean.AddressListBean companyInvAdress) {
        this.address = companyInvAdress;
        tvLocName.setText(address.getTrue_name());
        tvLoca.setText(String.format("%s%s", address.getArea_info(), address.getAddress()));
    }

    @Override
    public void onGetInvContentList(String s) {
        try {
            InvContentItemBean invItemBean;
            JSONObject obj = new JSONObject(s);
            JSONObject data = obj.optJSONObject("datas");
            JSONArray array = data.optJSONArray("invoice_content_list");
            for (int i = 0; i < array.length(); i++) {
                String content = array.optString(i);
                if (i == 3) {
                    invItemBean = new InvContentItemBean(content, true);
                } else {
                    invItemBean = new InvContentItemBean(content, false);
                }
                invContentList.add(invItemBean);
            }
            selectInvContentDialog.notifyRcl();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSelectedInvContentItem(int position) {
        for (int i = 0; i < invContentList.size(); i++) {
            if (i == position) {
                invContentList.get(i).setSelected(true);
                String invContent = invContentList.get(i).getContent();
                tvInvContent.setText(invContent);
            } else {
                invContentList.get(i).setSelected(false);
            }
        }
        selectInvContentDialog.notifyRcl();
    }

    public String getCNinv_title() {
        return etInvTitle.getText().toString();
    }

    public String getCNSbh() {
        return etSbh.getText().toString();
    }

    public String getCNBank() {
        return etBank.getText().toString();
    }

    public String getCNBanknum() {
        return etAccount.getText().toString();
    }

    public String getCNInv_content() {
        return tvInvContent.getText().toString();
    }

    public String getCNComtel() {
        return etNum.getText().toString();
    }
}
