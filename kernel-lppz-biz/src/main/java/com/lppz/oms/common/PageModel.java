package com.lppz.oms.common;

import java.util.ArrayList;
import java.util.List;


/**
 * 分页模型
 * 
 * @author
 * 
 * @param <T>
 */
public class PageModel<T>
{

	private Integer page;// 当前页码
	private Integer totalCount;// 总记录条数
	private Integer pageCount;// 总页数
	private Integer pageSize = 15;

	private List<T> list = new ArrayList<T>();

	public PageModel(final Integer page, final Integer totalCount)
	{
		this.page = page;
		this.totalCount = totalCount;
	}

	public PageModel(final Integer page, final Integer totalCount, final Integer pageSize)
	{
		this.page = page;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
	}

	public PageModel(final Integer page, final Integer pageSize, final List<T> results)
	{
		this.page = page;
		this.totalCount = results.size();
		this.pageSize = pageSize;
		getCurrentList(results);
	}

	public Integer getPage()
	{
		return page;
	}

	public void setPage(final Integer page)
	{
		this.page = page;
	}

	public Integer getTotalCount()
	{
		return totalCount;
	}

	public void setTotalCount(final Integer totalCount)
	{
		this.totalCount = totalCount;
	}

	public Integer getPageCount()
	{
		pageCount = totalCount / pageSize;
		if (totalCount % pageSize > 0)
		{
			pageCount++;
		}
		return pageCount;
	}

	public void setPageCount(final Integer pageCount)
	{
		this.pageCount = pageCount;
	}

	public Integer getPageSize()
	{
		return pageSize;
	}

	public int[] getRange()
	{
		if (page != null)
		{
			final int start = (page - 1) * pageSize;
			return new int[]{start, pageSize};
		}
		else
		{
			return null;
		}
	}

	/**
	 * 获取分页数据集合
	 * 
	 * @return
	 */
	public List<T> getList()
	{
		return list;
	}

	public void setList(final List<T> list)
	{
		this.list = list;
	}


	/**
	 * @param resultList
	 * 
	 */
	public void getCurrentList(final List<T> resultList)
	{
		// final int pageCount = getPageCount();
		// this.page
		// this.totalCount
		// this.pageSize
		List<T> currentList = new ArrayList<T>();
		pageCount = totalCount / pageSize;
		final int start = (page - 1) * pageSize;
		int end = page * pageSize;
		if (totalCount <= end)
		{
			end = totalCount;
		}
		if (pageCount > 0)
		{
			currentList = resultList.subList(start, end);
		}
		else
		{
			currentList = resultList;
		}
		list.addAll(currentList);
	}


}
