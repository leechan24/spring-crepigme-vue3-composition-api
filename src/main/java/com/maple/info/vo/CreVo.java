package com.maple.info.vo;

import java.time.LocalDate;

import lombok.Data;

@Data // lombok으로 getter/setter 자동 생성 
public class CreVo {

    private String idx;
    private String name;
    private String morph;
    private LocalDate birthday;
    private int price;
    private String mother;
    private String father;
    private String etc;
    private String sex;
    private String homeYn;
    private String color;
    private int salePrice;
    private String imgPath;

    // getter / setter
    public String getIdx() { return idx; }
    public void setIdx(String idx) { this.idx = idx; }
    // ... 나머지 필드도 동일

}
