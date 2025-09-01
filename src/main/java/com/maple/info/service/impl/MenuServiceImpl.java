package com.maple.info.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.maple.info.mapper.MenuMapper;
import com.maple.info.service.MenuService;
import com.maple.info.vo.MenuVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

	private final MenuMapper menuMapper; // MyBatis Mapper 주입

    @Override
    public List<MenuVo> getAllMenu() {
        List<MenuVo> menus = menuMapper.selectAllMenu();

        // 하위 메뉴 붙이기 (level 2)
        menus.forEach(parent -> {
            List<MenuVo> children = menus.stream()
                .filter(child -> parent.getId().equals(child.getParentId()))
                .collect(Collectors.toList());  // Java 8~11 호환
            parent.setChildren(children);
        });

        // 상위 메뉴만 반환
        return menus.stream()
                .filter(menu -> menu.getLv() == 1)
                .collect(Collectors.toList()); // Java 8~11 호환
    }

    @Override
    public MenuVo getMenuById(String id) {
        return menuMapper.selectMenuById(id);
    }

    @Override
    public void createMenu(MenuVo menu) {
        menuMapper.insertMenu(menu);
    }

    @Override
    public void updateMenu(MenuVo menu) {
        menuMapper.updateMenu(menu);
    }

    @Override
    public void deleteMenu(String id) {
        menuMapper.deleteMenu(id);
    }
}
