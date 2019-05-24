package org.henrya.pingapi.reflect;

import java.lang.reflect.Field;

public class ReflectUtils {
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