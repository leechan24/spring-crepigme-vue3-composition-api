package com.maple.info.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.maple.info.vo.CreVo;
import com.maple.info.vo.MouseVo;

@Mapper
public interface CreMapper {

    // Cre 테이블 전체 조회
    List<CreVo> selectCreList();

    // Cre 단일 조회
    CreVo selectCre(@Param("idx") String idx);

    // Cre 등록
    int insertCre(CreVo vo);

    // Cre 수정
    int updateCre(CreVo vo);

    // Cre 삭제
    int deleteCre(@Param("idx") String idx);

    // Mouse 테이블 전체 조회
    List<MouseVo> selectMouseList();
}
