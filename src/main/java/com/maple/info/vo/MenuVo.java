package com.maple.info.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MenuVo {

    private String id;       // 메뉴 ID
    private String parentId;  // 상위 메뉴 ID (루트 메뉴면 null)
    private String name;      // 메뉴명
    private String path;      // 메뉴 클릭 시 이동 경로
    private int sortOrder;    // 메뉴 정렬순서
    private int lv;        // 메뉴 레벨 (1: 상위, 2: 하위)

    private LocalDateTime createdAt; // 생성일
    private LocalDateTime updatedAt; // 수정일

    private List<MenuVo> children;   // 하위 메뉴 리스트
}
