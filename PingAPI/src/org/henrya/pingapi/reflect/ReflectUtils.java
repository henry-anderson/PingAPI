package org.henrya.pingapi.reflect;

import java.lang.reflect.Field;

/**
 * A class to make dealing with reflection slightly easier.
 * @author Henry Anderson
 */
public class ReflectUtils {
	
	/**
	 * Iterates through all of the fields in a class and returns the first one that is an instance of the specified class type
	 * @param clazz The class to check
	 * @param type The type of the field
	 * @return The field
	 */
	public static Field getFirstFieldByType(Class<?> clazz, Class<?> type) {
		for(Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			if(field.getType() == type) {
				return field;
			}
		}
		return null;
	}
}