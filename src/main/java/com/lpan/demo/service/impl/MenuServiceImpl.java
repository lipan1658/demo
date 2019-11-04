package com.lpan.demo.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpan.demo.dao.MenuRepository;
import com.lpan.demo.entity.Menu;
import com.lpan.demo.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {
	
	@Autowired
	private MenuRepository menuRepository;

	public List<Menu> findMenuListByPidAndStatus(Integer pid) {
		return menuRepository.findMenuListByPidAndStatus(pid,"1");
	}

	

}
