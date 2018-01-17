package com.lppz.mongoapi.bean;

import java.util.List;

import org.bson.Document;

public class DictModel {
	
	private String table;//mongodb 表名
	private boolean dayLiving;//是否日活
	private boolean saveSync;
	private DictMongoEsModel esModel; //mongo存es相关数据model
	private DictMongoRedisModel redisModel;//mongo存redis相关数据model
	private String pk;
	private List<String> kegArrayFields;//需要分桶的数组字段，多个字段只支持snsactivity表中storeId和storeName这种直接关联的多数组
	private boolean useNew;
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public DictMongoEsModel getEsModel() {
		return esModel;
	}
	public void setEsModel(DictMongoEsModel esModel) {
		this.esModel = esModel;
	}
	public DictMongoRedisModel getRedisModel() {
		return redisModel;
	}
	public void setRedisModel(DictMongoRedisModel redisModel) {
		this.redisModel = redisModel;
	}
	public boolean isDayLiving() {
		return dayLiving;
	}
	public void setDayLiving(boolean dayLiving) {
		this.dayLiving = dayLiving;
	}
	public boolean isSaveSync() {
		return saveSync;
	}
	public void setSaveSync(boolean saveSync) {
		this.saveSync = saveSync;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public List<String> getKegArrayFields() {
		return kegArrayFields;
	}
	public void setKegArrayFields(List<String> kegArrayField) {
		this.kegArrayFields = kegArrayField;
	}
	
	public boolean isUseNew() {
		return useNew;
	}
	public void setUseNew(boolean useNew) {
		this.useNew = useNew;
	}
	public static DictModel build(Document document){
		/*Document{{_id=597592d484146a10a87d4a5a, 
		*table=status, 
		*esModel=Document{{indexName=status-, type=com.lppz.app.Status, surffixFormat=yyyy-MM-dd, mongoEsMap=Document{{statusId=statusId}}}}, 
		*redisModel=Document{{mongoRedisMixSubPk=commondId, mongoRedisMixSubCloumn=likedCount}}}}
		**/
		DictModel dictModel = new DictModel();
		dictModel.setTable(document.get("table", String.class));
		dictModel.setPk(document.get("pk", String.class));
		dictModel.setKegArrayFields(document.get("kegArrayField", List.class));
		dictModel.setDayLiving(document.getBoolean("dayLiving", false));
		dictModel.setSaveSync(document.getBoolean("saveSync", false));
		dictModel.setEsModel(DictMongoEsModel.build(document.get("esModel",Document.class)));
		dictModel.setRedisModel(DictMongoRedisModel.build(document.get("redisModel", Document.class)));
		dictModel.setUseNew(document.getBoolean("useNew", true));
		return dictModel;
	}
	@Override
	public String toString() {
		return "DictModel [table=" + table + ", dayLiving=" + dayLiving
				+ ", saveSync=" + saveSync + ", esModel=" + esModel
				+ ", redisModel=" + redisModel + ", pk=" + pk
				+ ", kegArrayFields=" + kegArrayFields + ", useNew=" + useNew
				+ "]";
	}
	
}
