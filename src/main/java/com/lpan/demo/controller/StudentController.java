package com.lpan.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lpan.demo.entity.Student;
import com.lpan.demo.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	@GetMapping("/findAll")
	@ResponseBody
	public List<Student> findAll(){
		return studentService.findAll();
	}
	
	@PostMapping("/save")
	public String save(Student student) {
		return "SUCCESS";
	}

}
