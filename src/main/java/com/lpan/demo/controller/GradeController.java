package com.lpan.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lpan.demo.entity.Grade;
import com.lpan.demo.service.GradeService;

@Controller
@RequestMapping("/grade")
public class GradeController {
	
	@Autowired
	private GradeService gradeService;
	
	@GetMapping("/findAll")
	@ResponseBody
	public List<Grade> findAll(){
		return gradeService.findAll();
	}

}
