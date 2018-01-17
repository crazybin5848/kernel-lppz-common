package com.lppz.mongoapi.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;

import com.lppz.elasticsearch.search.SearchCondition;
import com.lppz.mongoapi.bean.DictModel;
import com.lppz.util.curator.listener.ZookeeperProcessListen;

public interface MongoDao {
	
	/**
	 * 插入
	 * @param table 表名
	 * @param document 需要插入的数据
	 */
	public void insert(String table,Document document);
	/**
	 * 插入
	 * @param table 表名
	 * @param documents 需要插入的数据集合
	 */
	public void insertBatch(String table,List<Document> documents);
	
	/**
	 * 数组push操作，包含分桶操作
	 * 客户端对相同行操作必须是串行的，不然分桶可能不准，将导致数组分页查询不准
	 * 业务模块发送RocketMq需使用orderly方式，并使用业务主键的hash绝对值做orderly条件，保证相同业务行的数据在同一队列中串行
	 * 消费端同样使用orderly方式消费，确保消息的顺序
	 * @param table 表名
	 * @param pk 业务主键名称，如会员编号
	 * @param pkValue 业务主键值
	 * @param arrayFiled 数字字段名称
	 * @param documents 数组内容
	 */
	public void push(String table, String pk, String pkValue, String arrayFiled, List<Object> documents);
	
	/**
	 * 
	 * @param table
	 * @param pk 业务主键名，如会员编号
	 * @param pkValue 业务主键值
	 * @param arrayField 需要pull的数组字段名，内嵌文档使用parent.child方式
	 * @param value 需要pull的值
	 * @return 成操作条数
	 */
	public long pull(String table, String pk, String pkValue, String arrayField, Object value);
	
	/**
	 * 
	 * @param table
	 * @param pk 业务主键名，如会员编号
	 * @param pkValue 业务主键值
	 * @param arrayField 需要pull的数组字段名，内嵌文档使用parent.child方式
	 * @param value 需要pull的值
	 * @return 成操作条数
	 */
	public long pullAll(String table, String pk, String pkValue, String arrayField, List<Object> values);
	
	/**
	 * 更新操作 只支持$set
	 * @param table 表名
	 * @param query 更新条件
	 * @param update 更新内容，不包含$set等命令
	 * @param upsert 存在更新还是存在不更新 //TODO
	 * @param isMutil 是否更新多行
	 */
	public void update(String table, Document query, Document update, boolean upsert, boolean isMutil);
	
	/**
	 * +-n操作
	 * @param table 操作的表名
	 * @param pk 业务主键名
	 * @param pkValue 业务主键值，如会员编号
	 * @param column 自增字段名，粉丝数
	 * @param opValue 正值表示+n，负值表示-n 
	 * @return 操作后的值
	 */
	public long incr(String table, String pk, String pkValue, String column, int opValue);
	
	/**
	 * 查询自增属性字段值
	 * @param pkValue 业务主键值，如会员编号
	 * @param column 自增字段名，粉丝数
	 * @return 对应属性的当前值
	 */
	public long getIncrValue(String pkValue, String column);
	
	/**
	 * 删除
	 * @param table 表名
	 * @param filter 删除条件
	 */
	public long delete(String table, Document filter);
	
	/**
	 * redis存放
	 * @param cacheModel 
	 * key=>会员编号 
	 * value.key=>字段名 
	 * value.value=>字段值
	 */
	public void saveInRedis(Map<String,Map<String,String>> cacheModel);
	
	/**
	 * 查询缓存
	 * 如果缓存不存在查询mongo再刷新缓存
	 * 后三个参数用于查询mongo中的数据
	 * @param cacheKey 缓存键，如会员编号
	 * @param table 表名
	 * @param columnName 缓存键字段名
	 * @param fieldNames 缓存字段
	 * @return 结果集map
	 */
	public Map<String,String> findCache(String cacheKey,String table,String columnName,List<String> fieldNames);
	
	/**
	 * 查询方法，不支持分页
	 * @param table 表名
	 * @param query 查询条件
	 * @param fieldNames 返回字段列表
	 * @param sort排序，key为排序字段，value为1/-1对应升序和降序，多排序字段以传入顺序先后处理
	 * @param skip 分页起始位置
	 * @param limit 分页大小
	 * @return
	 */
	public List<Document> findMongo(String table, Document query, List<String> fieldNames, Document sort, Integer skip, Integer limit);
	
	/**
	 * 数组的查询，支持分页
	 * mongo.push.array.max.length（默认值1000）要能被pageSize整除分页结果才能正确
	 * @param table 表名
	 * @param pk 业务主键，如会员编号
	 * @param pkValue 业务主键值
	 * @param fieldName 查询的字段
	 * @param resultFields 返回字段，不用传入fieldName字段，可包含内嵌文档的部分字段，如fieldName.a
	 * @param pageNo 页数
	 * @param pageSize 分页大小
	 * @return 返回fieldName列表
	 */
	public List<Document> pagingArrays(String table, String pk, String pkValue, String fieldName
			, List<String> resultFields, int pageNo, int pageSize, boolean desc);
	
	/**
	 * es查询方法
	 * @param sc es查询参数
	 * @return 查询结果
	 * rows List<SearchResult>结果集合
	 * total 符合条件总数
	 * sucess 是否成功
	 * message 提示消息
	 */
	public Map<String, Object> searchInEs(SearchCondition sc);
	
	/**
	 * 写es方法
	 * @param indexName es模版名
	 * @param body es保存内容
	 * @param id 业务主键，用于控制更新还是插入操作
	 * @param esDtoClass body对应的es模版dto类
	 * @param isUpdate 是否更新操作
	 */
	public void saveInEs(String indexName, String body, String id, Class esDtoClass, boolean ... isUpdate);
	
	/**
	 * 删除es数据
	 * @param table mongo表名
	 * @param id
	 */
	public int deleteInEs(String table, String id);
	
	/**
	 * 根据表名获取字典数据
	 * @param table 表名
	 * @return
	 */
	public DictModel getDictModel(String table);
	
	/**
	 * 增加用户未读私信
	 * @param userId
	 * @param category
	 * @param msgId
	 */
	public void addUnRead(String userId, String category, String msgId);
	
	/**
	 * 标记已读私信
	 * @param userId
	 * @param category
	 * @param msgId
	 */
	public void readed(String userId, String category, String msgId);
	
	/**
	 * 获取用户未读私信列表
	 * @param userId
	 * @param category
	 * @return
	 */
	public Set<String> getUnReadMsgs(String userId, String category);
	
	/**
	 * 如果不存在，设置key value 和过期时间（s），返回OK；如果存在，设置失败，返回null
	 * @param key 键格式化为app:::lock:::%s
	 * @param value 值
	 * @param expire 过期时间，单位秒
	 * @return 成功OK，失败null
	 */
	public String setNxEx(String key, String value, int expire);
	
	/**
	 * 删除setNxEx设置key-value
	 * @param key键格式化为app:::lock:::%s
	 * @return 成功1，失败0
	 */
	public Long delNxEx(String key);
	
	public void addListen(String key, ZookeeperProcessListen zkListen);
	
}
