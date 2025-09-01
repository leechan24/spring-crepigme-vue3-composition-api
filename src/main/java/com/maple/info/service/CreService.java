package com.maple.info.service;

import java.util.List;
import com.maple.info.vo.CreVo;
import com.maple.info.vo.MouseVo;

public interface CreService {
	 List<CreVo> getCreList();
     List<MouseVo> getMouseList();
     CreVo getCre(String idx);
     CreVo create(CreVo vo);
     CreVo update(CreVo vo);
     void delete(String idx);
}
