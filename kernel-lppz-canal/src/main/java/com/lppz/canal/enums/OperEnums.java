package com.lppz.canal.enums;


public enum OperEnums {

	INSERT(1, "INSERT"), UPDATE(2, "UPDATE"), DELETE(3, "DELETE"), CREATE(4,
			"CREATE"), ALTER(5, "ALTER"), ERASE(6, "ERASE"), QUERY(7, "QUERY"), TRUNCATE(
			8, "TRUNCATE"), RENAME(9, "RENAME"), CINDEX(10, "CINDEX"), DINDEX(
			11, "DINDEX");

	private int value;
	private String name;

	OperEnums(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static OperEnums valueOf(int value) {
	      switch (value) {
	        case 1: return INSERT;
	        case 2: return UPDATE;
	        case 3: return DELETE;
	        case 4: return CREATE;
	        case 5: return ALTER;
	        case 6: return ERASE;
	        case 7: return QUERY;
	        case 8: return TRUNCATE;
	        case 9: return RENAME;
	        case 10: return CINDEX;
	        case 11: return DINDEX;
	        default: return null;
	      }
	    }
}
