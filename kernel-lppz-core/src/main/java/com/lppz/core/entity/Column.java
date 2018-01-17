package com.lppz.core.entity;

import java.io.Serializable;

public class Column implements Cloneable, Serializable {
	private static final long serialVersionUID = -6226348998874210093L;
	private String _name;
	private String _description;
	private String _type;
	private String _defaultValue;
	public Column(String _name,String _description,String _type,String _defaultValue){
		this._name=_name;
		this._description=_description;
		this._type=_type;
		this._defaultValue=_defaultValue;
	}
	public String getName() {
		return _name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_defaultValue == null) ? 0 : _defaultValue.hashCode());
		result = prime * result
				+ ((_description == null) ? 0 : _description.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime * result + ((_type == null) ? 0 : _type.hashCode());
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
		Column other = (Column) obj;
		if (_defaultValue == null) {
			if (other._defaultValue != null)
				return false;
		} else if (!_defaultValue.equals(other._defaultValue))
			return false;
		if (_description == null) {
			if (other._description != null)
				return false;
		} else if (!_description.equals(other._description))
			return false;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		if (_type == null) {
			if (other._type != null)
				return false;
		} else if (!_type.equals(other._type))
			return false;
		return true;
	}
	public void setName(String _name) {
		this._name = _name;
	}
	public String getDescription() {
		return _description;
	}
	public void setDescription(String _description) {
		this._description = _description;
	}
	public String getType() {
		return _type;
	}
	public void setType(String _type) {
		this._type = _type;
	}
	public String getDefaultValue() {
		return _defaultValue;
	}
	public void setDefaultValue(String _defaultValue) {
		this._defaultValue = _defaultValue;
	}

	
}