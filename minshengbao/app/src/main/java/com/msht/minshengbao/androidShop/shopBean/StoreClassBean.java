package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class StoreClassBean {

    public StoreClassBean(ChildStoreClassBean topBean, List<ChildStoreClassBean> childList) {
        TopBean = topBean;
        this.childList = childList;
    }

    public StoreClassBean() {

    }

    public ChildStoreClassBean getTopBean() {
        return TopBean;
    }

    public void setTopBean(ChildStoreClassBean topBean) {
        TopBean = topBean;
    }

    private ChildStoreClassBean TopBean;

    public List<ChildStoreClassBean> getChildList() {
        return childList;
    }

    private List<ChildStoreClassBean> childList;

    public void setChildList(List<ChildStoreClassBean> childList) {
        this.childList = childList;
    }

    public static class ChildStoreClassBean {
       /**
        * id : 3
        * name : 一级1
        * level : 1
        * pid : 0
        */

       private String id;
       private String name;
       private int level;
       private int pid;

       public String getId() {
           return id;
       }

       public void setId(String id) {
           this.id = id;
       }

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       public int getLevel() {
           return level;
       }

       public void setLevel(int level) {
           this.level = level;
       }

       public int getPid() {
           return pid;
       }

       public void setPid(int pid) {
           this.pid = pid;
       }
   }
}
