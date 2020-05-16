package it.croway.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;

public class Application {

	DIApplication diApplication;
	HttpApplication httpApplication;

	// TODO reflection per recuperare le classi annotate
	public Application(List<Class<? extends Object>> classes) {
		Properties prop = new Properties();
		try {
			prop.load(Application.class.getResourceAsStream("/META-INF/implementation.properties"));
			String diApplicationQualifiedImplementation = prop.getProperty("it.croway.core.DIApplication");
			String httpApplicationQualifiedImplementation = prop.getProperty("it.croway.core.HttpApplication");
				
			setDiApplication(
						(DIApplication) Class.forName(diApplicationQualifiedImplementation).getConstructors()[0]
								.newInstance(classes));
				setHttpApplication(
						(HttpApplication) Class.forName(httpApplicationQualifiedImplementation).getConstructors()[0]
								.newInstance(getDiApplication()));
			
			getDiApplication().register();
			System.out.println("di registered");
			
			getHttpApplication().register();
			System.out.println("http server started");
		} catch (IOException |InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | SecurityException | ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public DIApplication getDiApplication() {
		return diApplication;
	}

	private void setDiApplication(DIApplication diApplication) {
		this.diApplication = diApplication;
	}

	public HttpApplication getHttpApplication() {
		return httpApplication;
	}

	private void setHttpApplication(HttpApplication httpApplication) {
		this.httpApplication = httpApplication;
	}

}
