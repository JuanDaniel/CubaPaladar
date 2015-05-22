package org.cuba.paladar.Model;

public class ItemMenu {
    private int icon;
    private String title;
    private int menu;

    public ItemMenu(int icon, String title, int menu) {
        super();
        this.icon = icon;
        this.title = title;
        this.menu = menu;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMenu() {
        return this.menu;
    }
}
