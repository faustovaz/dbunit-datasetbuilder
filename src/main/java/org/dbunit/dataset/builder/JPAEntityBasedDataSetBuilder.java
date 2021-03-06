package org.dbunit.dataset.builder;

import java.beans.IntrospectionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.builder.util.ReflectionUtil;
import org.dbunit.dataset.builder.util.StringUtil;

public class JPAEntityBasedDataSetBuilder {

	private Collection<Object> models;
	public final String COLUMN_NAME = "COLUMN_NAME";
	public final String COLUMN_VALUE = "COLUMN_VALUE";
	
	public JPAEntityBasedDataSetBuilder(Object object){
		this.models = new ArrayList<Object>();
		this.models.add(object);
	}
	
	public JPAEntityBasedDataSetBuilder(Collection<Object> objects){
		this.models = objects;
	}
	
	public IDataSet build() throws DataSetException{
		return this.build(new DataSetBuilder());
	}
	
	public IDataSet build(DataSetBuilder builder) throws DataSetException{
		for(Object model : this.models)
			builder.add(rowBuild(builder, model));
		return builder.build();
	}
	
	public DataRowBuilder rowBuild(DataSetBuilder builder, Object model) throws DataSetException{
		ensurePresenceOfTable(model);
		DataRowBuilder row = builder.newRow(modelName(model));
		row = modelRow(row, model);
		return row;
	}
	
	public String modelName(Object model) throws DataSetException{
		ensurePresenceOfTable(model);
		Table table = model.getClass().getAnnotation(Table.class);
		String tableName = table.name().isEmpty() ? StringUtil.normalizeTableName(model.getClass().getSimpleName()) : table.name(); 
		return table.schema().isEmpty() ? tableName : table.schema() + "." + tableName;
			
	}
	
	public DataRowBuilder modelRow(DataRowBuilder row, Object model) throws DataSetException{
		row = mapFieldsWith(Column.class, row, model);
		row = mapFieldsWith(JoinColumn.class, row, model);
		return row;
	}
	
	public DataRowBuilder mapFieldsWith(Class<? extends Annotation> annotationClass, DataRowBuilder row, Object model) throws DataSetException{
		Field[] fields = model.getClass().getDeclaredFields();
		for(Field field : fields){
			Map<String, Object> columnData = getColumnDataFrom(annotationClass, field, model);
			if(!columnData.isEmpty())
				row.with((String) columnData.get(COLUMN_NAME), columnData.get(COLUMN_VALUE));
		}
		return row;
	}
	
	protected Map<String, Object> getColumnDataFrom(Class<? extends Annotation> annotationClass, Field field, Object model) throws DataSetException{
		if(annotationClass.equals(Column.class))
			return getColumnDataFromColumnAnnotation(field, model);
		if(annotationClass.equals(JoinColumn.class))
			return getColumnDataFromJoinColumnAnnotation(field, model);
		return new HashMap<String, Object>();
	}

	protected Map<String, Object> getColumnDataFromColumnAnnotation(Field field, Object model) throws DataSetException{
		try{
			Column annotation = ReflectionUtil.getColumnAnnotation(Column.class, field, model);
			if(annotation != null)
				return mapData(annotation.name(), field, model);
			return new HashMap<String, Object>();			
		}
		catch(IntrospectionException e){
			throw new DataSetException("Error trying to get Column annotation of" + field.getName() + " from " + model.getClass().getName(), e.getCause());
		}
	}
	
	protected Map<String, Object> getColumnDataFromJoinColumnAnnotation(Field field, Object model) throws DataSetException{
		try{
			JoinColumn annotation = ReflectionUtil.getColumnAnnotation(JoinColumn.class, field, model);
			field.setAccessible(true);
			Object object = field.get(model);
			if(object != null){
				Field idAnnotatedField = ReflectionUtil.getFieldWith(Id.class, object);
				if((annotation != null) && (idAnnotatedField != null))
					return mapData(annotation.name(), idAnnotatedField, object);
			}
			return new HashMap<String, Object>();			
		}
		catch(IllegalAccessException iae){
			throw new DataSetException("Error trying to get JoinColumn annotation of" + field.getName() + " from " + model.getClass().getName(), iae.getCause());
		}
		catch(IntrospectionException e){
			throw new DataSetException("Error trying to get JoinColumn annotation of" + field.getName() + " from " + model.getClass().getName(), e.getCause());
		}
	}
	
	protected Map<String, Object> mapData(String columnName, Field field, Object model) throws DataSetException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		try{
			field.setAccessible(true);
			map.put(COLUMN_NAME, columnName);
			map.put(COLUMN_VALUE, field.get(model));			
			return map;
		}
		catch(Exception e){
			throw new DataSetException("Error trying to map " + field.getName() + " from " + model.getClass().getName(), e.getCause());
		}
	}
	
	public void ensurePresenceOfTable(Object model) throws DataSetException{
		if(!isAnnotationPresent(Table.class, model))
			throw new DataSetException("No Table annotation found in " + model.getClass().getName());
	}
	
	protected Boolean isAnnotationPresent(Class<? extends Annotation> clazz, Object object){
		return object.getClass().isAnnotationPresent(clazz);
	}
}
