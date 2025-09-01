package com.maple.info.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maple.info.service.CreService;
import com.maple.info.vo.CreVo;
import com.maple.info.vo.MouseVo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CreController {

	private  final  CreService creService;

	@GetMapping("/cre/list")
    public List<CreVo> creList() {
		return creService.getCreList();
	}

	@GetMapping("/mouse/list")
    public List<MouseVo> mouseList() {
		return creService.getMouseList();
	}

    @GetMapping("/cre/{id}")
    public CreVo getcre(@PathVariable String idx) {
        return creService.getCre(idx);
    }
    @PostMapping("/cre")
    public CreVo create(@RequestBody CreVo vo) {
        return creService.create(vo);
    }

    @PatchMapping("/cre")
    public CreVo update(@RequestBody CreVo vo) {
        return creService.update(vo);
    }

    @DeleteMapping("/cre/{id}")
    public void delete(@PathVariable String idx) {
        creService.delete(idx);
    }
}