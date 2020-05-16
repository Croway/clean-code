package it.croway.core;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.croway.core.di.Service;
import it.croway.core.http.Path;

public abstract class DIApplication {

	private List<Class<? extends Object>> classes;

	public DIApplication(List<Class<? extends Object>> classes) {
		this.classes = classes;
	}

	public void register() {
		Set<Class<? extends Annotation>> annotazioniGestite = new HashSet<>();

		annotazioniGestite.add(Service.class);
		annotazioniGestite.add(Path.class);

		Set<Class<? extends Object>> classiGestite = new HashSet<>();
		Set<Class<? extends Object>> httpClasses = new HashSet<>();

		for (Class<? extends Object> clazz : classes) {
			for (Class<? extends Annotation> ann : annotazioniGestite) {
				if (clazz.isAnnotationPresent(ann)) {
					classiGestite.add(clazz);

					if (Path.class.equals(ann)) {
						httpClasses.add(clazz);
					}
				}
			}
		}

		registerDi();
	}

	protected abstract void registerDi();

	public abstract <T> T getBean(Class<T> clazz);

	public Collection<Class<? extends Object>> getClassByAnnotation(Class<? extends Annotation> annotationClazz) {
		return classes.stream().filter(clazz -> clazz.isAnnotationPresent(annotationClazz))
				.collect(Collectors.toList());
	}

	public List<Class<? extends Object>> getClasses() {
		if(this.classes == null)
			return new ArrayList<>();
		
		return Collections.unmodifiableList(classes);
	}

}
