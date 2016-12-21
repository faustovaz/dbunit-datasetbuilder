package org.dbunit.dataset.builder.util;

public class StringUtil {
	public static String normalizeTableName(String tableName){
		return tableName
					.replaceAll("(\\p{Upper})", "_$1")
					.substring(1).toLowerCase();
	}

}