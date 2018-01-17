package com.lppz.util.logback;

import java.io.Serializable;

public class LogBackBizEsVo implements Serializable{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((esIdxName == null) ? 0 : esIdxName.hashCode());
		result = prime * result
				+ ((idxSurffix == null) ? 0 : idxSurffix.hashCode());
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
		LogBackBizEsVo other = (LogBackBizEsVo) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (esIdxName == null) {
			if (other.esIdxName != null)
				return false;
		} else if (!esIdxName.equals(other.esIdxName))
			return false;
		if (idxSurffix == null) {
			if (other.idxSurffix != null)
				return false;
		} else if (!idxSurffix.equals(other.idxSurffix))
			return false;
		return true;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -1159782743178685238L;

	private String esIdxName;
	private String className;
	private String idxSurffix;
	public String getEsIdxName() {
		return esIdxName;
	}
	public void setEsIdxName(String esIdxName) {
		this.esIdxName = esIdxName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getIdxSurffix() {
		return idxSurffix;
	}
	public void setIdxSurffix(String idxSurffix) {
		this.idxSurffix = idxSurffix;
	}
}