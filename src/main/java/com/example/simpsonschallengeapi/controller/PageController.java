package com.example.simpsonschallengeapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@RequestMapping("/")
	public String index() {
		return "Index";
	}
}
