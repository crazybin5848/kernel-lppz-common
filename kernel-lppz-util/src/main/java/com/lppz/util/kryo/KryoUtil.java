package com.lppz.util.kryo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(KryoUtil.class);
	public  static <T> byte[] kyroSeriLize(T t,int maxBufferSize) throws IOException {
			Output output=new Output(1,maxBufferSize);
			KryoObjectOutput ko=new KryoObjectOutput(output);
			try {
				ko.writeObject(t);
				ko.flushBuffer();
				byte[] bb = output.toBytes(); 
				return bb;
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
				throw e;
			}
			finally{
				ko.cleanup();
			}
		}
		
		public static <T> T kyroDeSeriLize(byte[] bb,Class<T> clazz) throws Exception {
			Input input =new Input(bb);
			KryoObjectInput ko=new KryoObjectInput(input);
			try {
				T t=ko.readObject(clazz);
				return t;
			} catch (IOException | ClassNotFoundException e) {
				logger.error(e.getMessage(),e);
				throw e;
			}
			finally{
				ko.cleanup();
			}
		}
}

