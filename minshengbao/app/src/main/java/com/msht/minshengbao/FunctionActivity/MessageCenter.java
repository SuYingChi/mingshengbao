package com.msht.minshengbao.FunctionActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
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
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageCenter extends BaseActivity implements View.OnClickListener {
    private XListView mListView;
    private ListViewAdapter mAdapter;
    private RelativeLayout relayout;
    private LinearLayout   Lordertype,Lgonggao;
    private View Rnodata;
    private View view1,view2;
    private RelativeLayout Delect;
    private RelativeLayout Cancel;
    private ImageView Imgedit;
    private TextView tv_naviga;
    private TextView tv_nodata;
    private TextView tv_type1,tv_type2;
    private String userId, password,type="1";
    private boolean refreshType=false;
    private boolean dataType=false;
    private boolean visiblecheck = false;
    public  boolean isMulChoice = false;
    public  static final String MY_ACTION = "ui";   //广播跳转意图
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private int pageNo;
    private int pageIndex=0;
    private int pos=-1;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> arraylist=new ArrayList<String>() ;
    private ArrayList<HashMap<String, Boolean>> checkList = new ArrayList<HashMap<String, Boolean>>();
    private String idLIst="";
    Handler listinformHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonArray =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            Intent broadcast=new Intent();
                            broadcast.setAction(MY_ACTION);
                            broadcast.putExtra("broadcast", "2");
                            sendBroadcast(broadcast);
                            setResult(2);
                            if (!refreshType){
                                mListView.stopRefresh(true);
                            }else {
                                mListView.stopLoadMore();
                            }
                            if (!dataType){
                                if(jsonArray.length()>0){
                                    if (pageNo==1){
                                        mList.clear();
                                        checkList.clear();
                                    }
                                }
                            }else {
                                if (pageNo==1){
                                    mList.clear();
                                    checkList.clear();
                                }
                            }
                            initView();
                        }else {
                            Toast.makeText(context, Error, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    Handler deleteHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            delectSuccess();
                        }else {
                            failure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    arraylist.clear();
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
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
    private void delectSuccess() {
        idLIst="";
        if (isMulChoice){
            arraylist.clear();
            loadData(1);
        }else {
            mList.remove(pos);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void initView() {
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
            Rnodata.setVisibility(View.VISIBLE);
        }else {
            Rnodata.setVisibility(View.GONE);
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
        Imgedit=(ImageView)findViewById(R.id.id_right_img);
        Imgedit.setVisibility(View.VISIBLE);
        Imgedit.setImageResource(R.drawable.delete2x);
        relayout=(RelativeLayout)findViewById(R.id.id_layout_edit);
        Rnodata=findViewById(R.id.id_nodata_view);
        view1=findViewById(R.id.id_view1);
        view2=findViewById(R.id.id_view2);
        tv_type1=(TextView)findViewById(R.id.id_type1);
        tv_type2=(TextView)findViewById(R.id.id_type2);
        tv_nodata=(TextView)findViewById(R.id.id_tv_nodata);
        tv_nodata.setText("亲，您还没有消息哦！");
        Delect=(RelativeLayout)findViewById(R.id.id_re_delect);
        Cancel=(RelativeLayout)findViewById(R.id.id_re_cancel);
        Lordertype=(LinearLayout)findViewById(R.id.id_li_ordertype);
        Lgonggao=(LinearLayout)findViewById(R.id.id_li_gonggao);
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
                String Id=mList.get(positions).get("id");
                String types=mList.get(positions).get("type");
                Intent intent=new Intent(context,MessageDetail.class);
                intent.putExtra("id",Id);
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
        String validateURL = UrlUtil.Inform_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type",type);
        textParams.put("page",pageNum);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                listinformHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                listinformHandler.sendMessage(msg);
            }
        });
    }
    private void initEvent() {
        Imgedit.setOnClickListener(this);
        Delect.setOnClickListener(this);
        Cancel.setOnClickListener(this);
        Lgonggao.setOnClickListener(this);
        Lordertype.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_right_img:
                relayout.setVisibility(View.VISIBLE);
                visiblecheck = true;
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.id_re_cancel:
                relayout.setVisibility(View.GONE);
                visiblecheck = false;
                for (int i=0;i<mList.size();i++){
                    checkList.get(i).put("ischeck",false);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.id_re_delect:
                isMulChoice=true;
                delectitem();
                break;
            case R.id.id_li_ordertype:
                type="1";
                settype1();
                break;
            case R.id.id_li_gonggao:
                type="2";
                settype2();
                break;
            default:
                break;
        }
    }

    private void settype1() {
        customDialog.show();
        dataType=true;
       // mList.clear();
       // checkList.clear();
        view1.setBackgroundResource(R.color.colorOrange);
        view2.setBackgroundResource(R.color.white);
        tv_type1.setTextColor(0xfff96331);
        tv_type2.setTextColor(0xff555555);
        loadData(1);
    }

    private void settype2() {
        customDialog.show();
        dataType=true;
       // mList.clear();
       // checkList.clear();
        view1.setBackgroundResource(R.color.white);
        view2.setBackgroundResource(R.color.colorOrange);
        tv_type1.setTextColor(0xff555555);
        tv_type2.setTextColor(0xfff96331);
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
                Deleteitem();
                dialog.dismiss();
            }
        });
    }
    private void delectitem() {
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
            Deleteitem();
        }
    }
    private boolean matchjudge(String idLIst) {
        if (idLIst.equals("")){
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
    private void Deleteitem() {
        customDialog.show();
        String validateURL = UrlUtil.Inform_delect;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",idLIst);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                deleteHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                deleteHandler.sendMessage(msg);
            }
        });
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
                item.item_time =(TextView)convertView.findViewById(R.id.id_inform_time);
                item.item_left_txt = (TextView) convertView.findViewById(R.id.id_item_title);
                item.item_content=(TextView)convertView.findViewById(R.id.id_item_content);
                item.item_cb=(CheckBox)convertView.findViewById(R.id.id_checkBox);
                convertView.setTag(item);
            } else {                // 有直接获得ViewHolder
                item = (ViewHolder) convertView.getTag();
            }
            if(visiblecheck){
                item.item_cb.setVisibility(View.VISIBLE);
            }else{
                item.item_cb.setVisibility(View.GONE);
            }
            item.item_content.setText(mList.get(position).get("content"));
            item.item_left_txt.setText(mList.get(position).get("title"));
            item.item_time.setText(mList.get(position).get("time"));
            item.item_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkList.get(position).put("ischeck",isChecked);
                }
            });
            item.item_cb.setChecked( checkList.get(position).get("ischeck"));
            return convertView;
        }
        private class ViewHolder {
            CheckBox item_cb;
            TextView item_left_txt;
            TextView item_content;
            TextView item_time;
        }
    }
}
