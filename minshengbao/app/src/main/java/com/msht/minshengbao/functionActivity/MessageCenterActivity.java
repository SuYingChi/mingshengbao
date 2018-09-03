package com.msht.minshengbao.functionActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 *
 * @author hong
 * @date 2016/05/10
 */
public class MessageCenterActivity extends BaseActivity implements View.OnClickListener {
    private XListView mListView;
    private ListViewAdapter mAdapter;
    private RelativeLayout relayout;
    private LinearLayout layoutOrderType, layoutNotice;
    private View rNotData;
    private View view1,view2;
    private RelativeLayout layoutDelect;
    private RelativeLayout layoutCancel;
    private ImageView imgEdit;
    private TextView tvType1, tvType2;
    private String  userId, password,type="1";
    private boolean refreshType=false;
    private boolean dataType=false;
    private boolean visibleCheck = false;
    public  boolean isMulChoice = false;
    /**
     * MY_ACTION 广播跳转意图
     */
    public  static final String MY_ACTION = "ui";
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private int pageNo;
    private int pageIndex=0;
    private int pos=-1;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> arraylist=new ArrayList<String>() ;
    private ArrayList<HashMap<String, Boolean>> checkList = new ArrayList<HashMap<String, Boolean>>();
    private String idLIst="";
    private final ListInformationHandler listinformHandler=new ListInformationHandler(this);
    private final DeleteHandler deleteHandler=new DeleteHandler(this);
    private static class ListInformationHandler extends Handler{
        private WeakReference<MessageCenterActivity> mWeakReference;
        private ListInformationHandler(MessageCenterActivity activity) {
            mWeakReference = new WeakReference<MessageCenterActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final MessageCenterActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            Intent broadcast=new Intent();
                            broadcast.setAction(MY_ACTION);
                            broadcast.putExtra("broadcast", "2");
                            activity.sendBroadcast(broadcast);
                            activity.setResult(2);
                            if (!activity.refreshType){
                                activity.mListView.stopRefresh(true);
                            }else {
                                activity.mListView.stopLoadMore();
                            }
                            if (!activity.dataType){
                                if(activity.jsonArray.length()>0){
                                    if (activity.pageNo==1){
                                        activity.mList.clear();
                                        activity.checkList.clear();
                                    }
                                }
                            }else {
                                if (activity.pageNo==1){
                                    activity.mList.clear();
                                    activity.checkList.clear();
                                }
                            }
                            activity.onInfomationData();
                        }else {
                            ToastUtil.ToastText(activity.context,error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class DeleteHandler extends Handler{
        private WeakReference<MessageCenterActivity> mWeakReference;
        public  DeleteHandler(MessageCenterActivity activity) {
            mWeakReference = new WeakReference<MessageCenterActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final MessageCenterActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:

                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onDelectSuccess();
                        }else {
                            activity.failure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.arraylist.clear();
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void failure(String error) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onDelectSuccess() {
        idLIst="";
        if (isMulChoice){
            arraylist.clear();
            loadData(1);
        }else {
            mList.remove(pos);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void onInfomationData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String types=jsonObject.getString("type");
                String title=jsonObject.getString("title");
                String content = jsonObject.getString("content");
                String flag=jsonObject.getString("flag");
                String time = jsonObject.getString("time");
                HashMap<String, Boolean> check = new HashMap<String, Boolean>();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("type",types);
                map.put("title",title);
                map.put("content", content);
                map.put("flag",flag);
                map.put("time", time);
                check.put("ischeck",false);
                mList.add(map);
                checkList.add(check);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mList.size()==0){
            mAdapter.notifyDataSetChanged();
            rNotData.setVisibility(View.VISIBLE);
        }else {
            rNotData.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int positions=position-1;
                idLIst = mList.get(positions).get("id");
                pos = positions;
                showdelete();
                return true;
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        context=this;
        setCommonHeader("消息中心");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(context, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(context, SharedPreferencesUtil.Password,"");
        imgEdit =(ImageView)findViewById(R.id.id_right_img);
        imgEdit.setVisibility(View.VISIBLE);
        imgEdit.setImageResource(R.drawable.delete2x);
        relayout=(RelativeLayout)findViewById(R.id.id_layout_edit);
        rNotData =findViewById(R.id.id_nodata_view);
        view1=findViewById(R.id.id_view1);
        view2=findViewById(R.id.id_view2);
        tvType1 =(TextView)findViewById(R.id.id_type1);
        tvType2 =(TextView)findViewById(R.id.id_type2);
        TextView tvNotData =(TextView)findViewById(R.id.id_tv_nodata);
        tvNotData.setText("亲，您还没有消息哦！");
        layoutDelect =(RelativeLayout)findViewById(R.id.id_re_delect);
        layoutCancel =(RelativeLayout)findViewById(R.id.id_re_cancel);
        layoutOrderType =(LinearLayout)findViewById(R.id.id_li_ordertype);
        layoutNotice =(LinearLayout)findViewById(R.id.id_li_gonggao);
        mListView = (XListView) findViewById(R.id.lv_swipe_listview);
        mAdapter= new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setPullLoadEnable(true);
        customDialog.show();
        initData();
        initEvent();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int positions=position-1;
                String mId=mList.get(positions).get("id");
                String types=mList.get(positions).get("type");
                Intent intent=new Intent(context,MessageDetailActivity.class);
                intent.putExtra("id",mId);
                intent.putExtra("type",types);
                startActivity(intent);
            }
        });
    }
    private void initData() {
       customDialog.show();
        loadData(1);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                refreshType=false;
                dataType=false;
                loadData(1);
            }
            @Override
            public void onLoadMore() {
                refreshType=true;
                loadData(pageIndex + 1);
            }
        });
    }
    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String validateURL = UrlUtil.INFORM_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("page",pageNum);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,listinformHandler);
    }
    private void initEvent() {
        imgEdit.setOnClickListener(this);
        layoutDelect.setOnClickListener(this);
        layoutCancel.setOnClickListener(this);
        layoutNotice.setOnClickListener(this);
        layoutOrderType.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_right_img:
                relayout.setVisibility(View.VISIBLE);
                visibleCheck = true;
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.id_re_cancel:
                relayout.setVisibility(View.GONE);
                visibleCheck = false;
                for (int i=0;i<mList.size();i++){
                    checkList.get(i).put("ischeck",false);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.id_re_delect:
                isMulChoice=true;
                onDeleteItem();
                break;
            case R.id.id_li_ordertype:
                type="1";
                onSetType();
                break;
            case R.id.id_li_gonggao:
                type="2";
                onSetTypeTwo();
                break;
            default:
                break;
        }
    }

    private void onSetType() {
        customDialog.show();
        dataType=true;
        view1.setBackgroundResource(R.color.colorOrange);
        view2.setBackgroundResource(R.color.white);
        tvType1.setTextColor(0xfff96331);
        tvType2.setTextColor(0xff555555);
        loadData(1);
    }

    private void onSetTypeTwo() {
        customDialog.show();
        dataType=true;
        view1.setBackgroundResource(R.color.white);
        view2.setBackgroundResource(R.color.colorOrange);
        tvType1.setTextColor(0xff555555);
        tvType2.setTextColor(0xfff96331);
        loadData(1);
    }

    private void showdelete() {
        LayoutInflater inflater=LayoutInflater.from(this);
        LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.self_make_dialog,null);
        final Dialog dialog=new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        final RelativeLayout btnDelete=(RelativeLayout)layout.findViewById(R.id.id_query_layout);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMulChoice=false;
                onDeleteData();
                dialog.dismiss();
            }
        });
    }
    private void onDeleteItem() {
        for (int i=0;i<checkList.size();i++){
            if (checkList.get(i).get("ischeck")){
                arraylist.add(mList.get(i).get("id"));
            }
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : arraylist) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        idLIst=result.toString();
        if (matchjudge(idLIst)){
            onDeleteData();
        }
    }
    private boolean matchjudge(String idLIst) {
        if (TextUtils.isEmpty(idLIst)||idLIst.equals("")){
            new PromptDialog.Builder(this)
                    .setTitle("民生宝")
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage("请选择要删除的消息！")
                    .setButton1("确定", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            return false;
        }else {
            return true;
        }
    }
    private void onDeleteData() {
        customDialog.show();
        String validateURL = UrlUtil.INFORM_DELETE;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",idLIst);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,deleteHandler);
    }

    public  class ListViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private Context mContext = null;
        public ListViewAdapter(Context context) {
            this.mContext = context;

        }
        @Override
        public int getCount() {
            return mList.size();
        }
        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final int i=position;
            ViewHolder item;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message_view, parent, false);
                item = new ViewHolder();
                item.itemTime =(TextView)convertView.findViewById(R.id.id_inform_time);
                item.itemLeftTxt = (TextView) convertView.findViewById(R.id.id_item_title);
                item.itemContent =(TextView)convertView.findViewById(R.id.id_item_content);
                item.itemCb =(CheckBox)convertView.findViewById(R.id.id_checkBox);
                convertView.setTag(item);
            } else {                // 有直接获得ViewHolder
                item = (ViewHolder) convertView.getTag();
            }
            if(visibleCheck){
                item.itemCb.setVisibility(View.VISIBLE);
            }else{
                item.itemCb.setVisibility(View.GONE);
            }
            item.itemContent.setText(mList.get(position).get("content"));
            item.itemLeftTxt.setText(mList.get(position).get("title"));
            item.itemTime.setText(mList.get(position).get("time"));
            item.itemCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkList.get(position).put("ischeck",isChecked);
                }
            });
            item.itemCb.setChecked( checkList.get(position).get("ischeck"));
            return convertView;
        }
        private class ViewHolder {
            CheckBox itemCb;
            TextView itemLeftTxt;
            TextView itemContent;
            TextView itemTime;
        }
    }

    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
