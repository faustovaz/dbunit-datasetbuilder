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
		ensurePresenceOfTable();
		DataSetBuilder dsBuilder = new DataSetBuilder();
		for(Object model : this.models)
			dsBuilder.newRow(model.getClass().getAnnotation(Table.class).name()).add();
		return dsBuilder.build();
	}
	
	public void ensurePresenceOfTable() throws DataSetException{
		for(Object model : this.models)
			if(!isAnnotationPresent(Table.class, model))
				throw new DataSetException("No Table annotation found in " + model.getClass().getName());
	}
	
	protected Boolean isAnnotationPresent(Class<? extends Annotation> clazz, Object object){
		return object.getClass().isAnnotationPresent(clazz);
	}
}
