package com.lpan.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.lpan.demo.entity.Menu;
import com.lpan.demo.service.MenuService;
import com.lpan.demo.utils.PageUtil;

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
			//采用递归
			menu.setMenus(this.findMenuListByPidAndStatus(menu.getMenuId()));
			returnList.add(menu);
		}
		return returnList;
	}
	
	
	@GetMapping("/findAllByPage")
	@ResponseBody
	public Page<Menu> findAllByPage(HttpServletRequest request){
		Map<String, Object> params = WebUtils.getParametersStartingWith(request, "filter_");
		Pageable pageable = PageUtil.covertPageable(request);
		Page<Menu> page = menuService.findAllByPage(pageable);
		return page;
	}
	
	@GetMapping("deleteById")
	@ResponseBody
	public Map<String,Object> deleteById(Integer menuId){
		Map<String,Object> result = new HashMap<>();
		try {
			//menuService.deleteById(menuId);
			result.put("msg", "操作成功");
			result.put("flag", "success");
		} catch (Exception e) {
			result.put("msg", "操作成功");
			result.put("flag", "success");
		}
		return result;
	}

}
