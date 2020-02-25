package com.example.clockmallservice.util;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2020/1/14 15:25
 * @description
 **/
public enum SortEnum {

    /**
     * 默认排序
     */
    DEFAULT(0,"默认排序"),
    /**
     * 价格升序
     */
    AMOUNT_ASC(1,"价格升序"),
    /**
     * 价格降序
     */
    AMOUNT_DESC(2,"价格降序"),
    ;
    private Integer type;
    private String sortType;

    SortEnum(Integer type, String sortType) {
        this.type = type;
        this.sortType = sortType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
