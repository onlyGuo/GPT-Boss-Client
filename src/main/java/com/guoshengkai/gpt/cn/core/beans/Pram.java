package com.guoshengkai.gpt.cn.core.beans;


import com.guoshengkai.gpt.cn.core.annotation.po.LongTextField;
import com.guoshengkai.gpt.cn.core.annotation.po.vali.Validation;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * POJO字段封装类
 * @author 郭胜凯
 * @time 2015-下午10:54:36
 * @email 719348277@qq.com
 */
public class Pram {

	private String dbField;

	private Field field;
	
	private Object value;

	public Pram(){}
	public Pram(String dbField, Field field, Object value){
		this.dbField = dbField;
		this.field = field;
		this.value = value;
	}

	public String getDbField() {
		return dbField;
	}

	public void setDbField(String dbField) {
		this.dbField = dbField;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getDbType() {
		if (null == field){
			return null;
		}
		if (field.getGenericType().equals(String.class)) {
			if (field.getAnnotation(LongTextField.class) != null){
				return "LONGTEXT";
			}else if (field.getAnnotation(Validation.class) != null) {
				Validation annotation = field.getAnnotation(Validation.class);
				if (annotation.maxLength() == 0){
					return "VARCHAR(255)";
				}else if (annotation.maxLength() <= 1024){
					return "VARCHAR(" + annotation.maxLength() + ")";
				}else if (annotation.maxLength() <= 65535) {
					return "TEXT";
				}else {
					return "LONGTEXT";
				}
			}
			return "VARCHAR(255)";
		}else if (field.getGenericType().equals(int.class) || field.getGenericType().equals(Integer.class)) {
			return "INT(11)";
		}else if (field.getGenericType().equals(long.class) || field.getGenericType().equals(Long.class)) {
			return "BIGINT(20)";
		}else if (field.getGenericType().equals(float.class) || field.getGenericType().equals(Float.class)) {
			return "FLOAT";
		}else if (field.getGenericType().equals(double.class) || field.getGenericType().equals(Double.class)) {
			return "DOUBLE";
		}else if (field.getGenericType().equals(boolean.class) || field.getGenericType().equals(Boolean.class)) {
			return "TINYINT(1)";
		}else if (field.getGenericType().equals(byte.class) || field.getGenericType().equals(Byte.class)) {
			return "TINYINT(1)";
		}else if (field.getGenericType().equals(char.class) || field.getGenericType().equals(Character.class)) {
			return "CHAR(1)";
		}else if (field.getGenericType().equals(short.class) || field.getGenericType().equals(Short.class)) {
			return "SMALLINT(6)";
		}else if (field.getGenericType().equals(BigDecimal.class)) {
			return "DECIMAL(20,4)";
		}else if (field.getGenericType().equals(java.util.Date.class)) {
			return "DATETIME";
		}else if (field.getGenericType().equals(java.sql.Date.class)) {
			return "DATE";
		}else if (field.getGenericType().equals(java.sql.Time.class)) {
			return "TIME";
		}else if (field.getGenericType().equals(java.sql.Timestamp.class)) {
			return "TIMESTAMP";
		}else{
			return "VARCHAR(255)";
		}
	}
}
