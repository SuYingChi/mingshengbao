package com.msht.minshengbao.androidShop.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class AddInvPersonFragment extends ShopBaseFragment implements IGetInvContentView {
    public static final int REQUESTCODE_INV_PERSON = 300;
    private ShopAddressListBean.DatasBean.AddressListBean address;
    private String amount;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.location_name)
    TextView tvLocName;
    @BindView(R.id.location)
    TextView tvLoca;
    @BindView(R.id.tv_inv)
    TextView tvInv;
    @BindView(R.id.inv_title)
    EditText etInvTitle;
    private boolean hasInvContent = false;
    private List<InvContentItemBean> invContentList = new ArrayList<InvContentItemBean>();
    private SelectInvContentDialog selectInvContentDialog;
    private String invContent;


    @Override
    protected int setLayoutId() {
        return R.layout.inv_person_detail;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            amount = bundle.getString("data");
            address = (ShopAddressListBean.DatasBean.AddressListBean) bundle.getSerializable("address");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        tvAmount.setText(StringUtil.getPriceSpannable12String(getContext(), amount, R.style.big_money, R.style.big_money));
        tvLocName.setText(address.getTrue_name());
        tvLoca.setText(String.format("%s%s", address.getArea_info(), address.getAddress()));
        return mRootView;
    }

    @OnClick({R.id.rlt_location, R.id.ll_inv_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlt_location:
                Intent intent = new Intent(getActivity(), ShopAddressListActivity.class);
                //如果直接startActivityForResult会导致requestcode为默认值
                getActivity().startActivityForResult(intent, REQUESTCODE_INV_PERSON);
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

    public void setAddress(ShopAddressListBean.DatasBean.AddressListBean address) {
        this.address = address;
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
                invContent = invContentList.get(i).getContent();
                tvInv.setText(invContent);
            } else {
                invContentList.get(i).setSelected(false);
            }
        }
        selectInvContentDialog.notifyRcl();
    }


    public String getInvContent() {
        return invContent;
    }
}
