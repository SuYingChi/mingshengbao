package com.msht.minshengbao.androidShop.shopBean;

import java.util.List;

public class ShopCarBean {

    private String storeName;
    private boolean isEdit;
    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
    public boolean isCheckStore() {
        return isCheckStore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopCarBean)) return false;

        ShopCarBean that = (ShopCarBean) o;

        if (isEdit != that.isEdit) return false;
        if (isCheckStore != that.isCheckStore) return false;
        if (!storeName.equals(that.storeName)) return false;
        if (!storeId.equals(that.storeId)) return false;
        return datasBean.equals(that.datasBean);
    }

    @Override
    public int hashCode() {
        int result = storeName.hashCode();
        result = 31 * result + (isEdit ? 1 : 0);
        result = 31 * result + (isCheckStore ? 1 : 0);
        result = 31 * result + storeId.hashCode();
        result = 31 * result + datasBean.hashCode();
        return result;
    }

    public void setCheckStore(boolean checkStore) {
        isCheckStore = checkStore;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    private boolean isCheckStore;
    private String storeId;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public DatasBean getDatasBean() {
        return datasBean;
    }

    public void setDatasBean(DatasBean datasBean) {
        this.datasBean = datasBean;
    }

    private DatasBean datasBean;
    public static class DatasBean{
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DatasBean)) return false;

            DatasBean datasBean = (DatasBean) o;

            return goodBeanList.equals(datasBean.goodBeanList);
        }

        @Override
        public int hashCode() {
            return goodBeanList.hashCode();
        }

        public DatasBean(List<goodBean> goodBeanList) {

            this.goodBeanList = goodBeanList;
        }

        public List<goodBean> getGoodBeanList() {
            return goodBeanList;
        }

        public void setGoodBeanList(List<goodBean> goodBeanList) {
            this.goodBeanList = goodBeanList;
        }

        private List<goodBean> goodBeanList;
    public static class goodBean {


        private String carId;
        private boolean storageSate;

        public boolean isEdite() {
            return isEdite;
        }

        public void setEdite(boolean edite) {
            isEdite = edite;
        }

        private  boolean isEdite;

        public boolean isCheckStore() {
            return isCheckStore;
        }

        public void setCheckStore(boolean checkStore) {
            isCheckStore = checkStore;
        }

        private  boolean isCheckStore;
        private  String storeName;

        public goodBean(String storeName, String storeId, boolean checkStore, boolean edit) {
            this.storeName = storeName;
            this.storeId = storeId;
            isCheckStore = checkStore;
            isEdite = edit;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        private  String storeId;



        public String getGoodImage() {
            return goodImage;
        }

        public void setGoodImage(String goodImage) {
            this.goodImage = goodImage;
        }

        public int getGoodNum() {
            return goodNum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof goodBean)) return false;

            goodBean goodBean = (goodBean) o;

            if (isEdite != goodBean.isEdite) return false;
            if (isCheckStore != goodBean.isCheckStore) return false;
            if (goodNum != goodBean.goodNum) return false;
            if (isSelected != goodBean.isSelected) return false;
            if (!carId.equals(goodBean.carId)) return false;
            if (!storeName.equals(goodBean.storeName)) return false;
            if (!storeId.equals(goodBean.storeId)) return false;
            if (!goodImage.equals(goodBean.goodImage)) return false;
            if (!goodPrice.equals(goodBean.goodPrice)) return false;
            if (!goodStorageNum.equals(goodBean.goodStorageNum)) return false;
            if (!goodName.equals(goodBean.goodName)) return false;
            if (!goodGuige.equals(goodBean.goodGuige)) return false;
            if (!isPickupSelf.equals(goodBean.isPickupSelf)) return false;
            return goodId.equals(goodBean.goodId);
        }

        @Override
        public int hashCode() {
            int result = carId.hashCode();
            result = 31 * result + (isEdite ? 1 : 0);
            result = 31 * result + (isCheckStore ? 1 : 0);
            result = 31 * result + storeName.hashCode();
            result = 31 * result + storeId.hashCode();
            result = 31 * result + goodImage.hashCode();
            result = 31 * result + goodNum;
            result = 31 * result + goodPrice.hashCode();
            result = 31 * result + goodStorageNum.hashCode();
            result = 31 * result + goodName.hashCode();
            result = 31 * result + goodGuige.hashCode();
            result = 31 * result + isPickupSelf.hashCode();
            result = 31 * result + (isSelected ? 1 : 0);
            result = 31 * result + goodId.hashCode();
            return result;
        }

        public void setGoodNum(int goodNum) {
            this.goodNum = goodNum;
        }

        public String getGoodPrice() {
            return goodPrice;
        }

        public void setGoodPrice(String goodPrice) {
            this.goodPrice = goodPrice;
        }

        public String getGoodStorageNum() {
            return goodStorageNum;
        }

        public void setGoodStorageNum(String goodStorageNum) {
            this.goodStorageNum = goodStorageNum;
        }

        public String getGoodName() {
            return goodName;
        }

        public void setGoodName(String goodName) {
            this.goodName = goodName;
        }

        public String getGoodGuige() {
            return goodGuige;
        }

        public void setGoodGuige(String goodGuige) {
            this.goodGuige = goodGuige;
        }

        public String getIsPickupSelf() {
            return isPickupSelf;
        }

        public void setIsPickupSelf(String isPickupSelf) {
            this.isPickupSelf = isPickupSelf;
        }

        private String goodImage;
        private int goodNum;
        private String goodPrice;
        private String goodStorageNum;
        private String goodName;
        private String goodGuige;
        private String isPickupSelf;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


        private boolean isSelected;

        public String getGoodId() {
            return goodId;
        }

        public void setGoodId(String goodId) {
            this.goodId = goodId;
        }

        private String goodId;

        public String getCarId() {
            return carId;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public boolean getStorageSate() {
            return storageSate;
        }

        public void setStorageSate(boolean storageSate) {
            this.storageSate = storageSate;
        }
    }
    }
}
