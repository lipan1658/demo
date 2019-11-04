package com.lpan.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lpan.demo.entity.Menu;
import com.lpan.demo.service.MenuService;

@Controller
@RequestMapping("/sys")
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	@GetMapping("/findMenuListByPidAndStatus")
	@ResponseBody
	public List<Menu> findMenuListByPidAndStatus(Integer pid){
		List<Menu> returnList = new ArrayList<Menu>();
		List<Menu> menuList = menuService.findMenuListByPidAndStatus(pid);
		for (Menu menu : menuList) {
			menu.setMenus(this.findMenuListByPidAndStatus(menu.getMenuId()));
			returnList.add(menu);
		}
		return returnList;
	}

}
