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

public class ObjectBasedDataSetBuilderTest {

	@Test
	public void ensurePresenceOfATable() throws DataSetException {
		ObjectBasedDataSetBuilder builder = new ObjectBasedDataSetBuilder(ModelBuilder.aModel());
		IDataSet dataSet = builder.build();
		assertArrayEquals(new String[]{"model"}, dataSet.getTableNames());
	}
	
	@Test
	public void ensurePresenceOfTables() throws DataSetException{
		ObjectBasedDataSetBuilder builder = new ObjectBasedDataSetBuilder(ModelBuilder.threeModels());
		IDataSet dataSet = builder.build();
		assertArrayEquals(new String[]{"model", "model_two", "model_three"}, dataSet.getTableNames());
	}
	
	@Test(expected=DataSetException.class)
	public void ensureExceptionIsThrown() throws DataSetException{
		ObjectBasedDataSetBuilder builder = new ObjectBasedDataSetBuilder(ModelBuilder.aNotAModel());
		builder.build();
	}
	
	@Test
	public void ensurePresenceOfTableName() throws DataSetException{
		ObjectBasedDataSetBuilder builder = new ObjectBasedDataSetBuilder(ModelBuilder.aModelWithNoTableName());
		IDataSet dataSet = builder.build();
		assertArrayEquals(new String[]{"model_with_no_table_name"}, dataSet.getTableNames());
	}
	
	@Test
	public void ensurePresenceOfColumns() throws DataSetException{
		List<String> columnsNames = new ArrayList<String>();
		ObjectBasedDataSetBuilder builder = new ObjectBasedDataSetBuilder(ModelBuilder.aModel());
		Column[] columns = builder.build().getTableMetaData("model").getColumns();
		for(Column column : columns) columnsNames.add(column.getColumnName());
		assertArrayEquals(new String[]{"id_model", "model_value"}, columnsNames.toArray());
	}
	
	@Test
	public void ensurePresenceOfColumnsAndValues() throws DataSetException{
		ObjectBasedDataSetBuilder builder = new ObjectBasedDataSetBuilder(ModelBuilder.aModel());
		ITable table = builder.build().getTable("model");
		assertEquals(1, table.getRowCount());
		assertEquals(1, table.getValue(0, "id_model"));
		assertEquals("value", table.getValue(0, "model_value"));
	}
}
