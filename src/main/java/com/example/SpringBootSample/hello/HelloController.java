package com.example.SpringBootSample.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {
	
	@Autowired
	private HelloService helloService;
	
	@GetMapping("/hello")
	public String getHello() {
		// -> hello.html
		return "hello";
	}
	
	@PostMapping("/hello")
	public String postHelloResponse(@RequestParam("text1") String param, Model model) {
		
		// -> sample, param
		model.addAttribute("sample", param);

		// -> response.html
		return "hello/response";
	}
	
	@PostMapping("/hello/db")
	public String getEmployee(@RequestParam("text2") String id, Model model) {
		
		Employee employee = helloService.getEmployees(id);
		model.addAttribute("employee", employee);

		// -> db.html
		return "hello/db";
	}
}
