package com.android.achievix.Model;

import android.graphics.drawable.Drawable;

public class AppSelectModel {
    private final String name;
    private final String packageName;
    private final Drawable icon;
    public boolean selected;

    public AppSelectModel(String name, String packageName, Drawable icon, boolean selected) {
        this.name = name;
        this.icon = icon;
        this.packageName = packageName;
        this.selected = selected;
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

    public void getSelected(boolean selected) {
        this.selected = selected;
    }
}