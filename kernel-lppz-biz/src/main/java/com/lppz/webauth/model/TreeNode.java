package com.lppz.webauth.model;

import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

/**
 *
 */
public class TreeNode
{
	private String nodeCode;

	private String nodeName;

	private String checked;

	private String parentCode;

	/**
	 *
	 */
	public TreeNode(final String nodeCode, final String nodeName, final String checked, final String parentCode)
	{
		super();
		this.nodeCode = nodeCode;
		this.nodeName = nodeName;
		this.checked = checked;
		this.parentCode = parentCode;
	}

	/**
	 * @return the parentCode
	 */
	public String getParentCode()
	{
		return parentCode;
	}

	/**
	 * @param parentCode the parentCode to set
	 */
	public void setParentCode(final String parentCode)
	{
		this.parentCode = parentCode;
	}

	private Map<String, TreeNode> children;



	/**
	 *
	 */
	public TreeNode(final String nodeCode, final String nodeName, final String checked)
	{
		super();
		this.nodeCode = nodeCode;
		this.nodeName = nodeName;
		this.checked = checked;
	}

	/**
	 * @return the nodeCode
	 */
	public String getNodeCode()
	{
		return nodeCode;
	}

	/**
	 * @param nodeCode the nodeCode to set
	 */
	public void setNodeCode(final String nodeCode)
	{
		this.nodeCode = nodeCode;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName()
	{
		return nodeName;
	}

	/**
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(final String nodeName)
	{
		this.nodeName = nodeName;
	}

	/**
	 * @return the checked
	 */
	public String getChecked()
	{
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(final String checked)
	{
		this.checked = checked;
	}

	/**
	 * @return the children
	 */
	public Map<String, TreeNode> getChildren()
	{
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(final Map<String, TreeNode> children)
	{
		this.children = children;
	}

	public static void getJsonTree(final TreeNode root, final JSONObject parentNode)
	{
		final Map<String, TreeNode> map = root.getChildren();
		if (null != map)
		{

			final JSONArray node = new JSONArray();
			parentNode.put("children", node);

			final Set<String> key = map.keySet();
			String parentChecked = (String) parentNode.get("checked");
			for (final Object element : key)
			{
				final String s = (String) element;
				final TreeNode treeNode = map.get(s);
				final JSONObject childNode = new JSONObject();
				childNode.put("id", treeNode.getNodeCode());
				childNode.put("text", treeNode.getNodeName());

				if (StringUtils.hasText(treeNode.getChecked()))
				{
					childNode.put("checked", treeNode.getChecked());
				}else {//如果子节点没有被选中，父节点状态设置为indeterminate=true，避免js控件默认加载所有子节点checked=true
					parentNode.remove("checked");
					parentNode.put("indeterminate", "true");
				}

				node.add(childNode);
				getJsonTree(map.get(s), childNode);

				// System.out.println(map.get(s));
			}
		}
	}

	@Override
	public String toString() {
		return "TreeNode [nodeCode=" + nodeCode + ", nodeName=" + nodeName
				+ ", checked=" + checked + ", parentCode=" + parentCode
				+ ", children=" + children + "]";
	}
}
