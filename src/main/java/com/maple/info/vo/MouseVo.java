package com.maple.info.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data // lombok으로 getter/setter 자동 생성 
public class MouseVo {
	
    private String idx;
    private String name;
    private String morph;
    private LocalDateTime birthday;
    private String	price;
    private String	mother;
    private String	father;
    private String	etc;
	private String	sex;
    private String	homeYn;
    private String	salePrice;

}
