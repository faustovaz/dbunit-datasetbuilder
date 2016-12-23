package org.dbunit.dataset.builder;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.builder.databuilder.ModelBuilder;
import org.junit.Test;

public class JPAEntityBasedDataSetBuilderTest {

	@Test
	public void ensurePresenceOfATable() throws DataSetException {
		JPAEntityBasedDataSetBuilder builder = new JPAEntityBasedDataSetBuilder(ModelBuilder.aModel());
		IDataSet dataSet = builder.build();
		assertArrayEquals(new String[]{"model"}, dataSet.getTableNames());
	}
	
	@Test
	public void ensurePresenceOfTables() throws DataSetException{
		JPAEntityBasedDataSetBuilder builder = new JPAEntityBasedDataSetBuilder(ModelBuilder.threeModels());
		IDataSet dataSet = builder.build();
		assertArrayEquals(new String[]{"model", "model_two", "model_three"}, dataSet.getTableNames());
	}
	
	@Test(expected=DataSetException.class)
	public void ensureExceptionIsThrown() throws DataSetException{
		JPAEntityBasedDataSetBuilder builder = new JPAEntityBasedDataSetBuilder(ModelBuilder.aNotAModel());
		builder.build();
	}
	
	@Test
	public void ensurePresenceOfTableName() throws DataSetException{
		JPAEntityBasedDataSetBuilder builder = new JPAEntityBasedDataSetBuilder(ModelBuilder.aModelWithNoTableName());
		IDataSet dataSet = builder.build();
		assertArrayEquals(new String[]{"model_with_no_table_name"}, dataSet.getTableNames());
	}
	
	@Test
	public void ensurePresenceOfColumns() throws DataSetException{
		List<String> columnsNames = new ArrayList<String>();
		JPAEntityBasedDataSetBuilder builder = new JPAEntityBasedDataSetBuilder(ModelBuilder.aModel());
		Column[] columns = builder.build().getTableMetaData("model").getColumns();
		for(Column column : columns) 
			columnsNames.add(column.getColumnName());
		assertArrayEquals(new String[]{"id_model", "model_value"}, columnsNames.toArray());
	}
	
	@Test
	public void ensurePresenceOfColumnsAndValues() throws DataSetException{
		JPAEntityBasedDataSetBuilder builder = new JPAEntityBasedDataSetBuilder(ModelBuilder.aModel());
		ITable table = builder.build().getTable("model");
		assertEquals(1, table.getRowCount());
		assertEquals(1, table.getValue(0, "id_model"));
		assertEquals("value", table.getValue(0, "model_value"));
	}
	
	@Test
	public void ensurePresenceOfAnnotatedMethodValues() throws DataSetException{
		JPAEntityBasedDataSetBuilder builder = new JPAEntityBasedDataSetBuilder(ModelBuilder.aAnnotedMethodModel());
		ITable table = builder.build().getTable("model_one");
		assertEquals(1, table.getRowCount());
		assertEquals(1, table.getValue(0, "id_model"));
		assertEquals("value", table.getValue(0, "model_value"));
	}
	
	@Test
	public void ensureJoinColumnAreMapped() throws DataSetException{
		JPAEntityBasedDataSetBuilder builder = new JPAEntityBasedDataSetBuilder(ModelBuilder.aModelWithJoinColumn());
		ITable table = builder.build().getTable("model_with_join_column");
		assertEquals(1, table.getRowCount());
		assertEquals(1, table.getValue(0, "id_model"));
		assertEquals("value", table.getValue(0, "model_value"));
		assertEquals(1, table.getValue(0, "another_model_id"));
	}
}
