package org.dbunit.dataset.builder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Table;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.builder.util.StringUtil;

public class ObjectBasedDataSetBuilder {

	private Collection<Object> models;
	
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
			builder.addDataSet(buildDataSetFor(builder, model));
		return builder.build();
	}
	
	public IDataSet buildDataSetFor(DataSetBuilder builder, Object model) throws DataSetException{
		ensurePresenceOfTable(model);
		DataRowBuilder row = builder.newRow(modelName(model));
		return row.add().build();
	}
	
	public String modelName(Object model) throws DataSetException{
		ensurePresenceOfTable(model);
		Table table = model.getClass().getAnnotation(Table.class);
		return table.name().isEmpty() ? StringUtil.normalizeTableName(model.getClass().getSimpleName()) : table.name();
	}
	
	public void ensurePresenceOfTable(Object model) throws DataSetException{
		if(!isAnnotationPresent(Table.class, model))
			throw new DataSetException("No Table annotation found in " + model.getClass().getName());
	}
	
	protected Boolean isAnnotationPresent(Class<? extends Annotation> clazz, Object object){
		return object.getClass().isAnnotationPresent(clazz);
	}
}
