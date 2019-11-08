package com.lpan.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lpan.demo.entity.Menu;

public interface MenuService {
	
	List<Menu> findMenuListByPidAndStatus(Integer pid);
	
	Page<Menu> findAllByPage(Pageable pageable);

	void deleteById(Integer menuId);

}
