package com.lppz.serialize;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.lppz.dubbo.oms.pojo.Product;
import com.lppz.util.kryo.KryoUtil;

public class ProductSerializer extends JsonSerializer<Product> {  
    @Override  
    public void serialize(Product value, JsonGenerator jgen, SerializerProvider provider)   
      throws IOException, JsonProcessingException {  
        jgen.writeStartObject();  
        jgen.writeBinaryField("product",KryoUtil.kyroSeriLize(value, -1));
        jgen.writeEndObject();  
    }  
}  