package org.dbunit.dataset.builder.databuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="model")
public class Model {
	@Id
	@Column(name="id_model")
	private int id;
	@Column(name="model_value")
	private String value;
	
	public Model(int id, String value){
		this.id = id;
		this.value = value;
	}
}

@Table(name="model_one")
class ModelOne{
	private int id;
	private String value;
	
	public ModelOne(int id, String value) {
		this.id = id;
		this.value = value;
	}
	
	@Column(name="id_model")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="model_value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}

@Table(name="model_two")
class ModelTwo{}

@Table(name="model_three")
class ModelThree{}

@Table
class ModelWithNoTableName{}

class NotAModel{}