package br.com.calcard.android.app.model;

public class SettingsData {

    public SettingsData(String nameMenu, int imgMenu) {
        this.nameMenu = nameMenu;
        this.imgMenu = imgMenu;
    }

    public String getNameMenu() {
        return nameMenu;
    }

    public void setNameMenu(String nameMenu) {
        this.nameMenu = nameMenu;
    }

    public int getImgMenu() {
        return imgMenu;
    }

    public void setImgMenu(int imgMenu) {
        this.imgMenu = imgMenu;
    }

    private String nameMenu;
    private int imgMenu;
}
