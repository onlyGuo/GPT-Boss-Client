package com.guoshengkai.gpt.cn.core.sql.where;

/**
 * 比较运算符枚举
 */
public enum C {

	/**
	 * 比较运算符
	 */
	EQ("=", "等于"),
	NE("<>", "不等于"),
	LIKE("LIKE", "相似"),

	/**
	 * 以xxx开头
	 */
	START_WITH("LIKE", "以XXX开头"),
	/**
	 * 以xxx结尾
	 */
	END_WITH("LIKE", "以XXX结尾"),
	NLIKE("NOT LIKE", "字段中不包含"),
	DA(">", "大于"),
	XIAO("<", "小于"),
	IN( "IN", "在XXX内"),
	NOTIN("NOT IN", "不在XXX内"),
	DE(">=", "大于或等于"),
	XE("<=", "小于或等于");

	private String value;

	private String dscp;

	C(String value, String dscp){
		this.value = value;
		this.dscp = dscp;
	}

	public String value(){
		return value;
	}

}
