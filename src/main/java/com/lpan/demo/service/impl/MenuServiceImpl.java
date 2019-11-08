package com.lpan.demo.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Override
	public Page<Menu> findAllByPage(Pageable pageable) {
		return menuRepository.findAll(pageable);
	}

	@Override
	public void deleteById(Integer menuId) {
		menuRepository.deleteById(menuId);
	}

	
	

}
