package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ComfirmShopGoodBean;
import com.msht.minshengbao.androidShop.shopBean.OrderVoucherBean;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

import java.util.List;

public class OrdersGoodChildListAdapter extends MyHaveHeadAndFootRecyclerAdapter<ComfirmShopGoodBean.GoodsBean> {

    private final OrdersChildListlistener ordersChildListlistener;
    private String storeName;
    private int storeDoorService;
    private boolean isNeedEtVisible;
    private boolean isHasVoucher;
    private String selectedVoucherId;
    private String selectedVoucherPrice;
    private String selectedVoucherDesc;
    private List<OrderVoucherBean> voucherList;

    public OrdersGoodChildListAdapter(Context context, OrdersChildListlistener ordersChildListlistener) {
        super(context, R.layout.item_orders_child_car_list);
        this.ordersChildListlistener = ordersChildListlistener;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Integer.MIN_VALUE) {
            View headitemView = LayoutInflater.from(context).inflate(head_layoutId, parent, false);
            return new RecyclerHolder(context, headitemView);
        } else if (viewType == Integer.MAX_VALUE) {
            View footItemView = LayoutInflater.from(context).inflate(foot_layoutId, parent, false);
            return new RecyclerHolder(context, footItemView);
        } else if (viewType == 2) {
            View footItemView = LayoutInflater.from(context).inflate(R.layout.store_dore_layout, parent, false);
            return new RecyclerHolder(context, footItemView);
        } else if (viewType == 3) {
            View footItemView = LayoutInflater.from(context).inflate(R.layout.order_voucher, parent, false);
            return new RecyclerHolder(context, footItemView);
        } else {
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            return new RecyclerHolder(context, itemView);
        }

    }

    @Override
    public void convert(RecyclerHolder holder, final ComfirmShopGoodBean.GoodsBean goodsBean, final int position) {
        if (holder.getItemViewType() == Integer.MIN_VALUE) {
            TextView tvStore = holder.getView(R.id.store);
            tvStore.setText(storeName);
        } else if (holder.getItemViewType() == Integer.MAX_VALUE) {
            EditText et = holder.getView(R.id.et_address);
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    ordersChildListlistener.onMessaged(s.toString());
                }
            });
        } else if (holder.getItemViewType() == 2) {
            RadioGroup radioGroup = holder.getView(R.id.radio_group);
            EditText et = holder.getView(R.id.et_userid);
            if (isNeedEtVisible) {
                et.setVisibility(View.VISIBLE);
            } else {
                et.setVisibility(View.GONE);
            }
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    ordersChildListlistener.onInputUserId(s.toString());
                }
            });
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        ordersChildListlistener.onNoHasFocus();
                    }
                }
            });
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.no_need_door) {
                        ordersChildListlistener.etVisible(false);
                    } else if (checkedId == R.id.need_door) {
                        ordersChildListlistener.etVisible(true);
                    }
                }
            });
        } else if (holder.getItemViewType() == 3) {
            holder.setText(R.id.desc, selectedVoucherDesc);
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ordersChildListlistener.onShowVoucherDialog(voucherList);
                }
            });
        } else {
            holder.setImage(R.id.iv, goodsBean.getGoods_image_url());
            holder.setText(R.id.name, goodsBean.getGoods_name());
            holder.setText(R.id.price, StringUtil.getPriceSpannable12String(context, goodsBean.getGoods_price(), R.style.big_money, R.style.big_money));
            holder.setText(R.id.num, "X" + goodsBean.getGoods_num());
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ordersChildListlistener.onGoGoodDetail(goodsBean.getGoods_id());
                }
            });
        }
    }

    public void setStoreName(String store_name) {
        this.storeName = store_name;
    }

    public void setStoreDoorService(int storeDoorService) {
        this.storeDoorService = storeDoorService;
    }

    public void setIsNeedEtVisible(boolean isNeedEtVisible) {
        this.isNeedEtVisible = isNeedEtVisible;
    }

    public void isHasVoucher(boolean isHasVoucher) {
        this.isHasVoucher = isHasVoucher;
    }



    public void setVoucherinfo(String voucherTid, String voucherPrice, String voucherDesc) {
        selectedVoucherId = voucherTid;
        selectedVoucherPrice = voucherPrice;
        selectedVoucherDesc = voucherDesc;
    }

    public void setVoucherList(List<OrderVoucherBean> voucherList) {
        this.voucherList = voucherList;
    }

    public interface OrdersChildListlistener {
        void onMessaged(String message);

        void onGoGoodDetail(String goods_id);

        void etVisible(boolean etVisible);

        void onInputUserId(String s);

        void onNoHasFocus();

        void onShowVoucherDialog(List<OrderVoucherBean> voucherList);
    }

    @Override
    public int getItemCount() {
        return (datas == null ? 0 : storeDoorService == 1 ? isHasVoucher ? datas.size() + 4 : datas.size() + 3 : isHasVoucher ? datas.size() + 3 : datas.size() + 2);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (getItemViewType(position) == Integer.MIN_VALUE) {
            convert(holder, null, position);
        } else if (getItemViewType(position) == Integer.MAX_VALUE) {
            convert(holder, null, position);
        } else if (getItemViewType(position) == 2) {
            convert(holder, null, position);
        } else if (getItemViewType(position) == 3) {
            convert(holder, null, position);
        } else {
            convert(holder, datas.get(position - 1), position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //店铺名布局
        if (position == 0 && head_layoutId != Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        if (isHasVoucher) {
            if (storeDoorService == 1) {
                //有上门服务时的店家备注布局
                if (position == datas.size() + 3 && foot_layoutId != Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                }//代金券布局
                else if (position == datas.size() + 2) {
                    return 3;
                } else if (position == datas.size() + 1) {
                    //上门服务布局
                    return 2;
                } else {
                    //商品布局
                    return 1;
                }
            } else {
                if (position == datas.size() + 2 && foot_layoutId != Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                } else if (position == datas.size() + 1) {
                    return 3;
                } else {
                    //商品布局
                    return 1;
                }
            }
        } else {
            if (storeDoorService == 1) {
                //有上门服务时的店家备注布局
                if (position == datas.size() + 2 && foot_layoutId != Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                } else if (position == datas.size() + 1) {
                    //上门服务布局
                    return 2;
                } else {
                    //商品布局
                    return 1;
                }
            } else {
                if (position == datas.size() + 1 && foot_layoutId != Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                } else {
                    //商品布局
                    return 1;
                }
            }
        }
    }
}
