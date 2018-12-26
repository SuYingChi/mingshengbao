package com.msht.minshengbao.androidShop.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.InvListAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseFragment;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.InvItemBean;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IGetInvListView;
import com.msht.minshengbao.androidShop.viewInterface.IdeleteInvItemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NeedInvFrgment extends ShopBaseFragment implements IGetInvListView, InvListAdapter.InvListListener,IdeleteInvItemView  {

    private InvFrgmentListener invFrgmentListener;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    private List<InvItemBean> invList = new ArrayList<InvItemBean>();
    private InvListAdapter invAdapter;
    private String deleteInvId;



    @Override
    protected int setLayoutId() {
        return R.layout.need_inv;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       super.onCreateView(inflater, container, savedInstanceState);
        invAdapter = new InvListAdapter(getContext(), invList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcl.setLayoutManager(layoutManager);
        rcl.setAdapter(invAdapter);
        rcl.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        ShopPresenter.getInvList(this);
       return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        invFrgmentListener= (InvFrgmentListener)getActivity();

    }

    @Override
    public void onGetInvList(String s) {
        try {
            invList.clear();
            JSONObject obj = new JSONObject(s);
            JSONArray array = obj.optJSONObject("datas").optJSONArray("invoice_list");
            for(int i=0;i<array.length();i++){
                JSONObject objj = array.optJSONObject(i);
                String inv_id = objj.optString("inv_id");
                String inv_title = objj.optString("inv_title");
                String inv_code = objj.optString("inv_code");
                String inv_content = objj.optString("inv_content");
                InvItemBean bean = new InvItemBean(inv_content, false, inv_id, inv_title, inv_code);
                invList.add(bean);
            }
            invAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeleteItem(int position) {
        deleteInvId = invList.get(position).getInv_id();
        PopUtil.showComfirmDialog(getContext(), null, "点击确认删除该条发票内容", "cancel", "ok", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopPresenter.deleteInvItem(NeedInvFrgment.this);
            }
        },false);
    }

    @Override
    public void onCheckChange(boolean isChecked, int position) {
        InvItemBean bean = invList.get(position);
        if (!bean.isCheck() && isChecked) {
            for (int i = 0; i < invList.size(); i++) {
                InvItemBean bean2 = invList.get(i);
                if (bean2.isCheck() && i != position) {
                    bean2.setCheck(false);
                    invList.set(i, bean2);
                    break;
                }
            }
            bean.setCheck(true);
            invList.set(position,bean);
            invAdapter.notifyDataSetChanged();
            invFrgmentListener.onSelectedInv(bean);
        }
    }

    @Override
    public void onAddNewInv() {
        invFrgmentListener.onAddNewInv();
    }

    @Override
    public void onDeleteInvSuccess(String s) {
        PopUtil.showAutoDissHookDialog(getContext(),"删除发票条目",0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ShopPresenter.getInvList(NeedInvFrgment.this);
            }
        },1500);
    }

    @Override
    public String getInvId() {
        return deleteInvId;
    }



    public interface InvFrgmentListener{

        void onAddNewInv();

        void onSelectedInv(InvItemBean selectedInvItem);
    }
}
