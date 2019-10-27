package com.lpan.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lpan.demo.entity.Person;

@Repository
public interface PersonRepository extends BaseRepository<Person, Integer>{

	/**
	 * age > ?
	 * @param age
	 * @return
	 */
	public List<Person> findByAgeGreaterThan(Integer age);
	
	/**
	 * age > ? and name like ?
	 * @param age
	 * @param name
	 * @return
	 */
	public List<Person> findByAgeGreaterThanAndNameLike(Integer age,String name);
	
	
	/**
	 * 通过参数位置查询
	 * @param id
	 * @param name
	 * @return
	 */
	@Query(value = " from Person where id = ?1 and name =?2 ")
	public Person findPersonByPos(Integer id,String name);
	
	/**
	 * 通过具名参数查询
	 * @param name
	 * @param id
	 * @return
	 */
	@Query(value = " from Person where id = :id and name =:name ")
	public Person findPersonByNamed(String name,Integer id);
	
	/**
	 * 使用@Param指定具名参数查询
	 * @param personName
	 * @param personId
	 * @return
	 */
	@Query(value = " from Person where id = :id and name =:name ")
	public Person findPersonByNamedParams(@Param("name") String personName,@Param("id") Integer personId);
}
