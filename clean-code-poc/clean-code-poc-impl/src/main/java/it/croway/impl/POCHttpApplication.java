package it.croway.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.sun.net.httpserver.HttpServer;

import it.croway.core.DIApplication;
import it.croway.core.HttpApplication;
import it.croway.core.http.HttpVerb;
import it.croway.core.http.Path;

public class POCHttpApplication extends HttpApplication {

	private Map<String, Callable> callableCache = new HashMap<>();

	public POCHttpApplication(DIApplication diApplication) {
		super(diApplication);
	}

	@Override
	protected void register() {
		try {
			String host = "localhost";
			int port = 8080;
			HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);
			
			System.out.println("starting httpserver on " + host + ":" + port);

			ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
			server.setExecutor(threadPoolExecutor);

			for (Class<? extends Object> clazz : getDiApplication().getClassByAnnotation(Path.class)) {
				Path p = clazz.getAnnotation(Path.class);

				String path = p.value();
				HttpVerb verb = p.verb();

				for (Method method : clazz.getMethods()) {
					if (method.isAnnotationPresent(Path.class)) {
						Object obj = getDiApplication().getBean(clazz);

						String fullPath = path.startsWith("/") ? path
								: "/" + path + (method.getAnnotation(Path.class).value().isEmpty() ? ""
										: "/" + method.getAnnotation(Path.class).value());

						if (callableCache.containsKey(fullPath)) {
							throw new RuntimeException("Path " + fullPath + " already used");
						}

						callableCache.put(fullPath, new Callable(method, obj));

						server.createContext(fullPath, httpExchange -> {
							if (verb.toString().equalsIgnoreCase(httpExchange.getRequestMethod())) {
								System.out.println("serving request with thread " + Thread.currentThread().getName());

								Callable callable = callableCache.get(httpExchange.getRequestURI().getPath());
								Method methodToInvoke = callable.method;
								Object object = callable.object;

								try (OutputStream os = httpExchange.getResponseBody();) {
									String response = (String) methodToInvoke.invoke(object);

									httpExchange.sendResponseHeaders(200, response.length());
									os.write(response.getBytes());
									os.flush();
								} catch (IllegalAccessException | IllegalArgumentException
										| InvocationTargetException e) {
									e.printStackTrace();
								}
							}
						});
						System.out.println("started path: " + fullPath);
					}
				}

			}

			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	class Callable {
		public Method method;
		public Object object;

		public Callable(Method method, Object object) {
			super();
			this.method = method;
			this.object = object;
		}

	}

}
