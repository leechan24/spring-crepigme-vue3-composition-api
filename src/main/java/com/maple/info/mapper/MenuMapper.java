package com.maple.info.mapper;

import com.maple.info.vo.MenuVo;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper {
    List<MenuVo> selectAllMenu();
    MenuVo selectMenuById(String id);
    int insertMenu(MenuVo menu);
    int updateMenu(MenuVo menu);
    int deleteMenu(String id);
}
