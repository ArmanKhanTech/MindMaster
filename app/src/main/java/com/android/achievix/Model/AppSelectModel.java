package com.android.achievix.Model;

import android.graphics.drawable.Drawable;

public class AppSelectModel {
    private final String name;
    private final Drawable icon;
    public boolean selected;
    private String packageName;
    private String extra1;

    public AppSelectModel(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }

    public AppSelectModel(String name, String packageName, Drawable icon, String extra1) {
        this.name = name;
        this.icon = icon;
        this.packageName = packageName;
        this.extra1 = extra1;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getExtra1() {
        return extra1;
    }

    public void getSelected(boolean selected) {
        this.selected = selected;
    }
}