package it.croway.project;

import java.util.Arrays;

import it.croway.core.Application;

public class ApplicationConfiguration {

	public static void main(String[] args) {
		new Application(Arrays.asList(HelloResource.class, HelloService.class));
	}
}
