package com.lpan.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpan.demo.entity.Person;
import com.lpan.demo.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {
	
	@Autowired
	private PersonService personService;
	
	@GetMapping("/findAllSort")
	public List<Person> findAllSort(){
		Sort sort = Sort.by("name");
		List<Person> list = personService.findAllBySort(sort);
		for (Person person : list) {
			System.out.println(person.getName());
		}
		return list;
	}

}
