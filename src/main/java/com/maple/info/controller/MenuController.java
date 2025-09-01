package com.maple.info.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maple.info.service.MenuService;
import com.maple.info.vo.MenuVo;

@RestController
@RequestMapping("/api") 
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menu/list")
    public List<MenuVo> getMenuList() {
    	List<MenuVo> list =  menuService.getAllMenu(); // <- 메서드명 수정
        return menuService.getAllMenu(); 
    }

    @PostMapping("/menu/add")
    public void addMenu(@RequestBody MenuVo menu) {
        menuService.createMenu(menu); // <- 메서드명 수정
    }

    @PutMapping("/menu/update")
    public void updateMenu(@RequestBody MenuVo menu) {
        menuService.updateMenu(menu); // <- 메서드명 수정
    }

    @DeleteMapping("/menu/delete/{idx}")
    public void deleteMenu(@PathVariable String id) {
        menuService.deleteMenu(id); // <- 메서드명 수정
    }
}
