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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.TypeConvertUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    private View relayout;
    private View layoutOrderType, layoutNotice;
    private View rNotData;
    private View view1,view2;
    private Button   btnDelete;
    private CheckBox selectBox;
    private TextView rightText;
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
    private int unreadPos=-1;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> arrayList =new ArrayList<String>() ;
    private ArrayList<HashMap<String, Boolean>> checkList = new ArrayList<HashMap<String, Boolean>>();
    private String idLIst="";
    private final ListInformationHandler listInformationHandler =new ListInformationHandler(this);
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
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
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
                            activity.onInformationData();
                        }else {
                            ToastUtil.ToastText(activity.context,error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
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
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onDeleteSuccess();
                        }else {
                            activity.arrayList.clear();
                            activity.failure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.arrayList.clear();
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
    private void onDeleteSuccess() {
        idLIst="";
        if (isMulChoice){
            arrayList.clear();
            mList.clear();
            checkList.clear();
            mAdapter.notifyDataSetChanged();
            loadData(1);
        }else {
            mList.remove(pos);
            checkList.remove(pos);
            mAdapter.notifyDataSetChanged();
            loadData(1);
        }
    }
    private void onInformationData() {
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
                if (selectBox.isChecked()){
                    check.put("isCheck",true);
                }else {
                    check.put("isCheck",false);
                }
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
                onShowDeleteDialog();
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
        rightText =(TextView)findViewById(R.id.id_tv_rightText);
        rightText.setVisibility(View.VISIBLE);
        rightText.setText("编辑");
        relayout=findViewById(R.id.id_layout_edit);
        rNotData =findViewById(R.id.id_nodata_view);
        view1=findViewById(R.id.id_view1);
        view2=findViewById(R.id.id_view2);
        tvType1 =(TextView)findViewById(R.id.id_type1);
        tvType2 =(TextView)findViewById(R.id.id_type2);
        TextView tvNotData =(TextView)findViewById(R.id.id_tv_nodata);
        tvNotData.setText("亲，您还没有消息哦！");
        btnDelete=(Button)findViewById(R.id.id_btn_delete);
        selectBox=(CheckBox)findViewById(R.id.id_select_checkBox);
        layoutOrderType=findViewById(R.id.id_li_ordertype);
        layoutNotice =findViewById(R.id.id_li_gonggao);
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
                unreadPos=positions;
                String mId=mList.get(positions).get("id");
                String types=mList.get(positions).get("type");
                Intent intent=new Intent(context,MessageDetailActivity.class);
                intent.putExtra("id",mId);
                intent.putExtra("type",types);
                startActivityForResult(intent,ConstantUtil.VALUE1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ConstantUtil.VALUE1){
            if (mList!=null&&mList.size()>=unreadPos){
                mList.get(unreadPos).put("flag","1");
                mAdapter.notifyDataSetChanged();
            }
        }
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
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams, listInformationHandler);
    }
    private void initEvent() {
        layoutNotice.setOnClickListener(this);
        layoutOrderType.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        rightText.setTag(0);
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag=(Integer)v.getTag();
                switch (tag){
                    case 0:
                        v.setTag(1);
                        rightText.setText("完成");
                        onSetEditLayout();
                        break;
                    case 1:
                        v.setTag(0);
                        rightText.setText("编辑");
                        onSetFinishLayout();
                        break;
                    default:
                        break;
                }
            }
        });
        selectBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    for (int i=0;i<checkList.size();i++){
                        checkList.get(i).put("isCheck",true);
                    }
                    mAdapter.notifyDataSetChanged();
                }else {
                    for (int i=0;i<checkList.size();i++){
                        checkList.get(i).put("isCheck",false);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void onSetFinishLayout() {
        relayout.setVisibility(View.GONE);
        visibleCheck = false;
        if (selectBox.isChecked()){
            selectBox.setChecked(false);
        }
        mAdapter.notifyDataSetChanged();
    }
    private void onSetEditLayout() {
        if (!type.equals(ConstantUtil.VALUE_TWO)){
            relayout.setVisibility(View.VISIBLE);
            visibleCheck = true;
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_delete:
                isMulChoice=true;
                onDeleteItem();
                break;
            case R.id.id_li_ordertype:
                type="1";
                onSetType();
                break;
            case R.id.id_li_gonggao:
                type="2";
                onSetType();
                break;
            default:
                break;
        }
    }

    private void onSetType() {
        customDialog.show();
        if (type.equals(ConstantUtil.VALUE_ONE)){
            dataType=true;
            view1.setBackgroundResource(R.color.colorOrange);
            view2.setBackgroundResource(R.color.white);
            tvType1.setTextColor(0xfff96331);
            tvType2.setTextColor(0xff555555);
        }else {
            dataType=true;
            view1.setBackgroundResource(R.color.white);
            view2.setBackgroundResource(R.color.colorOrange);
            tvType1.setTextColor(0xff555555);
            tvType2.setTextColor(0xfff96331);
        }
        loadData(1);
    }
    private void onShowDeleteDialog() {
        LayoutInflater inflater=LayoutInflater.from(this);
        LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.self_make_dialog,null);
        final Dialog dialog=new AlertDialog.Builder(context).create();
        if (dialog.getWindow()!=null){
            dialog.getWindow().setContentView(layout);
        }
        dialog.show();
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
            if (checkList.get(i).get("isCheck")){
                arrayList.add(mList.get(i).get("id"));
            }
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : arrayList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        idLIst=TypeConvertUtil.listToString(arrayList);
        if (onIsHaveData(idLIst)){
            onDeleteData();
        }
    }
    private boolean onIsHaveData(String idLIst) {
        idLIst=idLIst.trim();
        if (TextUtils.isEmpty(idLIst)){
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
            if (mList!=null){
                return mList.size();
            }else {
                return 0;
            }

        }
        @Override
        public Object getItem(int position) {
            if (mList!=null){
                return mList.get(position);
            }else {
                return null;
            }
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder item;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message_view, parent, false);
                item = new ViewHolder();
                item.itemTime =(TextView)convertView.findViewById(R.id.id_inform_time);
                item.itemLeftTxt = (TextView) convertView.findViewById(R.id.id_item_title);
                item.itemContent =(TextView)convertView.findViewById(R.id.id_item_content);
                item.itemCb =(CheckBox)convertView.findViewById(R.id.id_checkBox);
                item.flagImg=(ImageView)convertView.findViewById(R.id.id_flag_img);
                convertView.setTag(item);
            } else {
                item = (ViewHolder) convertView.getTag();
            }
            if(visibleCheck){
                item.itemCb.setVisibility(View.VISIBLE);
            }else{
                item.itemCb.setVisibility(View.GONE);
            }
            String flag=mList.get(position).get("flag");
            if (flag.equals(ConstantUtil.VALUE_ZERO)){
                item.flagImg.setVisibility(View.VISIBLE);
            }else {
                item.flagImg.setVisibility(View.GONE);
            }
            item.itemContent.setText(mList.get(position).get("content"));
            item.itemLeftTxt.setText(mList.get(position).get("title"));
            item.itemTime.setText(mList.get(position).get("time"));
            item.itemCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkList.get(position).put("isCheck",isChecked);
                    if (checkList.size()>0){
                        checkList.get(position).put("isCheck",isChecked);
                    }
                }
            });
            if (checkList.size()>0){
                item.itemCb.setChecked(checkList.get(position).get("isCheck"));
            }
            return convertView;
        }
        private class ViewHolder {
            CheckBox itemCb;
            ImageView flagImg;
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
