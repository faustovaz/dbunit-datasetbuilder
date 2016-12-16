package org.dbunit.dataset.builder;

import static org.junit.Assert.assertArrayEquals;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
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
}
