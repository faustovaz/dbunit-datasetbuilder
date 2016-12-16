package org.dbunit.dataset.builder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Table;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;

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
	
	public void ensurePresenceOfTable(Object model) throws DataSetException{
		if(!isAnnotationPresent(Table.class, model))
			throw new DataSetException("No Table annotation found in " + model.getClass().getName());
	}
	
	public IDataSet build(DataSetBuilder builder) throws DataSetException{
		for(Object model : this.models)
			builder.addDataSet(buildDataSetFor(model));
		return builder.build();
	}
	
	public IDataSet buildDataSetFor(Object model) throws DataSetException{
		ensurePresenceOfTable(model);
		DataSetBuilder builder = new DataSetBuilder();
		return builder.newRow(model.getClass().getAnnotation(Table.class).name()).add().build();
	}
	
	protected Boolean isAnnotationPresent(Class<? extends Annotation> clazz, Object object){
		return object.getClass().isAnnotationPresent(clazz);
	}
}
