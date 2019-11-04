package com.lpan.demo.service;

import java.util.List;

import com.lpan.demo.entity.Menu;

public interface MenuService {
	
	List<Menu> findMenuListByPidAndStatus(Integer pid);

}
