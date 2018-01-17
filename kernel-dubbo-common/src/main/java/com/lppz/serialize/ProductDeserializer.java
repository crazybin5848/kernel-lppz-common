package com.lppz.serialize;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.node.IntNode;

import com.lppz.dubbo.oms.pojo.Product;
import com.lppz.util.kryo.KryoUtil;

public class ProductDeserializer extends JsonDeserializer<Product> {  
   
    @Override  
    public Product deserialize(JsonParser jp, DeserializationContext ctxt)   
      throws IOException, JsonProcessingException {  
        JsonNode node = jp.getCodec().readTree(jp);  
        byte[] bb = node.get("product").getBinaryValue();  
        try {
			Product pp=KryoUtil.kyroDeSeriLize(bb, Product.class);
			return pp;
        } catch (Exception e) {
			e.printStackTrace();
		}
        return null;  
    }  
}  