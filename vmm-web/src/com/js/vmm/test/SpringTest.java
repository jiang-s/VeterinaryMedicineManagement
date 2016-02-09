package com.js.vmm.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.js.vmm.entity.Product;

public class SpringTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext context = 
	             new ClassPathXmlApplicationContext("Beans.xml");

	      Product obj = (Product) context.getBean("product");

	      obj.getpId();
	      
	}

}
