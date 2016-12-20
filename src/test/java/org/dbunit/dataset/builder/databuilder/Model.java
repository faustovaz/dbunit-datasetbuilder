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
class ModelOne{}

@Table(name="model_two")
class ModelTwo{}

@Table(name="model_three")
class ModelThree{}

@Table
class ModelWithNoTableName{}

class NotAModel{}