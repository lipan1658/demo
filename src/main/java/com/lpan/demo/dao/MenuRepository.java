package com.lpan.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lpan.demo.entity.Menu;

@Repository
public interface MenuRepository extends BaseRepository<Menu, Integer>{

	@Query(value = "select m from Menu m where m.pid = ?1 and m.status = ?2")
	List<Menu> findMenuListByPidAndStatus(Integer pid,String status);

}
