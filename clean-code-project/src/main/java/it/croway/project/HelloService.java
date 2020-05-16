package it.croway.project;

import it.croway.core.di.Service;

@Service
public class HelloService {
	
	public HelloService() {
	}
	
	public String sayHello() {
		return "Hello world";
	}

}
