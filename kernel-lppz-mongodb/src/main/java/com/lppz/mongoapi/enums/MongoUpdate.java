package com.lppz.mongoapi.enums;

public enum MongoUpdate {
	set("$set"),push("$push"),pushAll("$pushAll"),pull("$pull"),pullAll("$pullAll"),inc("$inc"),redis("redis"),es("es");
	
	private String op;
	
	private MongoUpdate(String op) {
		this.op = op;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}
	
	public static MongoUpdate convertOf(String op){
		MongoUpdate[] values = values();
		for (MongoUpdate value : values) {
			if (value.getOp().equals(op)) {
				return value;
			}
		}
		return null;
	}

}
