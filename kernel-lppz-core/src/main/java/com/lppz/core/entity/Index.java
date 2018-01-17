package com.lppz.core.entity;

import java.io.Serializable;

public abstract interface Index extends Cloneable, Serializable {
	public abstract boolean isUnique();
	public abstract void setIsUnique(boolean _isUnique);
	public abstract String getName();

	public abstract void setName(String paramString);
	
	public abstract int getColumnCount();

	public abstract Column getColumn(int paramInt);

	public abstract Column[] getColumns();

	public abstract boolean hasColumn(Column paramColumn);

	public abstract void addColumn(Column paramColumn);

	public abstract void removeColumn(Column paramColumn);

	public abstract void removeColumn(int paramInt);

}