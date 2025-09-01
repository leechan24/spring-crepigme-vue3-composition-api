package com.maple.info.service;

import com.maple.info.vo.MenuVo;
import java.util.List;

public interface MenuService {
    List<MenuVo> getAllMenu(); 
    MenuVo getMenuById(String id);
    void createMenu(MenuVo menu);
    void updateMenu(MenuVo menu);
    void deleteMenu(String id);
}
