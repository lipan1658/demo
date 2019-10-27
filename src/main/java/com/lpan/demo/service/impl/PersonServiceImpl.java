package com.lpan.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lpan.demo.dao.PersonRepository;
import com.lpan.demo.entity.Person;
import com.lpan.demo.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService{
	
	@Autowired
	private PersonRepository personService;

	@Override
	public List<Person> findAllBySort(Sort sort) {
		return personService.findAll(sort);
	}

}
