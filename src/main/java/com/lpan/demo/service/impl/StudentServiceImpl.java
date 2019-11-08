package com.lpan.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpan.demo.dao.StudentRepository;
import com.lpan.demo.entity.Student;
import com.lpan.demo.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService{
	
	@Autowired
	private StudentRepository studentRepository;

	@Override
	public List<Student> findAll() {
		return studentRepository.findAll();
	}

}
