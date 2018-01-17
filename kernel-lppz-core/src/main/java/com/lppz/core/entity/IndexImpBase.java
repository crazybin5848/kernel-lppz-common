package com.lppz.core.entity;

import java.util.ArrayList;
public class IndexImpBase implements Index {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String _name;
	private boolean _isUnique;
	public void setIsUnique(boolean _isUnique) {
		this._isUnique = _isUnique;
	}

	protected ArrayList<Column> _columns;
	public static Index build(boolean _isUnique,String _name){
		Index index=new IndexImpBase();
		index.setIsUnique(_isUnique);
		index.setName(_name);
		return index;
	}
	public IndexImpBase() {
		this._columns = new ArrayList<Column>();
	}

	public String getName() {
		return this._name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public int getColumnCount() {
		return this._columns.size();
	}

	public Column getColumn(int idx) {
		return ((Column) this._columns.get(idx));
	}

	public Column[] getColumns() {
		return  (Column[]) this._columns
				.toArray(new Column[this._columns.size()]);
	}


	public boolean hasColumn(Column column) {
		for (int idx = 0; idx < this._columns.size(); ++idx) {
			Column curColumn = getColumn(idx);
			if (column.equals(curColumn)) {
				return true;
			}
		}
		return false;
	}

	public void addColumn(Column column) {
		if (column == null)
			return;
		if(!hasColumn(column))
		this._columns.add(column);
	}

	public void removeColumn(Column column) {
		this._columns.remove(column);
	}

	public void removeColumn(int idx) {
		this._columns.remove(idx);
	}

	@Override
	public boolean isUnique() {
		return _isUnique;
	}

}