package com.mehrdadpy.com.ship.model;

/**
 * Created by Bio-imaging Lab on 8/13/2017.
 */

public class ItemSlideMenu {

    private String title;
    private int imgId;

    public ItemSlideMenu (int imgId, String title) {

        this.imgId = imgId;
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public int getImgId() {
        return imgId;
    }
}
