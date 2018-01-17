package com.lppz.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSON;

public class DiamondClusterBean {
	private List<DiamondClusterNodeBean> clusterNodes;

	public List<DiamondClusterNodeBean> getClusterNodes() {
		return clusterNodes;
	}

	public void setClusterNodes(List<DiamondClusterNodeBean> clusterNodes) {
		this.clusterNodes = clusterNodes;
	}
	
	
	private static DiamondClusterNodeBean buildClusterNodes(int index){
		Map<String,Integer> ports = new HashMap<>();
		ports.put("restport", 15000 + index);
		ports.put("dobboport", 25000 + index);
		ports.put("wsport", 35000 + index);
		
		Map<String,String> params = new HashMap<>();
		params.put("sourcingTrigger.isrun", "false");
		params.put("waitSourcingTrigger.isrun", "false");
		params.put("sourcingExceptionTrigger.isrun", "true");
		
		DiamondClusterNodeBean nodeBean = new DiamondClusterNodeBean();
		nodeBean.setPorts(ports);
		nodeBean.setParams(params);
		
		return nodeBean;
		
	}
	public static void main(String[] args) {
		DiamondClusterBean bean = new DiamondClusterBean();
		List<DiamondClusterNodeBean> portArray = new ArrayList<>();
		
		
		
		portArray.add(buildClusterNodes(0));
		portArray.add(buildClusterNodes(1));
		
		bean.setClusterNodes(portArray);
		
		System.out.println(JSON.toJSON(portArray));
		
		Map<String, DiamondClusterBean> map = new HashMap<>();
		map.put("IT-LiCheng", bean);
		
		System.out.println(new Yaml().dump(map));
		
		System.out.println("--------------");
		System.out.println(new Yaml().dump(portArray));
	}
}
