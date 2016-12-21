package org.dbunit.dataset.builder.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ReflectionUtil {

	public static <T extends Annotation> T getColumnAnnotation(Class<T> annotationClass, Field field, Object model) throws IntrospectionException{
		T annotation = field.getAnnotation(annotationClass);
		if(annotation == null){
			BeanInfo beanInfo = Introspector.getBeanInfo(model.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for(PropertyDescriptor property : propertyDescriptors){
				if (property.getName().equals(field.getName()))
					return (T) property.getReadMethod().getAnnotation(annotationClass);
			}			
		}
		return annotation;
	}
	
}
