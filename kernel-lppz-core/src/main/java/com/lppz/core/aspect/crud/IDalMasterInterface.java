package com.lppz.core.aspect.crud;

import java.util.List;

import com.lppz.core.annotation.Dal;
import com.lppz.core.annotation.HashKeyInsertDs;
import com.lppz.core.annotation.HashKeyListDs;
import com.lppz.core.annotation.HashKeyListInserDs;
import com.lppz.core.entity.DalEntity;
/**
 *
 */
@Dal
public interface IDalMasterInterface
{
	@HashKeyListDs
	public <T> void batchInsertOrUpdate(@HashKeyListInserDs List<DalEntity<T>> list) throws Exception;

	/**
	 * @param routeKey
	 * @throws Exception
	 */
	public <T> int insertOrUpdate(@HashKeyInsertDs DalEntity<T> d) throws Exception;
}
