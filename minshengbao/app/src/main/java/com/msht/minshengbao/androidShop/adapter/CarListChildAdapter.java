package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.shopBean.ShopStoreBean;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CarListChildAdapter extends MyHaveHeadViewRecyclerAdapter<JSONObject> {


    private boolean editstatus = true;
    private boolean isGoodCbCheckedAll = false;
    private int selecteGoodNum = 0;
    private boolean isCbStoreChecked = false;
    private boolean isCbStoreStartNotify = false;
    private boolean isCbGoodStartNotify = false;


    private boolean isAllSelectNotify = false;
    private boolean isSelectAll = false;
    private CarListChildListener carListChildListener;
    private ShopStoreBean storeBean;
    private boolean initUnselectState = false;


    public CarListChildAdapter(Context context) {
        super(context, R.layout.item_child_car_list);
    }

    @Override
    public void convert(final RecyclerHolder holder, final JSONObject obj, final int position) {

        if (holder.getItemViewType() == Integer.MIN_VALUE) {
            TextView tvStore = holder.getView(R.id.store);
            String storeName = obj.optString("store_name");
            String storeId = obj.optString("store_id");
            storeBean = new ShopStoreBean(storeName, storeId);
            tvStore.setText(storeName);
            CheckBox cbSelectStore = holder.getView(R.id.select_store);
            //店铺选择状态改变的时候根据情况发起notifyDataSetChanged() 让商品的CheckBox发生改变
            cbSelectStore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isCbStoreChecked = isChecked;
                    if (!initUnselectState && !isAllSelectNotify) {
                        //选择店铺 商品还没有全选 需要刷新,
                        if ((isCbStoreChecked && !isGoodCbCheckedAll)) {
                            isCbStoreStartNotify = true;
                            isCbGoodStartNotify = false;
                            notifyDataSetChanged();
                            carListChildListener.onCheckStoreItem(storeBean);
                        }// 取消选择店铺，但是商品全选了 需要刷新
                        else if (!isCbStoreChecked && isGoodCbCheckedAll) {
                            isCbStoreStartNotify = true;
                            isCbGoodStartNotify = false;
                            notifyDataSetChanged();
                            carListChildListener.onUncheckStoreItem(storeBean);
                        } //此时店铺变为未选，是因为取消选择了一个商品
                        else if (!isCbStoreChecked && !isGoodCbCheckedAll) {
                            carListChildListener.onUnCheckGoodAndunCheckStoreItem(storeBean);
                        } else {
                            carListChildListener.onCheckStoreItem(storeBean);
                        }
                    }
                }
            });
            //刷新购物车，所有货物设为未选中状态
            if (initUnselectState) {
                cbSelectStore.setChecked(obj.optBoolean("storecheck", false));
            }
            //底部的全选按钮选中状态的改变发起调用的notifyDataSetChanged()的时候走进来 isAllSelectNotify =true 标志开始notify子列表
            //全选触发，此时店铺按钮是未选，设置为已选
            else if (isAllSelectNotify && isSelectAll && !isCbStoreStartNotify && !isCbGoodStartNotify) {
                if (!isCbStoreChecked) {
                    cbSelectStore.setChecked(true);
                }
            }
            //更改店铺已选为不选
            else if (isAllSelectNotify && !isSelectAll && !isCbStoreStartNotify && !isCbGoodStartNotify) {
                if (isCbStoreChecked) {
                    cbSelectStore.setChecked(false);
                }
            } else if (!isAllSelectNotify && !isCbStoreStartNotify && isCbGoodStartNotify) {
                //商品的checkbox那边选中状态的更改调用notifyDataSetChanged()的时候走进来 店铺没被选，但是商品全选了 设置店铺为选中，此时走进onCheckedChanged(true),但因为isCbStoreChecked为true，没有再次触发notifyDataSetChanged();
                if (!isCbStoreChecked && isGoodCbCheckedAll) {
                    cbSelectStore.setChecked(true);
                }
                //选择店铺 有商品取消了全选 需要店铺设置为未选中,同時全选按钮设为未选，此时全选按钮不触发notify
                else if (isCbStoreChecked && !isGoodCbCheckedAll) {
                    cbSelectStore.setChecked(false);
                }
            }
        } else {
            String goodsName = obj.optString("goods_name");
            final String goodsid = obj.optString("goods_id");
            TextView tvName = holder.getView(R.id.name);
            tvName.setText(goodsName);
            String goodsSpec = obj.optString("goods_spec");
            TextView tvJingle = holder.getView(R.id.jingle);
            if(TextUtils.isEmpty(goodsSpec)){
                tvJingle.setVisibility(View.GONE);
            }else {
                tvJingle.setVisibility(View.VISIBLE);
                tvJingle.setText(goodsSpec);
            }
            TextView tvRemainNum = holder.getView(R.id.remain_num);
            String goodsStorage = obj.optString("goods_storage");
            if (TextUtils.isEmpty(goodsStorage)) {
                goodsStorage = "商品已下架或不支持购买";
            }
            tvRemainNum.setText(String.format("库存量：%s件", goodsStorage));
            String goodsPrice = obj.optString("goods_price");
            TextView tvGoodsPrice = holder.getView(R.id.price);
            tvGoodsPrice.setText(StringUtil.getPriceSpannable12String(context, goodsPrice, R.style.small_money, R.style.small_money));
            if (editstatus) {
                tvGoodsPrice.setVisibility(View.VISIBLE);
            } else {
                tvGoodsPrice.setVisibility(View.INVISIBLE);
            }
            final CheckBox cbSelect = holder.getView(R.id.select);
            //点击选择商品的时候根据情况发起notifyDataSetChanged() 让店铺的CheckBox发生改变
            cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!initUnselectState && !isAllSelectNotify) {
                        //未选择店铺 商品全选了 需要刷新,或者 店铺被选，但是商品取消全选了 需要刷新
                        if (!isCbStoreChecked && isChecked) {
                            selecteGoodNum++;
                            LogUtils.e("------onCheckGoodItem===" + obj+"------childPosition-----"+(position - 1));
                            carListChildListener.onCheckGoodItem(obj, storeBean, position - 1);
                            if (selecteGoodNum == getDatas().size() - 1) {
                                isGoodCbCheckedAll = true;
                                isCbGoodStartNotify = true;
                                isCbStoreStartNotify = false;
                                notifyDataSetChanged();
                            }
                        } else if (isCbStoreChecked && !isChecked) {
                            selecteGoodNum--;
                            carListChildListener.onUncheckGoodItem(obj, storeBean, position - 1);
                            isGoodCbCheckedAll = false;
                            isCbGoodStartNotify = true;
                            isCbStoreStartNotify = false;
                            notifyDataSetChanged();
                        }
                        //选择了店铺，但商品还没有全选，notifydatasetchange的时候 商品的CheckBox更改为选中状态，回调进来，更改selecteGoodNum和isGoodCbCheckedAll的值
                        else if (isCbStoreChecked && isChecked) {
                            selecteGoodNum++;
                            carListChildListener.onCheckGoodItem(obj, storeBean, position - 1);
                            if (selecteGoodNum == getDatas().size() - 1) {
                                isGoodCbCheckedAll = true;
                            }
                        }
                        //取消选择了店铺，选中的商品还需要更改为未选中 回调进来，更改selecteGoodNum和isGoodCbCheckedAll的值
                        else {
                            selecteGoodNum--;
                            carListChildListener.onUncheckGoodItem(obj, storeBean, position - 1);
                            isGoodCbCheckedAll = false;
                        }
                    } else if (!initUnselectState) {
                        //全选，先选择了店铺，但商品还没有全选，notifydatasetchange的时候 商品的CheckBox更改为选中状态，回调进来，更改selecteGoodNum和isGoodCbCheckedAll的值
                        if (isChecked) {
                            selecteGoodNum++;
                            if (selecteGoodNum == getDatas().size() - 1) {
                                isGoodCbCheckedAll = true;
                            }
                        }
                        //全选，先取消选择了店铺，选中的商品还需要更改为未选中 回调进来，更改selecteGoodNum和isGoodCbCheckedAll的值，
                        else {
                            selecteGoodNum--;
                            isGoodCbCheckedAll = false;
                        }
                    }
                }
            });
            //刷新购物车，所有货物设为未选中状态
            if (initUnselectState) {
               // cbSelect.setChecked(false);
                cbSelect.setChecked(obj.optBoolean("goodcheck", false));
            }
            //底部的全选按钮选中状态的改变发起调用的notifyDataSetChanged()的时候走进来
            else if (isAllSelectNotify && isSelectAll && !isCbStoreStartNotify && !isCbGoodStartNotify) {
                if (!cbSelect.isChecked()) {
                    cbSelect.setChecked(true);
                }
            }
            //取消全选
            else if (isAllSelectNotify && !isSelectAll && !isCbStoreStartNotify && !isCbGoodStartNotify) {
                if (cbSelect.isChecked()) {
                    cbSelect.setChecked(false);
                }
            } else
                //店铺的checkbox那边选中状态的更改调用notifyDataSetChanged()的时候走进来
                //选择了店铺，但此时该商品还没被选中，则设置为选中
                if (!isAllSelectNotify && isCbStoreStartNotify && !isCbGoodStartNotify) {
                    if (isCbStoreChecked && !cbSelect.isChecked()) {
                        cbSelect.setChecked(true);
                    }
                    //取消选择了店铺，但此时该商品还是选中，则设置为未选中
                    else if (!isCbStoreChecked && cbSelect.isChecked()) {
                        cbSelect.setChecked(false);
                    }

                }
            final TextView tvNum = holder.getView(R.id.number);
            String goodsNum = obj.optString("goods_num");
            tvNum.setText(goodsNum);
            ImageView iv = holder.getView(R.id.iv);
            String goodsImageUrl = obj.optString("goods_image_url");
            GlideUtil.loadRemoteImg(context, iv, goodsImageUrl);
            if (obj.has("gift_list")) {
                JSONArray giftList = obj.optJSONArray("gift_list");
                List<JSONObject> list = JsonUtil.jsonArrayToList(giftList.toString());
            }

            LinearLayout tvReduce = holder.getView(R.id.ll_reduce);
            tvReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String goodNum = obj.optString("goods_num");
                    int num = Integer.valueOf(goodNum);
                    String goodsStorage = obj.optString("goods_storage");
                    int goodsStorageNum;
                    if (TextUtils.isEmpty(goodsStorage)) {
                        goodsStorageNum = -1;
                    } else {
                        goodsStorageNum = Integer.valueOf(goodsStorage);
                    }
                    if (goodsStorageNum == -1) {
                        PopUtil.showComfirmDialog(context, "提示", "商品已经下架", null, "好", null, null, true);
                    } else if (num == 1) {
                        PopUtil.showComfirmDialog(context, "提示", "请选择至少一件该商品", null, "好", null, null, true);
                    } /*else if (num > goodsStorageNum) {
                        PopUtil.showComfirmDialog(context, "提示", "库存量不足", null, "好", null, null, true);
                    }*/ else {
                        num--;
                        try {
                            obj.put("goods_num", num + "");
                            tvNum.setText(String.valueOf(num + ""));
                            if (cbSelect.isChecked()) {
                                carListChildListener.onCheckGoodItem(obj, storeBean, position - 1);
                            }
                            carListChildListener.onModifyItemNum(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            LinearLayout tvPlus = holder.getView(R.id.ll_plus);
            tvPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String goodNum = obj.optString("goods_num");
                    int num = Integer.valueOf(goodNum);
                    String goodsStorage = obj.optString("goods_storage");
                    int goodsStorageNum;
                    if (TextUtils.isEmpty(goodsStorage)) {
                        goodsStorageNum = -1;
                    } else {
                        goodsStorageNum = Integer.valueOf(goodsStorage);
                    }
                    if (goodsStorageNum == -1) {
                        PopUtil.showComfirmDialog(context, "提示", "商品已经下架", null, "好", null, null, true);
                    } else if (num >= goodsStorageNum) {
                        PopUtil.showComfirmDialog(context, "提示", "库存量不足", null, "好", null, null, true);
                    } else {
                        num++;
                        try {
                            obj.put("goods_num", num + "");
                            tvNum.setText(String.valueOf(num + ""));
                            if (cbSelect.isChecked()) {
                                carListChildListener.onCheckGoodItem(obj, storeBean, position - 1);
                            }
                            carListChildListener.onModifyItemNum(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    carListChildListener.onGotoGoodDetail(goodsid);
                }
            });
        }
        //刷新到最后一项了，重置标志位
        if (position == datas.size() - 1) {
            if (isAllSelectNotify) {
                setAllSelectNotify(isSelectAll, false, initUnselectState);
            } else if (initUnselectState) {
                initUnselectState = false;
            }
        }

    }

    public void editStatus() {
        editstatus = true;
    }

    public void finishStatus() {
        editstatus = false;
    }


    public void setAllSelectNotify(boolean isSelectAll, boolean isNotifyAdapter, boolean initUnselectState) {
        this.initUnselectState = initUnselectState;
        if (initUnselectState) {
            isGoodCbCheckedAll = false;
            selecteGoodNum = 0;
            isCbStoreChecked = false;
            isCbStoreStartNotify = false;
            isCbGoodStartNotify = false;
            isAllSelectNotify = false;
            this.isSelectAll = false;
        } else {
            this.isSelectAll = isSelectAll;
            isAllSelectNotify = isNotifyAdapter;
            isCbGoodStartNotify = false;
            isCbStoreStartNotify = false;
        }
    }

    public interface CarListChildListener {
        void onUncheckGoodItem(final JSONObject goodObject, final ShopStoreBean store, final int childPosition);

        void onUncheckStoreItem(final ShopStoreBean store);

        void onCheckStoreItem(final ShopStoreBean store);

        void onCheckGoodItem(final JSONObject goodObject,final ShopStoreBean storeBean,final int childPosition);

        void onUnCheckGoodAndunCheckStoreItem(final ShopStoreBean storeBean);

        void onModifyItemNum(final JSONObject goodObject);

        void onGotoGoodDetail(final String goodsid);
    }

    public void setCarListChildListener(CarListChildListener carListChildListener) {
        this.carListChildListener = carListChildListener;
    }
}
