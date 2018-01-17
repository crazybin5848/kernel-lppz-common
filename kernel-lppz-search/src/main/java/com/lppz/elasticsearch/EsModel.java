package com.lppz.elasticsearch;

import org.elasticsearch.action.bulk.BulkItemResponse.Failure;

import com.lppz.util.EsJsonSourceModel;


public class EsModel<T extends EsJsonSourceModel> {
	private String index;
	private String type;
	private String id;
	private Object jsonSource;
	private T t;
	private Failure failure;
	public Failure getFailure() {
		return failure;
	}
	public void setFailure(Failure failure) {
		this.failure = failure;
	}
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}

	private EsDMlEnum enumType;
	public EsModel(String index,String type,String id,Object jsonSource,EsDMlEnum enumType){
		this.index=index;
		this.type=type;
		this.id=id;
		this.jsonSource=jsonSource;
		this.enumType=enumType;
	}
	public EsDMlEnum getEnumType() {
		return enumType;
	}
	public void setEnumType(EsDMlEnum enumType) {
		this.enumType = enumType;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Object getJsonSource() {
		return jsonSource;
	}
	public void setJsonSource(Object jsonSource) {
		this.jsonSource = jsonSource;
	}
	
	public enum EsDMlEnum{
		Insert,Update,Delete
	}

	@Override
	public String toString() {
		return "EsModel [index=" + index + ", type=" + type + ", id=" + id
				+ ", jsonSource=" + jsonSource + ", t=" + t + ", enumType="
				+ enumType + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((enumType == null) ? 0 : enumType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		result = prime * result
				+ ((jsonSource == null) ? 0 : jsonSource.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EsModel other = (EsModel) obj;
		if (enumType != other.enumType)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		if (jsonSource == null) {
			if (other.jsonSource != null)
				return false;
		} else if (!jsonSource.equals(other.jsonSource))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
