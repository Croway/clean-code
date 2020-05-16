package it.croway.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.croway.core.DIApplication;

public class POCDIApplication extends DIApplication {

	public POCDIApplication(List<Class<? extends Object>> classes) {
		super(classes);
	}

	@Override
	protected void registerDi() {
		List<Class<? extends Object>> cls = new ArrayList<>();

		for (Class<? extends Object> clazz : getClasses()) {
			cls.add(clazz);
		}

		cls.sort((o1, o2) -> o1.getConstructors()[0].getParameterCount() - o2.getConstructors()[0].getParameterCount());

		for (Class<? extends Object> clazz : cls) {
			try {
				Constructor<?> constructor = clazz.getConstructors()[0];
				if (constructor.getParameterCount() == 0) {
					DIContainer.put(clazz, clazz.newInstance());
				} else {
					Object[] constructorObject = new Object[constructor.getParameterCount()];
					int i = 0;
					for (Class<?> constructorParameterClass : constructor.getParameterTypes()) {
						constructorObject[i] = constructorParameterClass.cast(DIContainer.getInstance(constructorParameterClass));
						i++;
					}
					Object instance = constructor.newInstance(constructorObject);
					DIContainer.put(clazz, instance);
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException ex) {
				ex.printStackTrace();
			}
		}
	}

	public <T> T getBean(Class<T> clazz) {
		return DIContainer.getInstance(clazz);
	}

}

class DIContainer {
	private static final Map<Class<? extends Object>, Object> CONTAINER = new HashMap<>();

	private DIContainer() {
		// Singleton
	}

	public static void put(Class<? extends Object> clazz, Object impl) {
		CONTAINER.put(clazz, impl);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<? extends Object> clazz) {
		return (T) CONTAINER.get(clazz);
	}

}
