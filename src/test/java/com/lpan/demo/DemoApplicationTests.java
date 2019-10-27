package com.lpan.demo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.ExampleMatcher.MatcherConfigurer;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.lpan.demo.dao.PersonRepository;
import com.lpan.demo.entity.Person;
import com.lpan.demo.utils.SpecificationUtils;

@SpringBootTest
class DemoApplicationTests {
	
	@Autowired
	private PersonRepository personRepository;

	@Test
	void contextLoads() {
		
	}
	
	/**
	 * 保存
	 */
	@Test
	public void saveTest() {
		Person person = new Person();
		person.setName("张三");
		person.setAge(25);
		person.setCreateTime(new Date());
		personRepository.save(person);
	}
	
	@Test
	public void saveTest2() {
		for(int i=0;i<10;i++) {
			Person person = new Person();
			person.setName(String.valueOf((char)('A'+i)));
			person.setAge(25);
			person.setCreateTime(new Date());
			personRepository.save(person);
		}
	}
	
	/**
	 * 通过关键字查询  
	 */
	@Test
	public void findTest() {
		List<Person> list = personRepository.findByAgeGreaterThan(20);
		System.out.println(list);
		
	}
	
	@Test
	public void findTest2() {
		List<Person> list = personRepository.findByAgeGreaterThanAndNameLike(20, "张%");
		System.out.println(list);
		
	}
	
	@Test
	public void countTest() {
		long count = personRepository.count();
		System.out.println("count:"+count);
	}
	
	/**
	 *    根据Example进行count
	 */
	@Test
	public void countByExampe() {
		Person person = new Person();
		person.setAge(25);
		Example<Person> example = Example.of(person);
		long count = personRepository.count(example);
		System.out.println("count:"+count);
	}
	
	@Test
	public void countByExampe2() {
		Person person = new Person();
		person.setAge(25);
		person.setName("张三");
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING).withMatcher("name", GenericPropertyMatchers.contains());
		Example<Person> example = Example.of(person,matcher);
		long count = personRepository.count(example);
		System.out.println("count:"+count);
	}
	
	@Test
	public void countByExampe3() {
		Person person = new Person();
		person.setAge(25);
		person.setName("三");
		GenericPropertyMatcher genericPropertyMatcher = GenericPropertyMatcher.of(StringMatcher.STARTING);
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", genericPropertyMatcher);
		Example<Person> example = Example.of(person,matcher);
		long count = personRepository.count(example);
		System.out.println("count:"+count);
	}
	
	@Test
	public void specificationEqTest() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("EQ_age", 25);
		Specification<Person> specification = SpecificationUtils.newSpecification(map);
		List<Person> list = personRepository.findAll(specification);
		System.out.println(list);
	}
	
	@Test
	public void specificationLikeTest() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("LIKE_name", "张");
		Specification<Person> specification = SpecificationUtils.newSpecification(map);
		List<Person> list = personRepository.findAll(specification);
		System.out.println(list);
	}
	
	@Test
	public void orderTest() {
		Sort sort = Sort.by("name");
		List<Person> list = personRepository.findAll(sort);
		System.out.println(list);
	}
	
	@Test
	public void sqlQueryByPos() {
		Person person = personRepository.findPersonByPos(13, "A");
		System.out.println(person);
	}
	
	@Test
	public void sqlQueryByNamed() {
		Person person = personRepository.findPersonByNamed("A", 13);
		System.out.println(person);
	}
	
	@Test
	public void sqlQueryByNamedParams() {
		Person person = personRepository.findPersonByNamedParams("A", 13);
		System.out.println(person);
	}
	

}
