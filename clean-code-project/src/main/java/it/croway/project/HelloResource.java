package it.croway.project;

import it.croway.core.http.HttpVerb;
import it.croway.core.http.Path;

@Path(value = "hello", verb = HttpVerb.GET)
public class HelloResource {
	
	HelloService helloService;
	
	public HelloResource(HelloService helloService) {
		this.helloService = helloService;
	}

	@Path(value = "", verb = HttpVerb.GET)
	public String hello() {
		return helloService.sayHello();
	}
	
}
