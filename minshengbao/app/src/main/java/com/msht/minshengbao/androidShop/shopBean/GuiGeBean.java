package com.msht.minshengbao.androidShop.shopBean;

public class GuiGeBean {
    public String getGuigeGoodId() {
        return guigeGoodId;
    }

    public void setGuigeGoodId(String guigeGoodId) {
        this.guigeGoodId = guigeGoodId;
    }

    private  String guigeGoodId;

    public GuiGeBean(String guigeName, String guigeId, String guigeGoodid, Boolean ischeck) {
        this.guigeName = guigeName;
        this.guigeId = guigeId;
        this.guigeGoodId = guigeGoodid;
        this.isCheck = ischeck;
    }

    public String getGuigeName() {
        return guigeName;
    }

    public void setGuigeName(String guigeName) {
        this.guigeName = guigeName;
    }

    public String getGuigeId() {
        return guigeId;
    }

    public void setGuigeId(String guigeId) {
        this.guigeId = guigeId;
    }

    String guigeName;
    String guigeId;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    boolean isCheck;
}
