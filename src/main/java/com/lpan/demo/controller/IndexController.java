package com.lpan.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sys")
public class IndexController {
	
	@GetMapping("/index")
	public String index() {
		return "html/index";
	}
	@GetMapping("/index2")
	public String index2() {
		return "html/index2";
	}
	
	@GetMapping("/basicForm")
	public String basicForm() {
		return "html/form-layout-basic";
	}

}
