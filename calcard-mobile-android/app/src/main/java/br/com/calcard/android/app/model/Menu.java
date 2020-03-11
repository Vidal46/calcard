package br.com.calcard.android.app.model;

public class Menu {
    public Menu(Integer img, String name) {
        this.img = img;
        this.name = name;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Integer img;
    private String name;
}
