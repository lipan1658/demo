package com.lpan.demo.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.lpan.demo.entity.Person;

public interface PersonService {
	
	List<Person> findAllBySort(Sort sort);
}
