package com.navdrawerwithfragments.adapter;

public class CategoryItem {
    public int categoryId;
    public String categoryName;
    public String thumbnail;
    public int weight;

    public CategoryItem(String str, int i, String str2, int i2) {
        this.categoryName = str;
        this.categoryId = i;
        this.thumbnail = str2;
        this.weight = i2;
    }
}
