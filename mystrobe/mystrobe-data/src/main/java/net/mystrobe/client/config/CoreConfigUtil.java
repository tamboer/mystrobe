package net.mystrobe.client.config;

import net.mystrobe.client.WicketDSRuntimeException;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;

public class CoreConfigUtil {

	/**
	 * Return app name set at time of class generation.
	 * 
	 * @param clazz
	 * @return App name.
	 */
	public static String getGeneratedAppNameForClass(final Class<?> clazz)  {
		
		if (clazz == null) {
			throw new IllegalAccessError("Class argument can not be null.");
		}
		
		Class<?> parent = clazz;
		
		while( parent != null && !parent.isAnnotationPresent(GeneratorMeta.class)) {
			parent = parent.getSuperclass();
		}
		
		if (parent == null ) {
			throw new WicketDSRuntimeException(" Can not find generated app name for class: " + clazz.getName());
		} else {
			GeneratorMeta generatorMeta =  (GeneratorMeta) parent.getAnnotation(GeneratorMeta.class);
			return generatorMeta.appName();
		}
	}
}
