package org.dbunit.dataset.builder.databuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

@Table(name="model_with_join_column")
class ModelWithJoinColumn{
	@Id
	@Column(name="id_model")
	private Integer id;
	
	@Column(name="model_value")
	private String value;
	
	@JoinColumn(name="another_model_id")
	private Model model;
	
	public ModelWithJoinColumn(Integer id, String value, Model model) {
		this.id = id;
		this.value = value;
		this.model = model;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
}