package org.dbunit.dataset.builder.databuilder;

import java.util.ArrayList;
import java.util.Collection;

public class ModelBuilder {

	public static Model aModel(){
		return new Model();
	}
	
	public static ModelTwo aModelTwo(){
		return new ModelTwo();
	}
	
	public static ModelThree aModelThree(){
		return new ModelThree();
	}
	
	public static Collection<Object> threeModels(){
		Collection<Object> objects = new ArrayList<Object>();
		objects.add(aModel());
		objects.add(aModelTwo());
		objects.add(aModelThree());
		return objects;
	}
	
}
