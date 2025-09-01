package com.maple.info.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.maple.info.mapper.CreMapper;
import com.maple.info.service.CreService;
import com.maple.info.vo.CreVo;
import com.maple.info.vo.MouseVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreServiceImpl implements CreService {

    private final CreMapper creMapper; // MyBatis Mapper 주입

    @Override
    public List<CreVo> getCreList() {
        List<CreVo> vos = creMapper.selectCreList();

        // 날짜를 화면 표시용 String으로 변환
        return vos.stream().map(vo -> {

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MouseVo> getMouseList() {
        return creMapper.selectMouseList();
    }

    @Override
    public CreVo getCre(String idx) {
        CreVo vo = creMapper.selectCre(idx);

        return vo;
    }

    @Override
    public CreVo create(CreVo vo) {
        creMapper.insertCre(vo);
        return vo;
    }

    @Override
    public CreVo update(CreVo vo) {
        creMapper.updateCre(vo);
        return vo;
    }

    @Override
    public void delete(String idx) {
        creMapper.deleteCre(idx);
    }
}
