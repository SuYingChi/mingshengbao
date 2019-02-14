package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

import java.util.List;

/**
 * @Description RRecyclerAdapter封装
 * @Result 1.只有一种item的情况下，缓存的ViewHolder的数目为RecyclerView在滑动过程中所能在一屏内容纳的最大item个数+2
 * 2.有至少两种item显示的情况下，每种item的ViewHolder的缓存个数为单种item在一屏内最大显示个数+1
 */
public abstract class HaveHeadRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {

    protected Context context;
    protected List<T> datas;
    protected int layoutId;
    protected int head_layoutId = Integer.MIN_VALUE;
    protected String monetary_unit;
    protected OnItemClickListener onItemClickListener;//像listview一样添加点击事件
    protected MyApplication application = MyApplication.getInstance();
    private T headData;


    public HaveHeadRecyclerAdapter(Context context) {
        this.context = context;
        this.monetary_unit = context.getResources().getString(R.string.monetary_unit);
    }

    public HaveHeadRecyclerAdapter(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        this.monetary_unit = context.getResources().getString(R.string.monetary_unit);
    }

    public HaveHeadRecyclerAdapter(Context context, int layoutId, List<T> datas) {
        this.context = context;
        this.layoutId = layoutId;
        this.datas = datas;
        this.monetary_unit = context.getResources().getString(R.string.monetary_unit);
    }

    public void setHead_layoutId(int head_layoutId,T headData) {
        this.head_layoutId = head_layoutId;
        this.headData = headData;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        if(datas==null){
            return 0;
        }else if(head_layoutId!=Integer.MIN_VALUE){
            return  datas.size()+1;
        }else {
           return datas.size();
        }
    }
//多个type 使用第一个构造，重写该方法 根据不同type return RecyclerHolder
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Integer.MIN_VALUE) {
            View headitemView = LayoutInflater.from(context).inflate(head_layoutId, parent, false);
            return  new RecyclerHolder(context, headitemView);
        }else {
            //这么写会导致item宽度当为matchparent的时候显示不对,
            //View itemView = View.inflate(parent.getViewContext(), layoutId, null);
            //规范写法
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            return new RecyclerHolder(context, itemView);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position ==0&&head_layoutId !=Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder,final int position) {
        if(getItemViewType(position) == Integer.MIN_VALUE){
            convert(holder, headData, position);
            //有头布局
        } else if(head_layoutId !=Integer.MIN_VALUE){
            convert(holder, datas.get(position-1),position);
        }//子类没有头布局，当一般recycleview使用
        else {
            convert(holder, datas.get(position),position);
        }
    }

//多个type 使用holder.getitemviewtype区分
    public abstract void convert(RecyclerHolder holder, final T t, final int position);

    /**
     * 点击事件接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * 设置点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}