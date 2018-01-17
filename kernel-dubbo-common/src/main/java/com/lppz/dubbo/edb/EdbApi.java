package com.lppz.dubbo.edb;

import com.lppz.dubbo.edb.model.EdbRestModel;

/**
 * edb 上行api
 * @author licheng
 *
 */
public interface EdbApi {
	
	public String deliver2Edb(EdbRestModel edbRestModel);
	
	/**
	 * 同步店铺可售库存到E店宝
	 *
	 * @param itemDistributions 店铺商品库存分配集合
	 * @return
	 */
	boolean updateStoreStock2Edb(EdbRestModel edbRestModel);

	/**
	 * 同步商品数据到E店宝
	 *
	 * @param products 商品数据集合
	 * @param operate 操作类型0添加、1修改、2删除
	 */
//	void productSyn2Edb(List<Product> products, int operate,EdbProductException edbProductException);

}
