package com.maple.info.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // lombok으로 getter/setter 자동 생성 
@AllArgsConstructor
@NoArgsConstructor
public class NoticeVo {

	private String idx;        // 고유 ID (파일명 등)
    private String title;      // 공지 제목
    private String markdown;   // md 내용


}
