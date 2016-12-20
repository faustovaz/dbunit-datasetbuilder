package org.dbunit.dataset.builder.util;

import java.lang.reflect.Field;

public class StringUtil {
	public static String normalizeTableName(String tableName){
		return tableName
					.replaceAll("(\\p{Upper})", "_$1")
					.substring(1).toLowerCase();
	}
	
	public static String getterMethodNameOf(Field field){
		return "";
	}
}