package org.dbunit.dataset.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.builder.util.StringUtil;

public class ObjectBasedDataSetBuilder {

	private Collection<Object> models;
	public final String COLUMN_NAME = "COLUMN_NAME";
	public final String COLUMN_VALUE = "COLUMN_VALUE";
	
	public ObjectBasedDataSetBuilder(Object object){
		this.models = new ArrayList<Object>();
		this.models.add(object);
	}
	
	public ObjectBasedDataSetBuilder(Collection<Object> objects){
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
		return table.name().isEmpty() ? StringUtil.normalizeTableName(model.getClass().getSimpleName()) : table.name();
	}
	
	public DataRowBuilder modelRow(DataRowBuilder row, Object model) throws DataSetException{
		row = mapFieldsWithColumnAnnotation(row, model);
		row = mapFieldsWithJoinColumnAnnotation(row, model);
		return row;
	}
	
	public DataRowBuilder mapFieldsWithColumnAnnotation(DataRowBuilder row, Object model) throws DataSetException{
		Field[] fields = model.getClass().getDeclaredFields();
		for(Field field : fields){
			Map<String, Object> columnData = getColumnDataFrom(field, model);
			row.with((String) columnData.get(COLUMN_NAME), columnData.get(COLUMN_VALUE));
		}
		return row;
	}
	
	protected Map<String, Object> getColumnDataFrom(Field field, Object model) throws DataSetException{
		Map<String, Object> data = new HashMap<String, Object>();
		try{
			Column annotation = getColumnAnnotation(field, model);
			field.setAccessible(true);
			data.put(COLUMN_NAME, annotation.name());
			data.put(COLUMN_VALUE, field.get(model));
		}
		catch(Exception e){
			throw new DataSetException("Error trying to map " + field.getName() + " from " + model.getClass().getName(), e.getCause());
		}
		return data;
	}
	
	public Column getColumnAnnotation(Field field, Object model) throws SecurityException, NoSuchMethodException{
		Column annotation = field.getAnnotation(Column.class);
		if(annotation == null){
			model.getClass().getMethod("get" + field.getName(), new Class<?>[]{});
		}
		return annotation;
	}
	
	public DataRowBuilder mapFieldsWithJoinColumnAnnotation(DataRowBuilder row, Object model){
		return row;
	}
	
	public void ensurePresenceOfTable(Object model) throws DataSetException{
		if(!isAnnotationPresent(Table.class, model))
			throw new DataSetException("No Table annotation found in " + model.getClass().getName());
	}
	
	protected Boolean isAnnotationPresent(Class<? extends Annotation> clazz, Object object){
		return object.getClass().isAnnotationPresent(clazz);
	}
}
