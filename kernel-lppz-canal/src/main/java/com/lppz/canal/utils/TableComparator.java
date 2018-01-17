package com.lppz.canal.utils;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import com.lppz.canal.model.ChangeRow;

public class TableComparator implements Comparator<ChangeRow> {

	@Override
	public int compare(ChangeRow arg0, ChangeRow arg1) {
		if(StringUtils.isNotBlank(arg0.getPrimaryKeyValue()) && StringUtils.isNotBlank(arg1.getPrimaryKeyValue())){
			if(arg0.getPrimaryKeyValue().compareTo(arg1.getPrimaryKeyValue())<0){
				return 1;
			}
		}
		return 0;
	}

}
