package com.js.vmm.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("hello.do")
public class MyController {

	@RequestMapping(params = "method=test", method = RequestMethod.GET)
	public void test(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.print("Hello World!");

		out.flush();
		out.close();

	}
	
	@RequestMapping(params = "method=testPost", method = RequestMethod.POST)
	public void testPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		System.out.println(request.getParameterMap().toString());
		out.print("Hello World!");

		out.flush();
		out.close();

	}
	
	@RequestMapping(params = "method=saveRecord", method = RequestMethod.POST)
	public void saveRecord(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		System.out.println(request.getParameterMap().toString());
		out.print("同步成功");

		out.flush();
		out.close();

	}
	
}
