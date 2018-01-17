package com.lppz.dubbo.oms.init.enums;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.lppz.dubbo.oms.init.BaseEnumBuilder;
@JsonDeserialize(as=BaseEnumBuilder.class)
public interface BaseEnum extends Serializable{

}
