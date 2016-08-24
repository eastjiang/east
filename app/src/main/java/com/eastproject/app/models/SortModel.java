package com.eastproject.app.models;

/**
 * Created by jiang.dong on 2016/8/19.
 */
public class SortModel {
    private String name;//显示的数据
    private String sortLetters;//显示数据的首字母

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
