package com.lppz.webauth.rest.common;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

public class PageBoundsUtils {
	
	
	public static PageBounds createPageBounds(UriInfo uriInfo){
		final Map<String, List<String>> parameters = uriInfo.getQueryParameters();
		int page = parameterAsInteger(getSimpleParameter("page", parameters));
		Integer limit = parameterAsInteger(getSimpleParameter("rows", parameters));
		
		
		
		PageBounds pageBounds = new PageBounds(page, limit);
		final String sortColumn = getSimpleParameter("sort", parameters);
		final String sortDirection = getSimpleParameter("order", parameters);
		if (StringUtils.isNotEmpty(sortColumn))
		{
			String sort = (StringUtils.isEmpty(sortDirection) || sortDirection.equals("asc")) ? "":"desc";
			pageBounds.getOrders().add(Order.create(sortColumn, sort));
		}
		
		return pageBounds;
	}
	
	private static String getSimpleParameter(final String name, final Map<String, List<String>> parameters)
	{
		final List<String> value = parameters.get(name);
		return !CollectionUtils.isEmpty(value) ? (String) value.iterator().next() : null;
	}
	private static Integer parameterAsInteger(final String parameter)
	{
		Integer result = null;
		if (StringUtils.isNotEmpty(parameter))
		{
			result = Integer.valueOf(parameter);
		}
		return result;
	}
}
