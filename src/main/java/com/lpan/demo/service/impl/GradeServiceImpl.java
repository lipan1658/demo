package com.lpan.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpan.demo.dao.GradeRepository;
import com.lpan.demo.entity.Grade;
import com.lpan.demo.service.GradeService;

@Service
public class GradeServiceImpl implements GradeService{
	
	@Autowired
	private GradeRepository gradeRepository;

	@Override
	public List<Grade> findAll() {
		return gradeRepository.findAll();
	}

}
