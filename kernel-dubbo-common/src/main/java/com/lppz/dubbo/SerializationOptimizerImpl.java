package com.lppz.dubbo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.lppz.dubbo.oms.init.CacheParam;
import com.lppz.dubbo.oms.init.enums.BaseEnum;

public class SerializationOptimizerImpl implements SerializationOptimizer {    

@Override
public Collection<Class> getSerializableClasses() {
	List<Class> classes = new LinkedList<Class>();
	classes.add(CacheParam.class);
	classes.add(Class.class);
	classes.add(BaseEnum.class);
    return classes;
}

}